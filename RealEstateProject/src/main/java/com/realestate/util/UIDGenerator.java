package com.realestate.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.realestate.repository.UserInfoRepository;
import com.realestate.repository.InvoiceRepository;
import com.realestate.repository.OrderRepository;

public class UIDGenerator {


	public static String uniqueIdGenerator(Long num, String type)
	{
		String zero="000000000";
		String stringVal=Long.toString(num);
		int x=stringVal.length();
		int cnt=10-x;
		String uid=zero.substring(0, cnt)+stringVal;
		
		switch (type) {
		case "User":
			uid="USR"+uid;
			break;
		case "Order":
			uid="ORD"+uid;
			break;
		case "Invoice":
			uid="INC"+uid;
			break;		
		}
		
		System.out.println(uid);
		return uid;
	}
	/*public static void main(String[] args) {
		UIDGenerator u=new UIDGenerator();
		u.uniqueIdGenerator(1, "User");
		u.uniqueIdGenerator(12 ,"Order");
		u.uniqueIdGenerator(123,"Invoice");
		
				
	}*/
	
	
}
