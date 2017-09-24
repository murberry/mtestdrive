package com.mtestdrive.web.manage;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
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
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ResourceUtil;

import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.MaseratiConstants.RouteStatus;
import com.mtestdrive.entity.ObdGatherInfoEntity;
import com.mtestdrive.entity.RouteInfoEntity;
import com.mtestdrive.service.ObdGatherInfoServiceI;
import com.mtestdrive.service.RouteInfoServiceI;

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

import com.mtestdrive.entity.RouteInfoEntity;
import com.mtestdrive.service.RouteInfoServiceI;

/**
 * @Title: Controller
 * @Description: 试驾路线
 * @author zhangdaihao
 * @date 2017-03-10 17:39:29
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/routeInfoController")
public class RouteInfoController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RouteInfoController.class);

	@Autowired
	private RouteInfoServiceI routeInfoService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;

	@Autowired
	private ObdGatherInfoServiceI obdGatherInfoService;

	/**
	 * @Title: getRouteById
	 * @Description: 路线ID得到经纬度
	 * @param: @param
	 *             id
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: AjaxJson
	 * @throws @author:
	 *             mengtaocui
	 */
	@RequestMapping(params = "getRouteById", method = RequestMethod.GET)
	@ResponseBody
	public AjaxJson getRouteById(@RequestParam(required = true) String id, HttpServletRequest request) {
		AjaxJson aj = new AjaxJson();

		RouteInfoEntity route = routeInfoService.get(RouteInfoEntity.class, id);
		if (route == null) {
			aj.setSuccess(false);
			aj.setMsg("试驾路线不存在");
		} else {
			List<ObdGatherInfoEntity> obdGathers = obdGatherInfoService.getDatasByTimeQuantum(route.getTermId(),
					route.getStartTime(), route.getEndTime());
			ObdGatherInfoEntity gather = null;
			Map<String, Object> map = null;
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			if (obdGathers != null && !obdGathers.isEmpty()) {
				ObdGatherInfoEntity fristObdGather = obdGathers.get(0);
				ObdGatherInfoEntity ListObdGather = obdGathers.get(obdGathers.size()-1);
				Float fristMileage = fristObdGather.getMileage();
				Float listMileage = ListObdGather.getMileage();
				Float x = listMileage-fristMileage;
				x = x/1000;
				for (int i = 0; i < obdGathers.size(); i++) {
					gather = obdGathers.get(i);
					map = new HashMap<String, Object>();
					map.put("lat", gather.getLat());
					map.put("lon", gather.getLon());
					map.put("Mileage", x);
					data.add(map);
				}
			}
			aj.setSuccess(true);
			aj.setObj(data);
		}
		return aj;
	}

	/**
	 * @Title: getObdDatasByTimeQuantum
	 * @Description: 根据设备ID，开始时间和结束时间获取时间段内的经纬度集合
	 * @param: @param
	 *             id
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: AjaxJson
	 * @throws @author:
	 *             mengtaocui
	 */
	@RequestMapping(params = "getObdDatasByTimeQuantum", method = RequestMethod.GET)
	@ResponseBody
	public AjaxJson getObdDatasByTimeQuantum(@RequestParam(required = true) String obdId,
			@RequestParam(required = true) String startTime, @RequestParam(required = true) String endTime,
			HttpServletRequest request) {
		AjaxJson aj = new AjaxJson();
		List<ObdGatherInfoEntity> obdGathers = obdGatherInfoService.getDatasByTimeQuantum(obdId,
				DateUtils.str2Date(startTime, DateUtils.datetimeFormat),
				DateUtils.str2Date(endTime, DateUtils.datetimeFormat));
		ObdGatherInfoEntity gather = null;
		Map<String, Object> map = null;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (obdGathers != null && !obdGathers.isEmpty()) {
			for (int i = 0; i < obdGathers.size(); i++) {
				gather = obdGathers.get(i);
				map = new HashMap<String, Object>();
				map.put("lat", gather.getLat());
				map.put("lon", gather.getLon());
				data.add(map);
			}
		}
		aj.setSuccess(true);
		aj.setObj(data);
		return aj;
	}

	/**
	 * 试驾路线列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("mpage/manage/routeInfo/routeInfoList");
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
	public void datagrid(RouteInfoEntity routeInfo, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(RouteInfoEntity.class, dataGrid);
		if (!ResourceUtil.getSessionUserName().getUserKey().equals("超级管理员")&&!ResourceUtil.getSessionUserName().getUserKey().equals("超级系统管理员")) {
			cq.add(Restrictions.eq("agencyId", ResourceUtil.getSessionUserName().getDepartid()));
		}
		cq.add(Restrictions.eq("routeStatus", RouteStatus.VALID));
		cq.addOrder("createTime", SortDirection.desc);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, routeInfo, request.getParameterMap());
		this.routeInfoService.getDataGridReturn(cq, true);
		for (Iterator iterator = dataGrid.getResults().iterator(); iterator.hasNext();) {
			RouteInfoEntity info = (RouteInfoEntity) iterator.next();
			info.setRemark(StringUtil.isNotEmpty(info.getRemark())?info.getRemark():"-");
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除试驾路线
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(RouteInfoEntity routeInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		routeInfo = systemService.getEntity(RouteInfoEntity.class, routeInfo.getId());
		message = "试驾路线删除成功";
		routeInfo.setRouteStatus(RouteStatus.INVALID);
		routeInfoService.updateEntitie(routeInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 添加试驾路线
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(RouteInfoEntity routeInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(routeInfo.getId())) {
			message = "试驾路线更新成功";
			RouteInfoEntity t = routeInfoService.get(RouteInfoEntity.class, routeInfo.getId());
			try {
				t.setUpdateBy(ResourceUtil.getSessionUserName().getId());
				t.setUpdateTime(DateUtils.gettimestamp());
				MyBeanUtils.copyBeanNotNull2Bean(routeInfo, t);
				routeInfoService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "试驾路线更新失败";
			}
		} else {
			message = "试驾路线添加成功";
			routeInfo.setAgencyId(ResourceUtil.getSessionUserName().getDepartid());
			routeInfo.setCreateBy(ResourceUtil.getSessionUserName().getId());
			routeInfo.setCreateTime(DateUtils.gettimestamp());
			routeInfo.setRouteStatus(ConstantStatus.VALID);
			routeInfoService.save(routeInfo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 试驾路线列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(RouteInfoEntity routeInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(routeInfo.getId())) {
			routeInfo = routeInfoService.getEntity(RouteInfoEntity.class, routeInfo.getId());
			req.setAttribute("routeInfoPage", routeInfo);
		}
		return new ModelAndView("mpage/manage/routeInfo/routeInfo");
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<RouteInfoEntity> list() {
		List<RouteInfoEntity> listRouteInfos = routeInfoService.getList(RouteInfoEntity.class);
		return listRouteInfos;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		RouteInfoEntity task = routeInfoService.get(RouteInfoEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody RouteInfoEntity routeInfo, UriComponentsBuilder uriBuilder) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<RouteInfoEntity>> failures = validator.validate(routeInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		// 保存
		routeInfoService.save(routeInfo);

		// 按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = routeInfo.getId();
		URI uri = uriBuilder.path("/rest/routeInfoController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody RouteInfoEntity routeInfo) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<RouteInfoEntity>> failures = validator.validate(routeInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		// 保存
		routeInfoService.saveOrUpdate(routeInfo);

		// 按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		routeInfoService.deleteEntityById(RouteInfoEntity.class, id);
	}
}
