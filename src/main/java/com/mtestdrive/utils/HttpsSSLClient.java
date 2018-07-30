package com.mtestdrive.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.util.PropertiesUtil;
import org.jeecgframework.core.util.StringUtil;
import org.json.JSONObject;


/**
 * Http SSL 请求工具类
 * @author Arlight
 *
 */

public class HttpsSSLClient {

    private static final Logger logger = Logger.getLogger(HttpClientUtil.class);

    private static  String USER_NAME ;
    private static  String PASSWORD ;
    private static  String GRANT_TYPE ;
    private static  String GRANT_SERVICE ;
    private static  String CLIENT_ID;
    private static  String CLIENT_SECRET;

    static {

        USER_NAME = "";
        PASSWORD = "";
        GRANT_TYPE = "password";
        GRANT_SERVICE = "https://login.salesforce.com/services/oauth2/token";
        CLIENT_ID = "3MVG9Y6d_Btp4xp7oyJw27xIVRBUzzehusxDk.4glFHzr.mksyyzooumEQbcvxX.Sd5lOB5MwZ6gCDh3tRMqh";
        CLIENT_SECRET = "1000095608080642702";
    }

    /**
     * 获取Https 请求客户端
     * @return
     */
    public static CloseableHttpClient createSSLInsecureClient() {
        SSLContext sslcontext = createSSLContext();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new HostnameVerifier() {
             @Override
            public boolean verify(String paramString, SSLSession paramSSLSession) {
                return true;
            }
        });
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        return httpclient;
    }
 
    /**
     * 获取初始化SslContext
     * @return
     */
    private static SSLContext createSSLContext() {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[] {new TrustAnyTrustManager()}, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslcontext;
    }
 
    /**
     * 自定义静态私有类
     */
    private static class TrustAnyTrustManager implements X509TrustManager {
         public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
         public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
         public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    /**
     *
     * @param url
     * @param charSet
     * @param token
     * @return
     * @throws Exception
     */
    public static String doGet(String url, String charSet, String token){
        HttpGet getMethod = new HttpGet(url);
        String Authorization = "Bearer " + token;
        getMethod.setHeader("Authorization", Authorization);
        getMethod.setHeader("Content-Type","application/json");
        return execute(url, charSet, getMethod);
    }

    /**
     *
     * @param url
     * @param charSet
     * @param header
     * @param urlParameters
     * @return
     * @throws Exception
     */
	public static String doPost(String url, String charSet, Header header, List<NameValuePair> urlParameters) throws Exception {
        HttpPost postMethod = new HttpPost(url);
        if (null!=header) {
            postMethod.setHeader(header);
        }
        if (null!=urlParameters) {
        	postMethod.setEntity(new UrlEncodedFormEntity(urlParameters));
        }
        return execute(url, charSet, postMethod);
    }


    private static String execute(String url, String charSet, HttpUriRequest method) {
        // When an instance CloseableHttpClient is no longer needed and is about to go out
        // of scope the connection manager associated with it must be shut down by calling the
        // CloseableHttpClient.close() method.
        CloseableHttpClient httpClient = null;
        if (StringUtils.startsWith(url, "https")) {
            httpClient = HttpsSSLClient.createSSLInsecureClient();
        } else {
            httpClient = HttpClients.createDefault();
        }
        charSet = StringUtils.isBlank(charSet) ? "UTF-8" : charSet;
        CloseableHttpResponse response = null;
        HttpEntity responseEntity = null;
        try {
            response = httpClient.execute(method);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state != HttpStatus.SC_OK) {
                throw new Exception("响应编码:"+state);
            }
            responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity, charSet);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                EntityUtils.consume(responseEntity);
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String doGetQueryList(String sql) {
        String[] tokens = HttpsSSLClient.getAccessToken();
        String url = tokens[1]+"/services/data/v34.0/query?q="+sql;
        String result=HttpsSSLClient.doGet(url,"UTF8", tokens[0]);
        return result;
    }

    public static String[] getAccessToken() {
        String[] results = new String[3];
        try {
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("grant_type", GRANT_TYPE));
            urlParameters.add(new BasicNameValuePair("client_id", CLIENT_ID));
            urlParameters.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
            urlParameters.add(new BasicNameValuePair("username", USER_NAME));
            urlParameters.add(new BasicNameValuePair("password", PASSWORD));

            String Content = HttpsSSLClient.doPost(GRANT_SERVICE, "UTF-8", null, urlParameters);//sendSSLPostRequest(GRANT_SERVICE, paramMap);
            JSONObject a = new JSONObject(Content);

            results[0] = StringUtil.getStrByObj(a.get("access_token"));
            results[1] = StringUtil.getStrByObj(a.get("instance_url"));
            results[2] = StringUtil.getStrByObj(a.get("token_type"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

	/**
     * 测试方法
     * @param args
     */
	public static void main(String[] args) throws Exception {

    	String sql="select+Id,GPSExternalID__c,PurchaseDealer__c,CarPurchase__c,PurchaseTime__c,PurchaseModel__c,LastModifiedDate+from+GPSTestDrive__c+where+CarPurchase__c=true+and+LastModifiedDate%3E2017-01-01T00:00:00Z";
        System.out.println(doGetQueryList(sql));
				
    }
 
}