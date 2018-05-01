package com.mtestdrive.web.manage;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.dto.CarInfoDto;
import com.mtestdrive.dto.YearMonthDto;
import com.mtestdrive.entity.AgencyInfoEntity;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.ObdGatherInfoEntity;
import com.mtestdrive.service.AgencyInfoServiceI;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.ObdGatherInfoServiceI;
import com.mtestdrive.vo.MonitoringVo;

/**   
 * @Title: Controller
 * @Description: 车辆信息
 * @author zhangdaihao
 * @date 2017-03-10 17:28:07
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/carInfoController")
public class CarInfoController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CarInfoController.class);

	@Autowired
	private CarInfoServiceI carInfoService;
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private ObdGatherInfoServiceI obdGatherInfoService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private AgencyInfoServiceI agencyInfoService;
	
	
	/**
	 * @Title: jumpToTravelingTrackPage   
	 * @Description: 跳转到行驶轨迹页面   
	 * @param: @param obdId
	 * @param: @return      
	 * @return: ModelAndView      
	 * @throws
	 */
	@RequestMapping(params = "jumpToTravelingTrackPage")
	public ModelAndView jumpToTravelingTrackPage(String obdId){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mpage/manage/carInfo/travelingTrackPage");
		return mv;
	}
	
	
	/**
	 * @Title: getCarByType   
	 * @Description: 根据车牌得到当前管理员所在4S店内所有车辆
	 * @param: @param type
	 * @param: @param request
	 * @param: @return      
	 * @return: AjaxJson      
	 * @throws
	 */
	@RequestMapping(params = "getCarByType")
	@ResponseBody
	public AjaxJson getCarByType(String type, HttpServletRequest request) {
		AjaxJson aj = new AjaxJson();
		
		aj.setObj(carInfoService.getCarByType(ResourceUtil.getSessionUserName().getDepartid(), type));
		
		//systemService.addLog("查询车型为 "+type+" 的车辆", Globals.Log_Type_OTHER, Globals.Log_Leavel_INFO);
		
		return aj;
	}
	
	
	/**
	 * @Title: jumpToMonitoringPage   
	 * @Description: 跳转到车辆监控页面
	 * @param: @param id
	 * @param: @param req
	 * @param: @return      
	 * @return: ModelAndView      
	 * @throws
	 */
	@RequestMapping(params = "jumpToMonitoringPage")
	public ModelAndView jumpToMonitoringPage(String id, HttpServletRequest req) {
		ModelAndView view = new ModelAndView();
		if(StringUtil.isNotEmpty(id)){
			CarInfoEntity carInfo = carInfoService.get(CarInfoEntity.class, id);
			if(carInfo != null){
				MonitoringVo mv = new MonitoringVo();
				//车辆基本信息
				mv.setCode(carInfo.getCode());
				mv.setDriveTotal(carInfo.getDriveTotal());
				mv.setType(carInfo.getType());
				mv.setVin(carInfo.getVin());
				mv.setName(carInfo.getName());
				mv.setPlateNo(carInfo.getPlateNo());
				mv.setPicPath(carInfo.getPicPath());
				mv.setSaleYear(carInfo.getSaleYear());
				mv.setStatus(carInfo.getStatus());
				
				//OBD数据
				ObdGatherInfoEntity og = obdGatherInfoService.getLastData(carInfo.getObdId());
				mv.setLatitude(og.getLat());
				mv.setLongitude(og.getLon());
				mv.setHead(og.getHead());
				mv.setSpd(og.getSpd());
				mv.setObdspd(og.getObdSpd());
				mv.setAlt(og.getAlt());
				mv.setMileage(og.getMileage());
				view.addObject("mvInfo", mv);
			}
			
		}
		view.setViewName("mpage/manage/carInfo/carMonitoring");
		return view;
	}
	
	
	@RequestMapping(params = "obd")
	public ModelAndView obd(String id, HttpServletRequest req) {
		ModelAndView view = new ModelAndView();
		if(StringUtil.isNotEmpty(id)){
			CarInfoEntity carInfo = carInfoService.get(CarInfoEntity.class, id);
			if(carInfo != null){
				view.addObject("carInfo", carInfo);
			}
			
		}
		view.setViewName("mpage/manage/carInfo/carObdId");
		return view;
	}
	
	/**
	 * 车辆信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("mpage/manage/carInfo/carInfoList");
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
	public void datagrid(CarInfoDto carInfo, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		List<TSRoleUser> sRoleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", ResourceUtil.getSessionUserName().getId());
		List<String> roleCodes = new ArrayList<String>(sRoleUser.size());
		for (int i = 0; i < sRoleUser.size(); i++) {
			roleCodes.add(sRoleUser.get(i).getTSRole().getRoleCode());
		}
		if (!roleCodes.contains("superAdmin")) {
			AgencyInfoEntity ae = new AgencyInfoEntity();
			ae.setId(ResourceUtil.getSessionUserName().getDepartid());
			carInfo.setAgency(ae);
		}
		CriteriaQuery cq = new CriteriaQuery(CarInfoEntity.class, dataGrid);
		String dealerName = request.getParameter("agency.name");
		String region = request.getParameter("agency.regionId");
		List<Object> idList = null;
		if (StringUtil.isNotEmpty(region)) {// 根据区域名称搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(AgencyInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.eq("regionId", region));

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
				cq.add(Restrictions.in("agency.id", idList));
			}
		}
		//加上经销商ID
		if(ResourceUtil.getSessionUserName().getUserKey().equals("超级管理员")){
			
		}else if(ResourceUtil.getSessionUserName().getUserKey().equals("超级系统管理员")){
			
		}else{
			cq.add(Restrictions.eq("agency.id", ResourceUtil.getSessionUserName().getDepartid()));
		}
		
		if (StringUtil.isNotEmpty(dealerName)) {
			// 根据经销商名称模糊搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(AgencyInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.like("name", carInfoService.getLikeStr(dealerName)));
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
				cq.add(Restrictions.in("agency.id", idList));
			}
		}
		
		String plateNo = request.getParameter("plateNo");
		String type = request.getParameter("type");
		String status = request.getParameter("status");
		YearMonthDto yearMonth = (YearMonthDto) request.getSession().getAttribute("yearMonth");
		if(yearMonth != null){
			carInfo.setMonth(yearMonth.getYear() + "-" + yearMonth.getMonth());
			carInfo.setType(yearMonth.getType());
		}
		
		if(StringUtil.isNotEmpty(plateNo)){
			cq.add(Restrictions.like("plateNo", carInfoService.getLikeStr(plateNo)));
		}
		if(StringUtil.isNotEmpty(type)){
			cq.add(Restrictions.eq("type", type));
		}
		if(StringUtil.isNotEmpty(status)){
			cq.add(Restrictions.eq("status", Integer.parseInt(status)));
		}
		
		cq.add(Restrictions.ne("status", ConstantStatus.INVALID));
		cq.addOrder("createTime", SortDirection.desc);
		//查询条件组装
		/*org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, carInfo, request.getParameterMap());*/
		//this.carInfoService.getDataGridReturn(cq, true);
		if(StringUtil.isNotEmpty(carInfo.getMonth())){
			carInfo.setStatus(ConstantStatus.INVALID);
			carInfoService.datagrid(carInfo, dataGrid);
		}else{
			this.carInfoService.getDataGridReturn(cq, true);
		}
		//carInfoService.datagrid(carInfo, dataGrid);
		TagUtil.datagrid(response, dataGrid);
	}

	
	
	@RequestMapping(params = "exportXls")
	public String exportXls(CarInfoDto carInfo, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		List<TSRoleUser> sRoleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", ResourceUtil.getSessionUserName().getId());
		List<String> roleCodes = new ArrayList<String>(sRoleUser.size());
		for (int i = 0; i < sRoleUser.size(); i++) {
			roleCodes.add(sRoleUser.get(i).getTSRole().getRoleCode());
		}
		if (!roleCodes.contains("superAdmin")) {
			AgencyInfoEntity ae = new AgencyInfoEntity();
			ae.setId(ResourceUtil.getSessionUserName().getDepartid());
			carInfo.setAgency(ae);
		}
		CriteriaQuery cq = new CriteriaQuery(CarInfoEntity.class, dataGrid);
		String dealerName = request.getParameter("agency.name");
		String region = request.getParameter("agency.regionId");
		List<Object> idList = null;
		if (StringUtil.isNotEmpty(region)) {// 根据区域名称搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(AgencyInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.eq("regionId", region));

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
				cq.add(Restrictions.in("agency.id", idList));
			}
		}
		//加上经销商ID
		if(ResourceUtil.getSessionUserName().getUserKey().equals("超级管理员")){
			
		}else if(ResourceUtil.getSessionUserName().getUserKey().equals("超级系统管理员")){
			
		}else{
			cq.add(Restrictions.eq("agency.id", ResourceUtil.getSessionUserName().getDepartid()));
		}
		
		if (StringUtil.isNotEmpty(dealerName)) {
			// 根据经销商名称模糊搜索
			DetachedCriteria salesDc = DetachedCriteria.forClass(AgencyInfoEntity.class);
			salesDc.add(Restrictions.eq("status", ConstantStatus.VALID));
			salesDc.add(Restrictions.like("name", carInfoService.getLikeStr(dealerName)));
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
				cq.add(Restrictions.in("agency.id", idList));
			}
		}
		
		String plateNo = request.getParameter("plateNo");
		String type = request.getParameter("type");
		String status = request.getParameter("status");
		YearMonthDto yearMonth = (YearMonthDto) request.getSession().getAttribute("yearMonth");
		if(yearMonth != null){
			carInfo.setMonth(yearMonth.getYear() + "-" + yearMonth.getMonth());
			carInfo.setType(yearMonth.getType());
		}
		
		if(StringUtil.isNotEmpty(plateNo)){
			cq.add(Restrictions.like("plateNo", carInfoService.getLikeStr(plateNo)));
		}
		if(StringUtil.isNotEmpty(type)){
			cq.add(Restrictions.eq("type", type));
		}
		if(StringUtil.isNotEmpty(status)){
			cq.add(Restrictions.eq("status", Integer.parseInt(status)));
		}
		
		cq.add(Restrictions.ne("status", ConstantStatus.INVALID));
		cq.addOrder("createTime", SortDirection.desc);
		
		if(StringUtil.isNotEmpty(carInfo.getMonth())){
			carInfo.setStatus(ConstantStatus.INVALID);
			carInfoService.datagrid(carInfo, dataGrid);
		}else{
			this.carInfoService.getDataGridReturn(cq, true);
		}
		
		List<CarInfoEntity> CarInfoList = dataGrid.getResults();
		for (CarInfoEntity carInfoEntity : CarInfoList) {
			carInfoEntity.setDearlerGroup(carInfoEntity.getAgency().getDearlerGroup());
		}
		
		modelMap.put(NormalExcelConstants.FILE_NAME,"非活跃车辆");
		modelMap.put(NormalExcelConstants.CLASS,CarInfoEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("非活跃车辆列表", "导出人:"+ ResourceUtil.getSessionUserName().getRealName(),
				"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,CarInfoList);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 方法描述:  查看成员列表
	 * 作    者： 张丹超
	 * @param request
	 * @param departid
	 * @return 
	 * 返回类型： ModelAndView
	 */
	@RequestMapping(params = "reportRecordsList")
	public ModelAndView reportRecordsList(HttpServletRequest request, String carId) {
		request.setAttribute("carId", carId);
		return new ModelAndView("mpage/manage/carInfo/reportRecords");
	}
	
	/**
	 * 删除车辆信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CarInfoEntity carInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		carInfo = systemService.getEntity(CarInfoEntity.class, carInfo.getId());
		carInfo.setObdId(null);//删除车辆时，OBD_ID需要置Null，否则新车装上该设备时会有重复OBD
		carInfo.setStatus(ConstantStatus.INVALID);
		message = "车辆信息删除成功";
		carInfoService.saveOrUpdate(carInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}

	
	@RequestMapping(params = "obdSaveUser")
	@ResponseBody
	public AjaxJson obdSaveUser(CarInfoEntity carInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
			message = "obd设备ID添加成功";
			CarInfoEntity t = carInfoService.get(CarInfoEntity.class, carInfo.getId());
			try {
				t.setObdId(carInfo.getObdId());
				carInfo.setUpdateTime(new Date());
				carInfoService.saveOrUpdate(t);
			} catch (Exception e) {
				e.printStackTrace();
				message = "obd设备ID添加失败";
			}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加车辆信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CarInfoEntity carInfo, HttpServletRequest request) {
		
		
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(carInfo.getId())) {
			message = "车辆信息更新成功";
			CarInfoEntity t = carInfoService.get(CarInfoEntity.class, carInfo.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(carInfo, t);
				carInfo.setUpdateTime(new Date());
				carInfoService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "车辆信息更新失败";
			}
		} else {
			message = "车辆信息添加成功";
			TSUser user = ResourceUtil.getSessionUserName();
			String departid = user.getDepartid();
			AgencyInfoEntity agency = agencyInfoService.get(AgencyInfoEntity.class, departid);
			carInfo.setAgency(agency);			
			/*String id = request.getParameter("id");
			// uploadFile.setBasePath("images/accordion");
			UploadFile uploadFile = new UploadFile(request);
			uploadFile.setCusPath("plug-in/accordion/images");
			uploadFile.setExtend("extend");
			uploadFile.setTitleField("iconclas");
			uploadFile.setRealPath("iconPath");
			uploadFile.setByteField("iconContent");
			uploadFile.setRename(false);
			systemService.uploadFile(uploadFile);
			carInfo.setPicPath(uploadFile.getRealPath());*/
			carInfo.setCreateTime(new Date());
			carInfoService.save(carInfo);
			
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 车辆信息列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CarInfoEntity carInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(carInfo.getId())) {
			carInfo = carInfoService.getEntity(CarInfoEntity.class, carInfo.getId());
			req.setAttribute("carInfoPage", carInfo);
		}
		return new ModelAndView("mpage/manage/carInfo/carInfo");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<CarInfoEntity> list() {
		List<CarInfoEntity> listCarInfos=carInfoService.getList(CarInfoEntity.class);
		return listCarInfos;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		CarInfoEntity task = carInfoService.get(CarInfoEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody CarInfoEntity carInfo, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<CarInfoEntity>> failures = validator.validate(carInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		carInfoService.save(carInfo);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = carInfo.getId();
		URI uri = uriBuilder.path("/rest/carInfoController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody CarInfoEntity carInfo) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<CarInfoEntity>> failures = validator.validate(carInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		carInfoService.saveOrUpdate(carInfo);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		carInfoService.deleteEntityById(CarInfoEntity.class, id);
	}
	
	private ProjectionList getProjectionList() {
		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("id").as("id"));
		return pList;
	}
}
