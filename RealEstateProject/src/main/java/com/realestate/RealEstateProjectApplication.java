package com.realestate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RealEstateProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateProjectApplication.class, args);
	}
}

//If does not connect to mysql then i got errors like :
//com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure
//org.springframework.jdbc.support.MetaDataAccessException: Could not get Connection for extracting meta data; nested exception is org.springframework.jdbc.CannotGetJdbcConnectionException: Could not get JDBC Connection; nested exception is com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure