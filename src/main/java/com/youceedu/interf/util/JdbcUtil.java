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
	 * @Description: 基于jdbc进行增删改
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
			ps = conn.prepareStatement(sql);//初始化第一次需要预编译
			ps.setInt(1, autoLog.getId());//进行存储
//			ps.setString(2, autoLog.getTestCase());
			rowcount = ps.executeUpdate();//进行增删改
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
	 * 基于jdbc进行查询
	 * @Title: handler   
	 * @Description: TODO
	 * @param: @param rs
	 * @param: @param className
	 * @param: @return      
	 * @return: List<Object>      
	 * @throws
	 */
	public static List<Object> jdbcQuery(String sql,AutoLog autoLog,Class<?> className){
		//初始化变量
		List<Object> list = new ArrayList<Object>();
		
		//初始化Driver,将驱动加载到drivermanager类
		try {
			PreparedStatement  ps = conn.prepareStatement(sql);//初始化第一次需要进行预编译
			ps.setInt(1, autoLog.getId());//设置值进行参数化
			ResultSet rs = ps.executeQuery();//查询获得行
			list = handler(rs, className);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//得到驱动类
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
	
	//得到实体类进行映射
	public static List<Object> handler(ResultSet rs,Class<?> className){
		
		List<Object> list = new ArrayList<Object>();
		Object object=null;
		Object columnValues = null;
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int cloumnCount = rsmd.getColumnCount();
			
			while (rs.next()) {
				object = className.newInstance();//实例化，class作为入参的时候，需要使用newinstance
				for (int i = 1; i < cloumnCount; i++) {
					String columnName = rsmd.getColumnName(i);//根据下标值得到列的名称
					columnValues = rs.getObject(columnName);//根据列的名称得到列的值
					
					//得到实体类进行映射，得到实体类的字段及对字段进行赋值
					Field field = AutoLog.class.getDeclaredField(columnName);//根据传递的内容在实体类中进行查找，得到实体类中的字段
					field.setAccessible(true);//因为实体类中字段的属性都是私有的，这里设置为允许访问
					field.set(AutoLog.class, columnValues);//设置是一类中的值
					
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
			
		//单个进行增删改
		String updatasql="updata autolog set testcase=ceshi where id = ?";
		AutoLog autoLog = new AutoLog();
		autoLog.setId(1);
		int count = jdbcUpdata(updatasql, autoLog);
		System.out.println(count);
	
		
		
		//批量进行增删改
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
		
		//单个进行增删改
				String updatasql="UPDATE autolog SET testCase='ceshi' WHERE id=?";
				AutoLog autoLog = new AutoLog();
				autoLog.setId(1);
				int count = jdbcUpdata(updatasql, autoLog);
				System.out.println(count);
				*/	
	}

}
