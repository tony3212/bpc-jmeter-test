package com.onlyou.jmeter.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * Http客户端工具类
 * @author HQH
 *
 */
public class HttpClientUtil {
	
	private static Logger logger = Logger.getLogger(HttpClientUtil.class);
	
	/**
	 * POST提交
	 * @param uri 请求路径
	 * @param jsonString JSON字符串
	 * @return 返回JSON字符串
	 * @throws Exception
	 */
	public static String post(String uri, String jsonString) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(uri);
		StringEntity entity = new StringEntity(jsonString, "utf-8");
		entity.setContentEncoding("utf-8");
		entity.setContentType("application/json");  //发送json数据需要设置contentType
		post.setEntity(entity);
		HttpResponse res = httpClient.execute(post);
		String response = null;
		if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			response = EntityUtils.toString(res.getEntity()); // 返回json格式
			logger.info("HTTP请求返回："+response);
		}
		
		return response;
	}

}
