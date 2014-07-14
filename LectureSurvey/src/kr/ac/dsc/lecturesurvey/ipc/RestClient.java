package kr.ac.dsc.lecturesurvey.ipc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import android.util.Log;

public class RestClient {
	
	static boolean bProcessing = false;

	private String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}


	/* This is a test function which will connects to a given
	 * rest service and prints it's response to Android Log with
	 * labels "Praeda".
	 */
	public JsonElement Request(String url, ArrayList<NameValuePair> params)
	{

		//return Map
		JsonElement json = null;

		HttpClient httpclient = new DefaultHttpClient();
		HttpParams httpparams = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpparams, 5000);
		HttpConnectionParams.setSoTimeout(httpparams, 5000);		

		// Prepare a request object
		//HttpGet httpget = new HttpGet(url);  //get 
		HttpPost httppost = new HttpPost(url); //post
		UrlEncodedFormEntity ent;
		try {
			ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			httppost.setEntity(ent);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Execute the request
		HttpResponse response;

		//while(bProcessing == false) {
			try {
				response = httpclient.execute(httppost);
				// Examine the response status
				Log.i("IPC_From",response.getStatusLine().toString());

				// Get hold of the response entity
				HttpEntity entity = response.getEntity();
				// If the response does not enclose an entity, there is no need
				// to worry about connection release

				if (entity != null) {
					
					bProcessing = true;

					// A Simple JSON Response Read
					InputStream instream = entity.getContent();
					String result= convertStreamToString(instream);
					Log.i("IPC_Response",result);

					JsonParser parser = new JsonParser();
					
					try {
						json = (JsonElement) parser.parse(result);
					} catch (Exception e) {
						// TODO: handle exception
					}

					// Closing the input stream will trigger connection release
					instream.close();
				}


			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		//}
		
		bProcessing = false;
		
		return json;
	}
	
	public JsonElement RequestMultipart(String url, MultipartEntity params)
	{

		//return Map
		JsonElement json = null;

		//URLEncoder.encode("userid", "UTF-8");
		//new BasicNameValuePair("userid", URLEncoder.encode("userid", "UTF-8"));
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams httpparams = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpparams, 5000);
		HttpConnectionParams.setSoTimeout(httpparams, 5000);		

		// Prepare a request object
		//HttpGet httpget = new HttpGet(url);  //get 
		HttpPost httppost = new HttpPost(url); //post
	    httppost.setHeader("Connection", "Keep-Alive");
	    httppost.setHeader("Accept-Charset", "UTF-8");
	    httppost.setHeader("ENCTYPE", "multipart/form-data");
		httppost.setEntity(params);

		// Execute the request
		HttpResponse response;
		//while(bProcessing == false) {		
			try {
				response = httpclient.execute(httppost);
				// Examine the response status
				Log.i("IPC_From",response.getStatusLine().toString());

				// Get hold of the response entity
				HttpEntity entity = response.getEntity();
				// If the response does not enclose an entity, there is no need
				// to worry about connection release

				if (entity != null) {
					
					bProcessing = true;
					
					// A Simple JSON Response Read
					InputStream instream = entity.getContent();
					String result= convertStreamToString(instream);
					Log.i("IPC_From",result);

					JsonParser parser = new JsonParser();

					json = (JsonElement) parser.parse(result);

					// Closing the input stream will trigger connection release
					instream.close();
				}


			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		//}
		
		bProcessing = false;
		
		return json;
	}

}
