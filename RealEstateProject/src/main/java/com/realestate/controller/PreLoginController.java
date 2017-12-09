package com.realestate.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static com.realestate.util.CommonConstants.*;
import static com.realestate.util.CommonConstants.APPROVED;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.realestate.domain.UserInfo;
import com.realestate.domain.Login;
import com.realestate.domain.SecurityQuestions;
import com.realestate.domain.User;
import com.realestate.exceptions.CustomException;
import com.realestate.repository.UserInfoRepository;
import com.realestate.repository.MaterialRepository;
import com.realestate.repository.SecurityQuestionsRepository;
import com.realestate.repository.StatusRepository;
import com.realestate.repository.UOMRepository;
import com.realestate.repository.UserRepository;
import com.realestate.service.LoginService;
import com.realestate.util.CommonsUtil;
import com.realestate.util.EnumConstants;
import com.realestate.util.PasswordEncryption;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping(value = "/prelogin")
public class PreLoginController {

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
	public StatusRepository statusRepo;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private LoginService loginService;

	private static final String ISSUER = "admin";
	static final long ONE_MINUTE_IN_MILLIS = 60000;// 1 min = 60000 milliseconds
	
	@Value("${jwt.secret}")
	private String secret;

	
	ObjectMapper mapper = CommonsUtil.getObjectMapperObject();

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ResponseEntity userLogin(@RequestBody Login login) throws ServletException, IOException, CustomException {

		if (login == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		if (servletContext.getAttribute(login.getUser_name()) != null) {
			return new ResponseEntity("{\"message\" : \"User already logged In\"}", HttpStatus.BAD_REQUEST);
		}

		User user = loginService.getUserByUsername(login.getUser_name());

		if (user == null) {
			return new ResponseEntity(EnumConstants.INVALID_USER.toString(), HttpStatus.UNAUTHORIZED);
		}else if(user.getPassword()==null)
		{
			return new ResponseEntity("{\"message\" : \"Password not yet created. Please set the password by checking status \", \"status\" : false }",
					HttpStatus.UNAUTHORIZED);
		}
		else if (!PasswordEncryption.checkPwd(login.getPassword(), user.getPassword())) {
			return new ResponseEntity(EnumConstants.INVALID_PASSWORD.toString(), HttpStatus.UNAUTHORIZED);
		} else if (!APPROVED.equalsIgnoreCase(user.getStatus().getStatus())) {
			return new ResponseEntity("{\"message\" : " + user.getStatus().getStatus() + ", \"status\" : false }",
					HttpStatus.UNAUTHORIZED);
		}

		servletContext.setAttribute(login.getUser_name(), login.getUser_name());
		Map<String, Object> map = new HashMap<String, Object>();

		Calendar date = Calendar.getInstance();
		long t = date.getTimeInMillis();
		
		Date afterAddingMins = new Date(t + (30 * ONE_MINUTE_IN_MILLIS));

		String token = Jwts.builder().setSubject(user.getUser_name()).setIssuedAt(new Date()).setIssuer(ISSUER)
				.signWith(SignatureAlgorithm.HS512, secret).compact();
				//.setExpiration(afterAddingMins).signWith(SignatureAlgorithm.HS512, secret).compact();

		if (user.getSqId() != null && user.getSqPwd() != null)
			map.put("token", token);

		ObjectMapper om = new ObjectMapper();
		String jsonString = om.writeValueAsString(user);

		JsonNode node = om.readTree(jsonString);
		JsonNode statusNode = ((ObjectNode) node).remove("status");
		((ObjectNode) node).put("status", statusNode.path("status"));
		map.put("user", node);

		return new ResponseEntity(map, HttpStatus.OK);
	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value = "/signin/admin", method = RequestMethod.POST)
	 * public ResponseEntity adminLogin(@RequestBody Login login) throws
	 * ServletException {
	 * 
	 * if (login != null) { Admin admin =
	 * adminRepo.findOne(login.getUsername());
	 * 
	 * if (admin == null) { return new ResponseEntity(
	 * EnumConstants.INVALID_USER.toString(), HttpStatus.UNAUTHORIZED); } else
	 * if (!PasswordEncryption.checkPwd(login.getPassword(),
	 * admin.getPassword())) { return new ResponseEntity(
	 * EnumConstants.INVALID_PASSWORD.toString(), HttpStatus.UNAUTHORIZED); }
	 * 
	 * String token = Jwts.builder().setSubject(admin.getName())
	 * .setIssuedAt(new Date()).setIssuer(ISSUER)
	 * .signWith(SignatureAlgorithm.HS512, secret).compact(); Map<String,
	 * String> map = new HashMap<String, String>(); map.put("token", token);
	 * map.put("user_type", "admin"); map.put("full_name", "ADMIN"); return new
	 * ResponseEntity(map, HttpStatus.OK); } else { return new ResponseEntity(
	 * EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST); } }
	 */
	@SuppressWarnings("all")
	@PostMapping(value = "/checkstatus")
	public ResponseEntity userStatus(@RequestBody String username)
			throws ServletException, JsonProcessingException, IOException, CustomException {

		if (username == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		Map<String, String> map = mapper.readValue(username, HashMap.class);
		
		User res=loginService.getUserByUsername(map.get(USER_NAME));
		
		JsonNode rootNode = mapper.readTree(mapper.writeValueAsString(res));
		JsonNode statusNode = ((ObjectNode) rootNode).remove(STATUS);

		((ObjectNode) rootNode).put("status", statusNode.path(STATUS));
		return new ResponseEntity(rootNode, HttpStatus.OK);

	}

	/*@SuppressWarnings("unchecked")
	public ResponseEntity user(String sqid) throws ServletException {

		SecurityQuestions res = sqRepo.findOne(Integer.parseInt(sqid));
		return new ResponseEntity(res, HttpStatus.OK);

	}*/

	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping(value = "/user/resetpwd/{username}")
	public ResponseEntity resetPassword(@PathVariable String username) {
		if (username == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		User res = userRepo.checkStatus(username);

		if (res == null)
			return new ResponseEntity(EnumConstants.URI_NOT_FOUND.toString(), HttpStatus.NOT_FOUND);

		if ("admin".equals(res.getUser_type()))
			return new ResponseEntity(
					"{\"message\" : \"You are admin, No need to check, Please login \", \"status\" : false }",
					HttpStatus.NOT_FOUND);

		if (res.getStatus() != null && "Pending".equalsIgnoreCase(res.getStatus().getStatus()))
			// "{\"message\" : " + user.getStatus()+ ", \"status\" : false }"
			// "{\"message\" : \"User not yet approved by ADMIN..! \",
			// \"status\" : false }"
			return new ResponseEntity("{\"message\" : \"User not yet approved by ADMIN..! \", \"status\" : false }",
					HttpStatus.NOT_FOUND);

		if (res.getStatus() != null && "Rejected".equalsIgnoreCase(res.getStatus().getStatus()))
			return new ResponseEntity("{\"message\" : \"User rejected by ADMIN..! \", \"status\" : false }",
					HttpStatus.NOT_FOUND);
		SecurityQuestions seq = res.getSqId();
		if (seq == null)
			return new ResponseEntity("{\"message\" : \"Security Question not in DB..! \", \"status\" : false }",
					HttpStatus.NOT_FOUND);

		return new ResponseEntity(seq, HttpStatus.OK);
	}*/

	/*
	 * @PostMapping(value = "/checksqans") public ResponseEntity
	 * checkUserToResetPwd(@RequestBody String jsonString) { if (jsonString ==
	 * null) return new ResponseEntity(EnumConstants.URI_NOT_FOUND.toString(),
	 * HttpStatus.NOT_FOUND);
	 * 
	 * String jsonKeys[] = { "user_name", "question", "pwd" };
	 * 
	 * Map<String, String> map = null;// new HashMap<String,String>();
	 * 
	 * ObjectMapper mapper = new ObjectMapper(); try { map =
	 * mapper.readValue(jsonString, HashMap.class); } catch (Exception e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } User res =
	 * userRepo.findByUsername(map.get(jsonKeys[0]));
	 * 
	 * if (res == null) return new
	 * ResponseEntity(EnumConstants.USER_NOT_FOUND.toString(),
	 * HttpStatus.NOT_FOUND);
	 * 
	 * SecurityQuestions seq = res.getSqId(); if (seq == null) return new
	 * ResponseEntity(EnumConstants.SECURITY_QUESTION_NOT_FOUND.toString(),
	 * HttpStatus.NOT_FOUND);
	 * 
	 * if (seq.getQuestion() == null ||
	 * !seq.getQuestion().equalsIgnoreCase(map.get(jsonKeys[1]))) { return new
	 * ResponseEntity(EnumConstants.SECURITY_QUESTION_NOT_FOUND.toString(),
	 * HttpStatus.NOT_FOUND); } String str = null; if (res.getSqPwd() != null &&
	 * PasswordEncryption.checkPwd(map.get(jsonKeys[2]), res.getSqPwd())) return
	 * new
	 * ResponseEntity("{\"message\" : \"Security question Password matched..! \", \"status\" : true }"
	 * , HttpStatus.OK); else return new ResponseEntity(
	 * "{\"message\" : \"Security question Password not matched..! \", \"status\" : false }"
	 * , HttpStatus.NOT_FOUND);
	 * 
	 * }
	 */

	@SuppressWarnings("all")
	@PostMapping(value = "/getsq")
	public ResponseEntity checkUserToResetPwd(@RequestBody String jsonString) throws CustomException, JsonParseException, JsonMappingException, IOException {
		if (jsonString == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);


		Map<String, String> map = mapper.readValue(jsonString, HashMap.class);
		
		SecurityQuestions seq=loginService.getUserSecurityQuestion(map.get(USER_NAME));
				
		return new ResponseEntity(seq, HttpStatus.OK);
		}

	@SuppressWarnings("all")
	@PostMapping(value = "/setpwd")
	@Transactional
	public ResponseEntity resetNewPassword(@RequestBody String jsonString) throws CustomException, JsonParseException, JsonMappingException, IOException {

		if (jsonString == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);


		Map<String, String> map = mapper.readValue(jsonString, HashMap.class);
		
		if (map.get(USER_NAME) == null || map.get(PASSWORD) == null || map.get(SEQ_ID) == null
				|| map.get(SEQ_PWD) == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		User u=loginService.resetNewPassword(map.get(USER_NAME),map.get(PASSWORD),map.get(SEQ_ID),map.get(SEQ_PWD));
		
		if (u == null)
			return new ResponseEntity("{\"message\" : \"User details not updated..! \", \"status\" : false }",
					HttpStatus.INTERNAL_SERVER_ERROR);

		return new ResponseEntity("{\"message\" : \"User details updated Successfully..! \", \"status\" : true }",
				HttpStatus.OK);

	}

	@SuppressWarnings("all")
	@PostMapping(value = "/forgotpwd")
	public ResponseEntity forgotPassword(@RequestBody String jsonString) throws CustomException, JsonParseException, JsonMappingException, IOException {

		if (jsonString == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		Map<String, String> map = mapper.readValue(jsonString, HashMap.class);

		if (map.get(USER_NAME) == null || map.get(PASSWORD) == null || map.get(SEQ_PWD) == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		Integer res=loginService.forgotPassword(map.get(USER_NAME), map.get(PASSWORD), map.get(SEQ_PWD));
		
		
		ResponseEntity resEntity = null;
		if (res <= 0)
			return new ResponseEntity(EnumConstants.PASSWORD_CHANGE_FAILED.toString(),
					HttpStatus.INTERNAL_SERVER_ERROR);

		return new ResponseEntity(EnumConstants.PASSWORD_CHANGE_SUCCESS.toString(), HttpStatus.OK);
	}

	@SuppressWarnings("all")
	@PostMapping(value = "/logout")
	public ResponseEntity logout(@RequestBody String jsonString) throws JsonParseException, JsonMappingException, IOException {

		Map<String, String> map = mapper.readValue(jsonString, HashMap.class);
		String username=(String)map.get(USER_NAME);
		if(username==null || "".equals(username))
			return new ResponseEntity("{\"message\" : \"Invalid User\"}", HttpStatus.UNAUTHORIZED);

		String uname=(String)servletContext.getAttribute(username);
		if(uname==null)
			return new ResponseEntity("{\"message\" : \"Sorry, user not logged in.\"}", HttpStatus.UNAUTHORIZED);
		servletContext.removeAttribute(map.get(USER_NAME));
		return new ResponseEntity("{\"message\" : \"User loggedout successfully\"}", HttpStatus.OK);
	}

	@SuppressWarnings("all")
	@PostMapping(value = "/newuser")
	public ResponseEntity addUser(@RequestBody UserInfo builder) {
		if (builder == null || builder.getBusiness_email() == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		User u = userRepo.findByUsername(builder.getBusiness_email());
		UserInfo uInfo=userInfoRepo.findUsersByFirmname(builder.getFirm_name());

		if (u != null || uInfo !=null)
			return new ResponseEntity("{\"message\" : \"User alreary registerd with these details.\" }",
					HttpStatus.BAD_REQUEST);

		UserInfo b = loginService.addUser(builder);

		if (b == null)
			return new ResponseEntity(EnumConstants.USER_NOT_CREATED.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity(EnumConstants.USER_CREATED.toString(), HttpStatus.OK);
	}

	@SuppressWarnings("all")
	@RequestMapping(value = "/**")
	public ResponseEntity NoUrl() {

		return new ResponseEntity("Sorry, Requested URI NOT FOUND...", HttpStatus.NOT_FOUND);
	}

}
