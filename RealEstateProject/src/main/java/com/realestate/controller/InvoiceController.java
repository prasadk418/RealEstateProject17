package com.realestate.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static com.realestate.util.CommonConstants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.realestate.domain.Invoice;
import com.realestate.domain.Order;
import com.realestate.domain.Status;
import com.realestate.repository.InvoiceRepository;
import com.realestate.repository.OrderRepository;
import com.realestate.repository.StatusRepository;
import com.realestate.util.CommonsUtil;
import com.realestate.util.EnumConstants;
import com.realestate.util.UIDGenerator;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {


	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private InvoiceRepository invoiceRepo;

	@Autowired
	private StatusRepository statusRepo;

	ObjectMapper mapper = CommonsUtil.getObjectMapperObject();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value="/new")
	public ResponseEntity generateInvoice(@RequestBody String jsonString) throws ParseException
	{
		if(jsonString==null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);


		String jsonKeys[] = { "order_id", "order_status", "unit_price",
				"delivery_quantity", "amount", "delivery_date",
				"confirmation_date", "comments"};


		ObjectMapper mapper = new ObjectMapper();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> map = null;// new HashMap<String,String>();

		try {
			map = mapper.readValue(jsonString, HashMap.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (map.get(jsonKeys[0]) == null || map.get(jsonKeys[1]) == null || map.get(jsonKeys[2]) == null || map.get(jsonKeys[3])==null || map.get(jsonKeys[4]) == null || map.get(jsonKeys[5]) == null || map.get(jsonKeys[6]) == null || map.get(jsonKeys[7] ) == null)
			return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);

		Order order = orderRepo.findByOrderId((String)(map.get(jsonKeys[0])));

		if(order==null)
			return new ResponseEntity(EnumConstants.ORDER_NOT_FOUND.toString(), HttpStatus.NOT_FOUND);

		Status s=statusRepo.findOne((String)map.get(jsonKeys[1]));

		order.setOrderStatus(s);
		order.setUnitPrice((Double)map.get(jsonKeys[2]));
		order.setDeliveryQuantity((Integer)map.get(jsonKeys[3]));
		order.setAmount((Double)map.get(jsonKeys[4]));
		Date delDate =null;
		if (map.get(jsonKeys[5]) != null && (! "".equals(map.get(jsonKeys[5]))))
			delDate = sd.parse((String)map.get(jsonKeys[5]));
		order.setDeliveryDate(delDate);
		Date confDate = null;
		confDate = sd.parse((String)map.get(jsonKeys[6]));
		order.setOrderConfirmationDate(confDate);
		order.setComments((String)map.get(jsonKeys[7]));

		Invoice invoice=new Invoice();

		String ss = sd.format(new Date());
		Date invoiceDate = sd.parse(ss);
		invoice.setInvoiceId(UIDGenerator.uniqueIdGenerator(invoiceRepo.count()+1L,"Invoice"));
		invoice.setInvoiceDate(invoiceDate);
		invoice.setOrderId(order);

		Invoice in=invoiceRepo.save(invoice);
		if(in==null)
			return new ResponseEntity(EnumConstants.INVOICE_NOT_CREATED.toString(), HttpStatus.INTERNAL_SERVER_ERROR);

		return new ResponseEntity(EnumConstants.INVOICE_CREATED.toString(), HttpStatus.OK);


	}

	/*
	@GetMapping(value="/{invoiceId}")
	public ResponseEntity searchByInvoiceId(@PathVariable("invoiceId") String invoiceId) throws IOException
	{
		if(invoiceId==null)
				return new ResponseEntity(EnumConstants.EMPTY_JSON_STRING.toString(), HttpStatus.BAD_REQUEST);


		Invoice invoice=invoiceRepo.findInvoiceById(Integer.parseInt(invoiceId));
		if(invoice==null)
			return new ResponseEntity(EnumConstants.INVOICE_NOT_FOUND.toString(), HttpStatus.NOT_FOUND);

		ObjectMapper om=new ObjectMapper();
		String jsonString =om.writeValueAsString(invoice);

		JsonNode node=om.readTree(jsonString);



			JsonNode orderNode=((ObjectNode)node).remove("orderId");	
			//adding  required order keys
			((ObjectNode)node).put("orderId" ,orderNode.path("orderId"));
			((ObjectNode)node).put("orderDate" ,orderNode.path("orderDate"));
			((ObjectNode)node).put("quantity" ,orderNode.path("quantity"));
			((ObjectNode)node).put("size" ,orderNode.path("size"));
			((ObjectNode)node).put("amount" ,orderNode.path("amount"));
			((ObjectNode)node).put("deliveryQuantity" ,orderNode.path("deliveryQuantity"));
			((ObjectNode)node).put("deliveryDate" ,orderNode.path("deliveryDate"));
			((ObjectNode)node).put("unitPrice" ,orderNode.path("unitPrice"));
			((ObjectNode)node).put("orderConfirmationDate" ,orderNode.path("orderConfirmationDate"));
			((ObjectNode)node).put("comments" ,orderNode.path("comments"));

			JsonNode materialNode=((ObjectNode)orderNode).remove("material");
			//adding material keys 
			((ObjectNode)node).put("material" ,materialNode.path("name"));

			JsonNode uomNode=((ObjectNode)orderNode).remove("uom");
			//adding uom keys
			((ObjectNode)node).put("uom" ,uomNode.path("name"));

			JsonNode builderNode=((ObjectNode)orderNode).remove("firm_name");
			//adding builder keys
			((ObjectNode)node).put("firm_name" ,builderNode.path("firm_name"));
			((ObjectNode)node).put("mobile" ,builderNode.path("mobile"));


		return new ResponseEntity(node, HttpStatus.OK);

	}

	 */
	@SuppressWarnings("all")
	@GetMapping(value = "/getallinvoices")
	public ResponseEntity getAllInvoices() throws IOException {

		Iterable<Invoice> invoices = invoiceRepo.findAll();


		String jsonString = mapper.writeValueAsString(invoices);

		JsonNode rootNode = mapper.readTree(jsonString);
		List<JsonNode> nodesList=createInvoiceJsonObject(rootNode);

		return new ResponseEntity(nodesList, HttpStatus.OK);
	}
	
	@SuppressWarnings("all")
	@PostMapping(value = "/getmyinvoices")
	public ResponseEntity getInvoicesByUser(@RequestBody String jsonString1) throws Exception {

		if (jsonString1 == null) {
			return new ResponseEntity("Request body is null..!", HttpStatus.BAD_REQUEST);
		}

		Map<String, Object> map = mapper.readValue(jsonString1, HashMap.class);
		
		List<Invoice> invoices = invoiceRepo.findInvoiceByUsername((String) map.get(USER_NAME));

		ObjectMapper om = CommonsUtil.getObjectMapperObject();
		String jsonString = om.writeValueAsString(invoices);

		JsonNode rootNode = om.readTree(jsonString);
		List<JsonNode> nodesList =createInvoiceJsonObject(rootNode);
		
		return new ResponseEntity(nodesList, HttpStatus.OK);
	}
	
	@SuppressWarnings("all")
	public List<JsonNode> createInvoiceJsonObject(JsonNode rootNode)
	{
		List<JsonNode> nodesList = new ArrayList<JsonNode>();
		Iterator<JsonNode> nodesItr = rootNode.elements();
		while (nodesItr.hasNext()) {
			JsonNode node = nodesItr.next();

			JsonNode orderNode = ((ObjectNode) node).remove("orderId");
			// adding required order keys
			((ObjectNode) node).put("orderId", orderNode.path("orderId"));
			((ObjectNode) node).put("orderDate", orderNode.path("orderDate"));
			// ((ObjectNode)node).put("orderStatus"
			// ,orderNode.path("orderStatus"));
			((ObjectNode) node).put("quantity", orderNode.path("quantity"));
			((ObjectNode) node).put("size", orderNode.path("size"));
			((ObjectNode) node).put("amount", orderNode.path("amount"));
			((ObjectNode) node).put("deliveryQuantity", orderNode.path("deliveryQuantity"));
			((ObjectNode) node).put("deliveryDate", orderNode.path("deliveryDate"));
			((ObjectNode) node).put("unitPrice", orderNode.path("unitPrice"));
			((ObjectNode) node).put("vendor_firm", orderNode.path("vendor_firm"));
			((ObjectNode) node).put("vendor_mobile", orderNode.path("vendor_mobile"));
			((ObjectNode) node).put("orderConfirmationDate", orderNode.path("orderConfirmationDate"));
			((ObjectNode) node).put("comments", orderNode.path("comments"));
			JsonNode materialNode = ((ObjectNode) orderNode).remove("material");
			// adding material keys
			((ObjectNode) node).put("material", materialNode.path("name"));

			JsonNode uomNode = ((ObjectNode) orderNode).remove("uom");
			// adding uom keys
			((ObjectNode) node).put("uom", uomNode.path("name"));

			JsonNode builderNode = ((ObjectNode) orderNode).remove("firm_name");
			// adding builder keys
			((ObjectNode) node).put("firm_name", builderNode.path("firm_name"));
			((ObjectNode) node).put("mobile", builderNode.path("mobile"));

			JsonNode statusNode = ((ObjectNode) orderNode).remove("orderStatus");
			((ObjectNode) node).put("orderStatus", statusNode.path("status"));

			nodesList.add(node);
		}

		return nodesList;
	}

	
}
