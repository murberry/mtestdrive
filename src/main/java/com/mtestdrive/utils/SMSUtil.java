package com.mtestdrive.utils;

import org.apache.log4j.Logger;
import org.jeecgframework.core.util.StringUtil;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SMSUtil {

	private final static String apiUrl = "http://gw.api.taobao.com/router/rest";
	private final static String appKey = "23766081";
	private final static String secret = "b2162f67e210055926506d50d40c5101";
	private final static String signName = "玛莎拉蒂中国";
	
	private static final Logger logger = Logger.getLogger(SMSUtil.class);

	/**
	 * @Title: sendAuthCode
	 * @Description: 发送验证码
	 * @param:
	 * @return: void
	 * @throws ApiException
	 * @throws @author:
	 *             mengtaocui
	 */
	public static boolean sendAuthCode(String tel, String authCode) {
		// 验证码模板CODE SMS_57195024 旧版短信验证码模版 SMS_63085173
		String json = "{\"authCode\":\"" + authCode + "\"}";
		return send(tel, json, "SMS_62520441"); // 新模版2022-0814
	}

	public static boolean sendTestDriveReportLink(String dealerName, String tel, String driveId, String driveId_cut) {
		String json = "{\"dealerName\":\""+dealerName+"\","
				       +"\"recode\":\""+driveId+"\","
				       +"\"recode_cut\":\""+driveId_cut+"\""
				       +"}";
		return send(tel, json, "SMS_141607724");
	}

	/**
	 * @Title: send
	 * @Description: 发送短信核心方法
	 * @param:
	 * @return: void
	 * @throws ApiException
	 * @throws @author:
	 *             mengtaocui
	 */
	public static boolean send(String tel, String jsonParam, String templateCode) {
		TaobaoClient client = new DefaultTaobaoClient(apiUrl, appKey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName(signName);
		req.setSmsParamString(jsonParam);
		req.setRecNum(tel);
		req.setSmsTemplateCode(templateCode);

		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
			logger.info(rsp.getBody());
			//errorCode==null表示无异常
			if (StringUtil.isEmpty(rsp.getErrorCode()))
				return true;
			else
				return false;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
	 * @Title: createRandomVcode
	 * @Description: 生成6位验证码
	 * @param:
	 * @return: String
	 * @throws @author:mengtaocui
	 */
	public static String createRandomVcode() {
		// 验证码
		String vcode = "";
		for (int i = 0; i < 6; i++) {
			vcode = vcode + (int) (Math.random() * 9);
		}
		return vcode;
	}

	public static void main(String[] args) {
		String driveId = "2c9a4cf96107c97c016140c6f64c4e06";
		// sendTestDriveReportLink("茵诺惟熙","139XXXXXXXX",driveId.substring(0,20), driveId.substring(20,32));
		sendAuthCode("13917264379","8888-8888");
	}
}