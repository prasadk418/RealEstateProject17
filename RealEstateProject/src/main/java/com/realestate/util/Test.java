package com.realestate.util;

public class Test {

	public static void main(String[] args) {
		
		String h=PasswordEncryption.encryptPwd("admin");
		System.out.println(h); //$2a$12$tMCEMBsRK2l6hiMw0k76ZOJ4iPz0J4bzXjFjbZGJdENFDIONEGTYK
		boolean b=PasswordEncryption.checkPwd("admin", h);
		System.out.println(b);
	}
}
