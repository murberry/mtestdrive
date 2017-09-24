package com.mtestdrive.web.manage.rest;

import java.util.Date;
import java.util.List;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtestdrive.dto.ObdGatherDataDto;
import com.mtestdrive.dto.ObdGatherInfoDto;
import com.mtestdrive.entity.ObdGatherInfoEntity;
import com.mtestdrive.service.ObdGatherInfoServiceI;

/**
 * TSUser的Restful API的Controller.
 * 
 * @author liuht
 */
@Controller
@RequestMapping(value = "/obdData")
public class OBDRestController {

	@Autowired
	private SystemService sysService;

	@Autowired
	private ObdGatherInfoServiceI obdGatherInfoService;

	@RequestMapping(params = "send", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AjaxJson> reviceObdData(@RequestBody List<ObdGatherInfoDto> obdDto) {
		AjaxJson aj = new AjaxJson();
		
		if(obdDto == null || obdDto.isEmpty()){
			aj.setMsg("参数有误");
		}else{
			ObdGatherInfoEntity oe = null;
			ObdGatherInfoDto info = null;
			ObdGatherDataDto data = null;
			for(int i = 0; i < obdDto.size(); i++){
				oe = new ObdGatherInfoEntity();
				if(obdDto.get(i) == null){
					aj.setMsg("参数有误");
				}else{
					info = (ObdGatherInfoDto) JSONHelper.json2Object(JSONHelper.map2json(obdDto.get(i)), ObdGatherInfoDto.class);
					//info = obdDto.get(i);
					if(info == null){
						aj.setMsg("参数有误");
					}else{
						data = info.getGnssData();
						if(data == null){
							aj.setMsg("参数有误");
						}else{
							//保存数据
							oe.setTermid(info.getTermId());
							oe.setGnsstime(data.getGnssTime());
							oe.setLat(data.getLat().floatValue());
							oe.setAlt(data.getAlt());
							oe.setLon(data.getLon().floatValue());
							oe.setHead(data.getHead());
							oe.setSpd(data.getSpd());//无用
							oe.setObdSpd(data.getObdSpd());
							oe.setMileage(data.getMileage());
							oe.setAccOn(data.getAccOn());
							oe.setCreateTime(DateUtils.getTimestamp());
							sysService.save(oe);
						}
					}
				}
			}
			aj.setMsg("SUCCESS");
		}
		return new ResponseEntity<AjaxJson>(aj, HttpStatus.OK);
	}
	
	@RequestMapping(params = "getObdData", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<AjaxJson> getObdData(String id, String startTime, String endTime) {
		AjaxJson aj = new AjaxJson();
		
		Date startDate = DateUtils.str2Timestamp(startTime);
		Date endDate = DateUtils.str2Timestamp(endTime);
		List<ObdGatherInfoEntity> oi = obdGatherInfoService.getDatasByTimeQuantum(id, startDate, endDate);
		aj.setObj(oi);
		return new ResponseEntity<AjaxJson>(aj, HttpStatus.OK);
	}
	
}
