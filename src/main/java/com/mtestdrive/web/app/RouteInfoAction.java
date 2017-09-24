package com.mtestdrive.web.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtestdrive.MaseratiConstants.RouteStatus;
import com.mtestdrive.entity.ObdGatherInfoEntity;
import com.mtestdrive.entity.RouteInfoEntity;
import com.mtestdrive.service.ObdGatherInfoServiceI;
import com.mtestdrive.service.RouteInfoServiceI;
import com.mtestdrive.vo.SalesmanInfoVo;

/**
 * @Title: Action
 * @Description: 试驾路线
 * @author zhangdaihao
 * @date 2017-03-10 17:39:29
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/routeInfoAction")
public class RouteInfoAction extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RouteInfoAction.class);

	@Autowired
	private RouteInfoServiceI routeInfoService;
	@Autowired
	private Validator validator;
	@Autowired
	private ObdGatherInfoServiceI obdGatherInfoService;
	
	/**
	 * @Title: getRoutes   
	 * @Description: 返回销售顾问所在经销商的所有试驾路线 
	 * @param: @return      
	 * @return: AjaxJson      
	 * @throws   
	 * @author: mengtaocui
	 */
	@RequestMapping(params = "getRoutes", method = RequestMethod.GET)
	@ResponseBody
	public AjaxJson  getRoutes(HttpServletRequest request) {
		AjaxJson aj = new AjaxJson();
		
		SalesmanInfoVo salesman = (SalesmanInfoVo) request.getSession().getAttribute("SalesmanInfo");
		if(salesman == null || StringUtil.isEmpty(salesman.getAgencyId())){
			aj.setSuccess(false);
		}else{
			List<Map<String, Object>> routeList = getRouteInfoByAgencyId(salesman.getAgencyId());
			aj.setObj(JSONHelper.toJSONArray(routeList));
			aj.setSuccess(true);
		}
		
		return aj;
	}
	
	/**
	 * @Title: getRouteInfoByAgencyId   
	 * @Description: 获取指定经销商的试驾路线  
	 * @param: @param agencyId
	 * @param: @return      
	 * @return: List<Map<String,Object>>      
	 * @throws   
	 * @author: mengtaocui
	 */
	public List<Map<String, Object>> getRouteInfoByAgencyId(String agencyId){
		
		//得到路线列表
		DetachedCriteria dc = DetachedCriteria.forClass(RouteInfoEntity.class);
		dc.add(Restrictions.eq("agencyId", agencyId));
		dc.add(Restrictions.eq("routeStatus", RouteStatus.VALID));
		List<RouteInfoEntity> reList = routeInfoService.findByDetached(dc);
		
		List<Map<String, Object>> routeList = new ArrayList<Map<String, Object>>();
		RouteInfoEntity route = null;
		List<Map<String, Object>> gatherList = null;
		List<ObdGatherInfoEntity> obdGatherInfo = null;
		ObdGatherInfoEntity garher = null;
		Map<String, Object> dataMap = null;
		Map<String, Object> routeMap = null;
		
		if(reList != null && !reList.isEmpty()){
			for(int i=0; i<reList.size(); i++){
				route = reList.get(i);
				gatherList = new ArrayList<Map<String, Object>>();
				routeMap = new HashMap<String, Object>();
				if(route != null){
					
					obdGatherInfo = obdGatherInfoService.getDatasByTimeQuantum(route.getTermId(), route.getStartTime(), route.getEndTime());
					if(obdGatherInfo != null && !obdGatherInfo.isEmpty()){
						for(int j=0; j<obdGatherInfo.size(); j++){
							garher = obdGatherInfo.get(j);
							dataMap = new HashMap<String, Object>();
							dataMap.put("lat", garher.getLat());
							dataMap.put("lon", garher.getLon());
							gatherList.add(dataMap);
						}
					}
				}
				routeMap.put("routeId", route.getId());
				routeMap.put("gathers", gatherList);
				
				routeList.add(routeMap);
			}
		}
		return routeList;
	}
	
	/**
	 * @Title: getRouteById   
	 * @Description: 路线ID得到经纬度  
	 * @param: @param id
	 * @param: @param request
	 * @param: @return      
	 * @return: AjaxJson      
	 * @throws   
	 * @author: mengtaocui
	 */
	@RequestMapping(params = "getRouteById", method = RequestMethod.GET)
	@ResponseBody
	public AjaxJson  getRouteById(@RequestParam(required = true)String id, HttpServletRequest request) {
		AjaxJson aj = new AjaxJson();
		
		RouteInfoEntity route = routeInfoService.get(RouteInfoEntity.class, id);
		if(route == null){
			aj.setSuccess(false);
			aj.setMsg("试驾路线不存在");
		}else{
			List<ObdGatherInfoEntity> obdGathers = obdGatherInfoService.getDatasByTimeQuantum(
					route.getTermId(), route.getStartTime(), route.getEndTime());
			ObdGatherInfoEntity gather = null;
			Map<String, Object> map = null;
			List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
			if(obdGathers != null && !obdGathers.isEmpty()){
				for(int i=0; i<obdGathers.size(); i++){
					gather = obdGathers.get(i);
					map = new HashMap<String, Object>();
					map.put("lat", gather.getLat());
					map.put("lon", gather.getLon());
					data.add(map);
				}
			}
			aj.setSuccess(true);
			aj.setObj(data);
		}
		return aj;
	}

}
