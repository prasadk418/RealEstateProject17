package com.realestate.controller;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.domain.Material;
import com.realestate.domain.SecurityQuestions;
import com.realestate.domain.Status;
import com.realestate.domain.UOM;
import com.realestate.repository.InvoiceRepository;
import com.realestate.repository.MaterialRepository;
import com.realestate.repository.OrderRepository;
import com.realestate.repository.SecurityQuestionsRepository;
import com.realestate.repository.StatusRepository;
import com.realestate.repository.UOMRepository;
import com.realestate.repository.UserInfoRepository;
import com.realestate.repository.UserRepository;
import com.realestate.util.CommonsUtil;
import com.realestate.util.EnumConstants;

@RestController
@RequestMapping(value = "/api")
public class GetAllController {

	@Autowired
	public UserRepository userRepo;

	@Autowired
	public UserInfoRepository userInfoRepo;

	@Autowired
	public UOMRepository uomRepo;

	@Autowired
	public MaterialRepository materialRepo;

	@Autowired
	public SecurityQuestionsRepository sqRepo;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private InvoiceRepository invoiceRepo;

	@Autowired
	public StatusRepository statusRepo;

	ObjectMapper mapper = CommonsUtil.getObjectMapperObject();
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/getuom/{material}")
	public ResponseEntity findUOMByMaterial(@PathVariable("material") String material) throws ServletException {

		if (material == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		Material m = materialRepo.findOne(Integer.parseInt(material));
		if (m == null)
			return new ResponseEntity(EnumConstants.MATERIAL_NOT_FOUND.toString(), HttpStatus.BAD_REQUEST);
		System.out.println(m.getId());

		Iterable<UOM> res =materialRepo.findUOMByMaterial(m.getId());
		return new ResponseEntity(res, HttpStatus.OK);

	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/getallmaterials")
	public ResponseEntity getAllMaterials() throws ServletException {

		Iterable<Material> res = materialRepo.findAll();
		return new ResponseEntity(res, HttpStatus.OK);

	}


	@GetMapping(value = "/getallsq")
	public ResponseEntity getAllSQ() {
		Iterable<SecurityQuestions> ques = sqRepo.findAll();
		return new ResponseEntity(ques, HttpStatus.OK);
	}

	@GetMapping(value = "/getallstatus")
	public ResponseEntity getAllStatuses() {
		Iterable<Status> status = statusRepo.findAll();
		return new ResponseEntity(status, HttpStatus.OK);
	}

	@GetMapping(value = "/getalluom")
	public ResponseEntity getAllUOM() {
		Iterable<UOM> status = uomRepo.findAll();
		return new ResponseEntity(status, HttpStatus.OK);
	}

	

	
}
