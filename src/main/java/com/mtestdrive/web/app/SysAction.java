package com.mtestdrive.web.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mtestdrive.MaseratiConstants.AuthCodeStatus;
import com.mtestdrive.dto.GroupDto;
import com.mtestdrive.entity.AuthCodeEntity;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.entity.QuestionInfoEntity;
import com.mtestdrive.entity.QuestionnaireInfoEntity;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.AuthCodeServiceI;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.utils.SMSUtil;
import com.mtestdrive.utils.ValidateUtil;

/**
 * @Title: Action
 * @Description: 销售顾问信息
 * @author zhangdaihao
 * @date 2017-03-10 17:25:38
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/sysAction")
public class SysAction extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SysAction.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private AuthCodeServiceI authCodeService;

	@Autowired
	private CarInfoServiceI carInfoService;
	@Autowired
	private Validator validator;
	@Autowired
	private DriveRecodsServiceI driveRecodsService;

	/**
	 * @Title: sendTestDriveReport @Description: 发送试驾报告短信 @param: tel @param:
	 *         testDriveId @return: AjaxJson @throws
	 */
	/*
	 * public AjaxJson sendTestDriveReport(@RequestParam(required = true) String
	 * tel,
	 * 
	 * @RequestParam(required = true) String testDriveId) {
	 * 
	 * AjaxJson aj = new AjaxJson();
	 * 
	 * if (SMSUtil.sendTestDriveReportLink(tel, testDriveId)) {
	 * aj.setSuccess(true); aj.setMsg("调查问卷发送成功"); } else {
	 * aj.setSuccess(false); aj.setMsg("调查问卷发送失败"); } return aj; }
	 */

	/**
	 * @Title: jumpToQuestionnairePage
	 * @Description: 跳转到试驾报告页面
	 * @param: @param
	 *             testDriveId
	 * @param: @return
	 * @return: ModelAndView
	 * @throws @author:
	 *             mengtaocui
	 */
	@RequestMapping(params = "jumpToQuestionnairePage")
	public ModelAndView jumpToQuestionnairePage(@RequestParam(required = true) String testDriveId) {
		ModelAndView view = new ModelAndView();
		view.addObject("testDriveId", testDriveId);
		view.setViewName("fenerateReport");
		return view;
	}

	@RequestMapping(params = "report", method = RequestMethod.POST)
	@ResponseBody
	public String report(HttpServletRequest request, @RequestBody GroupDto group) {
		String driveId = group.getDriveId();
		String[] ids = driveId.split(",");
		for (String id : ids) {
			List<QuestionnaireInfoEntity> qis = systemService
					.findByProperty(QuestionnaireInfoEntity.class, "driveId", id);
			if(!ListUtils.isNullOrEmpty(qis)){
				QuestionnaireInfoEntity qi = qis.get(0);
				qi.setCommitTime(DateUtils.date2Str(DateUtils.getTimestamp(), DateUtils.date_sdf2));
				systemService.updateEntitie(qi);
				String re1 = "";
				switch(group.getGroup_1()){
					case 1:
						re1 = "不满意";
						break;
					case 2:
						re1 = "满意";
						break;
				}
				
				String re2 = "";
				switch(group.getGroup_2()){
					case 1:
						re2 = "是";
						break;
					case 2:
						re2 = "不确定";
						break;
					case 3:
						re2 = "否";
						break;
				}
				
				String re3 = "";
				switch(group.getGroup_2()){
					case 1:
						re3 = "非常满意";
						break;
					case 2:
						re3 = "一般满意";
						break;
					case 3:
						re3 = "不满意";
						break;
				}
				
				String re4 = "";
				switch(group.getGroup_2()){
					case 1:
						re4 = "非常专业";
						break;
					case 2:
						re4 = "一般专业";
						break;
					case 3:
						re4 = "不专业";
						break;
				}
				// 获取答案
				List<QuestionInfoEntity> qiList = new ArrayList<QuestionInfoEntity>();
				QuestionInfoEntity qie1 = new QuestionInfoEntity();
				qie1.setQuestionnaireid(qi.getId());
				qie1.setQuestion(getQuestionTextByCode(1));
				qie1.setResult(re1);
				qiList.add(qie1);
				
				QuestionInfoEntity qie2 = new QuestionInfoEntity();
				qie2.setQuestionnaireid(qi.getId());
				qie2.setQuestion(getQuestionTextByCode(2));
				qie2.setResult(re2);
				qiList.add(qie2);
				
				QuestionInfoEntity qie3 = new QuestionInfoEntity();
				qie3.setQuestionnaireid(qi.getId());
				qie3.setQuestion(getQuestionTextByCode(3));
				qie3.setResult(re3);
				qiList.add(qie3);
				
				QuestionInfoEntity qie4 = new QuestionInfoEntity();
				qie4.setQuestionnaireid(qi.getId());
				qie4.setQuestion(getQuestionTextByCode(4));
				qie4.setResult(re4);
				qiList.add(qie4);
				// 保存答案
				systemService.batchSave(qiList);
				
				// 修改状态
				DriveRecodsEntity driveRecodsEntity = driveRecodsService.get(DriveRecodsEntity.class, id);
				driveRecodsEntity.setStatus(8);
				driveRecodsService.saveOrUpdate(driveRecodsEntity);
				carInfoService.subTimes(driveRecodsEntity.getCarId());
			}
		}
		
		
		

		// 结束
		return "1";
	}

	public String getQuestionTextByCode(int code) {
		switch (code) {
		case 1:
			return "试驾时您对车辆的整体表现是否满意？";
		case 2:
			return "试驾后您是否考虑购买？";
		case 3:
			return "我们的试驾路线您是否满意？";
		case 4:
			return "试驾过程中我们的销售是否讲解专业？";
		}
		return null;
	}

	/**
	 * @Title: checkPhoneAndAhthCode
	 * @Description: 校验验证码正确性
	 * @param: @param
	 *             phone
	 * @param: @param
	 *             code
	 * @param: @return
	 * @return: AjaxJson
	 * @throws @author:
	 *             mengtaocui
	 */
	@RequestMapping(params = "checkPhoneAndAhthCode", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson checkPhoneAndAhthCode(@RequestParam(required = true) String phone,
			@RequestParam(required = true) String code) {

		AjaxJson aj = new AjaxJson();
		if (authCodeService.checkAuthCode(phone, code)) {
			aj.setSuccess(true);
		} else {
			// 验证码错误
			aj.setSuccess(false);
			aj.setMsg("验证码错误");
		}
		return aj;
	}

	/**
	 * @Title: create
	 * @Description: 发送重置密码的短信验证码
	 * @param: @param
	 *             tel
	 * @param: @return
	 * @return: AjaxJson
	 * @throws @author:
	 *             mengtaocui
	 */
	@RequestMapping(params = "sendAuthCode", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson sendAuthCode(@RequestParam(required = true) String tel) {
		AjaxJson aj = new AjaxJson();
		// 验证手机号码合法性
		if (ValidateUtil.isPhoneLegal(tel)) {
			//验证手机号是否是时候销售顾问手机号
			
			List<SalesmanInfoEntity> seList = systemService.findByProperty(SalesmanInfoEntity.class, "mobile", tel);
			if(ListUtils.isNullOrEmpty(seList)){
				aj.setSuccess(false);
				aj.setMsg("手机号码不不合法");
			}else{
				// 查询最近一次发送验证码的时间
				AuthCodeEntity authCode = authCodeService.getLastCode(tel);

				if (authCode != null && authCode.getCreateTime() != null) {
					// 判断最近距离最近一次发送时间是否大于1分钟
					if (DateUtils.getTimeDifferenceAboutNow(authCode.getCreateTime().getTime()) >= 1) {
						// 距离上次发送时间大于1分钟，可以发送
						generateAuthCodeMsg(sendAuthCodeCore(tel), aj);
					} else {
						// 距离上次发送时间不到1分钟，提示请稍后再试
						aj.setSuccess(false);
						aj.setMsg("请稍后再试");
					}
				} else {
					// 之前没有发送过验证码，可以发送
					generateAuthCodeMsg(sendAuthCodeCore(tel), aj);
				}
			}
		} else {
			aj.setSuccess(false);
			aj.setMsg("手机号码不合法");
		}
		return aj;
	}

	public boolean sendAuthCodeCore(String phone) {
		String code = SMSUtil.createRandomVcode();

		// 短信发送成功，保存到数据库 SMSUtil.sendTestDriveReportLink(phone)
		if (SMSUtil.sendAuthCode(phone, code)) {
			AuthCodeEntity authCode = new AuthCodeEntity();
			authCode.setStatus(AuthCodeStatus.UNUSED);
			authCode.setPhone(phone);
			authCode.setCode(code);
			authCode.setCreateTime(DateUtils.getTimestamp());
			try {
				// 将手机号和验证码保存到数据库
				systemService.save(authCode);
			} catch (Exception e) {
				logger.error(e.getMessage());
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public AjaxJson generateAuthCodeMsg(boolean flag, AjaxJson aj) {
		if (aj == null)
			aj = new AjaxJson();

		if (flag) {
			aj.setSuccess(true);
			aj.setMsg("发送成功");
		} else {
			aj.setSuccess(false);
			aj.setMsg("验证码发送失败，请稍后再试");
		}
		return aj;
	}
}
