package com.mtestdrive.web.app;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.dto.CarInfoDto;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.entity.ObdGatherInfoEntity;
import com.mtestdrive.entity.ReportRecordsEntity;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.service.ObdGatherInfoServiceI;
import com.mtestdrive.service.ReportRecordsServiceI;
import com.mtestdrive.vo.CarArrangeVo;
import com.mtestdrive.vo.CarInfoVo;
import com.mtestdrive.vo.DriveRecodsVo;
import com.mtestdrive.vo.SalesmanInfoVo;

/**
 * @Title: Action
 * @Description: 车辆信息
 * @author zhangdaihao
 * @date 2017-03-10 17:28:07
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/carInfoAction")
public class CarInfoAction extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CarInfoAction.class);

	@Autowired
	private CarInfoServiceI carInfoService;
	@Autowired
	private Validator validator;
	@Autowired
	private DriveRecodsServiceI driveRecodsService;
	@Autowired
	private ReportRecordsServiceI reportRecordsService;
	@Autowired
	private ObdGatherInfoServiceI obdGatherInfoService;
	@RequestMapping(params = "index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) {
		List<Object[]> types = carInfoService.getCarTypes();
		request.setAttribute("types", types);
		//获取登录用户的门店
		SalesmanInfoVo salesmanInfo = (SalesmanInfoVo) request.getSession().getAttribute("SalesmanInfo");
		String agencyId = salesmanInfo.getAgencyId();
		
		String plateNo = request.getParameter("plateNo");
		String carType = request.getParameter("carType");
		
		CriteriaQuery cq = new CriteriaQuery(CarInfoEntity.class);
		cq.eq("agency.id", agencyId);
		if(StringUtil.isNotEmpty(plateNo)){
			try {
				plateNo=new String(plateNo.getBytes("ISO-8859-1"),"utf-8");
				cq.eq("plateNo", plateNo);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(StringUtil.isNotEmpty(carType)){
			cq.eq("type", carType);
		}
		cq.add();
		cq.add(Restrictions.ne("status", ConstantStatus.INVALID));
		//查出所有车辆
		List<CarInfoEntity> carInfoList =  carInfoService.getListByCriteriaQuery(cq, false);
		request.setAttribute("plateNo", plateNo);
		request.setAttribute("carType", carType);
		List<CarInfoVo> carInfoVoList = null;
		try {
			carInfoVoList = ListUtils.copyTo(carInfoList, CarInfoVo.class);
			for (CarInfoVo carInfoVo : carInfoVoList) {
				if(carInfoVo.getSaleYear()!=null){
					 Timestamp timestamp = new Timestamp(carInfoVo.getSaleYear().getTime());
					String saleYearTOString = DateUtils.timestamptoStr(timestamp);
					carInfoVo.setSaleYearTOString(saleYearTOString);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.setAttribute("carInfoList", carInfoVoList);
		return new ModelAndView("carInfo/index");
	}

	@RequestMapping(params = "detail", method = RequestMethod.GET)
	public ModelAndView detail(HttpServletRequest request) {
		String carId = request.getParameter("id");
		CarInfoEntity carInfo = carInfoService.get(CarInfoEntity.class, carId);
		CarInfoVo carInfoVo = new CarInfoVo();
		try {
			BeanUtils.copyProperties(carInfoVo, carInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DetachedCriteria recodsDc = DetachedCriteria.forClass(DriveRecodsEntity.class);
		recodsDc.add(Restrictions.eq("carId", carId));
		Date date = DateUtils.gettimestamp();
		//未来7天
		recodsDc.add(Restrictions.between("orderStartTime", date, DateUtils.plusDay(7, date)));
		
		List<DriveRecodsEntity> driveRecodsList = driveRecodsService.findByDetached(recodsDc);
		
		List<ReportRecordsEntity> reportRecordsList = reportRecordsService.getCarByTime(carId, date, DateUtils.plusDay(7, date));
	
		List<CarArrangeVo> carArrangeVoList = new ArrayList<CarArrangeVo>();
		for (ReportRecordsEntity reportRecordsEntity : reportRecordsList) {
			CarArrangeVo carArrangeVo = new CarArrangeVo();
			carArrangeVo.setStartTime(reportRecordsEntity.getStartTime());
			carArrangeVo.setEndTime(reportRecordsEntity.getEndTime());
			carArrangeVo.setStatus(2);
			carArrangeVoList.add(carArrangeVo);
		}
		for (DriveRecodsEntity driveRecodsEntity : driveRecodsList) {
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
		
		
		request.setAttribute("carArrangeVoList", carArrangeVoList);
		request.setAttribute("carInfo", carInfoVo);
		return new ModelAndView("carInfo/detail");
	}

	@RequestMapping(params = "condition", method = RequestMethod.GET)
	public ModelAndView condition(HttpServletRequest request) {
		String carId = request.getParameter("carId");
		CarInfoEntity carInfoEntity = carInfoService.get(CarInfoEntity.class, carId);
		CarInfoVo carInfoVo = new CarInfoVo();
		try {
			BeanUtils.copyProperties(carInfoVo, carInfoEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		String driveId = request.getParameter("driveId");
		DriveRecodsEntity driveRecodsEntity = driveRecodsService.get(DriveRecodsEntity.class, driveId);
		DriveRecodsVo driveRecodsVo=new DriveRecodsVo();
		try {
			BeanUtils.copyProperties(driveRecodsVo, driveRecodsEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("driveRecods", driveRecodsVo);
		request.setAttribute("carInfo", carInfoVo);
		return new ModelAndView("carInfo/condition");
	}
	
	@RequestMapping(params = "status", method = RequestMethod.GET)
	public ModelAndView status(HttpServletRequest request) {
		String id = request.getParameter("id");
		DriveRecodsEntity driveRecodsEntity = driveRecodsService.get(DriveRecodsEntity.class, id);
		driveRecodsEntity.setStatus(2);
		driveRecodsService.saveOrUpdate(driveRecodsEntity);
		return new ModelAndView(new RedirectView("login.action?page"));
	}
	
	@RequestMapping(params = "status5", method = RequestMethod.GET)
	public ModelAndView status5(HttpServletRequest request) {
		String id = request.getParameter("id");
		DriveRecodsEntity driveRecodsEntity = driveRecodsService.get(DriveRecodsEntity.class, id);
		driveRecodsEntity.setDriveEndTime(new Date());
		driveRecodsEntity.setStatus(5);
		//获取公里数
		CarInfoEntity carInfoEntity = carInfoService.get(CarInfoEntity.class, driveRecodsEntity.getCarId());
		List<ObdGatherInfoEntity> obdGatherInfoList = obdGatherInfoService.getDatasByTimeQuantum(carInfoEntity.getObdId(),driveRecodsEntity.getDriveStartTime(), new Date());
		if(obdGatherInfoList.size()!=0){
			ObdGatherInfoEntity frist = obdGatherInfoList.get(0);
			ObdGatherInfoEntity last = obdGatherInfoList.get(obdGatherInfoList.size()-1);
			Float m = last.getMileage()-frist.getMileage(); 
			Float km = m/1000;
			BigDecimal x1 = new BigDecimal(Float.toString(km));
			driveRecodsEntity.setMileage(x1);
		}
		driveRecodsEntity.getCarId();
		driveRecodsService.saveOrUpdate(driveRecodsEntity);
		
		carInfoEntity.setStatus(2);
		carInfoService.saveOrUpdate(carInfoEntity);
		//跳到试驾报告
		request.setAttribute("driveId", driveRecodsEntity.getId());
		
		return new ModelAndView(new RedirectView("driveRecodsAction.action?picture&driveId="+driveRecodsEntity.getId()));
	}
	
	@RequestMapping(params = "monitor", method = RequestMethod.GET)
	public ModelAndView monitor(HttpServletRequest request) {
		
		String driveId = request.getParameter("id");
		DriveRecodsEntity driveRecodsEntity = driveRecodsService.get(DriveRecodsEntity.class, driveId);
		Integer status = driveRecodsEntity.getStatus();
		if(status==3){
			driveRecodsEntity.setDriveStartTime(new Date());
			
			driveRecodsEntity.setStatus(4);
			driveRecodsService.saveOrUpdate(driveRecodsEntity);
		}
		DriveRecodsVo driveRecodsVo=new DriveRecodsVo();
		try {
			BeanUtils.copyProperties(driveRecodsVo, driveRecodsEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String carId = driveRecodsVo.getCarId();
		CarInfoEntity carInfoEntity = carInfoService.get(CarInfoEntity.class, carId);
		carInfoEntity.setStatus(1);
		carInfoEntity.setDriveTotal(carInfoEntity.getDriveTotal()+1);
		
		carInfoService.saveOrUpdate(carInfoEntity);
		CarInfoVo carInfoVo = new CarInfoVo();
		try {
			BeanUtils.copyProperties(carInfoVo, carInfoEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("driveRecods", driveRecodsVo);
		request.setAttribute("carInfo", carInfoVo);
		return new ModelAndView("carInfo/monitor");
	}

	@ResponseBody
	@RequestMapping(params = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CarInfoEntity> list(@RequestBody CarInfoDto car) {
		List<CarInfoEntity> listCarInfos = null;
		try {
			CriteriaQuery cq = new CriteriaQuery(CarInfoEntity.class);
			cq.eq("agency.id", car.getAgencyId());
			if (StringUtil.isNotEmpty(car.getType())) {
				cq.eq("type", car.getType());
			}
			if (StringUtil.isNotEmpty(car.getPlateNo())) {
				cq.eq("plateNo", car.getPlateNo());
			}
			cq.add();
			listCarInfos = carInfoService.getListByCriteriaQuery(cq, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listCarInfos;
	}

	/*
	 * @SuppressWarnings("rawtypes")
	 * 
	 * @RequestMapping(params = "arrange", method = {RequestMethod.GET,
	 * RequestMethod.POST})
	 * 
	 * @ResponseBody public List<CarInfoEntity> arrange(@PathVariable("id")
	 * String id) { List<CarInfoEntity> listCarInfos = null; try { CriteriaQuery
	 * cq = new CriteriaQuery(CarInfoEntity.class);
	 * 
	 * cq.add(); listCarInfos = carInfoService.getListByCriteriaQuery(cq,
	 * false); } catch (Exception e) { e.printStackTrace(); } return
	 * listCarInfos; }
	 */

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<CarInfoEntity> list() {
		List<CarInfoEntity> listCarInfos = carInfoService
				.getList(CarInfoEntity.class);
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
	public ResponseEntity<?> create(@RequestBody CarInfoEntity carInfo,
			UriComponentsBuilder uriBuilder) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<CarInfoEntity>> failures = validator
				.validate(carInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(
					BeanValidators.extractPropertyAndMessage(failures),
					HttpStatus.BAD_REQUEST);
		}

		// 保存
		carInfoService.save(carInfo);

		// 按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = carInfo.getId();
		URI uri = uriBuilder.path("/rest/carInfoAction/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody CarInfoEntity carInfo) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<CarInfoEntity>> failures = validator
				.validate(carInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(
					BeanValidators.extractPropertyAndMessage(failures),
					HttpStatus.BAD_REQUEST);
		}

		// 保存
		carInfoService.saveOrUpdate(carInfo);

		// 按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		carInfoService.deleteEntityById(CarInfoEntity.class, id);
	}

}
