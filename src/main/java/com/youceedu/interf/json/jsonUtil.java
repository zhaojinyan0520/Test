package com.youceedu.interf.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

public class jsonUtil {

	public static void main(String[] args) {
		String str = "";
		JSONObject json = JSON.parseObject(str);
		System.out.println(JSONPath.eval(json, "$.store.book"));

	}

}
