package com.mtestdrive.web.app;

import com.mtestdrive.MaseratiConstants;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Timestamp;
import java.util.*;

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
		DriveRecodsVo driveRecodsVo=convertToDriveRecodsVo(driveRecodsEntity);
		logger.info("查看车况 driveId="+driveId+" carId="+carId);

		request.setAttribute("driveRecods", driveRecodsVo);
		request.setAttribute("carInfo", carInfoVo);
		return new ModelAndView("carInfo/condition");
	}

	private DriveRecodsVo convertToDriveRecodsVo(DriveRecodsEntity driveRecodsEntity) {
		DriveRecodsVo driveRecodsVo=new DriveRecodsVo();
		driveRecodsVo.setCar(driveRecodsVo.getCar());
		driveRecodsVo.setCarId(driveRecodsEntity.getCarId());
		driveRecodsVo.setDriveEndTime(driveRecodsEntity.getDriveEndTime());
		driveRecodsVo.setDriver(driveRecodsEntity.getDriver());
		driveRecodsVo.setDriveStartTime(driveRecodsEntity.getDriveStartTime());
		driveRecodsVo.setEndPicPath(driveRecodsEntity.getEndPicPath());
		driveRecodsVo.setFeedback(driveRecodsEntity.getFeedback());
		driveRecodsVo.setId(driveRecodsEntity.getId());
		driveRecodsVo.setMileage(driveRecodsEntity.getMileage()==null?new BigDecimal(0):driveRecodsEntity.getMileage());
		driveRecodsVo.setOrderEndTime(driveRecodsEntity.getOrderEndTime());
		driveRecodsVo.setOrderStartTime(driveRecodsEntity.getOrderStartTime());
		driveRecodsVo.setRouteId(driveRecodsEntity.getRouteId());
		driveRecodsVo.setStartPicPath(driveRecodsEntity.getStartPicPath());
		driveRecodsVo.setStatus(driveRecodsEntity.getStatus());
		driveRecodsVo.setAgency(driveRecodsEntity.getAgency());
		driveRecodsVo.setCustomer(driveRecodsVo.getCustomer());
		driveRecodsVo.setSalesman(driveRecodsVo.getSalesman());
		return driveRecodsVo;
	}

	@RequestMapping(params = "status", method = RequestMethod.GET)
	public ModelAndView status(HttpServletRequest request) {
		String id = request.getParameter("id");
		DriveRecodsEntity driveRecodsEntity = driveRecodsService.get(DriveRecodsEntity.class, id);
		driveRecodsEntity.setStatus(MaseratiConstants.DriveRecodsStatus.CONFIRMED);//2
		driveRecodsService.saveOrUpdate(driveRecodsEntity);
        logger.info("确认车况 driveId="+id+" carId="+driveRecodsEntity.getCarId());
		return new ModelAndView(new RedirectView("login.action?page"));
	}
	
	@RequestMapping(params = "status5", method = RequestMethod.GET)
	public ModelAndView status5(HttpServletRequest request) {
		String id = request.getParameter("id");
		DriveRecodsEntity driveRecodsEntity = driveRecodsService.get(DriveRecodsEntity.class, id);
		driveRecodsEntity.setDriveEndTime(new Date());
		driveRecodsEntity.setStatus(MaseratiConstants.DriveRecodsStatus.COMPLETE);//5 完成试驾

		//获取OBD记录的公里数
		CarInfoEntity carInfoEntity = carInfoService.get(CarInfoEntity.class, driveRecodsEntity.getCarId());
		List<ObdGatherInfoEntity> obdGatherInfoList = obdGatherInfoService.getDatasByTimeQuantum(carInfoEntity.getObdId(),driveRecodsEntity.getDriveStartTime(), new Date());
		if(obdGatherInfoList.size()!=0){
			ObdGatherInfoEntity first = obdGatherInfoList.get(0);
			ObdGatherInfoEntity last = obdGatherInfoList.get(obdGatherInfoList.size()-1);
			Float m = last.getMileage()-first.getMileage();
			Float km = m/1000;
			BigDecimal x1 = new BigDecimal(Float.toString(km));
			driveRecodsEntity.setMileage(x1);
		}
		driveRecodsEntity.getCarId();
		driveRecodsService.updateEntitie(driveRecodsEntity);


		carInfoEntity.setStatus(MaseratiConstants.CarStatus.NO_USED);
		carInfoService.updateEntitie(carInfoEntity);
		logger.info("结束试驾 driveId="+driveRecodsEntity.getId()+" carId="+carInfoEntity.getId()+" endCarStatus="+carInfoEntity.getStatus());

		//跳到试驾报告
		request.setAttribute("driveId", driveRecodsEntity.getId());
		
		return new ModelAndView(new RedirectView("driveRecodsAction.action?picture&driveId="+driveRecodsEntity.getId()));
	}

    /**
     * 当前页面为试驾中的“试驾车位置”页面，由点击“首页Tab/试驾Tab-查看试驾/开始试驾”页面按钮进入
     * @param request
     * @return
     */
	@RequestMapping(params = "monitor", method = RequestMethod.GET)
	public ModelAndView monitor(HttpServletRequest request) {
		
		String driveId = request.getParameter("id");
		DriveRecodsEntity driveRecodsEntity = driveRecodsService.get(DriveRecodsEntity.class, driveId);
		Integer driveStatus = driveRecodsEntity.getStatus();
        String carId = driveRecodsEntity.getCarId();
        CarInfoEntity carInfoEntity = carInfoService.get(CarInfoEntity.class, carId);

		if(driveStatus==MaseratiConstants.DriveRecodsStatus.FORMALITIES
		   ||driveStatus == MaseratiConstants.DriveRecodsStatus.CONFIRMED){//3 or 2  若手续已办理或者已准备，则开始试驾
			driveRecodsEntity.setDriveStartTime(new Date()); //记录开始试驾时间
			driveRecodsEntity.setStatus(MaseratiConstants.DriveRecodsStatus.UNDERWAY);//=4 流程试驾中
			driveRecodsService.saveOrUpdate(driveRecodsEntity);

            carInfoEntity.setStatus(MaseratiConstants.CarStatus.TEST_DRIVING); //=1 车辆试驾中
            carInfoEntity.setDriveTotal(carInfoEntity.getDriveTotal()+1);
            carInfoService.updateEntitie(carInfoEntity);

            logger.info("开始试驾 driveId="+driveId+" carId="+carId+" carStatus="+carInfoEntity.getStatus());

		} else if (driveStatus==MaseratiConstants.DriveRecodsStatus.UNDERWAY){//4 若流程试驾中，则为重入该流程
            logger.info("重入流程 driveId="+driveId+" carId="+carId+" carStatus="+carInfoEntity.getStatus()+" driveStatus="+driveStatus);
        } else { //其他状态判定为非法进入该流程
		    logger.error("非法进入 driveId="+driveId+" carId="+carId+" carStatus="+carInfoEntity.getStatus()+" driveStatus="+driveStatus);
        }

		DriveRecodsVo driveRecodsVo=convertToDriveRecodsVo(driveRecodsEntity);
		CarInfoVo carInfoVo = new CarInfoVo();
		try {
			BeanUtils.copyProperties(carInfoVo, carInfoEntity);
		} catch (Exception e) {
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
