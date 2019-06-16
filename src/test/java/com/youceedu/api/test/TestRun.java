package com.youceedu.api.test;

import org.testng.annotations.Test;

import com.beust.jcommander.Parameter;
import com.youceedu.interf.util.ExcelUtil;
import com.youceedu.interf.util.HttpReqUtil;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeClass;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class TestRun {
	/**
	 * 初始化
	 */
   private String fileName = null;

   @Parameters({"fileName"})
   @BeforeTest
   public void beforeTest(String fileName) {
 	  this.fileName = fileName;
   }
  
  @Test(dataProvider = "testCases")
  public void f(String id,String Test_is_exec,String TestCase,String Req_type,String Req_host,String Req_interface ,String Req_data ,String Result_expected,String Is_Dep,String Dep_key) {
	//初始化
	  String actResult = "";
	  String reqUrl = Req_host + Req_interface;
	//打印日志
	  Reporter.log("请求接口:"+reqUrl);
	  Reporter.log("请求参数:"+Req_data);
	  Reporter.log("接口预期值:"+Result_expected);
	  if("YES".equals(Test_is_exec)){
		  if("GET".equals(Req_type)){
			  //发送get请求
			  actResult = HttpReqUtil.sendget(reqUrl, Req_data);
		  }else{
			  //发送post请求
			  actResult = HttpReqUtil.sendpost(reqUrl, Req_data);
		  }
	  }else{
		  System.out.println("此接口不执行");
	  }
	  
	  //打印日志
	  Reporter.log("接口实际返回值:"+actResult);
	  
	  //预期值和实际值做对比
	  //用jsonpath解析实际值actResult，和预期值Result_expected进行对比
	  //2.Result_expected=$.stutas=1;$.data.id=1050123;
	  //3.思路：
	  //3.1先从expResult得到$.status---->JSONPath.eval(actResultJSON,$.status)=得到实际服务器的返回值
	  //3.2再从expResult得到$.status对应的值1和A进行对比
	  
  }
 
  @DataProvider(name = "testCases")
  public Object[][] dp() {
	  Object[][] data = null;
	  try {
		  ExcelUtil excelUtil = new ExcelUtil(fileName);
		  data = excelUtil.getArrayCell(0);
	} catch (Exception e) {
		// TODO: handle exception
	}
    return data;
  }
  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {
  }



  @AfterTest
  public void afterTest() {
  }

  @BeforeSuite
  public void beforeSuite() {
  }

  @AfterSuite
  public void afterSuite() {
  }
  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
  }


}
