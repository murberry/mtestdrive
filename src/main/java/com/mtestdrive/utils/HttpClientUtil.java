package com.mtestdrive.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.util.PropertiesUtil;
import org.jeecgframework.core.util.StringUtil;
import org.json.JSONObject;

/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpClientUtil {

	private static final Logger logger = Logger.getLogger(HttpClientUtil.class);

	private static  String USER_NAME ;
	private static  String PASSWORD ;
	private static  String GRANT_TYPE ;
	private static  String GRANT_SERVICE ;
	private static  String CLIENT_ID;
	private static  String CLIENT_SECRET;

    static {
		PropertiesUtil util = new PropertiesUtil("sysConfig.properties");
		HttpClientUtil.USER_NAME=util.readProperty("sfapi.user.name");
		HttpClientUtil.PASSWORD=util.readProperty("sfapi.password");
		HttpClientUtil.GRANT_TYPE=util.readProperty("sfapi.grant.type");
		HttpClientUtil.GRANT_SERVICE=util.readProperty("sfapi.grant.service");
		HttpClientUtil.CLIENT_ID=util.readProperty("sfapi.client.id");
		HttpClientUtil.CLIENT_SECRET=util.readProperty("sfapi.client.secret");
	}

	public static String doPostGetAccessToken() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("grant_type", GRANT_TYPE);
		paramMap.put("client_id", CLIENT_ID);
		paramMap.put("client_secret", CLIENT_SECRET);
		paramMap.put("username", USER_NAME);
		paramMap.put("password", PASSWORD);
        String content = sendSSLPostRequest(GRANT_SERVICE, paramMap);
		
		JSONObject a = new JSONObject(content);
		String token = (String) a.get("access_token");
		return token;
	}
	
	public static String[] getAccessToken() {
		String[] results = new String[3];
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("grant_type", GRANT_TYPE);
			paramMap.put("client_id", CLIENT_ID);
			paramMap.put("client_secret", CLIENT_SECRET);
			paramMap.put("username", USER_NAME);
			paramMap.put("password", PASSWORD);
			String Content = sendSSLPostRequest(GRANT_SERVICE, paramMap);
			JSONObject a = new JSONObject(Content);
			results[0] = StringUtil.getStrByObj(a.get("access_token"));
			results[1] = StringUtil.getStrByObj(a.get("instance_url"));
			results[2] = StringUtil.getStrByObj(a.get("token_type"));
		} catch (Exception e) {
    		logger.error("无法取到token："+e.getMessage());
		}
		return results;
	}

	public static String doGetQueryList(String sql) {
		String[] tokens = getAccessToken();
		if (null!=tokens[0]) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("q", sql);
			String result = sendSSLGetRequest(
					tokens[1]+"/services/data/v34.0/query", paramMap,
					tokens[0]);
			return result;
		} else {
			return null;
		}
	}

	public static String doPatchInsertTestDrive() {
		String token = doPostGetAccessToken();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("democar__c", "a0N6F00000J3QtjUAF");
		paramMap.put("TestDriveDate__c", "2017-03-09");
		paramMap.put("Startkm__c", "10.00");
		paramMap.put("EndKm__c", "100");
		paramMap.put("Accuont__c", "0019000000Mniw7AAB");
		paramMap.put("Attachment__c",
				"<img alt='用户添加的图片 'src='https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png'></img>");
		// paramMap.put("RecordTypeId", "012900000003S5oA");
		String json = JSONObject.valueToString(paramMap);
		String result = sendSSLPATCHRequest(
				"https://lightningdemotzemaserati--all.cs31.my.salesforce.com/services/data/v34.0/sobjects/test_drive__c/GPSExternalID__c/402880195b0eae26015b0eb2227400",
				json, token);
		return result;
	}

	// 明细的保存
	public String doPatchSaveTestDrive(String democar__c, String TestDriveDate__c, String Startkm__c, String EndKm__c,
			String Accuont__c, String Attachment__c, String RecordTypeId) {
		String token = doPostGetAccessToken();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("democar__c", democar__c);
		paramMap.put("TestDriveDate__c", TestDriveDate__c);
		paramMap.put("Startkm__c", Startkm__c);
		paramMap.put("EndKm__c", EndKm__c);
		paramMap.put("Accuont__c", Accuont__c);
		paramMap.put("Attachment__c", Attachment__c);
		paramMap.put("RecordTypeId", RecordTypeId);
		String json = JSONObject.valueToString(paramMap);
		String result = sendSSLPATCHRequest(
				"https://lightningdemotzemaserati--all.cs31.my.salesforce.com/services/data/v34.0/sobjects/test_drive__c/GPSExternalID__c/15",
				json, token);
		return result;
	}

	public static String doPatchSaveCustomer() {
		String token = doPostGetAccessToken();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("Name", "崔梦涛");
		paramMap.put("Genger__c", "男");
		paramMap.put("PersonBirthdate", "2017-04-07");
		paramMap.put("PersonMobilePhone", "13127673365");
		paramMap.put("Dealer_Code__C", "63874");
		// paramMap.put("Owner", Attachment__c);
		paramMap.put("LeadsStatus__c", "Cold");
		paramMap.put("AccountSource__c", "厂家销售线索");
		paramMap.put("AccountSourceDetail__c", "400电话");
		String json = JSONObject.valueToString(paramMap);
		String result = sendSSLPATCHRequest(
				"https://lightningdemotzemaserati--all.cs31.my.salesforce.com/services/data/v34.0/sobjects/Account/GPSExternalID__c/297e25b75b28204",
				json, token);
		return result;
	}

	/**
	 * 向HTTPS地址发送POST请求
	 * 
	 * @param reqURL
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 响应内容
	 */
	@SuppressWarnings("finally")
	public static String sendSSLPostRequest(String reqURL, Map<String, String> params) {
		long responseLength = 0; // 响应长度
		String responseContent = null; // 响应内容
		HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
		X509TrustManager xtm = new X509TrustManager() { // 创建TrustManager
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		try {
			// TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");

			// 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);

			// 创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

			// 通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

			HttpPost httpPost = new HttpPost(reqURL); // 创建HttpPost
			List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 构建POST请求的表单参数
			for (Map.Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost); // 执行POST请求
			HttpEntity entity = response.getEntity(); // 获取响应实体

			if (null != entity) {
				responseLength = entity.getContentLength();
				responseContent = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity); // Consume response content
			}
			logger.debug("请求地址: " + httpPost.getURI());
			logger.debug(params.toString());
			logger.debug("响应状态: " + response.getStatusLine());
			logger.debug("响应长度: " + responseLength);
			logger.debug("响应内容: " + responseContent);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
			return responseContent;
		}
	}

	/**
	 * 向HTTPS地址发送GET请求
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 响应内容
	 */
	@SuppressWarnings("finally")
	public static String sendSSLGetRequest(String url, Map<String, String> params, String token) {
		long responseLength = 0; // 响应长度
		String result = null; // 响应内容
		HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
		X509TrustManager xtm = new X509TrustManager() { // 创建TrustManager
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		try {
			// TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");

			// 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);

			// 创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

			// 通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

			String apiUrl = url;
			StringBuffer param = new StringBuffer();
			int i = 0;
			for (String key : params.keySet()) {
				if (i == 0)
					param.append("?");
				else
					param.append("&");
				param.append(key).append("=").append(params.get(key));
				i++;
			}
			apiUrl += param;

			HttpClient httpclient = new DefaultHttpClient();
			try {
				HttpGet httpGet = new HttpGet(apiUrl);
				String Authorization = "Bearer " + token;
				httpGet.setHeader("Authorization", Authorization);
				httpGet.setHeader("Content-Type","application/json");
				HttpResponse response = httpclient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();

				logger.debug("执行状态码 : " + statusCode);

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					result = IOUtils.toString(instream, "UTF-8");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			logger.info("响应内容: " + result);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
			return result;
		}
	}

	/**
	 * 向HTTPS地址发送PATCH请求
	 * 
	 * @param url 请求地址
	 * @param json 请求参数
	 * @param token 请求参数
	 * @return 响应内容
	 */
	@SuppressWarnings("finally")
	public static String sendSSLPATCHRequest(String url, String json, String token) {
		long responseLength = 0; // 响应长度
		String result = null; // 响应内容
		HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
		X509TrustManager xtm = new X509TrustManager() { // 创建TrustManager
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		try {
			// TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");

			// 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);

			// 创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

			// 通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

			String apiUrl = url;

			HttpClient httpclient = new DefaultHttpClient();
			try {
				
				HttpPatch patch = new HttpPatch(apiUrl);
				String Authorization = "Bearer " + token;
				patch.setHeader("Authorization", Authorization);
				patch.setHeader("Content-Type", "application/json; charset=utf-8");
				patch.setHeader("Accept", "application/json");	
				patch.setEntity(new StringEntity(json.toString(), Charset.forName("UTF-8")));
				HttpResponse response = httpclient.execute(patch);
				
				int statusCode = response.getStatusLine().getStatusCode();

				logger.debug("执行状态码 : " + statusCode);

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					result = IOUtils.toString(instream, "UTF-8");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			logger.debug("响应内容: " + result);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
			return result;
		}
	}

	/**
	 * @Title: doPatchSaveTestDriveAppointment   
	 * @Description: 回传试驾数据 
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String doPatchSaveTestDriveAppointment(){
		  String[] tokens = getAccessToken();
		  Map<String, String> paramMap = new HashMap<String, String>();
		  paramMap.put("birthday__c", "2017-04-19");
		  paramMap.put("vin__c", "ZAMPP56E3E1069238");
		  paramMap.put("code__c", "69990");
		  paramMap.put("name__c", "CMT");
		  paramMap.put("address__c", "上海市");
		  paramMap.put("city__c", "上海市");
		  paramMap.put("provinces__c", "上海市");
		  paramMap.put("test_drive_date__c", "2017-04-20");
		  paramMap.put("mobile__c", "131xxxxxxxx");
		  paramMap.put("gender__c", "男");
		  paramMap.put("research_haBeen__c", "false");
		  paramMap.put("GPSMileage__c", "9.21");
		
		  String json = JSONObject.valueToString(paramMap);
		  String result = sendSSLPATCHRequest(tokens[1]+"/services/data/v34.0/sobjects/GPSTestDrive__c/GPSExternalID__c/402880195b0eae26015b0eb21e6722",json,tokens[0]);
		  return result;
	  }
	
	
	/**
	 * @Title: doPatchSaveQuestionnaire   
	 * @Description: 回传调研问卷数据
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String doPatchSaveQuestionnaire(){
		String[] tokens = getAccessToken();
		  Map<String, String> paramMap = new HashMap<String, String>();
		  
		  paramMap.put("GPSTestDrive__c", "a276F00000Bjx5vQAB");
		 
		  paramMap.put("SurveyQuestion__c", "试驾后您是否考虑购买？");
		  paramMap.put("SurveyResult__c", "是");
		  /* paramMap.put("SurveyQuestion__c", "试驾时您对车辆的整体表现是否满意？");
		  paramMap.put("SurveyResult__c", "满意");*/
		  
		  
		 /* Map<String, Object> map1 = new HashMap<>();
		  map1.put("SurveyQuestion__c", "试驾时您对车辆的整体表现是否满意？");
		  map1.put("SurveyResult__c", "满意");
		  
		  Map<String, Object> map2 = new HashMap<>();
		  map2.put("SurveyQuestion__c", "试驾后您是否考虑购买？");
		  map2.put("SurveyResult__c", "是");*/
		  
		 // paramMap.put("surveyResults", JSONHelper.map2json(maps));
		 
		
		  String json = JSONObject.valueToString(paramMap);
		  String result = sendSSLPATCHRequest(tokens[1]+"/services/data/v34.0/sobjects/GPSSurveyResult__c/GPSExternalID__c/a24p00000007MTxAAM",json,tokens[0]);
		  return result;
	  }
	
	public static void main(String[] args) {

		String sql="select+Id,GPSExternalID__c,PurchaseDealer__c,CarPurchase__c,PurchaseTime__c,PurchaseModel__c,LastModifiedDate+from+GPSTestDrive__c+where+CarPurchase__c=true+and+LastModifiedDate%3E2017-01-01T00:00:00Z";
		System.out.println(doGetQueryList(sql));
	}

}