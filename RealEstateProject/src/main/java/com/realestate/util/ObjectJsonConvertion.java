package com.realestate.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ObjectJsonConvertion {

	private static ObjectMapper omapper=ObjectJsonConvertion.getObjectMapperObject();
	  	
	public  static String getJson(Object obj)
	{
		String	jsonString="{}";
		
		try {
			//omapper.setSerializationInclusion(Include.NON_NULL);
			jsonString=omapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			System.out.println("Error occured at json to object convertion because of bad-user "+e.getMessage());
			e.printStackTrace();
		}
		return jsonString;
	}
	
	
	public static <T> T getObject(String json, Class<T> type) 
	{
		T t=null;
		try {
			
			t = omapper.readValue(json , type);
		} catch (Exception e) {
			
			System.out.println("Error occured at json to object convertion because of bad-user "+e.getMessage());
			e.printStackTrace();
		}
		return t;
	}
	
	
	public static ObjectMapper getObjectMapperObject()
	{
		
			if(omapper==null)
				omapper=new ObjectMapper();
					
		
		return omapper;
	}


}
