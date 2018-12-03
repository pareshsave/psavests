package com.sterlingTS.utils.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.JSONException;


public class configProperty {
	public static Map<String, Map> allProperties = new HashMap<String, Map>();
	
	public static void populatePropertiesMap() throws FileNotFoundException, IOException{
		try{
		JSONParser parse = new JSONParser();
		Object obj = null;
		try {
			//obj = parse.parse(new FileReader(configProperty.class.getClassLoader().getResource("GlobalConfig.json").getFile()));
			obj = parse.parse(new FileReader(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"Resources"+File.separator+"config"+File.separator+"GlobalConfig.json"));
			System.out.println("OBJ"+obj);
		} catch (org.json.simple.parser.ParseException e) {
			// TODO: handle exception
		}
		org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
		
		JSONArray envArr = null;
		envArr = (JSONArray) jsonObject.get("ENV");
		fillComponentProperties(envArr,allProperties);
		
		Iterator itr = allProperties.keySet().iterator();
		while(itr.hasNext())
		{
			Object obj1 = itr.next();
		}
		}catch(FileNotFoundException e1)
		{
			e1.printStackTrace();
		}catch(IOException e2)
		{
			e2.printStackTrace();
		}
	}
	
	public static ArrayList getKeksFromJsonObject(JSONObject jsonObj){
		ArrayList keys = new ArrayList();
		Iterator itrList = jsonObj.keys();
		while(itrList.hasNext())
		{
			keys.add(itrList.next());
		}
		
		return keys;
	}
	
	public static Map getKeysAndValuesFromJsonObject(JSONObject jsonObj)
	{
		Map keyandValuesMap = new HashMap();
		ArrayList Keys = getKeksFromJsonObject(jsonObj);
		for(int i=0;i<Keys.size();i++)
		{
			try{
			keyandValuesMap.put(Keys.get(i), jsonObj.get(Keys.get(i).toString()));
			}catch(JSONException e)
			{
				e.printStackTrace();
			}
		}
		
		return keyandValuesMap;
	}
	
	public static void fillComponentProperties(JSONArray msg,Map allProperties)
	{
		JSONObject singleObject = null;
		try {
			singleObject = new JSONObject(msg.get(0).toString());
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Map propMapKeys = getKeysAndValuesFromJsonObject(singleObject);
		Iterator itr = propMapKeys.keySet().iterator();
		while(itr.hasNext())
		{
			Map indvlPropKeys = new HashMap();
			Object obj1 = itr.next();
			JSONObject propSpecific = (JSONObject) propMapKeys.get(obj1);
			indvlPropKeys = getKeysAndValuesFromJsonObject(propSpecific);
			Iterator itr1 = indvlPropKeys.keySet().iterator();
			Map specificProps = new HashMap();
			while(itr1.hasNext())
			{
				Object obj2 = itr1.next();
				Object indvPropObjectSpecific = indvlPropKeys.get(obj2);
				specificProps.put(obj2, indvPropObjectSpecific);
			}
			allProperties.put(obj1.toString(), specificProps);
		}
	}
	public static Map getPropertiesMap(String propertyId)
	{
		return allProperties.get(propertyId);
	}
	
	static{
		try {
			populatePropertiesMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
