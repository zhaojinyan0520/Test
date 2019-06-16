package com.youceedu.interf.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpReqUtil {
	
	//设置header
	public static void reqconfig(HttpRequestBase httprequestbase,String param){
		httprequestbase.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
		if (new ParseJsonToMapUtil().isJsonArray(param)) {
			httprequestbase.setHeader("Content-Type","application/json;charset=utf-8");
		}else {
			httprequestbase.setHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		}
		
		//设置超时
		RequestConfig requestconfig = RequestConfig.custom()
		.setConnectionRequestTimeout(2000)
		.build();
		
		httprequestbase.setConfig(requestconfig);
	}
	
	//创建一个get方法
	public static String sendget(String url,String param){
		
		String requrl = url+"?"+param;
		CloseableHttpResponse response=null;
		CloseableHttpClient httpclient=null;
		String result = null;
		//得到httpclient，httpget对象
		httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(requrl);
		//添加header
		 reqconfig(httpget,param);
		//发送请求及得到服务器返回值
		try {
			response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity,"utf-8");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return result;
		
	}
	
	//创建一个post方法
	public static String sendpost(String url,String param){
		//初始化变量
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response=null;		
		String result=null;
		//得到httpclient，和httppost对象
		httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		//添加header
		 reqconfig(httppost,param);
		try {
			//设置消息体
			StringEntity Stringentity = new StringEntity(param);
			httppost.setEntity(Stringentity);
			
			response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				result  = EntityUtils.toString(entity,"utf-8");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
			
		
		return result;
		
	}
	
	public static void main(String[] args) throws Exception {
//		String url_g="https://deal-admin.kuick.cn/api/v1.7/app/1d226805-b259-4e16-a6fa-184a55ea3712/member/4808/customers";
//		String param="customer_group_id=all&start_index=0&count=20&access_token=1d1d78cd7986ab9ff269fe6466e30e1ea78c64244e5cc5fe9f589f518ab34f74e0b2b9fe05be9c57f5f40b614c09fc4a1f0a86ae5a6a2542b8e099b38c00741a&app_secret=bb1549b9-4981-4b38-a9a8-d4b95cb69427&https=1";
//		String tmp = sendget(url_g, param);
//		System.out.println(tmp);
		
		
//		String url_p="https://deal-admin.kuick.cn/api/v1.7/app/1d226805-b259-4e16-a6fa-184a55ea3712/file/18994/tags?access_token=1d1d78cd7986ab9ff269fe6466e30e1ea78c64244e5cc5fe9f589f518ab34f74e0b2b9fe05be9c57f5f40b614c09fc4a1f0a86ae5a6a2542b8e099b38c00741a&app_secret=bb1549b9-4981-4b38-a9a8-d4b95cb69427&https=1";
//		String param="tags=/9&kuick_user_id=4808";
//		String tmp2=sendpost(url_p, param);
//		System.out.println(tmp2);
	}

}
