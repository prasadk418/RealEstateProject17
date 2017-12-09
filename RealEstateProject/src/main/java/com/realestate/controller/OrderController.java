package com.realestate.controller;

import static com.realestate.util.CommonConstants.AMOUNT;
import static com.realestate.util.CommonConstants.COMMENTS;
import static com.realestate.util.CommonConstants.CONFM_DATE;
import static com.realestate.util.CommonConstants.DELV_DATE;
import static com.realestate.util.CommonConstants.DELV_QUANTITY;
import static com.realestate.util.CommonConstants.MATERIAL;
import static com.realestate.util.CommonConstants.ORDER_ID;
import static com.realestate.util.CommonConstants.ORDER_STATUS;
import static com.realestate.util.CommonConstants.QUANTITY;
import static com.realestate.util.CommonConstants.SIZE;
import static com.realestate.util.CommonConstants.UNIT_PRICE;
import static com.realestate.util.CommonConstants.UOM;
import static com.realestate.util.CommonConstants.USER_NAME;
import static com.realestate.util.CommonConstants.VENDOR_FIRM;
import static com.realestate.util.CommonConstants.VENDOR_MOBILE;
import static com.realestate.util.CommonConstants.PENDING;
import static com.realestate.util.CommonConstants.DELIVERED;
import static com.realestate.util.CommonConstants.REJECTED;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.realestate.domain.Invoice;
import com.realestate.domain.Material;
import com.realestate.domain.Order;
import com.realestate.domain.Status;
import com.realestate.domain.UOM;
import com.realestate.domain.UserInfo;
import com.realestate.repository.InvoiceRepository;
import com.realestate.repository.MaterialRepository;
import com.realestate.repository.OrderRepository;
import com.realestate.repository.StatusRepository;
import com.realestate.repository.UOMRepository;
import com.realestate.repository.UserInfoRepository;
import com.realestate.util.CommonConstants;
import com.realestate.util.CommonsUtil;
import com.realestate.util.EnumConstants;
import com.realestate.util.UIDGenerator;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private InvoiceRepository invoiceRepo;

	@Autowired
	private UserInfoRepository userInfoRepo;

	@Autowired
	private MaterialRepository materialRepo;

	@Autowired
	private UOMRepository uomRepo;

	@Autowired
	private StatusRepository statusRepo;

	@Autowired
	private InvoiceController invoiceController;

	ObjectMapper mapper = CommonsUtil.getObjectMapperObject();
	SimpleDateFormat sd = CommonsUtil.getSimpleDateFormatObject();

	@SuppressWarnings("all")
	@PostMapping(value = "/new")
	public ResponseEntity newOrder(@RequestBody String jsonString)
			throws ParseException, JsonParseException, JsonMappingException, IOException {

		if (jsonString == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		Order order = new Order();

		String jsonKeys[] = { "material", "uom", "quantity", "size", "user_name" };

		Map<String, Object> map = mapper.readValue(jsonString, HashMap.class);

		if (map.get(MATERIAL) == null || map.get(UOM) == null || map.get(QUANTITY) == null
				|| map.get(USER_NAME) == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		Material material = materialRepo.findOne((Integer) map.get(MATERIAL));
		if (material == null)
			return new ResponseEntity(EnumConstants.MATERIAL_NOT_FOUND.toString(), HttpStatus.BAD_REQUEST);
		UOM uom = uomRepo.findOne((Integer) map.get(UOM));
		if (uom == null)
			return new ResponseEntity(EnumConstants.UOM_NOT_FOUND.toString(), HttpStatus.BAD_REQUEST);

		String ss = sd.format(new Date());
		Date date = sd.parse(ss);
		order.setOrderId(UIDGenerator.uniqueIdGenerator(orderRepo.count() + 1L, "Order"));
		order.setOrderDate(date);
		order.setOrderStatus(statusRepo.findOne(PENDING));
		order.setMaterial(material);
		order.setUom(uom);
		order.setQualtity((Integer) (map.get(QUANTITY)));
		order.setSize((String) map.get(SIZE));

		UserInfo b = userInfoRepo.findUserByUserName((String) map.get(USER_NAME));
		if (b == null)
			return new ResponseEntity(EnumConstants.USER_NOT_FOUND.toString(), HttpStatus.NOT_FOUND);

		order.setFirm_name(b);

		Order o = orderRepo.save(order);
		if (o == null)
			return new ResponseEntity(EnumConstants.ORDER_NOT_CREATED.toString(), HttpStatus.NOT_IMPLEMENTED);
		return new ResponseEntity(EnumConstants.ORDER_CREATED.toString(), HttpStatus.OK);
	}

	@SuppressWarnings("all")
	@PostMapping(value = "/update")
	public ResponseEntity updateOrder(@RequestBody String jsonString)
			throws ParseException, JsonParseException, JsonMappingException, IOException {

		if (jsonString == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

		String jsonKeys[] = { "order_id", "order_status", "unit_price", "delivery_quantity", "amount", "delivery_date",
				"confirmation_date", "comments", "vendor_firm", "vendor_mobile" };
		Map<String, Object> map = mapper.readValue(jsonString, HashMap.class);

		Order order = orderRepo.findByOrderId((String) map.get(ORDER_ID));// findByOrderId((String)
																			// map.get(ORDER_ID));

		Status ss = statusRepo.findOne((String) map.get(ORDER_STATUS));
		order.setOrderStatus((ss));
		if (map.get(UNIT_PRICE) != null)
			order.setUnitPrice(((Number) (map.get(UNIT_PRICE))).doubleValue());
		order.setDeliveryQuantity((Integer) map.get(DELV_QUANTITY));
		if (map.get(AMOUNT) != null)
			order.setAmount(((Number) (map.get(AMOUNT))).doubleValue());
		System.out.println(map.get(DELV_DATE));
		Date delDate = null;
		if (map.get(DELV_DATE) != null && (!"".equals(map.get(DELV_DATE))))
			delDate = sd.parse((String) map.get(DELV_DATE));
		order.setDeliveryDate(delDate);
		Date confDate = null;
		if (map.get(CONFM_DATE) != null && (!"".equals(map.get(CONFM_DATE))))
			confDate = sd.parse((String) map.get(CONFM_DATE));
		order.setOrderConfirmationDate(confDate);
		order.setComments((String) map.get(COMMENTS));
		order.setVendor_firm((String) map.get(VENDOR_FIRM));
		order.setVendor_mobile((String) map.get(VENDOR_MOBILE));

		if (DELIVERED.equalsIgnoreCase(ss.getStatus()))
			return generateInvoice(order);

		Order o = orderRepo.save(order);
		if (o == null)
			return new ResponseEntity(EnumConstants.ORDER_NOT_CREATED.toString(), HttpStatus.NOT_IMPLEMENTED);
		return new ResponseEntity(EnumConstants.ORDER_UPDATED.toString(), HttpStatus.OK);
	}

	@SuppressWarnings("all")
	public ResponseEntity generateInvoice(Order order) throws ParseException {
		SimpleDateFormat sd = CommonsUtil.getSimpleDateFormatObject();

		Invoice invoice = new Invoice();

		String ss = sd.format(new Date());
		Date invoiceDate = sd.parse(ss);
		invoice.setInvoiceId(UIDGenerator.uniqueIdGenerator(invoiceRepo.count() + 1L, "Invoice"));
		invoice.setInvoiceDate(invoiceDate);
		invoice.setOrderId(order);

		Invoice in = invoiceRepo.save(invoice);
		if (in == null)
			return new ResponseEntity(EnumConstants.INVOICE_NOT_CREATED.toString(), HttpStatus.INTERNAL_SERVER_ERROR);

		return new ResponseEntity(EnumConstants.INVOICE_CREATED.toString(), HttpStatus.OK);

	}

	@SuppressWarnings("all")
	@PostMapping(value = "/getmyorders")
	public ResponseEntity getOrdersByUser(@RequestBody String jsonString1) throws Exception {

		if (jsonString1 == null) {
			return new ResponseEntity("Request body is null..!", HttpStatus.BAD_REQUEST);
		}

		Map<String, Object> map = mapper.readValue(jsonString1, HashMap.class);

		Iterable<Order> orders = orderRepo.findByUserName((String) map.get(USER_NAME));

		ObjectMapper om = CommonsUtil.getObjectMapperObject();
		;
		String jsonString = om.writeValueAsString(orders);

		JsonNode rootNode = om.readTree(jsonString);
		List<JsonNode> nodesList = createOrderJsonObject(rootNode);

		return new ResponseEntity(nodesList, HttpStatus.OK);

	}

	@SuppressWarnings("all")
	@PostMapping(value = "/getmyordersbystatus")
	public ResponseEntity getMyOrdersByStatus(@RequestBody String jsonString1) throws Exception {

		if (jsonString1 == null) {
			return new ResponseEntity("Request body is null..!", HttpStatus.BAD_REQUEST);
		}

		Map<String, Object> map = mapper.readValue(jsonString1, HashMap.class);

		List<Order> orders = orderRepo.findMyOdersByStatus((String) map.get(USER_NAME),
				(List<String>) map.get(ORDER_STATUS));

		String jsonString = mapper.writeValueAsString(orders);

		JsonNode rootNode = mapper.readTree(jsonString);
		List<JsonNode> nodesList = createOrderJsonObject(rootNode);

		return new ResponseEntity(nodesList, HttpStatus.OK);

	}

	@SuppressWarnings("all")
	@GetMapping(value = "/getallorders")
	public ResponseEntity getAllOrders() throws IOException {

		Iterable<Order> orders = orderRepo.findAll();

		String jsonString = mapper.writeValueAsString(orders);

		JsonNode rootNode = mapper.readTree(jsonString);
		List<JsonNode> nodesList = createOrderJsonObject(rootNode);

		return new ResponseEntity(nodesList, HttpStatus.OK);
	}

	@SuppressWarnings("all")
	@PostMapping(value = "/getallordersbystatus")
	public ResponseEntity getAllOrdersByStatus(@RequestBody String jsonString) throws IOException {

		if (jsonString == null) {
			return new ResponseEntity("Request body is null..!", HttpStatus.BAD_REQUEST);
		}

		Map<String, Object> map = mapper.readValue(jsonString, HashMap.class);
		List<String> statusList = (List<String>) map.get("order_status");

		Iterable<Order> orders = orderRepo.findAllOrdersByStatusList(statusList);

		String ordersJsonString = mapper.writeValueAsString(orders);

		JsonNode rootNode = mapper.readTree(ordersJsonString);
		List<JsonNode> nodesList = createOrderJsonObject(rootNode);

		return new ResponseEntity(nodesList, HttpStatus.OK);
	}

	@SuppressWarnings("all")
	private List<JsonNode> createOrderJsonObject(JsonNode rootNode) {
		List<JsonNode> nodesList = new ArrayList<JsonNode>();
		Iterator<JsonNode> nodesItr = rootNode.elements();
		while (nodesItr.hasNext()) {
			JsonNode node = nodesItr.next();
			JsonNode builderNode = ((ObjectNode) node).remove("firm_name");
			JsonNode materialNode = ((ObjectNode) node).remove("material");
			JsonNode uomNode = ((ObjectNode) node).remove("uom");
			JsonNode statusNode = ((ObjectNode) node).remove("orderStatus");

			// adding values required builder keys order
			((ObjectNode) node).put("firm_name", builderNode.path("firm_name"));
			((ObjectNode) node).put("mobile", builderNode.path("mobile"));
			((ObjectNode) node).put("material", materialNode.path("name"));
			((ObjectNode) node).put("material", materialNode.path("name"));
			((ObjectNode) node).put("uom", uomNode.path("name"));
			((ObjectNode) node).put("orderStatus", statusNode.path("status"));

			nodesList.add(node);
		}
		return nodesList;
	}

	@SuppressWarnings("all")
	@GetMapping(value = "/getallordershistory")
	public List<JsonNode> getAllOrdersHistory() throws IOException {
		List<JsonNode> finalList = new ArrayList<JsonNode>();

		List<Order> rejectedOrdersList = orderRepo.findAllOrdersByStatus(REJECTED);
		String rejectedOrdersJson = mapper.writeValueAsString(rejectedOrdersList);

		JsonNode rejectedRootNode = mapper.readTree(rejectedOrdersJson);
		List<JsonNode> ordersNode = createOrderJsonObject(rejectedRootNode);

		Iterable<Invoice> invoiceList = invoiceRepo.findAll();
		JsonNode invoiceRootNode = mapper.readTree(mapper.writeValueAsString(invoiceList));
		List<JsonNode> invoiceNode = invoiceController.createInvoiceJsonObject(invoiceRootNode);
		finalList.addAll(ordersNode);
		finalList.addAll(invoiceNode);

		return finalList;
	}

	@SuppressWarnings("all")
	@PostMapping(value = "/getmyordershistory")
	public ResponseEntity getMyOrdersHistory(@RequestBody String jsonString1) throws IOException {
		if (jsonString1 == null) {
			return new ResponseEntity("Request body is null..!", HttpStatus.BAD_REQUEST);
		}

		Map<String, Object> map = mapper.readValue(jsonString1, HashMap.class);

		List<JsonNode> finalList = new ArrayList<JsonNode>();

		List<Order> ordersList = orderRepo.findMyOdersByStatus((String) map.get(USER_NAME), Arrays.asList(REJECTED));
		JsonNode rejectedRootNode = mapper.readTree(mapper.writeValueAsString(ordersList));
		List<JsonNode> ordersNode = createOrderJsonObject(rejectedRootNode);

		Iterable<Invoice> invoiceList = invoiceRepo.findInvoiceByUsername((String) map.get(USER_NAME));
		JsonNode invoiceRootNode = mapper.readTree(mapper.writeValueAsString(invoiceList));
		List<JsonNode> invoiceNode = invoiceController.createInvoiceJsonObject(invoiceRootNode);

		finalList.addAll(ordersNode);
		finalList.addAll(invoiceNode);

		return new ResponseEntity(finalList, HttpStatus.OK);
	}

	@SuppressWarnings("all")
	@PostMapping(value = "/getvendorordersbystatus")
	public ResponseEntity getVendorInvolvedOrdersByStatus(@RequestBody String jsonString1)
			throws JsonProcessingException, IOException {
		if (jsonString1 == null) {
			return new ResponseEntity("Request body is null..!", HttpStatus.BAD_REQUEST);
		}

		Map<String, Object> map = mapper.readValue(jsonString1, HashMap.class);
		String username = ((String) map.get(USER_NAME));
		UserInfo userInfo = userInfoRepo.findUserByUserName(username);
		List<Order> orders = orderRepo.findVendorInvolvedOrdersByStatus(userInfo.getFirm_name(),
				(String) map.get(ORDER_STATUS));
		String jsonString = mapper.writeValueAsString(orders);

		JsonNode rootNode = mapper.readTree(jsonString);
		List<JsonNode> nodesList = createOrderJsonObject(rootNode);

		return new ResponseEntity(nodesList, HttpStatus.OK);

	}

	@SuppressWarnings("all")
	@PostMapping(value = "/getvendororders")
	public ResponseEntity getVendorInvolvedOrders(@RequestBody String jsonString1)
			throws JsonProcessingException, IOException {
		if (jsonString1 == null) {
			return new ResponseEntity("Request body is null..!", HttpStatus.BAD_REQUEST);
		}

		Map<String, Object> map = mapper.readValue(jsonString1, HashMap.class);
		String username = ((String) map.get(USER_NAME));
		UserInfo userInfo = userInfoRepo.findUserByUserName(username);
		List<Order> orders = orderRepo.findVendorInvolvedOrders(userInfo.getFirm_name());
		String jsonString = mapper.writeValueAsString(orders);

		JsonNode rootNode = mapper.readTree(jsonString);
		List<JsonNode> nodesList = createOrderJsonObject(rootNode);

		return new ResponseEntity(nodesList, HttpStatus.OK);

	}

	@SuppressWarnings("all")
	@PostMapping(value = "/getvendororderhistory")
	public ResponseEntity getVendorOrdersHistory(@RequestBody String jsonString1) throws IOException {
		if (jsonString1 == null) {
			return new ResponseEntity("Request body is null..!", HttpStatus.BAD_REQUEST);
		}

		Map<String, Object> map = mapper.readValue(jsonString1, HashMap.class);
		String username = ((String) map.get(USER_NAME));
		UserInfo userInfo = userInfoRepo.findUserByUserName(username);
		List<Order> orders = orderRepo.findVendorInvolvedOrdersByStatus(userInfo.getFirm_name(), REJECTED);
		JsonNode rejectedRootNode = mapper.readTree(mapper.writeValueAsString(orders));
		List<JsonNode> ordersNode = createOrderJsonObject(rejectedRootNode);

		List<Order> orders1 = orderRepo.findVendorInvolvedOrdersByStatus(userInfo.getFirm_name(), DELIVERED);
		JsonNode deliveredRootNode = mapper.readTree(mapper.writeValueAsString(orders1));
		List<JsonNode> ordersNode1 = createOrderJsonObject(deliveredRootNode);

		List<JsonNode> finalList = new ArrayList<JsonNode>();
		finalList.addAll(ordersNode);
		finalList.addAll(ordersNode1);

		return new ResponseEntity(finalList, HttpStatus.OK);
	}

	@SuppressWarnings("all")
	@GetMapping(value = "/getordersbymaterial/{material}")
	public ResponseEntity orderSearchByMaterial(@PathVariable("material") String material)
			throws JsonProcessingException, IOException {
		List<Order> orders = orderRepo.findByMaterial(material);
		JsonNode rejectedRootNode = mapper.readTree(mapper.writeValueAsString(orders));
		List<JsonNode> ordersNode = createOrderJsonObject(rejectedRootNode);
		return new ResponseEntity(ordersNode, HttpStatus.OK);

	}

}