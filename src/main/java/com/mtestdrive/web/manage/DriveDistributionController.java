package com.mtestdrive.web.manage;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mtestdrive.dto.DriveRecodsDto;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.service.SalesmanInfoServiceI;

/**   
 * @Title: Controller
 * @Description: 预约分配
 * @author zhangdaihao
 * @date 2017-03-10 17:36:12
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/driveDistributionController")
public class DriveDistributionController {

	@Autowired
	private DriveRecodsServiceI driveRecodsService;
	
	@Autowired
	private SalesmanInfoServiceI salesmanInfoService;
	
	/**
	 * 预约列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list0(HttpServletRequest request) {
		return new ModelAndView("mpage/manage/driveDistribution/driveDistributionList");
	}
	
	@RequestMapping(params = "datagrid")
	public void datagrid(DriveRecodsDto driveRecods, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class, dataGrid);
		cq.add(Property.forName("salesman").isNull());
		if(!ResourceUtil.getSessionUserName().getUserKey().equals("超级管理员")&&!ResourceUtil.getSessionUserName().getUserKey().equals("超级系统管理员")){
			cq.add(Restrictions.eq("agency.id", ResourceUtil.getSessionUserName().getDepartid()));
		}
		
		if(driveRecods != null && driveRecods.getCustomer() != null){
			if (StringUtil.isNotEmpty(driveRecods.getCustomer().getName()) || StringUtil.isNotEmpty(driveRecods.getCustomer().getName())) {
				cq.createAlias("customer", "customer");
				String name = driveRecods.getCustomer().getName();
				if (StringUtil.isNotEmpty(name)) {
					cq.like("customer.name", "%" + name + "%");
				}
			}
		}
		
		if(driveRecods.getAgency() != null && StringUtil.isNotEmpty(driveRecods.getAgency().getName())){
			cq.createAlias("agency", "agency");
			cq.like("agency.name", "%"+driveRecods.getAgency().getName() + "%");
		}
		cq.addOrder("createTime", SortDirection.desc);
		cq.add();
		cq.setResultTransformer(DriveRecodsEntity.class);
		this.driveRecodsService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	@RequestMapping(params = "distribution")
	public ModelAndView distribution(String id, HttpServletRequest req) {
		ModelAndView view = new ModelAndView();
		if(StringUtil.isNotEmpty(id)){
			DriveRecodsEntity driveRecods = driveRecodsService.get(DriveRecodsEntity.class, id);
			if(driveRecods != null){
				String agencyId = driveRecods.getAgency().getId();
				List<SalesmanInfoEntity> salesmanList = salesmanInfoService.getSalesmanByAgencyId(agencyId);
				view.addObject("salesmanList", salesmanList);
				view.addObject("driveRecods", driveRecods);
			}
			
		}
		view.setViewName("mpage/manage/driveDistribution/driveDistribution");
		return view;
	}
	
	@RequestMapping(params = "updateSaleman")
	@ResponseBody
	public AjaxJson updateSaleman(DriveRecodsEntity driveRecods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
			message = "预约分配经销商顾问添加成功";
			DriveRecodsEntity t = driveRecodsService.get(DriveRecodsEntity.class, driveRecods.getId());
			try {
				String salemanId = request.getParameter("salemanId");
				SalesmanInfoEntity salesman = salesmanInfoService.get(SalesmanInfoEntity.class, salemanId);
				t.setSalesman(salesman);
				t.setUpdateTime(new Date());
				driveRecodsService.saveOrUpdate(t);
				
				t.getCustomer().setCreateBy(salemanId);
				driveRecodsService.saveOrUpdate(t.getCustomer());
			} catch (Exception e) {
				e.printStackTrace();
				message = "预约分配经销商顾问添加失败";
			}
		j.setMsg(message);
		return j;
	}
}
