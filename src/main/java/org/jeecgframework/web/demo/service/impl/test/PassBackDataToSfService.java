package org.jeecgframework.web.demo.service.impl.test;

import java.util.*;

import com.mtestdrive.MaseratiConstants;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
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
		//取预约试驾状态为完成（status=5） 且没有同步到Salesforce（sf_id is null）的记录
		CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class);
//		cq.in("status", new Integer[]{MaseratiConstants.DriveRecodsStatus.COMPLETE,
//				                              MaseratiConstants.DriveRecodsStatus.GENERATEDREPORT}); //试驾状态暂时不管
		cq.isNull("sfId");
		cq.add();
		List<DriveRecodsEntity> quList = sysService.getListByCriteriaQuery(cq, false);
        logger.info("发送试驾数据      begin （本次回传试驾数据记录数："+quList.size()+')');

		if(!ListUtils.isNullOrEmpty(quList)){
			DriveRecodsEntity recods = null;
//			QuestionnaireInfoEntity quInfo = null;
			DriveRecodsSfDto sfDto = null;
			CarInfoEntity car =null;
			int gender = 0;
            String[] tokens = HttpClientUtil.getAccessToken();

			for(int i=0; i<quList.size(); i++){
                DriveRecodsEntity driveRecodsEntity = quList.get(i);
				if(driveRecodsEntity != null){
					sfDto = new DriveRecodsSfDto();
					sfDto.setSfId(driveRecodsEntity.getId().substring(0, 29));//SF  ID长度为30
					sfDto.setAddress(driveRecodsEntity.getAgency().getAddress());
					sfDto.setBirthday(DateUtils.date2Str(driveRecodsEntity.getCustomer().getBirthday(), DateUtils.date_sdf));
					sfDto.setCity(driveRecodsEntity.getAgency().getCityId());
					sfDto.setCode(driveRecodsEntity.getAgency().getCode());
					sfDto.setDriveDate(DateUtils.date2Str(driveRecodsEntity.getDriveStartTime(), DateUtils.date_sdf));
					
					gender = driveRecodsEntity.getCustomer().getGender();
					if(gender == 0)
						sfDto.setGender("未知");
					if(gender == 1)
						sfDto.setGender("男");
					if(gender == 2)
						sfDto.setGender("女");
					
					sfDto.setMileage(driveRecodsEntity.getMileage()+"");
					sfDto.setMobile(driveRecodsEntity.getCustomer().getMobile());
					sfDto.setName(driveRecodsEntity.getCustomer().getName());
					sfDto.setProvinces(driveRecodsEntity.getAgency().getProvinceId());
					car = sysService.get(CarInfoEntity.class, driveRecodsEntity.getCarId());
					if (null!=car) {
						sfDto.setVin(car.getVin());
					};
					sfDto.setId(driveRecodsEntity.getId());

					passBackTestDrive(sfDto, driveRecodsEntity, tokens);
				}
			}
		}
		logger.info("发送试驾数据       end");


	}

    /**
     * 回传的试驾信息，并取得在SF的记录ID，记录在本地试驾信息中
     * @param sfDto  回传的试驾信息
     * @param driveRec 本地的试驾信息
     * @param tokens 服务端鉴权信息
     */
	public void passBackTestDrive(DriveRecodsSfDto sfDto, DriveRecodsEntity driveRec, String[] tokens) {

		Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("GPSExternalID__c", sfDto.getId());
		paramMap.put("vin__c", sfDto.getVin());
		paramMap.put("birthday__c", sfDto.getBirthday());
		paramMap.put("code__c", sfDto.getCode());
		paramMap.put("name__c", sfDto.getName());
		paramMap.put("address__c", sfDto.getAddress());
		paramMap.put("city__c", sfDto.getCity());
		paramMap.put("provinces__c", sfDto.getProvinces());
		paramMap.put("test_drive_date__c", sfDto.getDriveDate());
		paramMap.put("mobile__c", sfDto.getMobile());
		paramMap.put("gender__c", sfDto.getGender());
		paramMap.put("research_haBeen__c", "true");
//		paramMap.put("GPSMileage__c", sfDto.getMileage()); //暂不回传，不能传nll，否则报错
		paramMap.put("endPicPath", sfDto.getEndPicPath());
		paramMap.put("salesmanName", sfDto.getSalesmanName());
		String json = JSONObject.valueToString(paramMap);

        String result = null;
        try {
            logger.info("正在回传： json="+json);
		    result = HttpClientUtil.sendSSLPATCHRequest(
				tokens[1]+"/services/data/v34.0/sobjects/GPSTestDrive__c/GPSExternalID__c/"+sfDto.getId(),
                    json, tokens[0]);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("试驾数据回传失败: result="+result+"   json="+json);//数据添加失败
            sysService.addSimpleLog(e.getMessage(), Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
        }

        if (result != null) {
            net.sf.json.JSONObject obj = JSONHelper.toJSONObject(result);
            String sfDriveId = StringUtil.getStrByObj(obj.get("id"));//数据添加成功之后，sf返回的ID

            //1.回传调查问卷
            List<QuestionInfoEntity> questionInfos = sysService.findByProperty(QuestionInfoEntity.class, "questionnaireid", sfDto.getId());
            QuestionInfoEntity qi = null;
            if(!ListUtils.isNullOrEmpty(questionInfos)){
                for(int i=0; i<questionInfos.size(); i++){
                    qi = questionInfos.get(i);
                    passBackQuestionnaire(qi.getId().substring(0, 29),
                            sfDriveId, qi.getQuestion().toString(), qi.getResult().toString());
                }
            }

            //2.更新本地sfId
            driveRec.setSfId(sfDriveId);
            sysService.updateEntitie(driveRec);

            logger.info("试驾数据同步成功, 本地sfId已更新：result="+result+"  sfId="+sfDriveId+"  json="+json);

        } else {
            logger.error("试驾数据添加后未正常返回（可能已经回传过了）: result="+result+"  json="+json);
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
//		String str = "{\"id\":\"a24p00000007MU7AAM\",\"success\":true,\"errors\":[]}";
//		net.sf.json.JSONObject obj = JSONHelper.toJSONObject(str);
//		System.out.println(obj.get("id"));


	}
}
