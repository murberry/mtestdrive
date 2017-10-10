package com.mtestdrive.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
 

/**
 * Http SSL 请求工具类
 * @author Arlight
 *
 */

public class HttpsSSLClient {
 
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
    
    public static String doGet(String url, String charSet) throws Exception {
        // When an instance CloseableHttpClient is no longer needed and is about to go out
        // of scope the connection manager associated with it must be shut down by calling the
        // CloseableHttpClient.close() method.
        if (StringUtils.isBlank(url)) {
            throw new Exception("The request url is blank.");
        }
        CloseableHttpClient httpClient = null;
        if (StringUtils.startsWith(url, "https")) {
            httpClient = HttpsSSLClient.createSSLInsecureClient();
        } else {
            httpClient = HttpClients.createDefault();
        }
        HttpGet getMethod = new HttpGet(url);
        return doGet(url, charSet, httpClient, getMethod);
    }
    
	public static String doPost(String url, String charSet, Header header, List<NameValuePair> urlParameters) throws Exception {
        // When an instance CloseableHttpClient is no longer needed and is about to go out
        // of scope the connection manager associated with it must be shut down by calling the
        // CloseableHttpClient.close() method.
        if (StringUtils.isBlank(url)) {
            throw new Exception("The request url is blank.");
        }
        CloseableHttpClient httpClient = null;
        if (StringUtils.startsWith(url, "https")) {
            httpClient = HttpsSSLClient.createSSLInsecureClient();
        } else {
            httpClient = HttpClients.createDefault();
        }
        HttpPost postMethod = new HttpPost(url);
        if (null!=header) {
            postMethod.setHeader(header);
        }
        if (null!=urlParameters) {
        	postMethod.setEntity(new UrlEncodedFormEntity(urlParameters));
        }
        return doPost(charSet, httpClient, postMethod);
    }
 
 
    private static String doGet(String url, String charSet, CloseableHttpClient httpClient, HttpGet getMethod)
            throws Exception {
        charSet = StringUtils.isBlank(charSet) ? "UTF-8" : charSet;
        CloseableHttpResponse response = null;
        HttpEntity responseEntity = null;
        try {
            response = httpClient.execute(getMethod);
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
    
    private static String doPost(String charSet, CloseableHttpClient httpClient, HttpPost postMethod)
            throws Exception {
        charSet = StringUtils.isBlank(charSet) ? "UTF-8" : charSet;
        CloseableHttpResponse response = null;
        HttpEntity responseEntity = null;
        try {
            response = httpClient.execute(postMethod);
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
    
    private static final String USER_NAME     = "347124781@qq.com";
	private static final String PASSWORD      = "maserati2016test";
	private static final String LOGIN_URL     = "https://test.salesforce.com";
	private static final String GRANT_TYPE    = "password";
	private static final String GRANT_SERVICE = "/services/oauth2/token";
	private static final String CLIENT_ID     = "3MVG9Se4BnchkASntWHG6BwWPdaiS3s0sbFkw08j6JoWS69QZYMeUivpx3ILhOuExgxyR1kRsqcq9FtnQBCEy";//上图中Consumer Key
	private static final String CLIENT_SECRET = "4077378209258026352";
    
	/**
     * 测试方法
     * @param args
     */
	public static void main(String[] args) throws Exception {
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("grant_type", GRANT_TYPE));
		urlParameters.add(new BasicNameValuePair("client_id", CLIENT_ID));
		urlParameters.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
		urlParameters.add(new BasicNameValuePair("username", USER_NAME));
		urlParameters.add(new BasicNameValuePair("password", PASSWORD));
	
		String sttt = HttpsSSLClient.doPost(LOGIN_URL+GRANT_SERVICE, "UTF-8", null, urlParameters);
		System.out.println(sttt);
				
    }
 
}