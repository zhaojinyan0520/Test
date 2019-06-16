package com.youceedu.interf.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.youcedeu.interf.model.AutoLog;
public class JdbcUtil {
	
	private static String url="jdbc:mysql://192.168.8.110:3306/interface?characterEncoding=UTF-8";
	private static String user="root";
	private static String password="123456";
	private static Connection conn = null;
	
	 
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * @Title: jdbcUpdata   
	 * @Description: ����jdbc������ɾ��
	 * @param: @param sql
	 * @param: @param autoLog
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	public static int jdbcUpdata(String sql,AutoLog autoLog){
		PreparedStatement ps = null;
		int rowcount = 0;
		
		try {
			ps = conn.prepareStatement(sql);//��ʼ����һ����ҪԤ����
			ps.setInt(1, autoLog.getId());//���д洢
//			ps.setString(2, autoLog.getTestCase());
			rowcount = ps.executeUpdate();//������ɾ��
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				close(null, ps, conn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return rowcount;
		
	}
	public static int[] dbcpupdata(List<AutoLog> list,String sql){
		PreparedStatement ps = null;
		int[] rowcount = null;
		try {
			
			ps = conn.prepareStatement(sql);
			for (AutoLog autoLog : list) {
				ps.setString(1, autoLog.getTestCase());
				ps.setString(2, autoLog.getReqType());
				ps.setString(3, autoLog.getReqUrl());
				ps.setString(4, autoLog.getReqData());
				ps.setString(5, autoLog.getExpResult());
				ps.setString(6, autoLog.getActResult());
				ps.setInt(7, autoLog.getResult());
				ps.setString(8, autoLog.getExecTime());
				ps.addBatch();
			}
			rowcount = ps.executeBatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rowcount;
		
	}	
	/**
	 * ����jdbc���в�ѯ
	 * @Title: handler   
	 * @Description: TODO
	 * @param: @param rs
	 * @param: @param className
	 * @param: @return      
	 * @return: List<Object>      
	 * @throws
	 */
	public static List<Object> jdbcQuery(String sql,AutoLog autoLog,Class<?> className){
		//��ʼ������
		List<Object> list = new ArrayList<Object>();
		
		//��ʼ��Driver,���������ص�drivermanager��
		try {
			PreparedStatement  ps = conn.prepareStatement(sql);//��ʼ����һ����Ҫ����Ԥ����
			ps.setInt(1, autoLog.getId());//����ֵ���в�����
			ResultSet rs = ps.executeQuery();//��ѯ�����
			list = handler(rs, className);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//�õ�������
		return list;
	}
	public static void close(ResultSet rs,PreparedStatement ps,Connection conn) throws Exception{
		if (rs!=null) {
			rs.close();
		}
		if (ps!=null) {
			ps.close();
		}
		if (conn!=null) {
			conn.close();
		}
	}
	
	//�õ�ʵ�������ӳ��
	public static List<Object> handler(ResultSet rs,Class<?> className){
		
		List<Object> list = new ArrayList<Object>();
		Object object=null;
		Object columnValues = null;
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int cloumnCount = rsmd.getColumnCount();
			
			while (rs.next()) {
				object = className.newInstance();//ʵ������class��Ϊ��ε�ʱ����Ҫʹ��newinstance
				for (int i = 1; i < cloumnCount; i++) {
					String columnName = rsmd.getColumnName(i);//�����±�ֵ�õ��е�����
					columnValues = rs.getObject(columnName);//�����е����Ƶõ��е�ֵ
					
					//�õ�ʵ�������ӳ�䣬�õ�ʵ������ֶμ����ֶν��и�ֵ
					Field field = AutoLog.class.getDeclaredField(columnName);//���ݴ��ݵ�������ʵ�����н��в��ң��õ�ʵ�����е��ֶ�
					field.setAccessible(true);//��Ϊʵ�������ֶε����Զ���˽�еģ���������Ϊ�������
					field.set(AutoLog.class, columnValues);//������һ���е�ֵ
					
				}
				
				list.add(object);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
		
	}
	public static void main(String[] args) {
		/**
			String sql="select * from autolog where id = ?";
			AutoLog autoLog = new AutoLog();
			autoLog.setId(1);
			List<Object> list = jdbcQuery(sql, autoLog, AutoLog.class);
			for (Object object : list) {
				AutoLog autoLog2=(AutoLog)object;
				System.out.println(autoLog2.getId());
				
			}
			
		//����������ɾ��
		String updatasql="updata autolog set testcase=ceshi where id = ?";
		AutoLog autoLog = new AutoLog();
		autoLog.setId(1);
		int count = jdbcUpdata(updatasql, autoLog);
		System.out.println(count);
	
		
		
		//����������ɾ��
		String insertsql="insert into autolog(testCase,reqType,reqUrl,reqData,expResult,actResult,result,execTime) values (?,?,?,?,?,?,?,?)";
		List<AutoLog> list = new ArrayList<AutoLog>();
		AutoLog autolog = new AutoLog();
		autolog.setTestCase("xx");
		autolog.setReqType("POST");
		autolog.setReqUrl("http://www.nuandao.com/public/lazyentrance");
		autolog.setReqData("isajax=1&remember=1&email=asfasfasfasdf@qq.com&password=3333333&agreeterms=1&itype=&book=1&m=0.12248663395993764");
		autolog.setExpResult("$.status=1");
		autolog.setActResult("$.status=1");
		autolog.setResult(0);
		autolog.setExecTime("2019-05-19 18:35:55");
		list.add(autolog);
		
		
		AutoLog autolog2 = new AutoLog();
		autolog2.setTestCase("cc");
		autolog2.setReqType("POST");
		autolog2.setReqUrl("http://www.nuandao.com/public/lazyentrance");
		autolog2.setReqData("isajax=1&remember=1&email=asfasfasfasdf@qq.com&password=3333333&agreeterms=1&itype=&book=1&m=0.12248663395993764");
		autolog2.setExpResult("$.status=1");
		autolog2.setActResult("$.status=1");
		autolog2.setResult(0);
		autolog2.setExecTime("2019-05-19 18:35:22");
		list.add(autolog2);
		int[] batchResult = dbcpupdata(list, insertsql);
		for (int tmp : batchResult) {
			System.out.println(tmp);
		}
		
		//����������ɾ��
				String updatasql="UPDATE autolog SET testCase='ceshi' WHERE id=?";
				AutoLog autoLog = new AutoLog();
				autoLog.setId(1);
				int count = jdbcUpdata(updatasql, autoLog);
				System.out.println(count);
				*/	
	}

}
