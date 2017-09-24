package com.mtestdrive.web.app;

import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.mtestdrive.entity.ObdGatherInfoEntity;
import com.mtestdrive.entity.ObdInfoEntity;
import com.mtestdrive.service.ObdGatherInfoServiceI;
import com.mtestdrive.service.ObdInfoServiceI;

/**
 * @Title: Action
 * @Description: OBD设备
 * @author zhangdaihao
 * @date 2017-03-14 11:13:43
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/obdInfoAction")
public class ObdInfoAction extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObdInfoAction.class);

	@Autowired
	private ObdInfoServiceI obdInfoService;
	@Autowired
	private ObdGatherInfoServiceI obdGatherInfoService;
	
	@Autowired
	private Validator validator;

	/**
	 * @Title: getLocationById   
	 * @Description: 返回最近一次OBD设备的定位数据  
	 * @param: @param id
	 * @param: @return      
	 * @return: AjaxJson      
	 * @throws   
	 * @author: mengtaocui
	 */
	@RequestMapping(params = "getLocationById", method = RequestMethod.GET)
	@ResponseBody
	public AjaxJson getLocationById(@RequestParam(required = true) String id) {
		AjaxJson aj = new AjaxJson();
		ObdGatherInfoEntity info = obdGatherInfoService.getLastData(id);
		aj.setObj(info);
		aj.setSuccess(true);
		return aj;
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<ObdInfoEntity> list() {
		List<ObdInfoEntity> listObdInfos = obdInfoService.getList(ObdInfoEntity.class);
		return listObdInfos;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		ObdInfoEntity task = obdInfoService.get(ObdInfoEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody ObdInfoEntity obdInfo, UriComponentsBuilder uriBuilder) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<ObdInfoEntity>> failures = validator.validate(obdInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		// 保存
		obdInfoService.save(obdInfo);

		// 按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = obdInfo.getId();
		URI uri = uriBuilder.path("/rest/obdInfoAction/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody ObdInfoEntity obdInfo) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<ObdInfoEntity>> failures = validator.validate(obdInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		// 保存
		obdInfoService.saveOrUpdate(obdInfo);

		// 按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		obdInfoService.deleteEntityById(ObdInfoEntity.class, id);
	}
}
