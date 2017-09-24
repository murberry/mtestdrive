package com.mtestdrive.web.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mtestdrive.MaseratiConstants.WD;
import com.mtestdrive.dto.YearMonthDto;
import com.mtestdrive.entity.AgencyInfoEntity;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.AgencyInfoServiceI;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.service.ObdDriveRecodsServiceI;
import com.mtestdrive.service.ObdGatherInfoServiceI;
import com.mtestdrive.service.QuestionnaireInfoServiceI;
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
@RequestMapping("/testDriveAnalyzeController")
public class TestDriveAnalyzeController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TestDriveAnalyzeController.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private DriveRecodsServiceI driveRecodsService;
	@Autowired
	private AgencyInfoServiceI agencyInfoService;
	@Autowired
	private CarInfoServiceI carInfoService;
	@Autowired
	private ObdGatherInfoServiceI obdGatherInfoService;
	@Autowired
	private SalesmanInfoServiceI salesmanInfoService;
	@Autowired
	private ObdDriveRecodsServiceI obdDriveRecodsService;
	@Autowired
	private QuestionnaireInfoServiceI questionnaireInfoService;

	/**
	 * @Title: turnoverRatio @Description: 试驾成交率
	 * @param: request
	 * @param: yearMonth
	 * @return: AjaxJson
	 * @throws
	 */
	@RequestMapping(params = "turnoverRatio")
	@ResponseBody
	public AjaxJson turnoverRatio(HttpServletRequest request, @RequestBody YearMonthDto yearMonth) {
		AjaxJson ajaxJson = new AjaxJson();
		
		// 获取权限
		List<TSRoleUser> sRoleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", ResourceUtil.getSessionUserName().getId());
		List<String> roleCodes = new ArrayList<String>(sRoleUser.size());
		for (int i = 0; i < sRoleUser.size(); i++) {
			roleCodes.add(sRoleUser.get(i).getTSRole().getRoleCode());
		}

		// 获取日期
		int year = yearMonth.getYear();
		int month = yearMonth.getMonth();
		String yearHelf[] = new String[6];
		for (int i = 0; i < 6; i++) {
			int c = month + i;
			if (12 < c) {
				yearHelf[i] = (year+1) + "-" + (c-12);
			} else {
				yearHelf[i] = year + "-" + c;
			}
		}
		
		// 分发方法
		if (roleCodes.contains("superAdmin")) {
			switch (yearMonth.getWd()) {
			case WD.MOTORCYCLE:
				ajaxJson.setObj(turnoverRatioCarType(yearHelf));
				break;
			case WD.QUYU:
				ajaxJson.setObj(getRegionTurnoverRatio(yearHelf));
				break;
			case WD.STORE:
				ajaxJson.setObj(getAgenTurnoverRatio(yearHelf, yearMonth.getRegion()));
				break;
			}
		} else {
			String departid = ResourceUtil.getSessionUserName().getDepartid();
			switch (yearMonth.getWd()) {
			case WD.MOTORCYCLE:
				ajaxJson.setObj(turnoverRatioCarType(yearHelf, departid));
				break;
			case WD.CONSULTANT:
				ajaxJson.setObj(getSalTurnoverRatio(yearHelf, departid));
				break;
			}
		}
		return ajaxJson;
	}

	private Map<String, Object> turnoverRatioCarType(String[] yearHelf, String... agency) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<TSType> carTypes = carInfoService.getAllCarTypes();
		if (!ListUtils.isNullOrEmpty(carTypes)) {
			StringBuilder sqlB = new StringBuilder("select truncate("
					+ "(select count(customer.id) from t_customer_info customer LEFT JOIN t_drive_recods drive ON customer.id = drive.customer_id"
					+ " where drive.car_type like :carType and date_format(drive.drive_start_time,'%Y-%c')in(:yearHelf) and customer.type='车主'");
			if (agency.length != 0) {
				sqlB.append(" and customer.agency_id =:agencyId");
			}
			sqlB.append(")/(select count(customer.id) from t_customer_info customer LEFT JOIN t_drive_recods drive ON customer.id = drive.customer_id"
					+ " where drive.car_type like :carType and date_format(drive.drive_start_time,'%Y-%c') =:month");
			if (agency.length != 0) {
				sqlB.append(" and customer.agency_id =:agencyId");
			}
			sqlB.append(")*100,2)");
			SQLQuery query;
			for (TSType carType : carTypes) {
				query = systemService.getSession().createSQLQuery(sqlB.toString());
				query.setParameter("carType", carType.getTypecode());
				query.setParameterList("yearHelf", yearHelf);
				query.setParameter("month", yearHelf[0]);
				if (agency.length != 0) {
					query.setParameter("agencyId", agency[0]);
				}
				List<?> resultList = query.list();
				if (!(ListUtils.isNullOrEmpty(resultList) || null == resultList.get(0)))
					map.put(carType.getTypename(), resultList.get(0));
			}
		}
		return map;
	}

	public Map<String, Object> getRegionTurnoverRatio(String[] yearHelf) {
		Map<String, Object> map = new HashMap<String, Object>();

		TSTypegroup group = systemService.getTypeGroupByCode("dearlerGroup");// 所有集团

		if (!ListUtils.isNullOrEmpty(group.getTSTypes())) {
			String sql = "select truncate("
					+ "(select count(customer.id) from t_customer_info customer LEFT JOIN t_agency_info agency ON customer.agency_id = agency.id"
					+ " LEFT JOIN t_drive_recods drive ON drive.customer_id = customer.id"
					+ " where agency.dearler_group =:dearlerGroup and date_format(drive.drive_start_time,'%Y-%c')in(:yearHelf) and customer.type='车主')/"
					+ "(select count(customer.id) from t_customer_info customer LEFT JOIN t_agency_info agency ON customer.agency_id = agency.id"
					+ " LEFT JOIN t_drive_recods drive ON drive.customer_id = customer.id"
					+ " where agency.dearler_group =:dearlerGroup and date_format(drive.drive_start_time,'%Y-%c') =:month)*100,2)";
			SQLQuery query;
			for (TSType region : group.getTSTypes()) {
				query = systemService.getSession().createSQLQuery(sql.toString());
				query.setParameter("dearlerGroup", region.getTypecode());
				query.setParameterList("yearHelf", yearHelf);
				query.setParameter("month", yearHelf[0]);
				List<?> resultList = query.list();
				if (!(ListUtils.isNullOrEmpty(resultList) || null == resultList.get(0)))
					map.put(region.getTypename(), resultList.get(0));
			}
		}
		return map;
	}

	/**
	 * 
	 * @Title: getSalTurnoverRatio
	 * @Description: 获取指销售顾问的试驾成交率
	 * @return: map
	 * @throws
	 */
	public Map<String, Object> getSalTurnoverRatio(String[] yearHelf, String agencyId) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<SalesmanInfoEntity> salesmans = salesmanInfoService.getSalesmanByAgencyId(agencyId);
		if (!ListUtils.isNullOrEmpty(salesmans)) {
			String sql = "select truncate("
					+ "(select count(customer.id) from t_customer_info customer LEFT JOIN t_drive_recods drive ON drive.customer_id = customer.id"
					+ " where customer.create_by =:salesmanId and date_format(drive.drive_start_time,'%Y-%c')in(:yearHelf) and customer.type='车主')/"
					+ "(select count(customer.id) from t_customer_info customer LEFT JOIN t_drive_recods drive ON drive.customer_id = customer.id"
					+ " where customer.create_by =:salesmanId and date_format(drive.drive_start_time,'%Y-%c') =:month)*100,2)";
			SQLQuery query;
			for (SalesmanInfoEntity salesman : salesmans) {
				query = systemService.getSession().createSQLQuery(sql.toString());
				query.setParameter("salesmanId", salesman.getId());
				query.setParameterList("yearHelf", yearHelf);
				query.setParameter("month", yearHelf[0]);
				List<?> resultList = query.list();
				if (!(ListUtils.isNullOrEmpty(resultList) || null == resultList.get(0)))
					map.put(salesman.getName(), resultList.get(0));
			}
		}
		return map;
	}

	/**
	 * @Title: getAgenTurnoverRatio
	 * @Description:获取指定经区域内经销商的试驾成交率
	 * @param: agencyId
	 * @param: time
	 * @return: map
	 * @throws
	 */
	public Map<String, Object> getAgenTurnoverRatio(String[] yearHelf, String region) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<AgencyInfoEntity> agencies = agencyInfoService.getAgencysByDearlerGroup(region);
		if (!ListUtils.isNullOrEmpty(agencies)) {
			String sql = "select truncate("
					+ "(select count(customer.id) from t_customer_info customer LEFT JOIN t_drive_recods drive ON drive.customer_id = customer.id"
					+ " where customer.agency_id =:agencyId and date_format(drive.drive_start_time,'%Y-%c')in(:yearHelf) and customer.type='车主')/"
					+ "(select count(customer.id) from t_customer_info customer LEFT JOIN t_drive_recods drive ON drive.customer_id = customer.id"
					+ " where customer.agency_id =:agencyId and date_format(drive.drive_start_time,'%Y-%c') =:month)*100,2)";
			SQLQuery query;
			for (AgencyInfoEntity agency : agencies) {
				query = systemService.getSession().createSQLQuery(sql.toString());
				query.setParameter("agencyId", agency.getId());
				query.setParameterList("yearHelf", yearHelf);
				query.setParameter("month", yearHelf[0]);
				List<?> resultList = query.list();
				if (!(ListUtils.isNullOrEmpty(resultList) || null == resultList.get(0)))
					map.put(agency.getName(), resultList.get(0));
			}
		}
		return map;
	}

	/**
	 * 销售顾问信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "jumpToTestDriveAnalyzePage")
	public ModelAndView list(HttpServletRequest request) {
		TSUser ts = ResourceUtil.getSessionUserName();

		List<TSRoleUser> sRoleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", ts.getId());

		// 判断该当前登录用户的角色，主要是为了区分超级管理员和经销商管理员
		if (sRoleUser == null || sRoleUser.isEmpty()) {
			request.setAttribute("msg", "抱歉，您没有权限访问该页面");
			return new ModelAndView("mpage/manage/errorMsg");
		} else {
			TSRoleUser roleUser = null;
			TSRole role = null;
			for (int i = 0; i < sRoleUser.size(); i++) {
				roleUser = sRoleUser.get(i);
				if (roleUser != null && roleUser.getTSRole() != null) {
					role = roleUser.getTSRole();
					if ("superAdmin".equals(role.getRoleCode())) {
						return new ModelAndView("mpage/manage/testDriveAnalyze/testDriveAnalyzeSuperAdmin");
					}
				}
			}
		}

		return new ModelAndView("mpage/manage/testDriveAnalyze/testDriveAnalyze");
	}

	@RequestMapping(params = "setSession")
	@ResponseBody
	public void setSession(HttpServletRequest request,HttpSession session,@RequestBody YearMonthDto yearMonth) {
		session.setAttribute("yearMonth", yearMonth);
	}

	@RequestMapping(params = "effective")
	@ResponseBody
	public AjaxJson effective(HttpServletRequest request, @RequestBody YearMonthDto yearMonth) {
		AjaxJson data = new AjaxJson();
		String month = new StringBuilder().append(yearMonth.getYear()).append("-").append(yearMonth.getMonth()).toString();
		List<TSRoleUser> sRoleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", ResourceUtil.getSessionUserName().getId());
		List<String> roleCodes = new ArrayList<String>(sRoleUser.size());
		for (int i = 0; i < sRoleUser.size(); i++) {
			roleCodes.add(sRoleUser.get(i).getTSRole().getRoleCode());
		}
		if (roleCodes.contains("superAdmin")) {
			switch (yearMonth.getWd()) {
			case WD.STORE:
				data.setObj(driveRecodsService.effective("agency.name", month, "", yearMonth.getRegion()));
				break;
			case WD.QUYU:
				data.setObj(driveRecodsService.effective("region.typename", month));
				break;
			case WD.MOTORCYCLE:
				data.setObj(driveRecodsService.effective("carType.typename", month, ""));
				break;
			}
		} else {
			String departid = ResourceUtil.getSessionUserName().getDepartid();
			switch (yearMonth.getWd()) {
			case WD.CONSULTANT:
				data.setObj(driveRecodsService.effective("salesman.name", month, departid));
				break;
			case WD.MOTORCYCLE:
				data.setObj(driveRecodsService.effective("carType.typename", month, departid));
				break;
			}
		}
		return data;
	}

	/**
	 * @Title: testDriveTimeLong
	 * @Description: 单次试驾时长
	 * @param: request
	 * @param: yearMonth
	 * @return: AjaxJson
	 * @author: dczhang
	 */
	@RequestMapping(params = "testDriveTimeLong")
	@ResponseBody
	public AjaxJson testDriveTimeLong(HttpServletRequest request, @RequestBody YearMonthDto yearMonth) {
		AjaxJson ajaxJson = new AjaxJson();
		String month = new StringBuilder().append(yearMonth.getYear()).append("-").append(yearMonth.getMonth()).toString();
		List<TSRoleUser> sRoleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", ResourceUtil.getSessionUserName().getId());
		List<String> roleCodes = new ArrayList<String>(sRoleUser.size());
		for (int i = 0; i < sRoleUser.size(); i++) {
			roleCodes.add(sRoleUser.get(i).getTSRole().getRoleCode());
		}
		if (roleCodes.contains("superAdmin")) {
			switch (yearMonth.getWd()) {
			case WD.MOTORCYCLE:
				ajaxJson.setObj(obdDriveRecodsService.testDriveTimeLong("carType.typename", month, ""));
				break;
			case WD.QUYU:
				ajaxJson.setObj(obdDriveRecodsService.testDriveTimeLong("region.typename", month));
				break;
			case WD.STORE:
				ajaxJson.setObj(obdDriveRecodsService.testDriveTimeLong("agency.name", month, "", yearMonth.getRegion()));
				break;
			}
		} else {
			String departid = ResourceUtil.getSessionUserName().getDepartid();
			switch (yearMonth.getWd()) {
			case WD.MOTORCYCLE:
				ajaxJson.setObj(obdDriveRecodsService.testDriveTimeLong("carType.typename", month, departid));
				break;
			case WD.CONSULTANT:
				ajaxJson.setObj(obdDriveRecodsService.testDriveTimeLong("salesman.name", month, departid));
				break;
			}
		}
		return ajaxJson;

	}

	@RequestMapping(params = "testDriveMileage")
	@ResponseBody
	public AjaxJson testDriveMileage(HttpServletRequest request, @RequestBody YearMonthDto yearMonth) {
		AjaxJson ajaxJson = new AjaxJson();
		String month = new StringBuilder().append(yearMonth.getYear()).append("-").append(yearMonth.getMonth()).toString();
		List<TSRoleUser> sRoleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", ResourceUtil.getSessionUserName().getId());
		List<String> roleCodes = new ArrayList<String>(sRoleUser.size());
		for (int i = 0; i < sRoleUser.size(); i++) {
			roleCodes.add(sRoleUser.get(i).getTSRole().getRoleCode());
		}
		if (roleCodes.contains("superAdmin")) {
			switch (yearMonth.getWd()) {
			case WD.MOTORCYCLE:
				ajaxJson.setObj(obdDriveRecodsService.testDriveMileage("carType.typename", month, ""));
				break;
			case WD.QUYU:
				ajaxJson.setObj(obdDriveRecodsService.testDriveMileage("region.typename", month));
				break;
			case WD.STORE:
				ajaxJson.setObj(obdDriveRecodsService.testDriveMileage("agency.name", month, "", yearMonth.getRegion()));
				break;
			}
		} else {
			String departid = ResourceUtil.getSessionUserName().getDepartid();
			switch (yearMonth.getWd()) {
			case WD.MOTORCYCLE:
				ajaxJson.setObj(obdDriveRecodsService.testDriveMileage("carType.typename", month, departid));
				break;
			case WD.CONSULTANT:
				ajaxJson.setObj(obdDriveRecodsService.testDriveMileage("salesman.name", month, departid));
				break;
			}
		}
		return ajaxJson;

	}

	@RequestMapping(params = "illegal")
	@ResponseBody
	public AjaxJson illegal(HttpServletRequest request, @RequestBody YearMonthDto yearMonth) {
		AjaxJson ajaxJson = new AjaxJson();
		String month = new StringBuilder().append(yearMonth.getYear()).append("-").append(yearMonth.getMonth()).toString();
		List<TSRoleUser> sRoleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", ResourceUtil.getSessionUserName().getId());
		List<String> roleCodes = new ArrayList<String>(sRoleUser.size());
		for (int i = 0; i < sRoleUser.size(); i++) {
			roleCodes.add(sRoleUser.get(i).getTSRole().getRoleCode());
		}
		if (roleCodes.contains("superAdmin")) {
			switch (yearMonth.getWd()) {
			case WD.MOTORCYCLE:
				ajaxJson.setObj(obdDriveRecodsService.illegal("carType.typename", month, ""));
				break;
			case WD.QUYU:
				ajaxJson.setObj(obdDriveRecodsService.illegal("region.typename", month));
				break;
			case WD.STORE:
				ajaxJson.setObj(obdDriveRecodsService.illegal("agency.name", month, "", yearMonth.getRegion()));
				break;
			}
		} else {
			String departid = ResourceUtil.getSessionUserName().getDepartid();
			switch (yearMonth.getWd()) {
			case WD.MOTORCYCLE:
				ajaxJson.setObj(obdDriveRecodsService.illegal("carType.typename", month, departid));
				break;
			case WD.CONSULTANT:
				ajaxJson.setObj(obdDriveRecodsService.illegal("salesman.name", month, departid));
				break;
			}
		}
		return ajaxJson;

	}

	@RequestMapping(params = "satisfied")
	@ResponseBody
	public AjaxJson satisfied(HttpServletRequest request, @RequestBody YearMonthDto yearMonth) {
		AjaxJson ajaxJson = new AjaxJson();
		String month = new StringBuilder().append(yearMonth.getYear()).append("-").append(yearMonth.getMonth()).toString();
		List<TSRoleUser> sRoleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", ResourceUtil.getSessionUserName().getId());
		List<String> roleCodes = new ArrayList<String>(sRoleUser.size());
		for (int i = 0; i < sRoleUser.size(); i++) {
			roleCodes.add(sRoleUser.get(i).getTSRole().getRoleCode());
		}
		if (roleCodes.contains("superAdmin")) {
			switch (yearMonth.getWd()) {
			case WD.MOTORCYCLE:
				ajaxJson.setObj(questionnaireInfoService.satisfied("carType.typename", month, ""));
				break;
			case WD.QUYU:
				ajaxJson.setObj(questionnaireInfoService.satisfied("region.typename", month));
				break;
			case WD.STORE:
				ajaxJson.setObj(questionnaireInfoService.satisfied("agency.name", month, "", yearMonth.getRegion()));
				break;
			}
		} else {
			String departid = ResourceUtil.getSessionUserName().getDepartid();
			switch (yearMonth.getWd()) {
			case WD.MOTORCYCLE:
				ajaxJson.setObj(questionnaireInfoService.satisfied("carType.typename", month, departid));
				break;
			case WD.CONSULTANT:
				ajaxJson.setObj(questionnaireInfoService.satisfied("salesman.name", month, departid));
				break;
			}
		}
		return ajaxJson;

	}
}
