package com.realestate.controller;

import static com.realestate.util.CommonConstants.FIRM_NAME;
import static com.realestate.util.CommonConstants.STATUS;
import static com.realestate.util.CommonConstants.USER_ID;
import static com.realestate.util.CommonConstants.USER_NAME;
import static com.realestate.util.CommonConstants.USER_TYPE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.realestate.domain.User;
import com.realestate.domain.UserInfo;
import com.realestate.domain.VendorDTO;
import com.realestate.exceptions.CustomException;
import com.realestate.repository.UserInfoRepository;
import com.realestate.repository.UserRepository;
import com.realestate.service.LoginService;
import com.realestate.util.CommonsUtil;
import com.realestate.util.EnumConstants;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	private LoginService loginService;

	@Autowired
	public UserRepository userRepo;

	@Autowired
	public UserInfoRepository userInfoRepo;

	ObjectMapper mapper = CommonsUtil.getObjectMapperObject();

	@SuppressWarnings("all")
	@GetMapping(value = "/vendorsbymaterial/{material}")
	public ResponseEntity vendorSearchByMaterial(@PathVariable("material") String material)
			throws JsonProcessingException, IOException {
		List<UserInfo> userInfo = userInfoRepo.vendorsByMaterial(material);
		JsonNode rootNode = mapper.readTree(mapper.writeValueAsString(userInfo));
		List<JsonNode> ordersNode = createUserJsonObject(rootNode);
		return new ResponseEntity(ordersNode, HttpStatus.OK);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(value = "/getusersbyfirmname")
	public ResponseEntity getBuilderByFirmname(@RequestBody String firmname)
			throws JsonProcessingException, IOException {
		if (firmname == null) {
			return new ResponseEntity("Request body is null..!", HttpStatus.BAD_REQUEST);
		}

		UserInfo users = userInfoRepo.findUsersByFirmname(firmname);
		if (users == null)
			return new ResponseEntity(EnumConstants.USER_NOT_CREATED.toString(), HttpStatus.NOT_FOUND);
		JsonNode rootNode = mapper.readTree(mapper.writeValueAsString(users));
		List<JsonNode> nodesList = createUserJsonObject(rootNode);
		return new ResponseEntity(nodesList, HttpStatus.OK);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(value = "/getusersbyusertypeandfirmname")
	public ResponseEntity getBuilderByFirmnameAndUsertype(@RequestBody String jsonString) throws Exception {
		if (jsonString == null) {
			return new ResponseEntity("Request body is null..!", HttpStatus.BAD_REQUEST);
		}
		Map<String, Object> map = mapper.readValue(jsonString, HashMap.class);

		List<UserInfo> users = userInfoRepo.findUsersByFirmnameAndUsertype((String) map.get(FIRM_NAME),
				(String) map.get(USER_TYPE));
		if (users == null || users.isEmpty())
			return new ResponseEntity(EnumConstants.USER_NOT_CREATED.toString(), HttpStatus.NOT_FOUND);
		JsonNode rootNode = mapper.readTree(mapper.writeValueAsString(users));
		List<JsonNode> nodesList = createUserJsonObject(rootNode);
		return new ResponseEntity(nodesList, HttpStatus.OK);

	}

	@SuppressWarnings("all")
	@GetMapping(value = "/getallusers")
	public ResponseEntity getAllBuilders() throws JsonProcessingException, IOException {
		Iterable<UserInfo> users = userInfoRepo.findAll();

		JsonNode rootNode = mapper.readTree(mapper.writeValueAsString(users));
		List<JsonNode> nodesList = createUserJsonObject(rootNode);
		return new ResponseEntity(nodesList, HttpStatus.OK);
	}

	@SuppressWarnings("all")
	@GetMapping(value = "/getusers/{status}")
	public ResponseEntity getBuildersOnStatus(@PathVariable("status") String status)
			throws JsonProcessingException, IOException {
		if (status == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		Iterable<UserInfo> users = userInfoRepo.getUsersByStatus(status);

		JsonNode rootNode = mapper.readTree(mapper.writeValueAsString(users));
		List<JsonNode> nodesList = createUserJsonObject(rootNode);
		return new ResponseEntity(nodesList, HttpStatus.OK);
	}

	@SuppressWarnings("all")
	@GetMapping(value = "/getusers/{status}/{type}")
	public ResponseEntity getBuildersOnStatusAndType(@PathVariable("status") String status,
			@PathVariable("type") String type) throws JsonProcessingException, IOException {
		if (status == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		Iterable<UserInfo> users = userInfoRepo.getUsersByStatusAndUserType(status, type);

		JsonNode rootNode = mapper.readTree(mapper.writeValueAsString(users));
		List<JsonNode> nodesList = createUserJsonObject(rootNode);
		return new ResponseEntity(nodesList, HttpStatus.OK);
	}

	@SuppressWarnings("all")
	private List<JsonNode> createUserJsonObject(JsonNode rootNode) {
		List<JsonNode> nodesList = new ArrayList<JsonNode>();
		Iterator<JsonNode> jsonNodeItr = rootNode.elements();
		while (jsonNodeItr.hasNext()) {
			JsonNode node = jsonNodeItr.next();
			JsonNode node1 = ((ObjectNode) node).remove(USER_ID);
			JsonNode statusNode = ((ObjectNode) node1).remove(STATUS);

			((ObjectNode) node).put(STATUS, statusNode.path(STATUS));
			((ObjectNode) node).put("user_type", node1.path("user_type"));

			nodesList.add(node);
		}
		return nodesList;
	}

	@SuppressWarnings("all")
	@PostMapping(value = "/me")
	public ResponseEntity getUserDetails(@RequestBody String jsonString)
			throws JsonProcessingException, IOException, CustomException, ServletException {
		if (jsonString == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		Map<String, Object> map = mapper.readValue(jsonString, HashMap.class);

		/*
		 * String jtoken = (String) map.get("token");
		 * 
		 * if (jtoken == null) return new
		 * ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(),
		 * HttpStatus.BAD_REQUEST);
		 * 
		 * try { Claims token = Jwts.parser().setSigningKey(secret)
		 * .parseClaimsJws(jtoken).getBody();
		 * 
		 * } catch (SignatureException ex) { throw new
		 * ServletException("Invalid Token"); } catch (MalformedJwtException ex)
		 * { throw new ServletException("JWT token is malformed"); } catch
		 * (ExpiredJwtException ex) { throw new
		 * ServletException("JWT token expired, So please do login"); }catch
		 * (Exception ex) { throw new ServletException(ex.getMessage()); }
		 */

		User user = loginService.getUserByUsername((String) map.get(USER_NAME));

		ObjectMapper om = new ObjectMapper();
		String jsonString1 = om.writeValueAsString(user);

		JsonNode node = om.readTree(jsonString1);
		JsonNode statusNode = ((ObjectNode) node).remove("status");
		((ObjectNode) node).put("status", statusNode.path("status"));
		map.put("user", node);

		return new ResponseEntity(map, HttpStatus.OK);

	}

	@SuppressWarnings("all")
	@PostMapping(value = "/searchVendorByFirm")
	public ResponseEntity searchVendorByFirmName(@RequestBody String jsonString)
			throws JsonParseException, JsonMappingException, IOException {
		if (jsonString == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		Map<String, Object> map = mapper.readValue(jsonString, HashMap.class);

		List<Object[]> userInfo = userInfoRepo.searchUserByFirmName((String) map.get(FIRM_NAME));
		List<VendorDTO> arr = new ArrayList<VendorDTO>();

		for (Object[] oa : userInfo) {
			System.out.println(oa.length);
			VendorDTO dto = new VendorDTO();
			String[] ss = new String[2];
			dto.setFirm_name((String) oa[0]);
			dto.setMobile((String) oa[1]);
			String[] maaray = ((String) oa[2]).split(",");
			System.out.println(maaray);
			dto.setMaterials(maaray);

			arr.add(dto);
		}

		return new ResponseEntity(arr, HttpStatus.OK);
	}

	@SuppressWarnings("all")
	@GetMapping(value = "/getallapprovedvendors")
	public ResponseEntity getApprovedVendors() throws JsonParseException, JsonMappingException, IOException {

		List<Object[]> userInfo = userInfoRepo.getApprovedVendors();
		List<VendorDTO> arr = new ArrayList<VendorDTO>();

		for (Object[] oa : userInfo) {
			System.out.println(oa.length);
			VendorDTO dto = new VendorDTO();
			String[] ss = new String[2];
			dto.setFirm_name((String) oa[0]);
			dto.setMobile((String) oa[1]);
			String[] maaray = ((String) oa[2]).split(",");
			System.out.println(maaray);
			dto.setMaterials(maaray);

			arr.add(dto);
		}

		return new ResponseEntity(arr, HttpStatus.OK);
	}
}
