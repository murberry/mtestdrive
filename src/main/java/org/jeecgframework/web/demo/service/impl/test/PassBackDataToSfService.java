package org.jeecgframework.web.demo.service.impl.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mtestdrive.dto.DriveRecodsSfDto;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.entity.QuestionInfoEntity;
import com.mtestdrive.entity.QuestionnaireInfoEntity;
import com.mtestdrive.utils.HttpClientUtil;

@Service("passBackDataToSfService")
public class PassBackDataToSfService {
	private static final Logger logger = Logger.getLogger(PassBackDataToSfService.class);

	@Autowired
	private SystemService sysService;

	/**
	 * @Title: work @Description:回传试驾数据 @param: @throws
	 * UnsupportedEncodingException @param: @throws ParseException @return:
	 * void @throws
	 */
	public void work() {
		List<QuestionnaireInfoEntity> quList = sysService.findByProperty(QuestionnaireInfoEntity.class, "commitTime", DateUtils.date2Str(DateUtils.getTimestamp(), DateUtils.date_sdf));
		if(!ListUtils.isNullOrEmpty(quList)){
			DriveRecodsEntity recods = null;
			QuestionnaireInfoEntity quInfo = null;
			DriveRecodsSfDto sfDto = null;
			CarInfoEntity car =null;
			int gender = 0;
			for(int i=0; i<quList.size(); i++){
				quInfo = quList.get(i);
				recods = sysService.get(DriveRecodsEntity.class, quInfo.getDriveId());
				if(recods != null){
					sfDto = new DriveRecodsSfDto();
					sfDto.setSfId(recods.getId().substring(0, 29));//SF  ID长度为30
					sfDto.setAddress(recods.getAgency().getAddress());
					sfDto.setBirthday(DateUtils.date2Str(recods.getCustomer().getBirthday(), DateUtils.date_sdf));
					sfDto.setCity(recods.getAgency().getCityId());
					sfDto.setCode(recods.getAgency().getCode());
					sfDto.setDriveDate(DateUtils.date2Str(recods.getDriveStartTime(), DateUtils.date_sdf));
					
					gender = recods.getCustomer().getGender();
					if(gender == 0)
						sfDto.setGender("未知");
					if(gender == 1)
						sfDto.setGender("男");
					if(gender == 2)
						sfDto.setGender("女");
					
					sfDto.setMileage(recods.getMileage()+"");
					sfDto.setMobile(recods.getCustomer().getMobile());
					sfDto.setName(recods.getCustomer().getName());
					sfDto.setProvinces(recods.getAgency().getProvinceId());
					car = sysService.get(CarInfoEntity.class, recods.getCarId());
					sfDto.setVin(car.getVin());
					sfDto.setId(quInfo.getId());
					passBackTestDrive(sfDto);
				}
			}
		}
		
	}

	/**
	 * @Title: passBackTestDrive @Description: 回传试驾数据 @param: @return:
	 * void @throws
	 */
	public void passBackTestDrive(DriveRecodsSfDto sfDto) {
		logger.error("开始回传试驾数据");
		String[] tokens = HttpClientUtil.getAccessToken();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("birthday__c", sfDto.getBirthday());
		paramMap.put("vin__c", sfDto.getVin());
		paramMap.put("code__c", sfDto.getCode());
		paramMap.put("name__c", sfDto.getName());
		paramMap.put("address__c", sfDto.getAddress());
		paramMap.put("city__c", sfDto.getCity());
		paramMap.put("provinces__c", sfDto.getProvinces());
		paramMap.put("test_drive_date__c", sfDto.getDriveDate());
		paramMap.put("mobile__c", sfDto.getMobile());
		paramMap.put("gender__c", sfDto.getGender());
		paramMap.put("research_haBeen__c", "true");
		paramMap.put("GPSMileage__c", sfDto.getMileage());

		String json = JSONObject.valueToString(paramMap);
		String result = HttpClientUtil.sendSSLPATCHRequest(
				tokens[1]+"/services/data/v34.0/sobjects/GPSTestDrive__c/GPSExternalID__c/"+sfDto.getSfId(),
				json, tokens[0]);
		
		try {
			net.sf.json.JSONObject obj = JSONHelper.toJSONObject(result);
			String sfDriveId = StringUtil.getStrByObj(obj.get("id"));//数据添加成功之后，sf返回的ID
			logger.info("试驾数据同步成功，ID:"+sfDriveId);
			//回传调查问卷
			List<QuestionInfoEntity> questionInfos = sysService.findByProperty(QuestionInfoEntity.class, "questionnaireid", sfDto.getId());
			QuestionInfoEntity qi = null;
			if(!ListUtils.isNullOrEmpty(questionInfos)){
				for(int i=0; i<questionInfos.size(); i++){
					qi = questionInfos.get(i);
					passBackQuestionnaire(qi.getId().substring(0, 29), 
							sfDriveId, qi.getQuestion().toString(), qi.getResult().toString());
				}
			}
		} catch (Exception e) {
			logger.error(result);//数据添加失败
			sysService.addSimpleLog(result, Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
		}
	}
	
	/**
	 * @Title: passBackQuestionnaire   
	 * @Description: 回传调查问卷数据到SF
	 * @param: @param id length 30
	 * @param: @param sfId length 30
	 * @param: @param question
	 * @param: @param qresult      
	 * @return: void      
	 * @throws
	 */
	public void passBackQuestionnaire(String id, String sfId, String question, String qresult) {
		 String[] tokens = HttpClientUtil.getAccessToken();
		 
		 Map<String, String> paramMap = new HashMap<String, String>();
		  paramMap.put("GPSTestDrive__c", sfId);
		  paramMap.put("SurveyQuestion__c", question);
		  paramMap.put("SurveyResult__c", qresult);
		  /* paramMap.put("GPSTestDrive__c", "a24p00000007MTxAAM");
		  paramMap.put("SurveyQuestion__c", "试驾后您是否考虑购买？");
		  paramMap.put("SurveyResult__c", "是");*/
		  String json = JSONObject.valueToString(paramMap);
		  String result = HttpClientUtil.sendSSLPATCHRequest(tokens[1]+"/services/data/v34.0/sobjects/GPSSurveyResult__c/GPSExternalID__c/"+id,json,tokens[0]);
		  try {
			net.sf.json.JSONObject sfObjId = JSONHelper.toJSONObject(result);
			logger.info("调查问卷同步成功，ID:"+sfObjId);
		} catch (Exception e) {
			logger.error(result);
		}
	}

	public static void main(String[] args) {
		String str = "{\"id\":\"a24p00000007MU7AAM\",\"success\":true,\"errors\":[]}";
		net.sf.json.JSONObject obj = JSONHelper.toJSONObject(str);
		System.out.println(obj.get("id"));
		
	}
}
