package com.mtestdrive.interfaces;

import java.util.*;

import com.mtestdrive.MaseratiConstants;
import com.mtestdrive.entity.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.service.SystemService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mtestdrive.dto.DriveRecodsSfDto;
import com.mtestdrive.utils.HttpClientUtil;

@Service("passBackDataToSfService")
public class PassBackDataToSfService {
    private static final Logger logger = Logger.getLogger(PassBackDataToSfService.class);
    private static final String TEST_AGENCY_ID = "2c9a4cf9622a343c016233f5f7472ff4";//测试经销商ID：茵诺惟熙

    @Autowired
    private SystemService sysService;
    public void work() {
        String[] tokens = HttpClientUtil.getAccessToken();
        if (null!=tokens[0]) {
            passBackAllTestDrive(tokens); // 回传试驾信息
            passBackAllDriveValid(tokens); // 回传试驾有效性信息
            passBackQuestionnaireList(tokens); // 回传所有问卷调研结果
        }
    }


    /**
     * 回传试驾数据
     * @param tokens
     */
    public void passBackAllTestDrive(String[] tokens) {
        //取没有同步到Salesforce（sf_id is null），并且试驾状态为完成及以上的合规记录
        CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class);
        cq.isNull("sfId"); //没有同步到SF的记录
        cq.notEq("agency.id", TEST_AGENCY_ID);//测试经销商不用传
        cq.in("status", new Integer[]{MaseratiConstants.DriveRecodsStatus.COMPLETE, //5 完成试驾
                MaseratiConstants.DriveRecodsStatus.GROUP, //8 已提交问卷
                MaseratiConstants.DriveRecodsStatus.GENERATEDREPORT}); //9 已生成报告
        cq.add();
        List<DriveRecodsEntity> quList = sysService.getListByCriteriaQuery(cq, false);

        if(!ListUtils.isNullOrEmpty(quList)){
            logger.info("-------------本次试驾回传记录数："+quList.size()+"---------------------");

            //构建客户来源字典表Map
            Map<String, String> quarryMap = new HashMap();
            TSTypegroup qGroup = sysService.getTypeGroupByCode("quarry");
            for (TSType tsType:qGroup.getTSTypes()){
                quarryMap.put(tsType.getTypecode(), tsType.getTypename());
            }
            Map<String, String> quarryDetailMap = new HashMap();
            TSTypegroup qdGroup = sysService.getTypeGroupByCode("quarryDetail");
            for (TSType tsType:qdGroup.getTSTypes()){
                quarryDetailMap.put(tsType.getTypecode(), tsType.getTypename());
            }


            //构建省市字典表Map
            Map<String, String> provinceCityMap = new HashMap();
            List<ChinaEntity> pcList = sysService.findByProperty(ChinaEntity.class, "pid", 0);
            for (ChinaEntity pc:pcList){
                provinceCityMap.put(pc.getId().toString(), pc.getName());
            }

            int gender = 0;

            for(int i=0; i<quList.size(); i++){
                DriveRecodsEntity driveRecodsEntity = quList.get(i);
                if(driveRecodsEntity != null){
                    DriveRecodsSfDto sfDto = new DriveRecodsSfDto();
                    sfDto.setSfId(driveRecodsEntity.getId().substring(0, 29));//SF  ID长度为30
                    sfDto.setAddress(driveRecodsEntity.getAgency().getAddress());
                    sfDto.setBirthday(DateUtils.date2Str(driveRecodsEntity.getCustomer().getBirthday(), DateUtils.date_sdf));
                    String cityId = driveRecodsEntity.getAgency().getCityId();
                    sfDto.setCity(provinceCityMap.get(cityId));
                    sfDto.setCode(driveRecodsEntity.getAgency().getCode());
                    sfDto.setDriveDate(DateUtils.date2Str(driveRecodsEntity.getDriveStartTime(), DateUtils.date_sdf));

                    gender = driveRecodsEntity.getCustomer().getGender();
                    if(gender == 0)
                        sfDto.setGender("未知");
                    if(gender == 1)
                        sfDto.setGender("男");
                    if(gender == 2)
                        sfDto.setGender("女");

                    sfDto.setMileage(String.valueOf(driveRecodsEntity.getMileage()));
                    sfDto.setEndPicPath(driveRecodsEntity.getEndPicPath());
                    sfDto.setDrivingLicensePicPath(driveRecodsEntity.getCustomer().getDrivingLicensePicPath());
                    sfDto.setMobile(driveRecodsEntity.getCustomer().getMobile());
                    sfDto.setName(driveRecodsEntity.getCustomer().getName());
                    sfDto.setSalesmanName(driveRecodsEntity.getSalesman().getName());
                    String provinceId = driveRecodsEntity.getAgency().getProvinceId();
                    sfDto.setProvinces(provinceCityMap.get(provinceId));
                    CarInfoEntity car = sysService.get(CarInfoEntity.class, driveRecodsEntity.getCarId());
                    if (null!=car) {
                        sfDto.setVin(car.getVin());
                    }
                    sfDto.setId(driveRecodsEntity.getId());
                    String quarry = quarryMap.get(String.valueOf(driveRecodsEntity.getCustomer().getQuarry()));
                    sfDto.setQuarry(quarry);//客户来源
                    String quarryDetail = quarryDetailMap.get(String.valueOf(driveRecodsEntity.getCustomer().getQuarryDetail()));
                    sfDto.setQuarryDetail(quarryDetail);//来源细分

                    passBackTestDrive(sfDto, driveRecodsEntity, tokens);
                }
            }
            logger.info("------------- 本次试驾回传结束 ---------------------");
        }


    }

    /**
     * 回传单次试驾信息，并取得在SF的记录ID，记录在本地试驾信息中
     * @param sfDto  回传的试驾信息
     * @param driveRec 本地的试驾信息
     * @param tokens 服务端鉴权信息
     */
    public void passBackTestDrive(DriveRecodsSfDto sfDto, DriveRecodsEntity driveRec, String[] tokens) {

        Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("GPSExternalID__c", driveRec.getId());
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
        paramMap.put("GPSMileage__c", "null".equals(sfDto.getMileage())?"0":sfDto.getMileage()); //不能传null，否则报错
        String licensePic=null==sfDto.getDrivingLicensePicPath()?"":("http://www.murberry.cn/mtestdrive/fileUpload.action?view&fileName="+sfDto.getDrivingLicensePicPath());
        paramMap.put("TestDrivePic__c", licensePic); //不能传null，否则报错
        paramMap.put("salesmanName__c", (StringUtils.isEmpty(sfDto.getSalesmanName())?"":sfDto.getSalesmanName()));
        paramMap.put("quarry__c", (StringUtils.isEmpty(sfDto.getQuarry())?"":sfDto.getQuarry()));
        if(!("-".equals(sfDto.getQuarryDetail())
                ||StringUtils.isEmpty(sfDto.getQuarryDetail()))) {//若无实质上的来源细分，则不传
            paramMap.put("SourceDetail__c", sfDto.getQuarryDetail());
        }
        String json = JSONObject.valueToString(paramMap);

        String result = null;
        try {
            logger.info("正在回传： json="+json);
            result = HttpClientUtil.sendSSLPATCHRequest(
                    tokens[1]+"/services/data/v34.0/sobjects/GPSTestDrive__c/GPSExternalID__c/"+sfDto.getId(),
                    json, tokens[0]);


            if (result != null) {

                if (!result.contains("errorCode")) {// 若未返回Errorcode，则obj属于正常

                    net.sf.json.JSONObject obj = JSONHelper.toJSONObject(result);
                    String sfDriveId = StringUtil.getStrByObj(obj.get("id"));//取数据添加成功之后，sf返回的ID

                    //更新本地sfId
                    driveRec.setSfId(sfDriveId);
                    sysService.updateEntitie(driveRec);

                    logger.info("试驾数据同步成功, 本地sfId已更新：result="+result+" driveId="+sfDto.getId());

                } else {
                    logger.error("试驾数据回传失败: 服务端返回的ErrorMsg="+result+" driveId="+sfDto.getId());
                }
            } else {
                logger.error("试驾数据回传失败: 服务端返回的result=null"+" driveId="+sfDto.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("试驾数据回传失败: 当前试驾json="+json+" sfDtoId="+sfDto.getId());
            sysService.addSimpleLog(e.getMessage(), Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
        }

    }

    /**
     * 回传所有问卷调研结果
     * @param tokens
     */
    public void passBackQuestionnaireList(String[] tokens) {

        //回传调查问卷
        CriteriaQuery cq = new CriteriaQuery(QuestionnaireInfoEntity.class);
        cq.isNotNull("commitTime"); //用户已经有反馈
        cq.isNull("syncTime");      //并且未同步到SF
        cq.add();
        List<QuestionnaireInfoEntity> qiList = sysService.getListByCriteriaQuery(cq, false);

        if(!ListUtils.isNullOrEmpty(qiList)){
            logger.info("开始回传问卷反馈，共计"+qiList.size()+"条");
            for(int i=0; i<qiList.size(); i++){
                QuestionnaireInfoEntity qi = qiList.get(i);
                DriveRecodsEntity dr = sysService.get(DriveRecodsEntity.class, qi.getDriveId());
                if (null== dr || StringUtils.isEmpty(dr.getSfId())) {
                    logger.error("当前问卷无对应的试驾流程或者流程未同步至Salesforce：问卷ID="+qi.getId()+" 试驾流程ID="+qi.getDriveId());
                    continue;
                }

                List<QuestionnaireQuestionEntity> qqList = sysService.findByProperty(QuestionnaireQuestionEntity.class, "questionnaireid", qi.getId());

                if(!ListUtils.isNullOrEmpty(qqList)) {
                    for(int j=0; j<qqList.size(); j++) {
                        QuestionnaireQuestionEntity qq = qqList.get(j);
                        passBackQuestionnaire(qq, dr.getSfId(), tokens);
                    }
                    //每同步完一组问题就记录同步完成时间
                    qi.setSyncTime(new Date());
                    sysService.updateEntitie(qi);
                }

            }
            logger.info("调查问卷同步结束");
        }
    }


    /**
     * 回传单条问卷调研结果
     * @param qq
     * @param sfDriveId
     * @param tokens
     */
    public void passBackQuestionnaire(QuestionnaireQuestionEntity qq, String sfDriveId, String[] tokens) {
        String qqId = qq.getId().substring(0, 29);

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("GPSTestDrive__c", sfDriveId);//SF生成的试驾流程ID
        paramMap.put("SurveyQuestion__c", qq.getQuestion());
        paramMap.put("SurveyResult__c", qq.getResult());

        String json = JSONObject.valueToString(paramMap);
        String result = null;
        try {
            result = HttpClientUtil.sendSSLPATCHRequest(tokens[1]+"/services/data/v34.0/sobjects/GPSSurveyResult__c/GPSExternalID__c/" + qqId,
                    json,
                    tokens[0]);
            if (null==result || result.contains("errorCode")) {
                logger.error("回传问卷问题失败, qqId="+qqId+" json="+json+" 服务端返回ErrMsg="+result);
            } else {
                logger.info("回传问卷问题成功, qqId="+qqId+" json="+json+" 服务端返回ErrMsg="+result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("回传问卷问题失败, qqId="+qqId+" json="+json+" 服务端返回ErrMsg="+result);
            sysService.addSimpleLog(e.getMessage(), Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
        }


    }

    /**
     * 回传试驾有效性
     * @param tokens
     */
    public void passBackAllDriveValid(String[] tokens) {
        //取没有将有效性信息同步到Salesforce（validSyncTime is null）的试驾记录
        CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class);
//        cq.eq("isValid", 1); //有效试驾
        cq.isNotNull("sfId"); //试驾信息已同步到SF
        cq.isNotNull("isValid"); //试驾有效性判断结束
        cq.isNull("validSyncTime");//且试驾有效性未同步到SF
        cq.notEq("agency.id", TEST_AGENCY_ID);//测试经销商不用传
        cq.add();
        List<DriveRecodsEntity> quList = sysService.getListByCriteriaQuery(cq, false);

        if(!ListUtils.isNullOrEmpty(quList)){
            logger.info("------------- 本次试驾有效性记录数："+quList.size()+"---------------------");

            for(int i=0; i<quList.size(); i++){
                DriveRecodsEntity driveRecodsEntity = quList.get(i);
                if(driveRecodsEntity != null){
                    passBackDriveValid(driveRecodsEntity, tokens);
                }
            }
            logger.info("------------- 本次试驾有效性回传结束 ---------------------");
        }


    }

    /**
     * 回传单次试驾有效性信息，并记录同步时间
     * @param driveRec 本地的试驾信息
     * @param tokens 服务端鉴权信息
     */
    public void passBackDriveValid(DriveRecodsEntity driveRec, String[] tokens) {

        Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("GPSExternalID__c", driveRec.getId());
        String isValid = String.valueOf(driveRec.getIsValid());
        paramMap.put("ValidTestDrive__c", "1".equals(isValid)?"true":"false");
        String json = JSONObject.valueToString(paramMap);

        try {
            logger.info("正在回传： json="+json + " driveId=" + driveRec.getId());
            String result = HttpClientUtil.sendSSLPATCHRequest(
                    tokens[1]+"/services/data/v34.0/sobjects/GPSTestDrive__c/GPSExternalID__c/"+ driveRec.getId(),
                    json, tokens[0]);

            if (result == null) {
                driveRec.setValidSyncTime(new Date());
                sysService.updateEntitie(driveRec);
                logger.info("试驾有效性同步成功, 本地ValidSyncTime已更新，" + " driveId=" + driveRec.getId());

            } else {
                logger.error("试驾有效性回传失败: 服务端返回的返回的result="+result+" driveId="+ driveRec.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("试驾有效性回传失败: 当前试驾有效性json="+json+" driveId="+ driveRec.getId());
            sysService.addSimpleLog(e.getMessage(), Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
        }

    }

}
