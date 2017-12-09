package com.realestate.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.domain.UserInfo;
import com.realestate.domain.User;
import com.realestate.repository.UserInfoRepository;
import com.realestate.repository.StatusRepository;
import com.realestate.repository.UserRepository;
import com.realestate.util.EnumConstants;
import com.realestate.util.PasswordEncryption;
import com.realestate.util.UIDGenerator;



@RestController
@RequestMapping("/api/builder")
public class BuilderController {

	@Autowired
	private UserInfoRepository builderRepo;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private StatusRepository statusRepo;
	
		
	@PostMapping(value="/update/{firmname}")
	public ResponseEntity updateBuilder(@PathVariable("firmname") String firmname , @RequestBody UserInfo builder)
	{
		UserInfo user=builderRepo.save(builder);
		if(user==null)
			return new ResponseEntity(EnumConstants.FAILURE_INFO.toString(), HttpStatus.OK);
		
		return new ResponseEntity(user, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/delete/{firmname}")
	public ResponseEntity deleteBuilder(@PathVariable("firmname") String firmname)
	{
		builderRepo.delete(firmname);
	UserInfo b=builderRepo.findOne(firmname);
	if(b!=null)
		return new ResponseEntity(EnumConstants.FAILURE_INFO.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
	
		return new ResponseEntity(EnumConstants.SUUCESS_INFO.toString(), HttpStatus.OK);
	
			
	}
	
	
	@SuppressWarnings("unchecked")
	@PostMapping(value="/changepwd")
	public ResponseEntity changePassword(@RequestBody String jsonString)
	{
		
		if(jsonString==null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);
		
		String jsonKeys[]={"user_name" , "oldpwd" ,"newpwd"};
		
				
		Map<String,String> map = null;//new HashMap<String,String>();

		ObjectMapper mapper = new ObjectMapper();
try {
	map =mapper.readValue(jsonString, HashMap.class);
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

		User user=userRepo.findByUsername(map.get(jsonKeys[0]));
		
		if(user==null)
			return new ResponseEntity(EnumConstants.INVALID_USER.toString(), HttpStatus.NOT_FOUND);
		
		if(user.getPassword()!=null && PasswordEncryption.checkPwd(map.get(jsonKeys[1]), user.getPassword()))
		{
		
			user.setPassword(PasswordEncryption.encryptPwd(map.get(jsonKeys[2])));
			user=userRepo.save(user);
				if(user==null)
					return new ResponseEntity(EnumConstants.PASSWORD_CHANGE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else
			return new ResponseEntity(EnumConstants.PASSWORD_INCORRECT.toString(), HttpStatus.UNAUTHORIZED);
			
		
		return new ResponseEntity(EnumConstants.PASSWORD_CHANGE_SUCCESS, HttpStatus.OK);
		
	}

	
/*	@RequestMapping(value="/**")
	public ResponseEntity NoUrl() {
		
		return new ResponseEntity(EnumConstants.URI_NOT_FOUND.toString(), HttpStatus.NOT_FOUND);
	}
	*/
	
}

