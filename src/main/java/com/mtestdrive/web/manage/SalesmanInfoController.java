package com.mtestdrive.web.manage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.SalesmanInfoServiceI;

/**   
 * @Title: Controller
 * @Description: 销售顾问信息
 * @author zhangdaihao
 * @date 2017-03-10 17:25:38
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/salesmanInfoController")
public class SalesmanInfoController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SalesmanInfoController.class);

	@Autowired
	private SalesmanInfoServiceI salesmanInfoService;
	@Autowired
	private SystemService systemService;
	


	/**
	 * 销售顾问信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("mpage/manage/salesmanInfo/salesmanInfoList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(SalesmanInfoEntity salesmanInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SalesmanInfoEntity.class, dataGrid);
		if(!ResourceUtil.getSessionUserName().getUserKey().equals("超级管理员")&&!ResourceUtil.getSessionUserName().getUserKey().equals("超级系统管理员")){
			cq.add(Restrictions.eq("agencyId", ResourceUtil.getSessionUserName().getDepartid()));
		}
		String name = salesmanInfo.getName();
		String englishName = salesmanInfo.getEnglishName();
		String employeeNo = salesmanInfo.getEmployeeNo();
		String mobile = salesmanInfo.getMobile();
		String agencyId = salesmanInfo.getAgencyId();
		if(StringUtil.isNotEmpty(name)){
			cq.add(Restrictions.like("name", salesmanInfoService.getLikeStr(name)));
		}
		if(StringUtil.isNotEmpty(englishName)){
			cq.add(Restrictions.like("englishName", salesmanInfoService.getLikeStr(englishName)));
		}
		if(StringUtil.isNotEmpty(employeeNo)){
			cq.add(Restrictions.like("employeeNo", salesmanInfoService.getLikeStr(employeeNo)));
		}
		if(StringUtil.isNotEmpty(mobile)){
			cq.add(Restrictions.like("mobile", salesmanInfoService.getLikeStr(mobile)));
		}
		if(StringUtil.isNotEmpty(agencyId)){
			cq.add(Restrictions.like("agencyId", agencyId));
		}
		
		cq.add(Restrictions.eq("status", ConstantStatus.VALID));
		cq.addOrder("createTime", SortDirection.desc);
		//查询条件组装器
		/*org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, salesmanInfo, request.getParameterMap());*/
		this.salesmanInfoService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除销售顾问信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(SalesmanInfoEntity salesmanInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		salesmanInfo = systemService.getEntity(SalesmanInfoEntity.class, salesmanInfo.getId());
		salesmanInfo.setStatus(ConstantStatus.INVALID);
		message = "销售顾问信息删除成功";
		salesmanInfoService.updateEntitie(salesmanInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加销售顾问信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(SalesmanInfoEntity salesmanInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(salesmanInfo.getId())) {
			message = "销售顾问信息更新成功";
			SalesmanInfoEntity t = salesmanInfoService.get(SalesmanInfoEntity.class, salesmanInfo.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(salesmanInfo, t);
				salesmanInfoService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "销售顾问信息更新失败";
			}
		} else {
			message = "销售顾问信息添加成功";
			TSUser ts = ResourceUtil.getSessionUserName();
			String departid = ts.getDepartid();
			salesmanInfo.setAgencyId(departid);
			salesmanInfo.setStatus(ConstantStatus.VALID);
			salesmanInfoService.save(salesmanInfo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 销售顾问信息列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(SalesmanInfoEntity salesmanInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(salesmanInfo.getId())) {
			salesmanInfo = salesmanInfoService.getEntity(SalesmanInfoEntity.class, salesmanInfo.getId());
			req.setAttribute("salesmanInfoPage", salesmanInfo);
		}
		return new ModelAndView("mpage/manage/salesmanInfo/salesmanInfo");
	}
}
