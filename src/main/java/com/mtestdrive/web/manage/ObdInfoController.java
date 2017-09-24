package com.mtestdrive.web.manage;
import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.mtestdrive.entity.ObdInfoEntity;
import com.mtestdrive.service.ObdInfoServiceI;

/**   
 * @Title: Controller
 * @Description: OBD设备
 * @author zhangdaihao
 * @date 2017-03-14 11:13:43
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/obdInfoController")
public class ObdInfoController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObdInfoController.class);

	@Autowired
	private ObdInfoServiceI obdInfoService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * OBD设备列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("mpage/manage/obd/obdInfoList");
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
	public void datagrid(ObdInfoEntity obdInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ObdInfoEntity.class, dataGrid);
		cq.addOrder("createTime", SortDirection.desc);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, obdInfo, request.getParameterMap());
		this.obdInfoService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除OBD设备
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(ObdInfoEntity obdInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		obdInfo = systemService.getEntity(ObdInfoEntity.class, obdInfo.getId());
		message = "OBD设备删除成功";
		obdInfoService.delete(obdInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加OBD设备
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ObdInfoEntity obdInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(obdInfo.getId())) {
			message = "OBD设备更新成功";
			ObdInfoEntity t = obdInfoService.get(ObdInfoEntity.class, obdInfo.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(obdInfo, t);
				obdInfoService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "OBD设备更新失败";
			}
		} else {
			message = "OBD设备添加成功";
			obdInfoService.save(obdInfo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * OBD设备列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(ObdInfoEntity obdInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(obdInfo.getId())) {
			obdInfo = obdInfoService.getEntity(ObdInfoEntity.class, obdInfo.getId());
			req.setAttribute("obdInfoPage", obdInfo);
		}
		return new ModelAndView("mpage/manage/obd/obdInfo");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<ObdInfoEntity> list() {
		List<ObdInfoEntity> listObdInfos=obdInfoService.getList(ObdInfoEntity.class);
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
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<ObdInfoEntity>> failures = validator.validate(obdInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		obdInfoService.save(obdInfo);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = obdInfo.getId();
		URI uri = uriBuilder.path("/rest/obdInfoController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody ObdInfoEntity obdInfo) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<ObdInfoEntity>> failures = validator.validate(obdInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		obdInfoService.saveOrUpdate(obdInfo);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		obdInfoService.deleteEntityById(ObdInfoEntity.class, id);
	}
}
