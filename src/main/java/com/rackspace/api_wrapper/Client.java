package com.rackspace.api_wrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * Client for the Rackspace API Wrapper
 * @author Chris
 *
 */
public class Client {
	protected final String BASE_URL = "https://api.emailsrvr.com/v0";
	protected String apiKey, secretKey;
	protected HttpClient client;
	protected HttpRequestBase request;
	
	/**
	 * Class constructor
	 * @param apiKey
	 * @param secretKey
	 */
	public Client(String apiKey, String secretKey) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		client = HttpClientBuilder.create().build();
	}
	
	/**
	 * Builds a GET request and returns the response
	 * @param 	url							url of request
	 * @param 	format						format of response
	 * @return	response from the sent request
	 * @throws 	ClientProtocolException
	 * @throws 	IOException
	 */
	public HttpResponse get(String url, String format) throws ClientProtocolException, IOException {
		request = new HttpGet(BASE_URL + url);
		signMessage();
		assignFormat(format);
		return client.execute(request);
	}
	
	/**
	 * Builds a POST request and returns the response
	 * @param 	url							url of request
	 * @param	data						data to be sent with request
	 * @param 	format						format of response
	 * @return	response from the sent request
	 * @throws 	ClientProtocolException
	 * @throws 	IOException
	 */
	public HttpResponse post(String url, HashMap<String, String> data, String format) throws ClientProtocolException, IOException {
		request = new HttpPost(BASE_URL + url);
		signMessage();
		assignFormat(format);
		request = formData((HttpPost)request, data);
		return client.execute(request);
	}
	
	/**
	 * Builds a PUT request and returns the response
	 * @param 	url							url of request
	 * @param	data						data to be sent with request
	 * @param 	format						format of response
	 * @return	response from the sent request
	 * @throws 	ClientProtocolException
	 * @throws 	IOException
	 */
	public HttpResponse put(String url, HashMap<String, String> data, String format) throws ClientProtocolException, IOException {
		request = new HttpPut(BASE_URL + url);
		signMessage();
		assignFormat(format);
		request = formData((HttpPut)request, data);
		return client.execute(request);
	}
	
	/**
	 * Builds a DELETE request and returns the response
	 * @param 	url							url of request
	 * @param	data						data to be sent with request
	 * @return	response from the sent request
	 * @throws 	ClientProtocolException
	 * @throws 	IOException
	 */
	public HttpResponse delete(String url, String format) throws ClientProtocolException, IOException {
		request = new HttpGet(BASE_URL + url);
		signMessage();
		assignFormat(format);
		return client.execute(request);
	}
	
	private HttpEntityEnclosingRequestBase formData(HttpEntityEnclosingRequestBase request, HashMap<String, String> data) 
		throws UnsupportedEncodingException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : data.keySet()) {
			params.add(new BasicNameValuePair(key, data.get(key)));
		}
		request.setEntity(new UrlEncodedFormEntity(params));
		return request;
	}
	
	private void assignFormat(String format) {
		request.setHeader("Accept", format);
	}
	
	/**
	 * Adds the X-Api-Signature to the request
	 */
	private void signMessage() {
		String ua = "Rackspace Management Interface";
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		// X-Api-Signature is a sha1 hash of concatenation the user key, user-agent, timestamp, and the secret key.  It is in hexadecimal.
		// It takes the sha1 in hex and converts to base64, which is what is needed.  Otherwise, will receive 403
		BigInteger bigint = new BigInteger(DigestUtils.sha1Hex(this.apiKey + ua + timestamp + this.secretKey), 16);
	    StringBuilder sb = new StringBuilder();
	    byte[] ba = Base64.encodeInteger(bigint);
	    for (byte b : ba) {
	        sb.append((char)b);
	    }
	    String sha1 = sb.toString();
		request.setHeader("X-Api-Signature", this.apiKey + ":" + timestamp + ":" + sha1);
	}
	
	/**
	 * Converts HttpResponse to String
	 * @param	response
	 * @return	Converted String
	 * @throws 	IOException
	 */
	public static String responseToString(HttpResponse response) throws IOException {
		BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}
}

