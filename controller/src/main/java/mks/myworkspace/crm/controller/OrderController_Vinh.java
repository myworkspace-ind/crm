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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.common.model.TableStructure;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.entity.OrderStatus;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.GoodsCategoryService;
import mks.myworkspace.crm.service.OrderCategoryService;
import mks.myworkspace.crm.service.OrderService;
import mks.myworkspace.crm.service.OrderStatusService;
import mks.myworkspace.crm.service.StorageService;
import mks.myworkspace.crm.transformer.JpaTransformer_Order;
import mks.myworkspace.crm.transformer.JpaTransformer_OrderDetail;
import mks.myworkspace.crm.transformer.JpaTransformer_Order_Handsontable;
import mks.myworkspace.crm.transformer.OrderConverter;

/**
 * Handles requests for Orders.
 */
@Controller
@Slf4j
@RequestMapping("/orders-vinh")
public class OrderController_Vinh extends BaseController {
	
	@Autowired
	GoodsCategoryService goodsCategoryService;
	
	@Autowired
	OrderService orderService;

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
	
	@Value("classpath:orders/orders-demo.json")
	private Resource resOrdersDemo;

	private String getDefaultOrderData() throws IOException {
		return IOUtils.toString(resOrdersDemo.getInputStream(), StandardCharsets.UTF_8);
	}
	@GetMapping("/load")
	@ResponseBody
	public Object getOrderData() throws IOException {
		log.debug("Get sample data from configuration file.");
		
		String jsonOrderCateTable = getDefaultOrderData();
		
		List<Order> lstOrders = orderService.getAllOrders();
		
		if (lstOrders == null || lstOrders.isEmpty()) {
			return jsonOrderCateTable;
		} else {
			JSONObject jsonObjTableOrderCate = new JSONObject(jsonOrderCateTable);

			JSONArray jsonObjColWidths = jsonObjTableOrderCate.getJSONArray("colWidths");
			int len = (jsonObjColWidths != null) ? jsonObjColWidths.length() : 0;
			int[] colWidths = new int[len];
			for (int i = 0; i < jsonObjColWidths.length(); i++) {
				colWidths[i] = jsonObjColWidths.getInt(i);
			}

			JSONArray jsonObjColHeaders = jsonObjTableOrderCate.getJSONArray("colHeaders");
			len = (jsonObjColHeaders != null) ? jsonObjColHeaders.length() : 0;
			String[] colHeaders = new String[len];
			for (int i = 0; i < jsonObjColHeaders.length(); i++) {
				colHeaders[i] = jsonObjColHeaders.getString(i);
			}
			List<Object[]> tblData = JpaTransformer_Order_Handsontable.convert2D(lstOrders);
			
			TableStructure tblOrder = new TableStructure(colWidths, colHeaders, tblData);

			return tblOrder;
	}
}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<?> deleteOrderById(@RequestParam("id") Long orderId, 
	                                               HttpServletRequest request,
	                                               HttpSession httpSession) {
	    try {
	        if (orderId == null) {
	            return ResponseEntity.badRequest().body(Map.of("errorMessage", "ID không được để trống."));
	        }
	        
	        storageService.deleteOrderById(orderId);

	        return ResponseEntity.ok()
	                .body(Map.of("message", "Order đã được xóa thành công!", "id", orderId));
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("errorMessage", e.getMessage()));
	    } catch (Exception e) {
	        log.debug("Error while deleting order with ID: {}. Error: {}", orderId, e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại sau!", "details", e.getMessage()));
	    }
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
			response.put("status", "success");
			response.put("message", "OrderStatus " + (order.getId() != null ? "updated" : "created") + " successfully.");
			log.debug("Order saved with ID: {}", savedOrderStatus.getId()); // Log kết quả ID

		} catch (Exception e) {
			log.error("Error saving/updating order: ", e);
			response.put("status", "error");
			response.put("message", "An error occurred while saving/updating the order.");
		}

		return ResponseEntity.ok(response);
	}
}	
