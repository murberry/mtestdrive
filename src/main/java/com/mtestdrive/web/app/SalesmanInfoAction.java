package com.mtestdrive.web.app;

import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.SalesmanInfoServiceI;

/**
 * @Title: Action
 * @Description: 销售顾问信息
 * @author zhangdaihao
 * @date 2017-03-10 17:25:38
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/salesmanInfoAction")
public class SalesmanInfoAction extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SalesmanInfoAction.class);

	@Autowired
	private SalesmanInfoServiceI salesmanInfoService;
	@Autowired
	private Validator validator;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<SalesmanInfoEntity> list() {
		List<SalesmanInfoEntity> listSalesmanInfos = salesmanInfoService.getList(SalesmanInfoEntity.class);
		return listSalesmanInfos;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SalesmanInfoEntity task = salesmanInfoService.get(SalesmanInfoEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SalesmanInfoEntity salesmanInfo, UriComponentsBuilder uriBuilder) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SalesmanInfoEntity>> failures = validator.validate(salesmanInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		// 保存
		salesmanInfoService.save(salesmanInfo);

		// 按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = salesmanInfo.getId();
		URI uri = uriBuilder.path("/rest/salesmanInfoAction/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SalesmanInfoEntity salesmanInfo) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SalesmanInfoEntity>> failures = validator.validate(salesmanInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		// 保存
		salesmanInfoService.saveOrUpdate(salesmanInfo);

		// 按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		salesmanInfoService.deleteEntityById(SalesmanInfoEntity.class, id);
	}
}
