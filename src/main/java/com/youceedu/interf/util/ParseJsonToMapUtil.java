package com.youceedu.interf.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ParseJsonToMapUtil {

	//ÿ���ӿڵķ���ֵ����תΪMap�洢
	private  Map<String, String> iResponJsonToMap = new HashMap<String, String>();
	
	//�洢�ӿ����ͽӿ�Map���
	private static Map<String,Map<String,String>> iNameAndIMap = new HashMap<String,Map<String,String>>();
	
	/**
	 * @Title: getINameAndIMap   
	 * @Description: �õ�iNameAndIMap,����洢�ӿ����ͽӿ�Map���
	 * @param: @return      
	 * @return: Map<String,Map<String,String>>      
	 * @throws
	 */
	public static Map<String,Map<String,String>> getINameAndIMap(){
		return iNameAndIMap;
	}
	
	/**
	 * @Title: setINameAndIMap   
	 * @Description: �洢�ӿ����ͽӿ�Map���
	 * @param: @param reqInterface
	 * @param: @param responseJson      
	 * @return: void      
	 * @throws
	 */
	public void setINameAndIMap(String reqInterface,String responseJson){
		iNameAndIMap.put(reqInterface, parseJsonToMap(responseJson));
	}
	
	
	
	/**
	 * @Title: isJsonString   
	 * @Description: �ж�����Ƿ�Ϊjson��ʽ���ַ�������
	 * @param: @param jsonString
	 * @param: @return      
	 * @return: boolean      
	 * @throws
	 */
	public  boolean isJsonString(String jsonString) {
        boolean flag = true;
        try {
            JSON.parseObject(jsonString);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
	
	/**
	 * @Title: isJsonArray   
	 * @Description: �ж�����Ƿ�ΪJSONArray����
	 * @param: @param jsonArrayString
	 * @param: @return      
	 * @return: boolean      
	 * @throws
	 */
    public  boolean isJsonArray(String jsonArrayString) {
        boolean flag = true;
        try {
            JSONArray.parseArray(jsonArrayString);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    
	/**
	 * @Title: isString
	 * @Description: ��JsonObject,��JsonArray����
	 * @param: @param str
	 * @param: @return      
	 * @return: boolean      
	 * @throws
	 */
    public  boolean isString(String str) {
        return !isJsonString(str) & !isJsonArray(str);
    }
    
    
	/**
	 * @Title: testParseJsonArray   
	 * @Description: ����JsonArray
	 * @param: @param jsonArrayString      
	 * @return: void      
	 * @throws
	 */
    public  void parseJsonArray(String jsonArrayString){
    	try{
    		JSONArray jsonArray = JSONArray.parseArray(jsonArrayString);
    		for(Object json:jsonArray){
    			String jsonItem = json.toString();
    			if(isJsonString(jsonItem)){
    				parseJsonToMap(jsonItem);
        		}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
    /**
     * @Title: parseJsonToMap   
     * @Description: ����String���͵�json����ÿ���ӿڵķ���ֵ����תΪMap
     * @param: @param responseJson
     * @param: @return      
     * @return: Map<String,String>      
     * @throws
     */
	public Map<String, String> parseJsonToMap(String responseJson){
		
		try{
			JSONObject json = JSON.parseObject(responseJson);
			for(Entry<String, Object> entry:json.entrySet()){
				
				//ע��-ֻ�ܵõ�json��1��key,value(value�п�����:������������,�༯,JSONObject,JSONArray)
				String key = entry.getKey();
				String value = String.valueOf(entry.getValue());

				//�ж�valueֵΪ""/null->������map�ѵ�ǰkey��ֵ��Ϊ��
				if(value.length()==0 || value.equals("null")){
					iResponJsonToMap.put(key, "null");
					continue;
				}

				//�ж�valueֵ�Ƿ�Ϊjson��
	            if (isJsonString(value)){
	            	iResponJsonToMap.put(key,value);
	            	parseJsonToMap(value);
	            }
				
	            //�ж�valueֵ�Ƿ�Ϊjson����
	            if (isJsonArray(value)) {
	            	iResponJsonToMap.put(key, value);
	                parseJsonArray(value);
	            }
	            
	            //�ж�valueֵ�Ƿ�Ϊ�ַ���
				if (isString(value)){
					if(iResponJsonToMap.containsKey(key)){
						iResponJsonToMap.put(key,iResponJsonToMap.get(key) +"," + value);
					}else{
						iResponJsonToMap.put(key, value);}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return iResponJsonToMap;
	}
	
	public static void main(String[] args) {

		//testcase1
//		ParseJsonToMapUtil parseJsonToMapUtil = new ParseJsonToMapUtil();
//		Map<String, String> tmp = parseJsonToMapUtil.parseJsonToMap("{\"status\":1,\"info\":\"\",\"data\":{\"id\":\"1048894\",\"lastlogin\":1527137945,\"logins\":[\"exp\",\"logins+1\"],\"lastip\":\"111.203.168.2\",\"checktype\":1}}");
//		System.out.println(" Map�ڵ�ǰ�� " + tmp);
//		System.out.println(" json parse value =  " + tmp.get("status"));
		
		//testcase2
//		ParseJsonToMapUtil parseJsonToMapUtil = new ParseJsonToMapUtil();
//		Map<String, String> tmp = parseJsonToMapUtil.parseJsonToMap("{\"a\":\"b\",\"c\":[{\"key1\":{\"key4\":{\"key5\":{\"key6\":[{\"key7\":[{\"key8\":0,\"key9\":[{\"name\":\"zhangsan\",\"score\":99,\"sex\":1},{\"name\":\"lisi\",\"score\":100},{\"name\":\"wangwu\",\"score\":30}]}]}]}}}},{\"key2\":\"value2\"},{\"key3\":\"value3\"}]}");
//		System.out.println(" Map�ڴ洢Ϊ�� " + tmp);
//		System.out.println(" json parse value =  " + tmp.get("key8"));
		
	}
}
