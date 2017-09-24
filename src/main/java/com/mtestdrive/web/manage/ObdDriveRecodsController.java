package com.mtestdrive.web.manage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.core.util.MyBeanUtils;

import com.mtestdrive.dto.ObdGatherDataDto;
import com.mtestdrive.entity.AgencyInfoEntity;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.CustomerInfoEntity;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.entity.ObdDriveRecodsEntity;
import com.mtestdrive.entity.ObdGatherInfoEntity;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.CustomerInfoServiceI;
import com.mtestdrive.service.ObdDriveRecodsServiceI;
import com.mtestdrive.service.ObdGatherInfoServiceI;
import com.mtestdrive.service.SalesmanInfoServiceI;
import com.mtestdrive.vo.DriveRecodsVo;
import com.mtestdrive.vo.PlaybackTrackVo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.math.BigDecimal;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**   
 * @Title: Controller
 * @Description: OBD试驾表
 * @author zhangdaihao
 * @date 2017-06-15 17:30:57
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/obdDriveRecodsController")
public class ObdDriveRecodsController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObdDriveRecodsController.class);

	@Autowired
	private ObdDriveRecodsServiceI obdDriveRecodsService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CarInfoServiceI carInfoService;
	@Autowired
	private ObdGatherInfoServiceI obdGatherInfoService;
	@Autowired
	private CustomerInfoServiceI customerInfoService;
	@Autowired
	private SalesmanInfoServiceI salesmanInfoService;
	/**
	 * OBD试驾表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/mtestdrive/obdDriveRecods/obdDriveRecodsList");
	}

	@RequestMapping(params = "openReport")
	public ModelAndView openReport(HttpServletRequest request) {
		String id = request.getParameter("id");
		PlaybackTrackVo ptv = new PlaybackTrackVo();
		ObdDriveRecodsEntity obdDriveRecodsEntity = obdDriveRecodsService.get(ObdDriveRecodsEntity.class, id);
		DriveRecodsVo driveRecodsVo = driveRecodsVo = new DriveRecodsVo();
		ptv.setDriveStartTime(obdDriveRecodsEntity.getDriveStartTime());
		ptv.setDriveEndTime(obdDriveRecodsEntity.getDriveEndTime());
		ptv.setMileage(obdDriveRecodsEntity.getMileage());
		try {
			MyBeanUtils.copyBean2Bean(driveRecodsVo, obdDriveRecodsEntity);
			if (StringUtils.isNotEmpty(driveRecodsVo.getCarId())) {
				CarInfoEntity car = carInfoService.get(CarInfoEntity.class, driveRecodsVo.getCarId());
				if (car != null) {
					MyBeanUtils.copyBean2Bean(driveRecodsVo.getCar(), car);
				}
			}
			if (StringUtils.isNotEmpty(obdDriveRecodsEntity.getCustomerId())) {
				CustomerInfoEntity customer = customerInfoService.get(CustomerInfoEntity.class, obdDriveRecodsEntity.getCustomerId());
				if (customer != null) {
					driveRecodsVo.setCustomer(customer);
				}
			}
			if (StringUtils.isNotEmpty(obdDriveRecodsEntity.getSalesmanId())) {
				SalesmanInfoEntity salesman = salesmanInfoService.get(SalesmanInfoEntity.class, obdDriveRecodsEntity.getSalesmanId());
				if (salesman != null) {
					driveRecodsVo.setSalesman(salesman);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(obdDriveRecodsEntity.getCarId() != null){
			CarInfoEntity carInfo = carInfoService.get(CarInfoEntity.class, obdDriveRecodsEntity.getCarId());
			ptv.setDriveStartTime(obdDriveRecodsEntity.getDriveStartTime());
			ptv.setDriveEndTime(obdDriveRecodsEntity.getDriveEndTime());
			ptv.setMileage(obdDriveRecodsEntity.getMileage());
			if(carInfo != null){					
				ptv.setPlateNo(carInfo.getPlateNo());
				ptv.setType(carInfo.getType());
				
				List<ObdGatherInfoEntity> obdGatherInfo = obdGatherInfoService.getDatasByTimeQuantum(carInfo.getObdId(), obdDriveRecodsEntity.getDriveStartTime(), obdDriveRecodsEntity.getDriveEndTime());
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
		request.setAttribute("info", ptv);
		request.setAttribute("driveRecodsVo", driveRecodsVo);
		Date endTime = driveRecodsVo.getDriveEndTime();
		Date startTime = driveRecodsVo.getDriveStartTime();
		long end = endTime.getTime();
		long start = startTime.getTime();
		long x = end-start;
		request.setAttribute("timeDifference", x);
		return new ModelAndView("mpage/manage/driveRecods/report");
	}
	
	
	@RequestMapping(params = "getGatherInfoByRecodId")
	@ResponseBody
	public AjaxJson getGatherInfoByRecodId(@RequestParam(value = "recodsId", required = true) String id,
			HttpServletRequest req) {
		AjaxJson aj = new AjaxJson();
		ObdDriveRecodsEntity dr = obdGatherInfoService.get(ObdDriveRecodsEntity.class, id);
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
	
	
	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(ObdDriveRecodsEntity obdDriveRecods,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ObdDriveRecodsEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, obdDriveRecods, request.getParameterMap());
		this.obdDriveRecodsService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除OBD试驾表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(ObdDriveRecodsEntity obdDriveRecods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		obdDriveRecods = systemService.getEntity(ObdDriveRecodsEntity.class, obdDriveRecods.getId());
		message = "OBD试驾表删除成功";
		obdDriveRecodsService.delete(obdDriveRecods);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加OBD试驾表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ObdDriveRecodsEntity obdDriveRecods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(obdDriveRecods.getId())) {
			message = "OBD试驾表更新成功";
			ObdDriveRecodsEntity t = obdDriveRecodsService.get(ObdDriveRecodsEntity.class, obdDriveRecods.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(obdDriveRecods, t);
				obdDriveRecodsService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "OBD试驾表更新失败";
			}
		} else {
			message = "OBD试驾表添加成功";
			obdDriveRecodsService.save(obdDriveRecods);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * OBD试驾表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(ObdDriveRecodsEntity obdDriveRecods, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(obdDriveRecods.getId())) {
			obdDriveRecods = obdDriveRecodsService.getEntity(ObdDriveRecodsEntity.class, obdDriveRecods.getId());
			req.setAttribute("obdDriveRecodsPage", obdDriveRecods);
		}
		return new ModelAndView("com/mtestdrive/obdDriveRecods/obdDriveRecods");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<ObdDriveRecodsEntity> list() {
		List<ObdDriveRecodsEntity> listObdDriveRecodss=obdDriveRecodsService.getList(ObdDriveRecodsEntity.class);
		return listObdDriveRecodss;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		ObdDriveRecodsEntity task = obdDriveRecodsService.get(ObdDriveRecodsEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody ObdDriveRecodsEntity obdDriveRecods, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<ObdDriveRecodsEntity>> failures = validator.validate(obdDriveRecods);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		obdDriveRecodsService.save(obdDriveRecods);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = obdDriveRecods.getId();
		URI uri = uriBuilder.path("/rest/obdDriveRecodsController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody ObdDriveRecodsEntity obdDriveRecods) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<ObdDriveRecodsEntity>> failures = validator.validate(obdDriveRecods);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		obdDriveRecodsService.saveOrUpdate(obdDriveRecods);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		obdDriveRecodsService.deleteEntityById(ObdDriveRecodsEntity.class, id);
	}
}
