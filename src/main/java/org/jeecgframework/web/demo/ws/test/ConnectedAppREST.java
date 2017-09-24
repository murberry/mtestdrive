package org.jeecgframework.web.demo.ws.test;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


@WebServlet(urlPatterns = { "/ConnectedAppREST" })
/**
 * Demo for Connect App/REST 
 * @author seetha
 *
 */
public class ConnectedAppREST extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String INSTANCE_URL = "INSTANCE_URL";

	private void showAccounts(String instanceUrl, String accessToken,
			PrintWriter writer) throws ServletException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();  

		HttpGet httpGet = new HttpGet();

		//add key and value
		httpGet.addHeader("Authorization", "OAuth " + accessToken);


		try {

			URIBuilder builder = new URIBuilder(instanceUrl+ "/services/data/v30.0/query");
			builder.setParameter("q", "SELECT Name, Id from Account LIMIT 100");

			httpGet.setURI(builder.build());

			CloseableHttpResponse closeableresponse = httpclient.execute(httpGet);  
			System.out.println("Response Status line :" + closeableresponse.getStatusLine());  


			if (closeableresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// Now lets use the standard java json classes to work with the
				// results
				try {

					// Do the needful with entity.  
					HttpEntity entity = closeableresponse.getEntity(); 
					InputStream rstream = entity.getContent();
					JSONObject authResponse = new JSONObject(
							new JSONTokener(rstream));



					System.out.println("Query response: "
							+ authResponse.toString(2));

					writer.write(authResponse.getInt("totalSize")
							+ " record(s) returned\n\n");

					JSONArray results = authResponse.getJSONArray("records");

					for (int i = 0; i < results.length(); i++) {
						writer.write(results.getJSONObject(i).getString("Id")
								+ ", "
								+ results.getJSONObject(i).getString("Name")
								+ "\n");
					}
					writer.write("\n");
				} catch (JSONException e) {
					e.printStackTrace();
					throw new ServletException(e);
				}
			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			httpclient.close();
		}
	}

	private String createAccount(String name, String instanceUrl,
			String accessToken, PrintWriter writer) throws ServletException,
			IOException {
		String accountId = null;

		CloseableHttpClient httpclient = HttpClients.createDefault();  

		JSONObject account = new JSONObject();

		try {
			account.put("Name", name);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}

		HttpPost httpost = new HttpPost(instanceUrl+ "/services/data/v30.0/sobjects/Account/");  

		httpost.addHeader("Authorization", "OAuth " + accessToken);


		StringEntity messageEntity = new StringEntity( account.toString(),
				ContentType.create("application/json"));

		httpost.setEntity(messageEntity);

		// Execute the request.  
		CloseableHttpResponse closeableresponse = httpclient.execute(httpost);  
		System.out.println("Response Status line :" + closeableresponse.getStatusLine());  

		try {

			writer.write("HTTP status " + closeableresponse.getStatusLine().getStatusCode() 
					+ " creating account\n\n");

			if (closeableresponse.getStatusLine().getStatusCode()  == HttpStatus.SC_CREATED) {
				try {

					// Do the needful with entity.  
					HttpEntity entity = closeableresponse.getEntity(); 
					InputStream rstream = entity.getContent();
					JSONObject authResponse = new JSONObject(
							new JSONTokener(rstream));


					System.out.println("Create response: "
							+ authResponse.toString(2));

					if (authResponse.getBoolean("success")) {
						accountId = authResponse.getString("id");
						writer.write("New record id " + accountId + "\n\n");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					// throw new ServletException(e);
				}
			}
		} finally {
			httpclient.close();
		}

		return accountId;
	}

	private void showAccount(String accountId, String instanceUrl,
			String accessToken, PrintWriter writer) throws ServletException,
			IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault(); 

		HttpGet httpGet = new HttpGet();

		//add key and value
		httpGet.addHeader("Authorization", "OAuth " + accessToken);



		try {
			URIBuilder builder = new URIBuilder(instanceUrl
					+ "/services/data/v30.0/sobjects/Account/" + accountId);

			httpGet.setURI(builder.build());

			//httpclient.execute(httpGet);

			CloseableHttpResponse closeableresponse = httpclient.execute(httpGet);  
			System.out.println("Response Status line :" + closeableresponse.getStatusLine());  


			if (closeableresponse.getStatusLine().getStatusCode()  == HttpStatus.SC_OK) {
				try {

					// Do the needful with entity.  
					HttpEntity entity = closeableresponse.getEntity(); 
					InputStream rstream = entity.getContent();
					JSONObject authResponse = new JSONObject(
							new JSONTokener(rstream));

					System.out.println("Query response: "
							+ authResponse.toString(2));

					writer.write("Account content\n\n");

					Iterator iterator = authResponse.keys();
					while (iterator.hasNext()) {
						String key = (String) iterator.next();

						Object obj = authResponse.get(key);
						String value = null;
						if (obj instanceof String) {
							value = (String) obj;
						}

						writer.write(key + ":" + (value != null ? value : "")
								+ "\n");
					}

					writer.write("\n");
				} catch (JSONException e) {
					e.printStackTrace();
					throw new ServletException(e);
				}
			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			httpclient.close();
		}
	}

	private void updateAccount(String accountId, String newName, String city,
			String instanceUrl, String accessToken, PrintWriter writer)
					throws ServletException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();  

		JSONObject update = new JSONObject();

		try {
			update.put("Name", newName);
			update.put("BillingCity", city);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}

		HttpPost httpost = new HttpPost(instanceUrl
				+ "/services/data/v30.0/sobjects/Account/" +accountId+"?_HttpMethod=PATCH");  


		httpost.addHeader("Authorization", "OAuth " + accessToken);


		StringEntity messageEntity = new StringEntity( update.toString(),ContentType.create("application/json"));

		httpost.setEntity(messageEntity);



		// Execute the request.  
		CloseableHttpResponse closeableresponse = httpclient.execute(httpost);  
		System.out.println("Response Status line :" + closeableresponse.getStatusLine());  


		try {
			writer.write("HTTP status " + closeableresponse.getStatusLine().getStatusCode()
					+ " updating account " + accountId + "\n\n");
		} finally {
			httpclient.close();

		}
	}

	private void deleteAccount(String accountId, String instanceUrl,
			String accessToken, PrintWriter writer) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();  

		HttpDelete delete = new HttpDelete(instanceUrl
				+ "/services/data/v30.0/sobjects/Account/" + accountId);

		delete.setHeader("Authorization", "OAuth " + accessToken);

		// Execute the request.  
		CloseableHttpResponse closeableresponse = httpclient.execute(delete);  
		System.out.println("Response Status line :" + closeableresponse.getStatusLine());  

		try {
			writer.write("HTTP status " + closeableresponse.getStatusLine().getStatusCode()
					+ " deleting account " + accountId + "\n\n");
		} finally {
			//delete.releaseConnection();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();

		String accessToken = (String) request.getSession().getAttribute(
				ACCESS_TOKEN);

		String instanceUrl = (String) request.getSession().getAttribute(
				INSTANCE_URL);

		if (accessToken == null) {
			writer.write("Error - no access token");
			return;
		}

		writer.write("We have an access token: " + accessToken + "\n"
				+ "Using instance " + instanceUrl + "\n\n");

		showAccounts(instanceUrl, accessToken, writer);

		String accountId = createAccount("My New Org", instanceUrl, accessToken, writer);

		if (accountId == null) {
			System.out.println("Account ID null");
		}

		showAccount(accountId, instanceUrl, accessToken, writer);
		showAccounts(instanceUrl, accessToken, writer);

		updateAccount(accountId, "My New Org, Inc", "San Francisco", instanceUrl, accessToken, writer);

		showAccount(accountId, instanceUrl, accessToken, writer);

		deleteAccount(accountId, instanceUrl, accessToken, writer);

		showAccounts(instanceUrl, accessToken, writer);
	}
}