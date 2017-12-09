package com.realestate.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.domain.Material;
import com.realestate.domain.SecurityQuestions;
import com.realestate.domain.UOM;
import com.realestate.exceptions.CustomException;
import com.realestate.service.AdminService;
import com.realestate.util.EnumConstants;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@RequestMapping(value = "/")
	public String checkUser() {
		return "App is now running...!";
	}

	/*@SuppressWarnings("unchecked")
	@PostMapping(value = "/addUser")
	public ResponseEntity addAdmin(@RequestBody Admin jsonString) {
		
		if(jsonString==null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);
		Admin admin = adminRepo.save(jsonString);

		if (admin == null)
			return new ResponseEntity(EnumConstants.USER_NOT_CREATED.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity(EnumConstants.USER_CREATED.toString(), HttpStatus.OK);
	}*/

	/*@PostMapping(value = "/updateUser/{id}")
	public ResponseEntity updateUser(@PathParam("id") String id,
			@RequestBody Admin jsonString) {
		Admin admin = null;
		boolean check = adminRepo.exists(id);
		if (check)
			admin = adminRepo.save(jsonString);

		if (admin == null)
			return new ResponseEntity(false, HttpStatus.NOT_FOUND);
		return new ResponseEntity(true, HttpStatus.OK);
	}*/

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/add/sq")
	public ResponseEntity addSqecurityQuestions(@RequestBody SecurityQuestions sq) throws CustomException {
		
		if(sq==null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);
	
		SecurityQuestions scQ=adminService.addSqecurityQuestions(sq);
		
		
		if (scQ == null)
			return new ResponseEntity(EnumConstants.SECURITY_QUESTION_NOT_CREATED.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity(EnumConstants.SECURITY_QUESTION_CREATED.toString(), HttpStatus.OK);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/add/material")
	public ResponseEntity addMaterial(@RequestBody  Material material) throws CustomException {
		
		if(material==null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);
	
		Material mm=adminService.addMaterial(material);
		
		
		if (mm == null)
			return new ResponseEntity(EnumConstants.MATERIAL_NOT_CREATED.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity(EnumConstants.MATERIAL_CREATED.toString(), HttpStatus.OK);

	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/add/uom")
	public ResponseEntity addUom(@RequestBody  String jsonString) throws Exception {
		
		if(jsonString==null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);
	
		String jsonKeys[] = { "uom_name", "material_id" };

		Map<String, Object> map = null;

		ObjectMapper mapper = new ObjectMapper();
		
			map = mapper.readValue(jsonString, HashMap.class);
		
		Integer mid=(Integer)map.get("material_id");
		List<String> uoms=(List<String>)map.get("uom_name");
		Integer res=adminService.addUom(mid, uoms);
		
			if (res == 0)
			return new ResponseEntity(EnumConstants.UOM_NOT_CREATED.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity(EnumConstants.UOML_CREATED.toString(), HttpStatus.OK);

	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	@PostMapping(value = "/changestatus")
	public ResponseEntity changeUserStatus(@RequestBody String jsonString) throws CustomException, JsonParseException, JsonMappingException, IOException {
		
		if (jsonString == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		String jsonKeys[] = { "user_name", "status" };

		Map<String, Object> map = null;

		ObjectMapper mapper = new ObjectMapper();
			map = mapper.readValue(jsonString, HashMap.class);
		Integer i=adminService.changeUserStatus(map, jsonKeys);
		
		if (i <= 0)
			return new ResponseEntity(EnumConstants.USER_STATUS_NOT_UPDATED.toString(),	HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity(EnumConstants.USER_STATUS_UPDATED.toString(),	HttpStatus.OK);

	}
	
}
