package org.jeecgframework.web.demo.service.impl.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mtestdrive.entity.QuestionnaireInfoEntity;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.utils.SMSUtil;

@Service("questionnaireService")
public class QuestionnaireService {
	@Autowired
	private DriveRecodsServiceI driveRecodsService;
	@Autowired
	private SystemService  systemService;
	
	private static final Logger logger = Logger.getLogger(QuestionnaireService.class);
	
	public void work(){
		logger.info("开始执行短信问卷发送任务---------------------------------------");
		//待发送待查问卷的试驾客户
		List<Map<String, String>> recodsList = driveRecodsService.getAwitSendRecodsId();
		List<Map<String, String>> recordsList2 = new ArrayList<Map<String, String>>();
		List<QuestionnaireInfoEntity> qiList = new ArrayList<QuestionnaireInfoEntity>();

		//相同用户合并
		for (Map<String, String> map : recodsList) {
			String recodesId = map.get("recodesId");//试驾表ID
			String mobile = map.get("customerMobile");
			QuestionnaireInfoEntity qi = null;
			qi = new QuestionnaireInfoEntity();
			qi.setDriveId(recodesId);
			qi.setSendTime(DateUtils.gettimestamp());
			qiList.add(qi);
			//循环是否有相同的手机号 x = 1就是有 x = 0 就是没有
			int x = 0;
			for (Map<String, String> map2 : recordsList2) {
				String mobile2 = map2.get("customerMobile");
				if(mobile2.equals(mobile)){
					String recodesId2 = map2.get("recodesId");
					map2.put("recodesId", recodesId2+","+recodesId);
					x = 1;
				}
			}
			if(x == 0){
				recordsList2.add(map);
			}
			
		}

		int total=0;
		int success = 0;
		if(recordsList2 != null && !recordsList2.isEmpty()){
			total = recordsList2.size();
			Map<String, String> map = null;
			for(int i=0; i<recordsList2.size(); i++){
				map = recordsList2.get(i);
				String recordsId = null;
				String dealerName = null;
				String customerMobile = null;
				if(map != null){
					recordsId = map.get("recodesId");//试驾表ID
					dealerName = map.get("dealerName");//经销商名称
					customerMobile = map.get("customerMobile");//客户手机号

                    String logMsg = "经销商名称："+dealerName+", 客户手机号："+customerMobile+", 试驾表ID："+recordsId;
					if(StringUtil.isNotEmpty(recordsId) && StringUtil.isNotEmpty(dealerName)
							&& StringUtil.isNotEmpty(customerMobile)){

                        //“阿里大于”参数长度限制为20
                        recordsId = recordsId.substring(0,20);

						//开始发送短信
						if(SMSUtil.sendTestDriveReportLink(dealerName, customerMobile, recordsId)){
							logger.info("调查问卷发送成功,"+logMsg);
							systemService.addSimpleLog("调查问卷发送成功,"+logMsg, Globals.LOG_TYPE_SEND_UESTIONNAIRE, Globals.Log_Leavel_INFO);
							success++;
						}else{
							logger.error("调查问卷发送失败,"+logMsg);
							systemService.addSimpleLog("调查问卷发送失败,"+logMsg, Globals.LOG_TYPE_SEND_UESTIONNAIRE, Globals.Log_Leavel_ERROR);
						}
					} else {
                        logger.error("调查问卷发送失败，发送参数为空："+logMsg);
                    }
				}
			}
			systemService.batchSave(qiList);
		}

		logger.info("短信问卷发送任务共计 "+total+" 条，发送成功 "+ success+" 条");
	}
}
