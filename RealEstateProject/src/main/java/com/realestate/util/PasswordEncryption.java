package com.realestate.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.realestate.domain.Material;
import com.realestate.domain.UOM;

public class PasswordEncryption {

	
	public static String encryptPwd(String pwd) {

        return BCrypt.hashpw(pwd, BCrypt.gensalt(12));
        }

	public static Boolean checkPwd(String pwd, String dbPwd)
	{
		return BCrypt.checkpw(pwd, dbPwd);
	}
	
	
	public static void main12(String[] args) {
		UOM u=new UOM();
		u.setId(1);
		u.setName("grade4");
		u.setSize(null);
		
		UOM u1=new UOM();
		u1.setId(2);
		u1.setName("grade2");
		u1.setSize(null);
		
		UOM u2=new UOM();
		u2.setId(3);
		u2.setName("tons");
		u2.setSize("10mm");
		
		UOM u3=new UOM();
		u2.setId(3);
		u2.setName("tons");
		u2.setSize("12mm");
		
		UOM u4=new UOM();
		u2.setId(3);
		u2.setName("Kg");
		u2.setSize("12mm");
		
		
		
		List<UOM> dbUOMList=new ArrayList<UOM>();
		dbUOMList.add(u);
		dbUOMList.add(u1);
		
		Material m=new Material();
		m.setId(1);
		m.setName("cement");
		m.setUom(dbUOMList);
		
		Material m1=new Material();
		List<UOM> dbUOMList1=new ArrayList<UOM>();
		dbUOMList1.add(u2);
		dbUOMList1.add(u3);
		dbUOMList1.add(u4);
		
		m.setId(2);
		m.setName("iron");
		m.setUom(dbUOMList1);
		
		//new list
		UOM u5=new UOM();
		u2.setId(4);
		u2.setName("bags");
		u2.setSize(null);
		Material m2=new Material();
		List<UOM> dbUOMList2=new ArrayList<UOM>();
		dbUOMList2.add(u5);
		
		m.setId(2);
		m.setName("iron");
		m.setUom(dbUOMList1);
		
		List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
		List<Integer> list2 = Arrays.asList(2, 3, 4, 6, 7);
		// Prepare a union
		List<Integer> union = new ArrayList<Integer>(list1);
		union.addAll(list2);
		System.out.println("union" + union);
		// Prepare an intersection
		List<Integer> intersection = new ArrayList<Integer>(list1);
		intersection.retainAll(list2);
		System.out.println("intersection" + intersection);
		// Subtract the intersection from the union
		union.removeAll(intersection);
		// Print the result
		union.retainAll(list1);
		for (Integer n : union) {
		    System.out.println(n);
		}
		
		try{
		PasswordEncryption p=new PasswordEncryption();
		String h=p.encryptPwd("dada");
		System.out.println(h);
		boolean bb=p.checkPwd("dada", null);
		System.out.println(bb);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
}
