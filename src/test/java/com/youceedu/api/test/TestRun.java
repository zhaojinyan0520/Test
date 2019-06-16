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
	 * ��ʼ��
	 */
   private String fileName = null;

   @Parameters({"fileName"})
   @BeforeTest
   public void beforeTest(String fileName) {
 	  this.fileName = fileName;
   }
  
  @Test(dataProvider = "testCases")
  public void f(String id,String Test_is_exec,String TestCase,String Req_type,String Req_host,String Req_interface ,String Req_data ,String Result_expected,String Is_Dep,String Dep_key) {
	//��ʼ��
	  String actResult = "";
	  String reqUrl = Req_host + Req_interface;
	//��ӡ��־
	  Reporter.log("����ӿ�:"+reqUrl);
	  Reporter.log("�������:"+Req_data);
	  Reporter.log("�ӿ�Ԥ��ֵ:"+Result_expected);
	  if("YES".equals(Test_is_exec)){
		  if("GET".equals(Req_type)){
			  //����get����
			  actResult = HttpReqUtil.sendget(reqUrl, Req_data);
		  }else{
			  //����post����
			  actResult = HttpReqUtil.sendpost(reqUrl, Req_data);
		  }
	  }else{
		  System.out.println("�˽ӿڲ�ִ��");
	  }
	  
	  //��ӡ��־
	  Reporter.log("�ӿ�ʵ�ʷ���ֵ:"+actResult);
	  
	  //Ԥ��ֵ��ʵ��ֵ���Ա�
	  //��jsonpath����ʵ��ֵactResult����Ԥ��ֵResult_expected���жԱ�
	  //2.Result_expected=$.stutas=1;$.data.id=1050123;
	  //3.˼·��
	  //3.1�ȴ�expResult�õ�$.status---->JSONPath.eval(actResultJSON,$.status)=�õ�ʵ�ʷ������ķ���ֵ
	  //3.2�ٴ�expResult�õ�$.status��Ӧ��ֵ1��A���жԱ�
	  
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
