package com.realestate.util;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonsUtil {

	private static final ObjectMapper om=new ObjectMapper();
	private static final SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

	private CommonsUtil(){}
	
	public static ObjectMapper getObjectMapperObject()
	{
		return om;
	}
	
	public static SimpleDateFormat getSimpleDateFormatObject()
	{
		return sd;
	}
	
}
