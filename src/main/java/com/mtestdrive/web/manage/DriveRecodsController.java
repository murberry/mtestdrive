package com.mtestdrive.web.manage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
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
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.MaseratiConstants.DriveRecodsStatus;
import com.mtestdrive.dto.DriveRecodsDto;
import com.mtestdrive.dto.ObdDriveDTO;
import com.mtestdrive.dto.ObdGatherDataDto;
import com.mtestdrive.entity.AgencyInfoEntity;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.CustomerInfoEntity;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.entity.ObdDriveRecodsEntity;
import com.mtestdrive.entity.ObdGatherInfoEntity;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.AgencyInfoServiceI;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.CustomerInfoServiceI;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.service.ObdDriveRecodsServiceI;
import com.mtestdrive.service.ObdGatherInfoServiceI;
import com.mtestdrive.service.SalesmanInfoServiceI;
import com.mtestdrive.vo.PlaybackTrackVo;

/**   
 * @Title: Controller
 * @Description: 试驾明细
 * @author zhangdaihao
 * @date 2017-03-10 17:36:12
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/driveRecodsController")
public class DriveRecodsController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DriveRecodsController.class);

	@Autowired
	private DriveRecodsServiceI driveRecodsService;
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private ObdGatherInfoServiceI obdGatherInfoService;
	
	@Autowired
	private CarInfoServiceI carInfoService;
	
	@Autowired
	private ObdDriveRecodsServiceI obdDriveRecodsService;
	
	@Autowired
	private AgencyInfoServiceI agencyInfoService;
	
	@Autowired
	private CustomerInfoServiceI customerInfoService;
	
	@Autowired
	private SalesmanInfoServiceI salesmanInfoService;
	
	@RequestMapping(params = "getJson")
	@ResponseBody
	public AjaxJson getJson(HttpServletRequest request) {
		AjaxJson aj = new AjaxJson();
		String termId = request.getParameter("termId");
		String sql = "SELECT GNSSTIME FROM t_obd_gather_info OBD2 WHERE OBD2.TERMID='"+termId+"' "
				+ "AND OBD2.ACCON=0 AND OBD2.GNSSTIME > '2017-05-23'  ORDER BY OBD2.GNSSTIME DESC";
		
		List<String> endTimes = obdGatherInfoService.findListbySql(sql);
		String sql1 = null;
		List list = new ArrayList();
		Map map = null;
		for(String time:endTimes){
			
			sql1 = "select obd1.termId,agen.name,car.type,car.plate_no,DATE_FORMAT(obd1.gnssTime,'%Y-%m-%d') 试驾日期,FORMAT((UNIX_TIMESTAMP('"+time+"')-UNIX_TIMESTAMP(obd1.gnssTime))/60,2) '试驾时长', "+
					"((select obd2.mileage from t_obd_gather_info obd2 where obd2.gnssTime ='"+time+"' and obd2.termId='"+termId+"' LIMIT 1 )-obd1.mileage)/1000 '里程' "+
					" from t_obd_gather_info obd1  "+
					" LEFT JOIN t_car_info car on car.obd_id = obd1.termId "+
					" LEFT JOIN t_agency_info  agen on car.agency_id = agen.id " +
					" where obd1.gnssTime > (select gnssTime from t_obd_gather_info obd   where termId='"+termId+"' and accOn=0 and gnssTime < '"+time+"' "+
					" ORDER BY gnssTime desc  LIMIT 1 ) and  obd1.termId=  '"+termId+"'        LIMIT 1 ";
		
			map = obdGatherInfoService.findOneForJdbc(sql1);
			if(map != null && !map.get("试驾时长").equals("0.0") && !map.get("试驾时长").equals("0.00"))
				list.add(map);
		}
		aj.setObj(list);
		return aj;
	}
	
	/**
	 * 预约列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list0")
	public ModelAndView list0(HttpServletRequest request) {
		return new ModelAndView("mpage/manage/driveRecods/driveRecodsList0");
	}
	


	/**
	 * 试驾明细列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("mpage/manage/driveRecods/driveRecodsList");
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
	public void datagrid(DriveRecodsDto driveRecods, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class, dataGrid);
		if(!ResourceUtil.getSessionUserName().getUserKey().equals("超级管理员")&&!ResourceUtil.getSessionUserName().getUserKey().equals("超级系统管理员")){
			cq.add(Restrictions.eq("agency.id", ResourceUtil.getSessionUserName().getDepartid()));
		}
		
		if (StringUtil.isNotEmpty(driveRecods.getRegionId()) || StringUtil.isNotEmpty(driveRecods.getAgencyCode())) {
			cq.createAlias("agency", "agency");
			String regionId = driveRecods.getRegionId();
			String code = driveRecods.getAgencyCode();
			if (StringUtil.isNotEmpty(regionId)) {
				cq.eq("agency.regionId", regionId);
			}
			if (StringUtil.isNotEmpty(code)) {
				cq.like("agency.code", code + "%");
			}
		}
		if(driveRecods.getAgency() != null && StringUtil.isNotEmpty(driveRecods.getAgency().getName())){
			cq.createAlias("agency", "agency");
			cq.like("agency.name", "%"+driveRecods.getAgency().getName() + "%");
		}
		if (driveRecods != null && driveRecods.getSalesman() != null &&
				StringUtil.isNotEmpty(driveRecods.getSalesman().getName())) {
			cq.createAlias("salesman", "salesman");
			cq.like("salesman.name", "%"+driveRecods.getSalesman().getName() + "%");
		}
		if(driveRecods != null && driveRecods.getCustomer() != null){
			if (StringUtil.isNotEmpty(driveRecods.getCustomer().getName()) || StringUtil.isNotEmpty(driveRecods.getCustomer().getName())) {
				cq.createAlias("customer", "customer");
				String name = driveRecods.getCustomer().getName();
				String mobile = driveRecods.getCustomerMobile();
				if (StringUtil.isNotEmpty(name)) {
					cq.like("customer.name", "%" + name + "%");
				}
				if (StringUtil.isNotEmpty(mobile)) {
					cq.eq("customer.mobile", mobile);
				}
			}
		}
		
		String status = request.getParameter("status");
		if ("0".equals(status)) {
			if (driveRecods.getStartTime() != null) {
				cq.ge("orderStartTime", DateUtils.str2Date(DateUtils.formatDate(driveRecods.getStartTime()), DateUtils.date_sdf));
			}
			if (driveRecods.getEndTime() != null) {
				cq.lt("orderEndTime", DateUtils.addDate(DateUtils.formatDate(driveRecods.getEndTime()), 1));
			}
		} else {
			if (driveRecods.getStartTime() != null) {
				cq.ge("driveStartTime", DateUtils.str2Date(DateUtils.formatDate(driveRecods.getStartTime()), DateUtils.date_sdf));
			}
			if (driveRecods.getEndTime() != null) {
				cq.lt("driveEndTime", DateUtils.addDate(DateUtils.formatDate(driveRecods.getEndTime()), 1));
			}
		}
		
		cq.in("status", DriveRecodsStatus.statusDescr(status));
		cq.addOrder("createTime", SortDirection.desc);
		cq.add();
		cq.setResultTransformer(DriveRecodsEntity.class);
		//查询条件组装器
		//org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, driveRecods, request.getParameterMap());
		this.driveRecodsService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	
	@RequestMapping(params = "obdDatagrid")
	public void obdDatagrid(DriveRecodsDto driveRecods, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ObdDriveRecodsEntity.class, dataGrid);
		String status = request.getParameter("status");
		String agencyName = request.getParameter("agencyName");
		String salesmanName = request.getParameter("salesmanName");
		String customerName = request.getParameter("customerName");
		String type = request.getParameter("type");
		List<Object> idList = null;
		if(!ResourceUtil.getSessionUserName().getUserKey().equals("超级管理员")&&!ResourceUtil.getSessionUserName().getUserKey().equals("超级系统管理员")){
			cq.add(Restrictions.eq("agencyId", ResourceUtil.getSessionUserName().getDepartid()));
		}
		
		
		if(agencyName != null && StringUtil.isNotEmpty(agencyName)){
			// 根据经销商名称模糊搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(AgencyInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.like("name", carInfoService.getLikeStr(agencyName)));
			salesDc.setProjection(getProjectionList());
			List<AgencyInfoEntity> agencyList = carInfoService.findByDetached(salesDc);

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
		if(salesmanName != null && StringUtil.isNotEmpty(salesmanName)){
			// 根据经销商名称模糊搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(SalesmanInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.like("name", salesmanInfoService.getLikeStr(salesmanName)));
			salesDc.setProjection(getProjectionList());
			List<AgencyInfoEntity> salesmanList = salesmanInfoService.findByDetached(salesDc);

			if (salesmanList != null) {
				idList = new ArrayList<Object>();
				for (Object sales : salesmanList) {
					idList.add(sales);
				}
				if (salesmanList.isEmpty()) {
					idList.add("");
				}
				cq.add(Restrictions.in("salesmanId", idList));
			}
		}
		if(customerName != null && StringUtil.isNotEmpty(customerName)){
			// 根据经销商名称模糊搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(CustomerInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.like("name", customerInfoService.getLikeStr(customerName)));
			salesDc.setProjection(getProjectionList());
			List<CustomerInfoEntity> customerList = customerInfoService.findByDetached(salesDc);

			if (customerList != null) {
				idList = new ArrayList<Object>();
				for (Object sales : customerList) {
					idList.add(sales);
				}
				if (customerList.isEmpty()) {
					idList.add("");
				}
				cq.add(Restrictions.in("customerId", idList));
			}
		}
		if(type!=null && StringUtil.isNotEmpty(type)){
			DetachedCriteria salesDc = DetachedCriteria.forClass(CarInfoEntity.class);
			salesDc.add(Restrictions.eq("type", type));
			salesDc.setProjection(getProjectionList());
			List<CarInfoEntity> carInfoList = carInfoService.findByDetached(salesDc);
			if(carInfoList!=null){
				idList = new ArrayList<Object>();
				for (Object sales : carInfoList) {
					idList.add(sales);
				}
				if (carInfoList.isEmpty()) {
					idList.add("");
				}
				cq.add(Restrictions.in("carId", idList));
			}
		}
		if(status != null && StringUtil.isNotEmpty(status)){
			cq.eq("status",Integer.parseInt(status));
		}
		if (driveRecods.getStartTime() != null) {
			cq.ge("driveStartTime", DateUtils.str2Date(DateUtils.formatDate(driveRecods.getStartTime()), DateUtils.date_sdf));
		}
		if (driveRecods.getEndTime() != null) {
			cq.lt("driveEndTime", DateUtils.addDate(DateUtils.formatDate(driveRecods.getEndTime()), 1));
		}
		cq.addOrder("createTime", SortDirection.desc);
		cq.add();
		cq.setResultTransformer(ObdDriveRecodsEntity.class);
		//查询条件组装器
		//org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, driveRecods, request.getParameterMap());
		this.driveRecodsService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 导出excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DriveRecodsDto driveRecods, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(ObdDriveRecodsEntity.class, dataGrid);
		String status = request.getParameter("status");
		String agencyName = request.getParameter("agencyName");
		String salesmanName = request.getParameter("salesmanName");
		String customerName = request.getParameter("customerName");
		List<Object> idList = null;
		if(!ResourceUtil.getSessionUserName().getUserKey().equals("超级管理员")&&!ResourceUtil.getSessionUserName().getUserKey().equals("超级系统管理员")){
			cq.add(Restrictions.eq("agencyId", ResourceUtil.getSessionUserName().getDepartid()));
		}
		
		
		if(agencyName != null && StringUtil.isNotEmpty(agencyName)){
			// 根据经销商名称模糊搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(AgencyInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.like("name", carInfoService.getLikeStr(agencyName)));
			salesDc.setProjection(getProjectionList());
			List<AgencyInfoEntity> agencyList = carInfoService.findByDetached(salesDc);

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
		if(salesmanName != null && StringUtil.isNotEmpty(salesmanName)){
			// 根据经销商名称模糊搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(SalesmanInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.like("name", salesmanInfoService.getLikeStr(salesmanName)));
			salesDc.setProjection(getProjectionList());
			List<AgencyInfoEntity> salesmanList = salesmanInfoService.findByDetached(salesDc);

			if (salesmanList != null) {
				idList = new ArrayList<Object>();
				for (Object sales : salesmanList) {
					idList.add(sales);
				}
				if (salesmanList.isEmpty()) {
					idList.add("");
				}
				cq.add(Restrictions.in("salesmanId", idList));
			}
		}
		if(customerName != null && StringUtil.isNotEmpty(customerName)){
			// 根据经销商名称模糊搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(CustomerInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.like("name", customerInfoService.getLikeStr(customerName)));
			salesDc.setProjection(getProjectionList());
			List<CustomerInfoEntity> customerList = customerInfoService.findByDetached(salesDc);

			if (customerList != null) {
				idList = new ArrayList<Object>();
				for (Object sales : customerList) {
					idList.add(sales);
				}
				if (customerList.isEmpty()) {
					idList.add("");
				}
				cq.add(Restrictions.in("customerId", idList));
			}
		}
		if(status != null && StringUtil.isNotEmpty(status)){
			cq.eq("status",Integer.parseInt(status));
		}
		if (driveRecods.getStartTime() != null) {
			cq.ge("driveStartTime", DateUtils.str2Date(DateUtils.formatDate(driveRecods.getStartTime()), DateUtils.date_sdf));
		}
		if (driveRecods.getEndTime() != null) {
			cq.lt("driveEndTime", DateUtils.addDate(DateUtils.formatDate(driveRecods.getEndTime()), 1));
		}
		cq.addOrder("createTime", SortDirection.desc);
		cq.add();
		List<ObdDriveRecodsEntity> tsDriveRecods = this.systemService.getListByCriteriaQuery(cq,false);
		List<ObdDriveDTO> obdDriveList = new ArrayList<ObdDriveDTO>();
		for (ObdDriveRecodsEntity obdDriveRecodsEntity : tsDriveRecods) {
			AgencyInfoEntity agencyInfo = systemService.getEntity(AgencyInfoEntity.class ,obdDriveRecodsEntity.getAgencyId() );
			CarInfoEntity carInfo = systemService.getEntity(CarInfoEntity.class ,obdDriveRecodsEntity.getCarId() );
			
			
			long endTime = obdDriveRecodsEntity.getDriveEndTime().getTime();
			long startTime = obdDriveRecodsEntity.getDriveStartTime().getTime();
			long time = endTime-startTime;
			float x = time/60000;
			ObdDriveDTO obdDrive = new ObdDriveDTO();
			obdDrive.setAgencyName(agencyInfo.getName());
			obdDrive.setPlateNo(carInfo.getPlateNo());
			obdDrive.setCarType(carInfo.getType());
			String customerId = obdDriveRecodsEntity.getCustomerId();
			if("/".equals(customerId)  || null== customerId){
				obdDrive.setCustomerName(customerId);
			}else{
				CustomerInfoEntity customer = systemService.getEntity(CustomerInfoEntity.class, obdDriveRecodsEntity.getCustomerId());
				obdDrive.setCustomerName(customer.getName());
			}
			String salesmanId = obdDriveRecodsEntity.getSalesmanId();
			if("/".equals(salesmanId)|| null == salesmanId ){
				obdDrive.setSalesmanName(salesmanId);
			}else{
				SalesmanInfoEntity salesman=systemService.getEntity(SalesmanInfoEntity.class, obdDriveRecodsEntity.getSalesmanId());
				obdDrive.setSalesmanName(salesman.getName());
			}
			obdDrive.setDriveStartTime(obdDriveRecodsEntity.getDriveStartTime());
			obdDrive.setDriveEndTime(obdDriveRecodsEntity.getDriveEndTime());
			obdDrive.setDriveTime(x);
			obdDrive.setMileage(obdDriveRecodsEntity.getMileage());
			obdDrive.setDearlerGroup(agencyInfo.getDearlerGroup());
			obdDrive.setDescription(obdDriveRecodsEntity.getDescription());
			obdDrive.setAchievement(obdDriveRecodsEntity.getAchievement());
			int s = obdDriveRecodsEntity.getStatus();
			if(s==1){
				status = "有效试驾";
			}else{
				status = "无效试驾";
			}
			
			obdDrive.setStatus(status);
			obdDriveList.add(obdDrive);
		}
		modelMap.put(NormalExcelConstants.FILE_NAME,"试驾明细");
		modelMap.put(NormalExcelConstants.CLASS,ObdDriveDTO.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("试驾明细列表", "导出人:"+ ResourceUtil.getSessionUserName().getRealName(),
				"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,obdDriveList);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	
	/**
	 * @Title: datagridTestDrive   
	 * @Description: 试驾明细 
	 * @param: @param driveRecods
	 * @param: @param request
	 * @param: @param response
	 * @param: @param dataGrid      
	 * @return: void      
	 * @throws
	 */
	@RequestMapping(params = "datagridTestDrive")
	public void datagridTestDrive(DriveRecodsDto driveRecods, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class, dataGrid);
		if(!ResourceUtil.getSessionUserName().getUserKey().equals("超级管理员")){
			cq.add(Restrictions.eq("agency.id", ResourceUtil.getSessionUserName().getDepartid()));
		}
		
		if (StringUtil.isNotEmpty(driveRecods.getRegionId()) || StringUtil.isNotEmpty(driveRecods.getAgencyCode())) {
			cq.createAlias("agency", "agency");
			String regionId = driveRecods.getRegionId();
			String code = driveRecods.getAgencyCode();
			if (StringUtil.isNotEmpty(regionId)) {
				cq.eq("agency.regionId", regionId);
			}
			if (StringUtil.isNotEmpty(code)) {
				cq.like("agency.code", code + "%");
			}
		}
		if(driveRecods.getAgency() != null && StringUtil.isNotEmpty(driveRecods.getAgency().getName())){
			cq.createAlias("agency", "agency");
			cq.like("agency.name", "%"+driveRecods.getAgency().getName() + "%");
		}
		if (driveRecods != null && driveRecods.getSalesman() != null &&
				StringUtil.isNotEmpty(driveRecods.getSalesman().getName())) {
			cq.createAlias("salesman", "salesman");
			cq.like("salesman.name", "%"+driveRecods.getSalesman().getName() + "%");
		}
		if(driveRecods != null && driveRecods.getCustomer() != null){
			if (StringUtil.isNotEmpty(driveRecods.getCustomer().getName()) || StringUtil.isNotEmpty(driveRecods.getCustomer().getName())) {
				cq.createAlias("customer", "customer");
				String name = driveRecods.getCustomer().getName();
				String mobile = driveRecods.getCustomerMobile();
				if (StringUtil.isNotEmpty(name)) {
					cq.like("customer.name", "%" + name + "%");
				}
				if (StringUtil.isNotEmpty(mobile)) {
					cq.eq("customer.mobile", mobile);
				}
			}
		}
		
		String status = request.getParameter("status");
		if ("0".equals(status)) {
			if (driveRecods.getStartTime() != null) {
				cq.ge("orderStartTime", DateUtils.str2Date(DateUtils.formatDate(driveRecods.getStartTime()), DateUtils.date_sdf));
			}
			if (driveRecods.getEndTime() != null) {
				cq.lt("orderEndTime", DateUtils.addDate(DateUtils.formatDate(driveRecods.getEndTime()), 1));
			}
		} else {
			if (driveRecods.getStartTime() != null) {
				cq.ge("driveStartTime", DateUtils.str2Date(DateUtils.formatDate(driveRecods.getStartTime()), DateUtils.date_sdf));
			}
			if (driveRecods.getEndTime() != null) {
				cq.lt("driveEndTime", DateUtils.addDate(DateUtils.formatDate(driveRecods.getEndTime()), 1));
			}
		}
		//cq.in("status", DriveRecodsStatusNew.statusDescr(status));
		cq.in("status", new Integer[]{5,6,7,8,9});
		cq.add();
		cq.setResultTransformer(DriveRecodsEntity.class);
		//查询条件组装器
		//org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, driveRecods, request.getParameterMap());
		this.driveRecodsService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "getAgency")
	@ResponseBody
	public List<AgencyInfoEntity> getAgency() {
		List<AgencyInfoEntity> AgencyInfoList = systemService.loadAll(AgencyInfoEntity.class);
		return AgencyInfoList;
	}
	
	/**
	 * easyui AJAX请求数据试驾车
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "getCarInfo")
	@ResponseBody
	public List<CarInfoEntity> getCarInfo() {
		List<CarInfoEntity> CarInfoList = systemService.loadAll(CarInfoEntity.class);
		return CarInfoList;
	}
	
	/**
	 * easyui AJAX请求数据销售
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "getSalesmanInfo")
	@ResponseBody
	public List<SalesmanInfoEntity> getSalesmanInfo() {
		List<SalesmanInfoEntity> SalesmanInfoList = systemService.loadAll(SalesmanInfoEntity.class);
		return SalesmanInfoList;
	}
	
	/**
	 * easyui AJAX请求数据客户
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "getCustomerInfo")
	@ResponseBody
	public List<CustomerInfoEntity> getCustomerInfo() {
		List<CustomerInfoEntity> CustomerInfoList = systemService.loadAll(CustomerInfoEntity.class);
		return CustomerInfoList;
	}
	
	
	/**
	 * 删除试驾明细
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(DriveRecodsEntity driveRecods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		driveRecods = systemService.getEntity(DriveRecodsEntity.class, driveRecods.getId());
		message = "试驾明细删除成功";
		driveRecodsService.delete(driveRecods);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加试驾明细
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(DriveRecodsEntity driveRecods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(driveRecods.getId())) {
			message = "试驾明细更新成功";
			DriveRecodsEntity t = driveRecodsService.get(DriveRecodsEntity.class, driveRecods.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(driveRecods, t);
				t.setUpdateTime(new Date());
				driveRecodsService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "试驾明细更新失败";
			}
		} else {
			message = "试驾明细添加成功";
			driveRecods.setStatus(0);
			driveRecods.setCreateTime(new Date());
			driveRecodsService.save(driveRecods);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 试驾明细列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(DriveRecodsEntity driveRecods, HttpServletRequest req) {
		String viewName = "mpage/manage/driveRecods/driveRecods";
		if (StringUtil.isNotEmpty(driveRecods.getId())) {
			driveRecods = driveRecodsService.getEntity(DriveRecodsEntity.class, driveRecods.getId());
			req.setAttribute("driveRecodsPage", driveRecods);
			if (!ArrayUtils.contains(DriveRecodsStatus.statusDescr(null), driveRecods.getStatus())) {
				viewName += "0";//如果没有试驾信息就跳转到预约详情页
			}
		}
		return new ModelAndView(viewName);
	}
	
    /**
     * @Title: jumpToplaybackTrackPage   
     * @Description: 转发到轨迹 回放页面
     * @param:  id
     * @param:  req
     * @return: ModelAndView      
     * @throws
     */
	@RequestMapping(params = "jumpToplaybackTrackPage")
	public ModelAndView jumpToplaybackTrackPage(@RequestParam(value = "id", required = true)String id, HttpServletRequest req) {
		DriveRecodsEntity dr = systemService.get(DriveRecodsEntity.class, id);
		PlaybackTrackVo ptv = new PlaybackTrackVo();
		if(dr != null){
			if(dr.getCarId() != null){
				CarInfoEntity carInfo = carInfoService.get(CarInfoEntity.class, dr.getCarId());
				
				
				
				ptv.setDriver(dr.getDriver());
				ptv.setDriveStartTime(dr.getDriveStartTime());
				ptv.setDriveEndTime(dr.getDriveEndTime());
				ptv.setMileage(dr.getMileage());

				if(carInfo != null){					
					ptv.setPlateNo(carInfo.getPlateNo());
					ptv.setType(carInfo.getType());
					
					List<ObdGatherInfoEntity> obdGatherInfo = obdGatherInfoService.getDatasByTimeQuantum(carInfo.getObdId(), dr.getDriveStartTime(), dr.getDriveEndTime());
					if(obdGatherInfo != null && !obdGatherInfo.isEmpty()){
						ObdGatherInfoEntity gatherInfo = null;
						ObdGatherDataDto gatherData = null;
						
						//行驶轨迹  定位点List
						List<ObdGatherDataDto> gatherDatas = new ArrayList<ObdGatherDataDto>();
						for(int i = 0; i < obdGatherInfo.size(); i++){
							gatherInfo = obdGatherInfo.get(i);
							gatherData = new ObdGatherDataDto();
							
							gatherData.setAlt(gatherInfo.getAlt());
							gatherData.setGnssTime(gatherInfo.getGnsstime());
							gatherData.setHead(gatherInfo.getHead());
							gatherData.setLat(new BigDecimal(gatherInfo.getLat()));
							gatherData.setLon(new BigDecimal(gatherInfo.getLon()));
							gatherData.setMileage(gatherInfo.getMileage());
							gatherData.setObdSpd(gatherInfo.getObdSpd());
							gatherData.setSpd(gatherInfo.getSpd());
							
							gatherDatas.add(gatherData);
						}
						ptv.setObdGathers(JSONHelper.toJSONString(gatherDatas));
					}
				}
			}
		}
		req.setAttribute("info", ptv);
		return new ModelAndView("mpage/manage/driveRecods/playbackTrack");
	}
	
	@RequestMapping(params = "jumpToplaybackTrackPageByObd")
	public ModelAndView jumpToplaybackTrackPageByObd(@RequestParam(value = "id", required = true)String id, HttpServletRequest req) {
		ObdDriveRecodsEntity dr = systemService.get(ObdDriveRecodsEntity.class, id);
		PlaybackTrackVo ptv = new PlaybackTrackVo();
		if(dr != null){
			if(dr.getCarId() != null){
				CarInfoEntity carInfo = carInfoService.get(CarInfoEntity.class, dr.getCarId());
				
				
				
				ptv.setDriveStartTime(dr.getDriveStartTime());
				ptv.setDriveEndTime(dr.getDriveEndTime());
				ptv.setMileage(dr.getMileage());

				if(carInfo != null){					
					ptv.setPlateNo(carInfo.getPlateNo());
					ptv.setType(carInfo.getType());
					
					List<ObdGatherInfoEntity> obdGatherInfo = obdGatherInfoService.getDatasByTimeQuantum(carInfo.getObdId(), dr.getDriveStartTime(), dr.getDriveEndTime());
					if(obdGatherInfo != null && !obdGatherInfo.isEmpty()){
						ObdGatherInfoEntity gatherInfo = null;
						ObdGatherDataDto gatherData = null;
						
						//行驶轨迹  定位点List
						List<ObdGatherDataDto> gatherDatas = new ArrayList<ObdGatherDataDto>();
						for(int i = 0; i < obdGatherInfo.size(); i++){
							gatherInfo = obdGatherInfo.get(i);
							gatherData = new ObdGatherDataDto();
							
							gatherData.setAlt(gatherInfo.getAlt());
							gatherData.setGnssTime(gatherInfo.getGnsstime());
							gatherData.setHead(gatherInfo.getHead());
							
							DecimalFormat df = new DecimalFormat("0.000000");
							
							gatherData.setLat(new BigDecimal(df.format(gatherInfo.getLat())));
							gatherData.setLon(new BigDecimal(df.format(gatherInfo.getLon())));
							gatherData.setMileage(gatherInfo.getMileage());
							gatherData.setObdSpd(gatherInfo.getObdSpd());
							gatherData.setSpd(gatherInfo.getSpd());
							
							gatherDatas.add(gatherData);
						}
						ptv.setObdGathers(JSONHelper.toJSONString(gatherDatas));
					}
				}
			}
		}
		req.setAttribute("info", ptv);
		return new ModelAndView("mpage/manage/driveRecods/playbackTrack");
	}
	
	@RequestMapping(params = "addorupdateByObd")
	public ModelAndView addorupdateByObd(ObdDriveRecodsEntity obdDriveRecods, HttpServletRequest req) {
		String viewName = "mpage/manage/driveRecods/driveRecods";
		if (StringUtil.isNotEmpty(obdDriveRecods.getId())) {
			ObdDriveDTO obdDrive = new ObdDriveDTO();
			obdDriveRecods = obdDriveRecodsService.getEntity(ObdDriveRecodsEntity.class, obdDriveRecods.getId());
			AgencyInfoEntity agencyInfo = agencyInfoService.getEntity(AgencyInfoEntity.class, obdDriveRecods.getAgencyId());
			CarInfoEntity carInfo = carInfoService.getEntity(CarInfoEntity.class, obdDriveRecods.getCarId());
			obdDriveRecods.setCustomerId(obdDriveRecods.getCustomerId().replace("/", ""));
			if(StringUtil.isNotEmpty(obdDriveRecods.getCustomerId())){
				CustomerInfoEntity customer = customerInfoService.getEntity(CustomerInfoEntity.class,obdDriveRecods.getCustomerId() );
				obdDrive.setCustomerName(customer.getName());
			}
			obdDriveRecods.setSalesmanId((obdDriveRecods.getSalesmanId().replace("/", "")));
			if(StringUtil.isNotEmpty(obdDriveRecods.getSalesmanId())){
				SalesmanInfoEntity salesman = salesmanInfoService.getEntity(SalesmanInfoEntity.class, obdDriveRecods.getSalesmanId());
				obdDrive.setSalesmanName(salesman.getName());
			}
			
			obdDrive.setAgencyName(agencyInfo.getName());
			obdDrive.setCarType(carInfo.getType());
			obdDrive.setContractPicPath(obdDriveRecods.getContractPicPath());
			
			obdDrive.setDriveEndTime(obdDriveRecods.getDriveEndTime());
			obdDrive.setDriveStartTime(obdDriveRecods.getDriveStartTime());
			obdDrive.setEndPicPath(obdDriveRecods.getEndPicPath());
			obdDrive.setMileage(obdDriveRecods.getMileage());
			
			req.setAttribute("driveRecodsPage", obdDrive);
			/*if (!ArrayUtils.contains(DriveRecodsStatusNew.statusDescr(null), obdDriveRecods.getStatus())) {
				viewName += "0";//如果没有试驾信息就跳转到预约详情页
			}*/
		}
		return new ModelAndView(viewName);
	}
	
	private ProjectionList getProjectionList() {
		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("id").as("id"));
		return pList;
	}
	
}
