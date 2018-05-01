package com.mtestdrive.web.app;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.mtestdrive.MaseratiConstants.CarStatus;
import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.MaseratiConstants.DriveRecodsStatus;
import com.mtestdrive.MaseratiConstants.ReportStatus;
import com.mtestdrive.dto.DriveRecodsDto;
import com.mtestdrive.dto.ObdGatherDataDto;
import com.mtestdrive.entity.AgencyInfoEntity;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.CustomerInfoEntity;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.entity.ObdGatherInfoEntity;
import com.mtestdrive.entity.ReportRecordsEntity;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.CustomerInfoServiceI;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.service.ObdGatherInfoServiceI;
import com.mtestdrive.service.ReportRecordsServiceI;
import com.mtestdrive.vo.CarArrangeVo;
import com.mtestdrive.vo.DriveRecodsVo;
import com.mtestdrive.vo.SalesmanInfoVo;

/**
 * @Title: Action
 * @Description: 出车明细
 * @author zhangdaihao
 * @date 2017-03-10 17:36:12
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/driveRecodsAction")
public class DriveRecodsAction extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DriveRecodsAction.class);

	@Autowired
	private DriveRecodsServiceI driveRecodsService;
	@Autowired
	private CarInfoServiceI carInfoService;
	@Autowired
	private CustomerInfoServiceI customerInfoService;
	@Autowired
	private ObdGatherInfoServiceI obdGatherInfoService;
	@Autowired
	private Validator validator;
	@Autowired
	private ReportRecordsServiceI reportRecordsService;

	
	/**
	 * @Title: 车辆是否启动
	 * @param: @param recodsId
	 * @param: @return      
	 * @return: AjaxJson      
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(params = "checkCarStatus", method = RequestMethod.POST)
	public AjaxJson checkCarStatus(String recodsId){
		AjaxJson aj = new AjaxJson();
		if(StringUtil.isEmpty(recodsId)){
			aj.setSuccess(false);
			aj.setMsg("recodsId 不能为空");
		}else{
			DriveRecodsEntity dr = driveRecodsService.get(DriveRecodsEntity.class, recodsId);
			if(dr == null){
				aj.setSuccess(false);
				aj.setMsg("预约不存在");
			}else{
				CarInfoEntity car = carInfoService.get(CarInfoEntity.class, dr.getCarId());
				if(car == null){
					aj.setSuccess(false);
					aj.setMsg("预约车辆不存在");
				}else{
					String obdId = car.getObdId();
					if(StringUtil.isEmpty(obdId)){
						aj.setSuccess(false);
						aj.setMsg("预约车辆没有安装OBD设备");
					}else{
						 Calendar nowTime = Calendar.getInstance();
						 nowTime.add(Calendar.MINUTE, 1);
						  
						 Calendar nowTime2 = Calendar.getInstance();
						 nowTime2.add(Calendar.MINUTE, -1);
						
						List<ObdGatherInfoEntity> obdGatherInfo = obdGatherInfoService
								.getDatasByTimeQuantum(obdId, nowTime2.getTime(), nowTime.getTime());
						if(ListUtils.isNullOrEmpty(obdGatherInfo)){
							aj.setSuccess(false);
							aj.setMsg("车辆尚未启动无法开始试驾！");
						}else{
							aj.setSuccess(true);
						}
					}
				}
			}
		}
		return aj;
	}
	
	/**
	 * @Title: cancleOrder   
	 * @Description: 取消预约   
	 * @param: @param orderId
	 * @param: @return      
	 * @return: AjaxJson      
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(params = "cancleOrder", method = RequestMethod.POST)
	public AjaxJson cancleOrder(String orderId){
		AjaxJson aj = new AjaxJson();
		if(StringUtil.isEmpty(orderId)){
			aj.setSuccess(false);
			aj.setMsg("orderId 不能为空");
		}else{
			DriveRecodsEntity dr = driveRecodsService.get(DriveRecodsEntity.class, orderId);
			if(dr == null){
				aj.setSuccess(false);
				aj.setMsg("预约不存在");
			}else{
				dr.setStatus(DriveRecodsStatus.CANCEL);
				driveRecodsService.saveOrUpdate(dr);
				aj.setSuccess(true);
				aj.setMsg("操作成功");
			}
		}
		return aj;
	}
	
	/**
	 * @Title: jumpToplaybackTrackPage   
	 * @Description: 根据试驾ID，返回路线数据
	 * @param: @param id
	 * @param: @param req
	 * @param: @return      
	 * @return: AjaxJson      
	 * @throws   
	 * @author: mengtaocui
	 */
	@RequestMapping(params = "getGatherInfoByRecodId")
	@ResponseBody
	public AjaxJson getGatherInfoByRecodId(@RequestParam(value = "recodsId", required = true) String id,
			HttpServletRequest req) {
		AjaxJson aj = new AjaxJson();
		DriveRecodsEntity dr = driveRecodsService.get(DriveRecodsEntity.class, id);
		if (dr != null) {
			if (dr.getCarId() != null) {
				CarInfoEntity carInfo = carInfoService.get(CarInfoEntity.class, dr.getCarId());
				if (carInfo != null) {

					List<ObdGatherInfoEntity> obdGatherInfo = obdGatherInfoService
							.getDatasByTimeQuantum(carInfo.getObdId(), dr.getDriveStartTime(), dr.getDriveEndTime());
					if (obdGatherInfo != null && !obdGatherInfo.isEmpty()) {
						ObdGatherInfoEntity gatherInfo = null;
						ObdGatherDataDto gatherData = null;

						// 行驶轨迹 定位点List
						List<ObdGatherDataDto> gatherDatas = new ArrayList<ObdGatherDataDto>();
						for (int i = 0; i < obdGatherInfo.size(); i++) {
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
						aj.setObj(gatherDatas);
					}
				}
			}
		}
		return aj;
	}

	@RequestMapping(params = "index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, DriveRecodsDto drive) {
		List<DriveRecodsVo> driveRecodses = null;
		SalesmanInfoVo salesmanInfo = (SalesmanInfoVo) request.getSession().getAttribute("SalesmanInfo");
		try {
			CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class);
			cq.eq("salesman.id", salesmanInfo.getId());
			if (StringUtil.isNotEmpty(drive.getCarId())) {
				cq.eq("carId", drive.getCarId());
				request.setAttribute("carId", drive.getCarId());
			} else {
				String carType = request.getParameter("carType");
				if (StringUtil.isNotEmpty(carType)) {
					List<CarInfoEntity> cars = carInfoService.getCarByType(salesmanInfo.getAgencyId(), carType);
					if (!ListUtils.isNullOrEmpty(cars)) {
						String carIds[] = new String[cars.size()];
						for (int i = 0; i < carIds.length; i++) {
							carIds[i] = cars.get(i).getId();
						}
						cq.in("carId", carIds);
					} else {
						cq.eq("carId", "null");
					}
					request.setAttribute("carType", carType);
				}
			}
			if (StringUtil.isNotEmpty(drive.getCustomerName())) {
				cq.like("customer.name", "%" + drive.getCustomerName() + "%");
			}
			if (StringUtil.isNotEmpty(drive.getCustomerMobile())) {
				cq.eq("customer.mobile", drive.getCustomerMobile());
			}
			if (drive.getStartTime() != null) {
				Date orderStartTime = drive.getStartTime();
				cq.ge("orderStartTime", DateUtils.str2Date(DateUtils.formatDate(orderStartTime), DateUtils.date_sdf));
				Date orderEndTime = DateUtils.addDate(
						DateUtils.formatDate(drive.getEndTime() == null ? orderStartTime : drive.getEndTime()), 1);
				cq.lt("orderEndTime", orderEndTime);
			}
			cq.add();
			cq.addOrder("orderStartTime", SortDirection.asc);
			List<DriveRecodsEntity> listDriveRecods = driveRecodsService.getListByCriteriaQuery(cq, false);
			if (!ListUtils.isNullOrEmpty(listDriveRecods)) {
				driveRecodses = ListUtils.copyTo(listDriveRecods, DriveRecodsVo.class);
				for (DriveRecodsVo driveRecodsVo : driveRecodses) {
					if (StringUtils.isNotEmpty(driveRecodsVo.getCarId())) {
						CarInfoEntity car = carInfoService.get(CarInfoEntity.class, driveRecodsVo.getCarId());
						if (car != null) {
							MyBeanUtils.copyBean2Bean(driveRecodsVo.getCar(), car);
						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 潜在试驾
		List<DriveRecodsVo> potentialDrive = new ArrayList<DriveRecodsVo>();
		// 已确认
		List<DriveRecodsVo> confirmedDrive = new ArrayList<DriveRecodsVo>();
		// 手续办理
		List<DriveRecodsVo> formalitiesDrive = new ArrayList<DriveRecodsVo>();
		// 开始试驾
		List<DriveRecodsVo> startDrive = new ArrayList<DriveRecodsVo>();
		// 正在试驾
		List<DriveRecodsVo> testDrive = new ArrayList<DriveRecodsVo>();
		// 试驾也完成
		List<DriveRecodsVo> finishDrive = new ArrayList<DriveRecodsVo>();
		if (!ListUtils.isNullOrEmpty(driveRecodses)) {
			for (DriveRecodsVo driveRecodsEntity : driveRecodses) {
				Integer status = driveRecodsEntity.getStatus();
				if (status == DriveRecodsStatus.ASK) {
					potentialDrive.add(driveRecodsEntity);
				}
				if (status == DriveRecodsStatus.ANSWER) {
					confirmedDrive.add(driveRecodsEntity);
				}
				if (status == DriveRecodsStatus.CONFIRMED || status == DriveRecodsStatus.FORMALITIES) {
					formalitiesDrive.add(driveRecodsEntity);
				}
				if (status == DriveRecodsStatus.FORMALITIES) {
					startDrive.add(driveRecodsEntity);
				}
				if (status == DriveRecodsStatus.UNDERWAY) {
					testDrive.add(driveRecodsEntity);
				}
				if (status == DriveRecodsStatus.COMPLETE  || status == DriveRecodsStatus.GENERATEDREPORT) {
					finishDrive.add(driveRecodsEntity);
				}
			}
		}
		request.setAttribute("potentialDrive", potentialDrive);
		request.setAttribute("confirmedDrive", confirmedDrive);
		request.setAttribute("formalitiesDrive", formalitiesDrive);
		request.setAttribute("startDrive", startDrive);
		request.setAttribute("testDrive", testDrive);
		request.setAttribute("finishDrive", finishDrive);

		// 查询车牌
		List<CarInfoEntity> carList = carInfoService.findByProperty(CarInfoEntity.class, "agency.id",
				salesmanInfo.getAgencyId());
		request.setAttribute("carList", carList);

		// 查询车型
		List<Object[]> types = carInfoService.getCarTypes();
		request.setAttribute("types", types);

		return new ModelAndView("driveRecods/index");
	}

	@RequestMapping(params = "add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		String id = request.getParameter("id");
		String customerId = request.getParameter("customerId");
		DriveRecodsVo driveRecodsVo = null;
		if (StringUtil.isNotEmpty(id)) {
			DriveRecodsEntity driveRecods = driveRecodsService.get(DriveRecodsEntity.class, id);
			driveRecodsVo = new DriveRecodsVo();
			try {
				MyBeanUtils.copyBean2Bean(driveRecodsVo, driveRecods);
				//BeanUtils.copyProperties(driveRecodsVo, driveRecods);
				if (StringUtils.isNotEmpty(driveRecodsVo.getCarId())) {
					CarInfoEntity car = carInfoService.get(CarInfoEntity.class, driveRecodsVo.getCarId());
					if (car != null) {
						MyBeanUtils.copyBean2Bean(driveRecodsVo.getCar(), car);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (StringUtil.isNotEmpty(customerId)) {
			CustomerInfoEntity customer = customerInfoService.get(CustomerInfoEntity.class, customerId);
			driveRecodsVo = new DriveRecodsVo();
			driveRecodsVo.setCustomer(customer);
		}
		request.setAttribute("driveRecodsVo", driveRecodsVo);

		// 查询车型
		DetachedCriteria dc = DetachedCriteria.forClass(CarInfoEntity.class);
		dc.add(Restrictions.eq("agency.id", ((CustomerInfoEntity)driveRecodsVo.getCustomer()).getAgencyId()));
		dc.add(Restrictions.eq("status", CarStatus.NO_USED));

		logger.debug("agency_id:" +((CustomerInfoEntity)driveRecodsVo.getCustomer()).getAgencyId());
		logger.debug("status:" + CarStatus.NO_USED);
				
		List<CarInfoEntity> cars = carInfoService.findByDetached(dc);
		logger.info("查询车辆信息集合size:" + cars.size());
		for (CarInfoEntity car :cars){
			logger.info("car id:" + car.getId()+"car type:"+car.getType()+"car plateno:"+car.getPlateNo());
		}

		
		List<Object[]> types = new ArrayList<Object[]>();//carInfoService.getCarTypes();
		for (int i = 0; i < cars.size(); i++) {
			CarInfoEntity car = cars.get(i);
			Object type[] = {car.getId(), car.getType()+":"+car.getPlateNo()};
			types.add(type);
		}
		request.setAttribute("types", types);

		return new ModelAndView("driveRecods/add");
	}

	@RequestMapping(params = "check", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson check(@RequestBody DriveRecodsDto drive){
		String message = null;
		AjaxJson j = new AjaxJson();
		if(StringUtil.isNotEmpty(drive.getCarId())){
			//获取当日试驾信息的时间占有
			CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class);
			cq.ge("orderStartTime", DateUtils.str2Date(DateUtils.formatDate(drive.getStartTime()), DateUtils.date_sdf));
			cq.lt("orderEndTime", DateUtils.addDate(DateUtils.formatDate(drive.getStartTime()), 1));
			cq.eq("carId", drive.getCarId());
			if(StringUtil.isNotEmpty(drive.getId())){
				cq.notEq("id", drive.getId());
			}
			cq.add();
			List<DriveRecodsEntity> listDriveRecods = driveRecodsService.getListByCriteriaQuery(cq, false);
			//填写的开始时间
			long startTime = drive.getStartTime().getTime();
			//填写的结束时间
			long endTime = drive.getEndTime().getTime()-1000L;
			for (DriveRecodsEntity driveRecodsEntity : listDriveRecods) {
				
				//已有的开始时间 和结束时间
				long orderStartTime = driveRecodsEntity.getOrderStartTime().getTime();
				long orderEndTime = driveRecodsEntity.getOrderEndTime().getTime()-1000L;//减一秒
				if(startTime>=orderStartTime && startTime<=orderEndTime){
					//已占有
					message="1";//此时车辆已被预约，请重新选择！
					j.setObj(message);
					return j;
				}
				
				if(endTime>=orderStartTime && endTime<=orderEndTime){
					message="1";//此时车辆已被预约，请重新选择！
					j.setObj(message);
					return j;
				}
				
			}

			//查询车辆报备信息
			DetachedCriteria reDc = DetachedCriteria.forClass(ReportRecordsEntity.class);
			reDc.add(Restrictions.ne("status", ReportStatus.FINISHED));
			reDc.add(Restrictions.eq("carId", drive.getCarId()));
			
			List<ReportRecordsEntity> reportList = reportRecordsService.findByDetached(reDc);
			for (ReportRecordsEntity reportRecordsEntity : reportList) {
				//报备的开始时间和结束时间
				long reportStartTime = reportRecordsEntity.getStartTime().getTime();
				long reportEndTime = reportRecordsEntity.getEndTime().getTime();

                //判断预约时间段与报备时间段是否有重合？
                // (报备开始时间<=预约开始时间<=报备结束时间) or (报备开始时间<=预约结束时间<=报备结束时间)
				if((reportStartTime<=startTime && startTime<=reportEndTime)
                    || (reportStartTime<=endTime && endTime<=reportEndTime)){
					message="3";//此时车辆处在报备期间，请重新选择！
					j.setObj(message);
					return j;
				}

			}
		}else{
			//参数问题
			message="0";
			j.setObj(message);
			return j;
		}
		
		
		message="2";
		j.setObj(message);
		return j;
	}
	
	@RequestMapping(params = "getBadTime", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson getBadTime(@RequestBody DriveRecodsDto drive, HttpServletRequest request) {
		//获取当日车辆预约信息
		CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class);
		cq.eq("carId", drive.getCarId());
		cq.ge("orderStartTime", DateUtils.str2Date(DateUtils.formatDate(drive.getOrderDate()), DateUtils.date_sdf));
		cq.lt("orderEndTime", DateUtils.addDate(DateUtils.formatDate(drive.getOrderDate()), 1));
		if(StringUtil.isNotEmpty(drive.getId())){
			cq.notEq("id", drive.getId());
		}
		cq.add();
		List<DriveRecodsEntity> listDriveRecods = driveRecodsService.getListByCriteriaQuery(cq, false);
		//获取当日车辆报备信息
		CriteriaQuery cq2 = new CriteriaQuery(ReportRecordsEntity.class);
		cq2.eq("carId", drive.getCarId());
		cq2.ge("startTime", DateUtils.str2Date(DateUtils.formatDate(drive.getOrderDate()), DateUtils.date_sdf));
		cq2.lt("endTime", DateUtils.addDate(DateUtils.formatDate(drive.getOrderDate()), 1));
		cq2.add();
		List<ReportRecordsEntity> reportRecordsList = reportRecordsService.getListByCriteriaQuery(cq2, false);
		List<CarArrangeVo> carArrangeVoList = new ArrayList<CarArrangeVo>();
		for (ReportRecordsEntity reportRecordsEntity : reportRecordsList) {
			CarArrangeVo carArrangeVo = new CarArrangeVo();
			carArrangeVo.setStartTime(reportRecordsEntity.getStartTime());
			carArrangeVo.setEndTime(reportRecordsEntity.getEndTime());
			carArrangeVo.setStatus(2);
			carArrangeVoList.add(carArrangeVo);
		}
		for (DriveRecodsEntity driveRecodsEntity : listDriveRecods) {
			CarArrangeVo carArrangeVo = new CarArrangeVo();
			carArrangeVo.setStartTime(driveRecodsEntity.getOrderStartTime());
			carArrangeVo.setEndTime(driveRecodsEntity.getOrderEndTime());
			carArrangeVo.setStatus(1);
			carArrangeVoList.add(carArrangeVo);
		}
		//排序
		Collections.sort(carArrangeVoList, new Comparator<CarArrangeVo>(){
			public int compare(CarArrangeVo o1, CarArrangeVo o2) {
				int i = o1.getStartTime().compareTo(o2.getStartTime());
				
				
				if(i>0){  
                    return 1;  
                }  
                if(i<0){  
                    return -1;  
                }  
                return 0; 
			}
		});
		AjaxJson j = new AjaxJson();
		j.setObj(carArrangeVoList);
		return j;
	}
	
	
	@RequestMapping(params = "save", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson save(@RequestBody DriveRecodsDto drive, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		SalesmanInfoVo salesmanInfo = (SalesmanInfoVo) request.getSession().getAttribute("SalesmanInfo");
		CarInfoEntity carInfo = customerInfoService.get(CarInfoEntity.class, drive.getCarId());
		if (StringUtil.isNotEmpty(drive.getId())) {
			DriveRecodsEntity t = driveRecodsService.get(DriveRecodsEntity.class, drive.getId());
			try {
				message = "修改成功！";
				t.setCarId(drive.getCarId());
				t.setOrderStartTime(drive.getStartTime());
				t.setOrderEndTime(drive.getEndTime());
				t.setUpdateTime(DateUtils.gettimestamp());
				t.setUpdateBy(salesmanInfo.getId());
				t.setCarType(carInfo.getType());
				driveRecodsService.saveOrUpdate(t);
				// systemService.addLog(message, Globals.Log_Type_UPDATE,
				// Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			DriveRecodsEntity t = new DriveRecodsEntity();
			try {
				
				SalesmanInfoEntity salesman = customerInfoService.get(SalesmanInfoEntity.class, salesmanInfo.getId());
				AgencyInfoEntity agency = customerInfoService.get(AgencyInfoEntity.class, salesman.getAgencyId());
				message = "添加成功！";
				CustomerInfoEntity customer = customerInfoService.get(CustomerInfoEntity.class, drive.getCustomerId());
				
				t.setAgency(agency);
				t.setSalesman(salesman);
				t.setCustomer(customer);
				t.setCarId(drive.getCarId());
				t.setOrderStartTime(drive.getStartTime());
				t.setOrderEndTime(drive.getEndTime());
				t.setStatus(1);
				t.setCreateTime(DateUtils.gettimestamp());
				t.setCreateBy(salesmanInfo.getId());
				t.setCarType(carInfo.getType());
				driveRecodsService.save(t);
				// systemService.addLog(message, Globals.Log_Type_INSERT,
				// Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return j;
	}

	@RequestMapping(params = "informations", method = RequestMethod.GET)
	public ModelAndView informations(HttpServletRequest request) {
		String id = request.getParameter("id");
		DriveRecodsVo driveRecodsVo = null;
		DriveRecodsEntity driveRecods = null;
		if (StringUtil.isNotEmpty(id)) {
			driveRecods = driveRecodsService.get(DriveRecodsEntity.class, id);
			driveRecodsVo = new DriveRecodsVo();
			try {
				MyBeanUtils.copyBean2Bean(driveRecodsVo, driveRecods);
				if (StringUtils.isNotEmpty(driveRecodsVo.getCarId())) {
					CarInfoEntity car = carInfoService.get(CarInfoEntity.class, driveRecodsVo.getCarId());
					if (car != null) {
						MyBeanUtils.copyBean2Bean(driveRecodsVo.getCar(), car);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("driveRecodsVo", driveRecodsVo);
		
		// 查询车型
		List<Object[]> types = carInfoService.getCarTypes();
		request.setAttribute("types", types);
		request.setAttribute("licensePicPath", driveRecods.getCustomer().getDrivingLicensePicPath());
		return new ModelAndView("driveRecods/informations");
	}

	@RequestMapping(params = "report", method = RequestMethod.GET)
	public ModelAndView report(HttpServletRequest request) {
		String id = request.getParameter("id");
		DriveRecodsVo driveRecodsVo = null;
		if (StringUtil.isNotEmpty(id)) {
			String endPicPath = request.getParameter("endPicPath");
			if(StringUtil.isNotEmpty(endPicPath)){
				try {
					endPicPath=new String(endPicPath.getBytes("ISO-8859-1"),"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			DriveRecodsEntity driveRecods = driveRecodsService.get(DriveRecodsEntity.class, id);
			if (StringUtil.isNotEmpty(endPicPath)) {
				driveRecods.setEndPicPath(endPicPath);
			}
			driveRecods.setStatus(DriveRecodsStatus.GENERATEDREPORT);
			carInfoService.saveOrUpdate(driveRecods);
			driveRecodsVo = new DriveRecodsVo();
			try {
				MyBeanUtils.copyBean2Bean(driveRecodsVo, driveRecods);
				if (StringUtils.isNotEmpty(driveRecodsVo.getCarId())) {
					CarInfoEntity car = carInfoService.get(CarInfoEntity.class, driveRecodsVo.getCarId());
					if (car != null) {
						MyBeanUtils.copyBean2Bean(driveRecodsVo.getCar(), car);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("driveRecodsVo", driveRecodsVo);
		Date endTime = driveRecodsVo.getDriveEndTime();
		Date startTime = driveRecodsVo.getDriveStartTime();
		long end = endTime==null?0L:endTime.getTime();
		long start = startTime==null?0L:startTime.getTime();
		long x = end-start;
		request.setAttribute("timeDifference", x);
		return new ModelAndView("driveRecods/report");
	}

	@RequestMapping(params = "picture", method = RequestMethod.GET)
	public ModelAndView picture(HttpServletRequest request) {
		String id = request.getParameter("driveId");
		DriveRecodsEntity driveRecodsEntity = driveRecodsService.get(DriveRecodsEntity.class, id);
		DriveRecodsVo driveRecodsVo = new DriveRecodsVo();
		try {
			MyBeanUtils.copyBean2Bean(driveRecodsVo, driveRecodsEntity);
			if (StringUtils.isNotEmpty(driveRecodsVo.getCarId())) {
				CarInfoEntity car = carInfoService.get(CarInfoEntity.class, driveRecodsVo.getCarId());
				if (car != null) {
					MyBeanUtils.copyBean2Bean(driveRecodsVo.getCar(), car);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("driveRecods", driveRecodsVo);
		return new ModelAndView("driveRecods/picture");
	}

	@RequestMapping(params = "management", method = RequestMethod.GET)
	public ModelAndView management(HttpServletRequest request) {
		//String id = request.getParameter("id");
		/*String picPath = request.getParameter("picPath");
		if(StringUtil.isNotEmpty(picPath)){
			try {
				picPath=new String(picPath.getBytes("ISO-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		DriveRecodsVo driveRecodsVo = driveRecodsService.attachPic(id, picPath);*/
		
		//request.setAttribute("driveRecodsVo", driveRecodsVo);
		return new ModelAndView("driveRecods/management");
	}

	@RequestMapping(params = "management", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson management(@RequestBody DriveRecodsDto drive, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(drive.getId()) && StringUtil.isNotEmpty(drive.getRouteId())) {
			DriveRecodsEntity t = driveRecodsService.get(DriveRecodsEntity.class, drive.getId());
			if (t != null) {
				CustomerInfoEntity customer = driveRecodsService.get(CustomerInfoEntity.class, t.getCustomer().getId());
				customer.setDrivingLicensePicPath(req.getParameter("drivingLicensePicPath"));
				driveRecodsService.updateEntitie(customer);
				
				
				t.setRouteId(drive.getRouteId());
				t.setTestDriveContractPicPath(drive.getTestDriveContractPicPath());
				t.setStatus(t.getStatus() + 1);
				driveRecodsService.saveOrUpdate(t);
			}
		}
		return j;
	}

	@RequestMapping(params = "confirm", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson confirm(@RequestBody DriveRecodsDto drive) {
		AjaxJson j = new AjaxJson();
		try {
			driveRecodsService.complete(drive.getId(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		DriveRecodsEntity task = driveRecodsService.get(DriveRecodsEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody DriveRecodsDto drive, UriComponentsBuilder uriBuilder) {
		DriveRecodsEntity driveRecods = new DriveRecodsEntity();
		CustomerInfoEntity customer = customerInfoService.get(CustomerInfoEntity.class, drive.getCustomerId());
		driveRecods.setCustomer(customer);
		driveRecods.setDriveStartTime(drive.getStartTime());
		driveRecods.setDriveEndTime(drive.getEndTime());
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<DriveRecodsEntity>> failures = validator.validate(driveRecods);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		// 保存
		driveRecodsService.save(driveRecods);
        logger.info("手工创建试驾预约记录成功："+driveRecods.toString());

		// 按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		// String id = driveRecods.getId();
		// URI uri = uriBuilder.path("/rest/driveRecodsAction/" +
		// id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		// headers.setLocation(uri);

		return new ResponseEntity(driveRecods, headers, HttpStatus.CREATED);
	}

	@RequestMapping(params = "update", method = { RequestMethod.PUT,
			RequestMethod.POST }, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody DriveRecodsDto drive) {
		DriveRecodsEntity driveRecods = driveRecodsService.get(DriveRecodsEntity.class, drive.getId());
		driveRecods.setDriveStartTime(drive.getStartTime());
		driveRecods.setDriveEndTime(drive.getEndTime());
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<DriveRecodsEntity>> failures = validator.validate(driveRecods);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		// 保存
		driveRecodsService.saveOrUpdate(driveRecods);

		// 按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		driveRecodsService.deleteEntityById(DriveRecodsEntity.class, id);
	}
	
	public static void main(String[] args){
		System.out.println("2017-06-02 13:00:00  Long:"+DateUtils.str2Date(DateUtils.formatDate("2017-06-02 13:00:00"), DateUtils.time_sdf).getTime());
		System.out.println("2017-06-02 14:00:00  Long:"+DateUtils.str2Date(DateUtils.formatDate("2017-06-02 14:00:00"), DateUtils.time_sdf).getTime());
		
		System.out.println("2017-06-02 13:10:45  Long:"+DateUtils.str2Date(DateUtils.formatDate("2017-06-02 13:10:45"), DateUtils.time_sdf).getTime());
		System.out.println("2017-06-02 13:40:59  Long:"+DateUtils.str2Date(DateUtils.formatDate("2017-06-02 13:40:59"), DateUtils.time_sdf).getTime());
	}
}
