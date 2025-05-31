/**
 * Licensed to MKS Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * MKS Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package mks.myworkspace.crm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.common.model.TableStructure;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.HistoryOrder;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.entity.OrderStatus;
import mks.myworkspace.crm.entity.dto.CustomerDetailJsonDTO;
import mks.myworkspace.crm.entity.dto.CustomerOrderDTO;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.GoodsCategoryService;
import mks.myworkspace.crm.service.HistoryOrderService;
import mks.myworkspace.crm.service.OrderCategoryService;
import mks.myworkspace.crm.service.OrderService;
import mks.myworkspace.crm.service.OrderStatusService;
import mks.myworkspace.crm.service.StorageService;
import mks.myworkspace.crm.transformer.JpaTransformer_Order;
import mks.myworkspace.crm.transformer.JpaTransformer_OrderDetail;
import mks.myworkspace.crm.transformer.JpaTransformer_OrderSearch;
import mks.myworkspace.crm.transformer.OrderConverter;

/**
 * Handles requests for the application home page.
 */
@Controller
@Slf4j
@RequestMapping("/orders-datatable")
public class OrderController_Datatable extends BaseController {

	/**
	 * This method is called when binding the HTTP parameter to bean (or model).
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// Sample init of Custom Editor

//        Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//        PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//        binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

	}

	/**
	 * Simply selects the home view to render by returning its name.
	 * 
	 * @return
	 */
	@Autowired
	StorageService storageService;

	@Autowired
	OrderCategoryService orderCategoryService;

	@Autowired
	OrderStatusService orderStatusService;

	@Autowired
	CustomerService customerService;

	@Autowired
	GoodsCategoryService goodsCategoryService;

	@Autowired
	OrderService orderService;
	
	@Autowired 
	HistoryOrderService historyOrderService;

	@Value("classpath:orders/orders-demo.json")
	private Resource resOrderDemo;

	@Value("${productList.colHeaders}")
	private String[] productListColHeaders;

	@Value("${productList.colWidths}")
	private int[] productListColWidths;

	@GetMapping("")
	public ModelAndView displayDatatableOrder(@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "customerId", required = false) Long customerId,
			@RequestParam(required = false) List<String> statuses, HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("ordersCRMScreen_Datatable");
		initSession(request, httpSession);
		List<OrderCategory> orderCategories;
		orderCategories = orderCategoryService.getAllOrderCategoriesWithOrderStatuses();
		log.debug("Fetching all order's categories.");

		if (categoryId != null) {
			List<OrderStatus> orderStatuses = orderStatusService.findByOrderCategories_Id(categoryId);
			mav.addObject("orderStatuses", orderStatuses);
			mav.addObject("selectedCategoryId", categoryId);
		} else {
			// Nếu không có categoryId, mặc định hiển thị orderStatuses của loại đơn hàng có
			// id là 1
			Long defaultCategoryId = 1L;
			List<OrderStatus> orderStatuses = orderStatusService.findByOrderCategories_Id(defaultCategoryId);
			mav.addObject("orderStatuses", orderStatuses);
			mav.addObject("selectedCategoryId", defaultCategoryId);
		}

		List<OrderStatus> listOrderStatuses;
		listOrderStatuses = orderStatusService.findAllOrderStatuses();
		log.debug("Fetching all order's statuses: {}", listOrderStatuses);

		List<Customer> listCustomers;
		listCustomers = customerService.getAllCustomers();

		List<GoodsCategory> listGoodsCategories;
		listGoodsCategories = goodsCategoryService.findAllGoodsCategory();

		List<Order> listOrders;
		listOrders = orderService.getAllOrders();
		log.debug("Fetched orders: {}", listOrders.toString());

		List<GoodsCategory> allGoodsCategories;
		allGoodsCategories = goodsCategoryService.findAllGoodsCategory();

		List<Customer> allSenders;
		allSenders = customerService.getAllCustomers();
		
		List<Long> statusList = (statuses != null && !statuses.isEmpty()) 
			    ? statuses.stream()
			              .map(Long::parseLong)
			              .collect(Collectors.toList())
			    : null;

		// Search functionality
		List<Order> ordersSearch = (customerId != null && categoryId != null && statuses != null)
				? orderService.searchOrders(customerId, categoryId, statusList)
				: listOrders;
		List<GoodsCategory> allGoodsCategoriesSearch = goodsCategoryService.findAllGoodsCategory();
		List<Customer> allSendersSearch = customerService.getAllCustomers();

		List<Object[]> dataSet;
		// dataSet = JpaTransformer_Order.convert2D(listOrders, allGoodsCategories,
		// allSenders);
		if (customerId == null && categoryId == null && statuses == null) {
			dataSet = JpaTransformer_Order.convert2D(listOrders, allGoodsCategories, allSenders);
		} else {
			dataSet = JpaTransformer_Order.convert2D(ordersSearch, allGoodsCategoriesSearch, allSendersSearch);
		}

		if (dataSet == null) {
			log.debug("DataSet is null, using demo data.");
			dataSet = getDemoData();
		} else {
			log.debug("IN DATASET:");
			for (Object[] row : dataSet) {
				log.debug("Row: " + Arrays.toString(row));
			}
		}
		 
		JSONArray jsonArray = new JSONArray();
		
//	    for (Customer customer : listCustomers) {
//	        JSONObject json = new JSONObject(customer);	        
//	        jsonArray.put(json);
//	    }
		
		for (Customer customer : listCustomers) {
			CustomerOrderDTO dto = new CustomerOrderDTO(customer);
		    JSONObject json = new JSONObject(dto);
		    jsonArray.put(json);
		}
		
	    List<OrderCategory> listOrderCategoryTransJson = orderCategories.stream()
	    	    .map(category -> new OrderCategory(category.getId(), category.getName(), category.getNote()))
	    	    .collect(Collectors.toList());
	    JSONArray jsonArrayB = new JSONArray(listOrderCategoryTransJson);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		mav.addObject("dataSet", dataSet);
		mav.addObject("listOrders", listOrders);
		mav.addObject("listCustomers", listCustomers);
		mav.addObject("customersJson", jsonArray.toString());
		mav.addObject("listOrderStatuses", listOrderStatuses);
		mav.addObject("orderCategories", orderCategories);
		mav.addObject("orderCateJson", jsonArrayB.toString());
		mav.addObject("listGoodsCategories", listGoodsCategories);

		return mav;
	}

	//@SuppressWarnings("unlikely-arg-type")
	@GetMapping("/search-orders")
	@ResponseBody
	public List<Object[]> searchOrders(@RequestParam(required = false) Long customerId,
			@RequestParam(required = false) Long orderCategoryId,
			@RequestParam(required = false) List<Long> statuses) {

//		List<Long> statusIds = null;
//		if (statuses != null && !statuses.isEmpty() && statuses.equals("null")) {
//		    try {
//		        statusIds = statuses.stream()
//		                            .map(Long::parseLong) // Convert from String to Long
//		                            .collect(Collectors.toList()); // Use Collectors.toList() for older Java versions
//		    } catch (NumberFormatException e) {
//		        log.error("Invalid status ID format: {}", statuses, e);
//		        throw new IllegalArgumentException("Status IDs must be numeric.");
//		        
//		    }
//		}
		
		List<GoodsCategory> allGoodsCategories;
		allGoodsCategories = goodsCategoryService.findAllGoodsCategory();

		List<Customer> allSenders;
		allSenders = customerService.getAllCustomers();

		List<Order> orders;
		orders = orderService.searchOrders(customerId, orderCategoryId, statuses);

		log.debug("customerId: {} " + customerId);
		log.debug("orderCategoryId: {} " + orderCategoryId);
		log.debug("statuses: {} " + statuses);

		return JpaTransformer_OrderSearch.convert2D(orders, allGoodsCategories, allSenders);
	}
	@GetMapping("/search-orders-list")
	@ResponseBody
	public List<Object[]> searchOrdersByList(@RequestParam(required = false) List<Long> customerIds,
			@RequestParam(required = false) List<Long> orderCategoryIds,
			@RequestParam(required = false) List<Long> statuses,
			@RequestParam(required = false) Optional<java.sql.Date> create_date,
			@RequestParam(required = false) Optional<java.sql.Date> delivery_date) {
		
		List<GoodsCategory> allGoodsCategories;
		allGoodsCategories = goodsCategoryService.findAllGoodsCategory();

		List<Customer> allSenders;
		allSenders = customerService.getAllCustomers();

		List<Order> orders;
		orders = orderService.searchOrdersByList(customerIds, orderCategoryIds, statuses, create_date, delivery_date);

		log.debug("customerId: {} " + customerIds);
		log.debug("orderCategoryId: {} " + orderCategoryIds);
		log.debug("statuses: {} " + statuses);

		return JpaTransformer_OrderSearch.convert2D(orders, allGoodsCategories, allSenders);
	}

	@GetMapping("/viewDetails/{id}")
	@ResponseBody
	public ResponseEntity<?> displayOrderDetails(@PathVariable("id") Long orderId) {
		Order order = orderService.getOrderById(orderId);
		List<OrderStatus> allOrderStatuses = orderStatusService.findAllOrderStatuses();
		List<GoodsCategory> allGoodsCategories = goodsCategoryService.findAllGoodsCategory();
		List<Customer> allSenders = customerService.getAllCustomers();
		List<Customer> allReceivers = customerService.getAllCustomers();
		List<OrderCategory> allOrderCategories = orderCategoryService.findAllOrderCategory();
		if (order != null) {
			Object[] orderDetailArray = JpaTransformer_OrderDetail.convert2D(order, allOrderStatuses,
					allGoodsCategories, allSenders, allReceivers, allOrderCategories);
			return ResponseEntity.ok(orderDetailArray);
		} else {
			log.debug("CANNOT FIND ORDER!");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
		}

	}

	@PostMapping(value = "/saveOrderData", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, String>> saveOrderData(@RequestBody String json) {
		Map<String, String> response = new HashMap<>();

		try {
			// Convert JSON string thành đối tượng Order
			Order order = OrderConverter.convertJsonToOrder_Update(json);

			log.debug("Order Details after JSON Conversion:");
			log.debug("Order ID: {}", order.getId());
			log.debug("Order Code: {}", order.getCode());
			log.debug("Create Date: {}", order.getCreateDate());
			log.debug("Delivery Date: {}", order.getDeliveryDate());
			log.debug("Requirement: {}", order.getCustomerRequirement());
			log.debug("Transport: {}", order.getTransportationMethod());

			if (order.getSender() != null) {
				log.debug("Customer Name: {}", order.getSender().getContactPerson());
				log.debug("Customer Email: {}", order.getSender().getEmail());
				log.debug("Customer Phone: {}", order.getSender().getPhone());
			}

			if (order.getGoodsCategory() != null) {
				log.debug("Goods Category: {}", order.getGoodsCategory().getName());
			}

			if (order.getOrderStatus() != null) {
				log.debug("Order Status: {}", order.getOrderStatus().getName());
			}

			// Lưu hoặc cập nhật đơn hàng
			Order savedOrder = storageService.saveOrUpdateOrder(order);
			response.put("status", "success");
			response.put("message", "Order " + (order.getId() != null ? "updated" : "created") + " successfully.");
			log.debug("Order saved with ID: {}", savedOrder.getId()); // Log kết quả ID

		} catch (Exception e) {
			log.error("Error saving/updating order: ", e);
			response.put("status", "error");
			response.put("message", "An error occurred while saving/updating the order.");
		}

		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "/saveOrderStatus", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, String>> saveOrderStatus(@RequestBody String json) {
		Map<String, String> response = new HashMap<>();

		try {
			// Convert JSON string thành đối tượng Order
			Order order = OrderConverter.convertJsonToOrder_UpdateOrderStatus(json);

			log.debug("Order Details after JSON Conversion:");
			log.debug("Order ID: {}", order.getId());

			if (order.getOrderStatus() != null) {
				log.debug("Order Status: {}", order.getOrderStatus().getName());
			}

			// Lưu hoặc cập nhật đơn hàng
			Order savedOrderStatus = storageService.updateOrderStatus(order);
			
			HistoryOrder historyOrder = new HistoryOrder(order, order.getOrderStatus(), new Date());
			// In thông tin của HistoryOrder ra console để kiểm tra
//			log.debug("HistoryOrder Details: ");
//			log.debug("Order ID: {}", historyOrder1.getOrder().getId());
//			log.debug("Order Status: {}", historyOrder1.getOrderStatus());
//			log.debug("Updated At: {}", historyOrder1.getUpdatedAt());
//
//			// Lưu đối tượng HistoryOrder
			historyOrderService.saveHistory(historyOrder);
			
			response.put("status", "success");
			response.put("message", "OrderStatus " + (order.getId() != null ? "updated" : "created") + " successfully.");
			log.debug("Order saved with ID: {}", savedOrderStatus.getId()); // Log kết quả ID

		} catch (Exception e) {
			log.error("Error saving/updating order : ", e);
			response.put("status", "error");
			response.put("message", "An error occurred while saving/updating the order.");
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/create-order", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, String>> createOrder(@RequestBody String json) {
		Map<String, String> response = new HashMap<>();

		try {
			// Convert JSON string thành đối tượng Order
			Order order = OrderConverter.convertJsonToOrder_Create(json);

			// Create Order
			Order savedOrder = storageService.saveOrUpdateOrder(order);
			
			
			
			log.debug("Saved Order: {}", savedOrder.toString());
			log.debug("Sender: {}", (savedOrder.getSender() != null ? savedOrder.getSender().getId() : "null"));
			log.debug( "Receiver: {}", (savedOrder.getReceiver() != null ? savedOrder.getReceiver().getId() : "null"));
			log.debug("GoodsCategory: {}", (savedOrder.getGoodsCategory() != null ? savedOrder.getGoodsCategory().getId() : "null"));
			log.debug("OrderStatus: {}", (savedOrder.getOrderStatus() != null ? savedOrder.getOrderStatus().getId() : "null"));
			log.debug("OrderCategory: {}", (savedOrder.getOrderCategory() != null ? savedOrder.getOrderCategory().getId() : "null"));
			response.put("status", "success");
			response.put("message", "Order created successfully.");
			log.debug("Order saved with ID: {}", savedOrder.getId()); // Log kết quả ID

			// Redirect to the orders datatable page
			response.put("reload", "true");

		} catch (Exception e) {
			log.error("Error saving/updating order: ", e);
			response.put("status", "error");
			response.put("message", "An error occurred while saving/updating the order.");
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping(value = { "/getAllOrderStatuses" }, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Object> getAllOrderStatuses() {
		try {
			List<OrderStatus> allOrderStatuses = orderStatusService.findAllOrderStatuses();

			// Chuyển đổi danh sách OrderStatus thành mảng 2 chiều
			Object[][] orderStatusData = JpaTransformer_OrderDetail.convert2D_OrderStatus(allOrderStatuses);

			// Chuyển đổi mảng 2 chiều thành danh sách danh sách để tương thích với JSON
			List<List<Object>> jsonCompatibleData = new ArrayList<>();
			for (Object[] row : orderStatusData) {
				jsonCompatibleData.add(Arrays.asList(row));
			}

			log.debug("Fetched all order statuses: {}", jsonCompatibleData);
			return ResponseEntity.ok(jsonCompatibleData);
		} catch (Exception e) {
			log.error("Error fetching order statuses: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}

//	@GetMapping("/orderDetail")
//	public ModelAndView displaycustomerDetailScreen(@RequestParam("id") Long orderId, HttpServletRequest request,
//			HttpSession httpSession) {
//		ModelAndView mav = new ModelAndView("ordersCRMScreen_Datatable.html");
//		initSession(request, httpSession);
//		mav.addObject("currentSiteId", getCurrentSiteId());
//		mav.addObject("userDisplayName", getCurrentUserDisplayName());
//		log.debug("Order Detail is running....");
//
//		Optional<Order> orderOpt = orderService.findById(orderId);
//
//		// Check if the customer exists and add to model
//		orderOpt.ifPresentOrElse(order -> {
//			mav.addObject("orderDetail", order);
//		}, () -> {
//			mav.addObject("errorMessage", "Order not found.");
//		});
//
//		return mav;
//	}

	@GetMapping(value = "/order-statuses", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Object> getOrderStatusesByCategory(@RequestParam("categoryId") Long categoryId) {
		try {
			List<OrderStatus> statuses = orderStatusService.findByOrderCategories_Id(categoryId);
			Object[][] orderStatusData = JpaTransformer_OrderDetail.convert2D_OrderStatus(statuses);
			// Chuyển đổi mảng 2 chiều thành danh sách danh sách để tương thích với JSON
			List<List<Object>> jsonCompatibleData = new ArrayList<>();
			for (Object[] row : orderStatusData) {
				jsonCompatibleData.add(Arrays.asList(row));
			}
			log.debug("Fetched order statuses for category {}: {}", categoryId, jsonCompatibleData);

			return ResponseEntity.ok(jsonCompatibleData);
		} catch (Exception e) {
			log.error("Error fetching order statuses for category {}: ", categoryId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}
	@GetMapping(value = "/order-statuses-list", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Object> getOrderStatusesByCategoryList(@RequestParam("categoryIds") List<Long> categoryIds) {
		try {
			List<OrderStatus> statuses = orderStatusService.findByOrderCategories_ListId(categoryIds);
			Object[][] orderStatusData = JpaTransformer_OrderDetail.convert2D_OrderStatus(statuses);
			// Chuyển đổi mảng 2 chiều thành danh sách danh sách để tương thích với JSON
			List<List<Object>> jsonCompatibleData = new ArrayList<>();
			for (Object[] row : orderStatusData) {
				jsonCompatibleData.add(Arrays.asList(row));
			}
			log.debug("Fetched order statuses for category {}: {}", categoryIds, jsonCompatibleData);

			return ResponseEntity.ok(jsonCompatibleData);
		} catch (Exception e) {
			log.error("Error fetching order statuses for category {}: ", categoryIds, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}

	@GetMapping("/get-sender-receiver-details")
	@ResponseBody
	public ResponseEntity<Map<String, String>> getSenderDetails(@RequestParam("customerId") Long customerId) {
		try {
			Optional<Customer> customerDetail = customerService.findById(customerId);

			if (customerDetail.isPresent()) {
				Customer customer = customerDetail.get();
				Map<String, String> senderDetail = new HashMap<>();
				senderDetail.put("phone", customer.getPhone());
				senderDetail.put("email", customer.getEmail());

				return ResponseEntity.ok(senderDetail);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping(value = "/delete-order", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, String>> deleteOrderById(@RequestBody String json) {
		Map<String, String> response = new HashMap<>();

		try {
			// Xóa đơn hàng
			Long orderId = OrderConverter.convertJsonToOrder_Delete(json);
			storageService.deleteOrderById(orderId);
			response.put("status", "success");
			response.put("message", "Order deleted successfully.");
			log.debug("Order deleted with ID: {}", orderId); // Log kết quả ID

		} catch (Exception e) {
			log.error("Error deleting order: ", e);
			response.put("status", "error");
			response.put("message", "An error occurred while deleting the order.");
		}

		return ResponseEntity.ok(response);
	}
//	@PostMapping(value = "/saveOrderData", consumes = "application/json", produces = "application/json")
//	@ResponseBody
//	public ResponseEntity<Map<String, String>> saveOrderData(@RequestBody Order order) {
//		log.debug("Received Order: {}", order.getId());
//		log.debug("Received Order Code: {}", order.getCode());
//		log.debug("Received Order Customer: {}", order.getCustomer());
//
//		Map<String, String> response = new HashMap<>();
//
//		try {
//			// Save or update the order
//			Order savedOrder = storageService.saveOrUpdateOrder(order);
//			response.put("status", "success");
//			response.put("message", "Order " + (order.getId() != null ? "updated" : "created") + " successfully.");
//			log.debug("Order saved with ID: {}", savedOrder.getId()); // Log the resulting ID
//		} catch (Exception e) {
//			log.error("Error saving/updating order: ", e);
//			response.put("status", "error");
//			response.put("message", "An error occurred while saving/updating the order.");
//		}
//
//		return ResponseEntity.ok(response);
//	}

//	@GetMapping(value = { "/get-orders" }, produces = "application/json")
//	@ResponseBody
//	public Object getOrderData() throws IOException {
//		log.debug("Get sample data from configuration file.");
//		
//		List<Object[]> jsonOrderTable = getDemoData();
//		List<Order> lstOrders = orderService.getAllOrders();
//		
//		if (lstOrders == null || lstOrders.isEmpty()) {
//			return jsonOrderTable;
//		} else {
//			//JSONArray jsonObjColWidths = jsonObjTableOrder.getJSONArray("colWidths");
//			int[] jsonObjColWidths = productListColWidths;
//			int len = (jsonObjColWidths != null) ? jsonObjColWidths.length : 0;
//			int[] colWidths = new int[len];
//			for (int i = 0; i < jsonObjColWidths.length; i++) {
//				colWidths[i] = jsonObjColWidths.length;
//			}
//			//JSONArray jsonObjColHeaders = jsonObjTableOrder.getJSONArray("colHeaders");
//			String[] jsonObjColHeaders = productListColHeaders;
//			len = (jsonObjColHeaders != null) ? jsonObjColHeaders.length : 0;
//			String[] colHeaders = new String[len];
//			for (int i = 0; i < jsonObjColHeaders.length; i++) {
//				colHeaders[i] = jsonObjColHeaders.toString();
//			}
//
//			List<Object[]> tblData = JpaTransformer_Order.convert2D(lstOrders);
//
//			TableStructure tblOrder = new TableStructure(colWidths, colHeaders, tblData);
//
//			return tblOrder;
//		}
//	}

	@GetMapping(value = { "/loaddata" }, produces = "application/json")
	@ResponseBody
	public TableStructure getProductTableData() {
		List<Object[]> lstProducts = getDemoData();

		TableStructure productTable = new TableStructure(productListColWidths, productListColHeaders, lstProducts);

		return productTable;
	}

	private List<Object[]> getDemoData() {
		List<Object[]> data = new ArrayList<Object[]>();

		data.add(new Object[] { "1", "D123A54", "2024-10-24", "Máy móc", "Nguyễn Văn A", "Xe tải", "..." });
		data.add(new Object[] { "2", "M123543", "2024-10-25", "Thực phẩm", "Nguyễn Văn B", "Máy bay", "..." });
		data.add(new Object[] { "3", "G123A54", "2024-10-24", "Mỹ phẩm", "Nguyễn Văn C", "Xe tải", "..." });
		data.add(new Object[] { "4", "H123473", "2024-10-25", "Thực phẩm", "Lê Trần Minh D", "Xe đông lạnh", "..." });

		return data;
	}

}
