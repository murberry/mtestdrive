package com.mtestdrive.web.manage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.entity.AgencyInfoEntity;
import com.mtestdrive.entity.CustomerInfoEntity;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.CustomerInfoServiceI;

/**
 * @Title: Controller
 * @Description: 客户信息
 * @author zhangdaihao
 * @date 2017-03-10 17:26:29
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/customerInfoController")
public class CustomerInfoController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CustomerInfoController.class);

	@Autowired
	private CustomerInfoServiceI customerInfoService;
	@Autowired
	private SystemService systemService;

	/**
	 * 客户信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("mpage/manage/customerInfo/customerInfoList");
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
	public void datagrid(CustomerInfoEntity customerInfo, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CustomerInfoEntity.class, dataGrid);// 查询条件组装器
		String customerName = request.getParameter("name");// 客户姓名
		String customerMobile = request.getParameter("mobile");// 客户手机号
		String region = request.getParameter("region");// 区域
		String dealerName = request.getParameter("agencyName");//
		String salesConsultantName = request.getParameter("salesConsultantName");
		List<Object> idList = null;
		if (StringUtil.isNotEmpty(region)) {// 根据区域名称搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(AgencyInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.eq("regionId", region));

			salesDc.setProjection(getProjectionList());
			List<AgencyInfoEntity> agencyList = customerInfoService.findByDetached(salesDc);

			if (agencyList != null) {
				idList = new ArrayList<Object>();
				for (Object sales : agencyList) {
					idList.add(sales);
				}
				if (agencyList.isEmpty()) {
					idList.add("");
				}
				cq.add(Restrictions.in("agencyId", idList));
			}
		}

		if (StringUtil.isNotEmpty(dealerName)) {
			// 根据经销商名称模糊搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(AgencyInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.like("name", customerInfoService.getLikeStr(dealerName)));
			salesDc.setProjection(getProjectionList());
			List<AgencyInfoEntity> agencyList = customerInfoService.findByDetached(salesDc);

			if (agencyList != null) {
				idList = new ArrayList<Object>();
				for (Object sales : agencyList) {
					idList.add(sales);
				}
				if (agencyList.isEmpty()) {
					idList.add("");
				}
				cq.add(Restrictions.in("agencyId", idList));
			}
		}

		if (StringUtil.isNotEmpty(salesConsultantName)) {// 根据销售顾问姓名模糊搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(SalesmanInfoEntity.class);
			salesDc.add(Restrictions.like("name", customerInfoService.getLikeStr(salesConsultantName)));
			salesDc.setProjection(getProjectionList());
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			List<SalesmanInfoEntity> salesList = customerInfoService .findByDetached(salesDc);

			if (salesList != null) {
				idList = new ArrayList<Object>();
				for (Object sales : salesList) {
					idList.add(sales);
				}
				if (salesList.isEmpty()) {
					idList.add("");
				}
				cq.add(Restrictions.in("createBy", idList));
			}
		}

		if (StringUtil.isNotEmpty(customerName))
			cq.add(Restrictions.like("name", customerInfoService.getLikeStr(customerName)));

		if (StringUtil.isNotEmpty(customerMobile))
			cq.add(Restrictions.like("mobile", customerInfoService.getLikeStr(customerMobile)));
		if(!ResourceUtil.getSessionUserName().getUserKey().equals("超级管理员")&&!ResourceUtil.getSessionUserName().getUserKey().equals("超级系统管理员")){
			cq.add(Restrictions.eq("agencyId", ResourceUtil.getSessionUserName().getDepartid()));
		}
		cq.add(Restrictions.eq("status", ConstantStatus.VALID));
		cq.addOrder("createTime", SortDirection.desc);
		/*org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, customerInfo, request.getParameterMap());*/
		this.customerInfoService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除客户信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CustomerInfoEntity customerInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		customerInfo = systemService.getEntity(CustomerInfoEntity.class, customerInfo.getId());
		customerInfo.setStatus(ConstantStatus.INVALID);
		message = "客户信息删除成功";
		customerInfoService.updateEntitie(customerInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 添加客户信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CustomerInfoEntity customerInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(customerInfo.getId())) {
			message = "客户信息更新成功";
			CustomerInfoEntity t = customerInfoService.get(CustomerInfoEntity.class, customerInfo.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(customerInfo, t);
				t.setUpdateTime(DateUtils.getTimestamp());
				t.setUpdateBy(ResourceUtil.getSessionUserName().getId());
				customerInfoService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "客户信息更新失败";
			}
		} else {
			message = "客户信息添加成功";

			customerInfo.setCreateBy(ResourceUtil.getSessionUserName().getId());
			customerInfo.setCreateTime(DateUtils.getTimestamp());
			customerInfo.setStatus(ConstantStatus.VALID);
			customerInfoService.save(customerInfo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 客户信息列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CustomerInfoEntity customerInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(customerInfo.getId())) {
			customerInfo = customerInfoService.getEntity(CustomerInfoEntity.class, customerInfo.getId());
			if (customerInfo != null) {
				if (StringUtil.isNotEmpty(customerInfo.getCreateBy())) {
					// 销售顾问信息
					SalesmanInfoEntity si = systemService.get(SalesmanInfoEntity.class, customerInfo.getCreateBy());
					if (si != null)
						customerInfo.setCreateUserName(si.getName());
				}

				if (StringUtil.isNotEmpty(customerInfo.getAgencyId())) {
					// 经销商信息
					AgencyInfoEntity agencyInfo = systemService.get(AgencyInfoEntity.class, customerInfo.getAgencyId());
					if (agencyInfo != null) {
						customerInfo.setAgencyName(agencyInfo.getName());
						customerInfo.setRegionName(agencyInfo.getRegionId());
						/*if (StringUtil.isNotEmpty(agencyInfo.getRegionId())) {
							// 区域信息
							TSType region = systemService.get(TSType.class, agencyInfo.getRegionId());
							if (region != null)
								customerInfo.setRegionName(region.getTypename());
						}*/
					}
				}

			}
			req.setAttribute("customerInfoPage", customerInfo);
		}
		return new ModelAndView("mpage/manage/customerInfo/customerInfo");
	}

	private ProjectionList getProjectionList() {
		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("id").as("id"));
		return pList;
	}
}
