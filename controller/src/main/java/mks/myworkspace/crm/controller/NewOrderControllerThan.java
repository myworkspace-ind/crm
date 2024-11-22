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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import mks.myworkspace.crm.transformer.OrderConverter;

/**
 * Handles requests for the application home page.
 */
@Controller
@Slf4j
@RequestMapping("/NewOrder")
public class NewOrderControllerThan extends BaseController {

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

	@Value("classpath:orders/orders-demo.json")
	private Resource resOrderDemo;

	@Value("${productList.colHeaders}")
	private String[] productListColHeaders;

	@Value("${productList.colWidths}")
	private int[] productListColWidths;

	@GetMapping("")
	public ModelAndView displayDatatableOrder(@RequestParam(value = "categoryId", required = false) Long categoryId,
			HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("fragments/createOrderModal_than");
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

		List<Object[]> dataSet = JpaTransformer_Order.convert2D(listOrders, listGoodsCategories, listCustomers);
		if (dataSet == null) {
			log.debug("DataSet is null, using demo data.");
			dataSet = getDemoData();
		} else {
			log.debug("IN DATASET:");
			for (Object[] row : dataSet) {
				log.debug("Row: " + Arrays.toString(row));
			}
		}

		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		mav.addObject("dataSet", dataSet);
		mav.addObject("listOrders", listOrders);
		mav.addObject("listCustomers", listCustomers);
		mav.addObject("listOrderStatuses", listOrderStatuses);
		mav.addObject("orderCategories", orderCategories);
		mav.addObject("listGoodsCategories", listGoodsCategories);

		return mav;
	}

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
