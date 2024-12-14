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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.GoodsCategoryService;
import mks.myworkspace.crm.service.HistoryOrderService;
import mks.myworkspace.crm.service.OrderCategoryService;
import mks.myworkspace.crm.service.OrderService;
import mks.myworkspace.crm.service.OrderStatusService;
import mks.myworkspace.crm.service.StorageService;
import mks.myworkspace.crm.transformer.JpaTransformer_Order;

/**
 * Handles requests for Orders.
 */
@Controller
@Slf4j
@RequestMapping("/orders")
public class OrderController extends BaseController {
	
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
	
	@Autowired 
	HistoryOrderService historyOrderService;
	

	@GetMapping("")
	public ModelAndView displayOrder(@RequestParam(value = "categoryId", required = false) Long categoryId, 
			HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("ordersCRMScreen_Hansontable");
		initSession(request, httpSession);
		
		Order newOrder = new Order();
		newOrder.setDeliveryDate(new Date());
		
		// Giả định trong hệ thống có 1 loại đơn hàng thôi
		// Load tất cả các trạng thái của đơn hàng này lên combobox
		List<OrderCategory> orderCategories;
		orderCategories = orderCategoryService.getAllOrderCategoriesWithOrderStatuses();
		log.debug("Fetching all order's categories.");
		
		// Nếu có categoryId, lấy trạng thái của loại đơn hàng đó
	    if (categoryId != null) {
	        List<OrderStatus> orderStatuses = orderStatusService.findByOrderCategories_Id(categoryId);
	        mav.addObject("orderStatuses", orderStatuses);
	        mav.addObject("selectedCategoryId", categoryId);
	    }else {
	        // Nếu không có categoryId, mặc định hiển thị orderStatuses của loại đơn hàng có id là 1
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
	    
	    mav.addObject("listCustomers", listCustomers);
	    mav.addObject("listOrderStatuses", listOrderStatuses);
		mav.addObject("orderCategories", orderCategories);
		mav.addObject("order", newOrder);

		return mav;
	}

	@GetMapping("new")
	public ModelAndView newOrder(@RequestParam(value = "categoryId", required = false) Long categoryId,
			HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("fragments/createOrderModal_v2");
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
		
		List<String> allCodes = orderService.findAllCodeOrders();

		List<Object[]> dataSet = JpaTransformer_Order.convert2D(listOrders, allGoodsCategories, allSenders);
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
		mav.addObject("orderCodes", allCodes);

		return mav;
	}

	@GetMapping("/load")
	@ResponseBody
	public Object getOrderData() throws IOException {
		log.debug("Get sample data from configuration file.");
		int[] colWidths = { 100, 100, 300, 300, 300, 300 };
		String[] colHeaders = { "Mã đơn hàng", "Ngày giao", "Loại hàng hóa", "Thông tin người gửi",
				"Phương tiện vận chuyển", "Thao tác", };
		List<Object[]> tblData = new ArrayList<>();
		Object[] data1 = new Object[] { "D123A54", "2024-10-24", "Máy móc", "Nguyễn Văn A", "Xe tải", "" };
		Object[] data2 = new Object[] { "M123543", "2024-10-25", "Thực phẩm", "Nguyễn Văn B", "Xe tải", "" };

		tblData.add(data1);
		tblData.add(data2);

		TableStructure tblOrder = new TableStructure(colWidths, colHeaders, tblData);

		return tblOrder;
	}

	@GetMapping("/list")
	public ModelAndView displayOrderConfiguration(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("ordersConfigurationCRMScreen_v2");

		initSession(request, httpSession);
		return mav;
	}
	@GetMapping("/history")
    public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
        // Lấy tất cả các lịch sử đơn hàng
        List<HistoryOrder> historyOrders = historyOrderService.findAll();

        // Tạo ModelAndView và truyền dữ liệu vào view
        ModelAndView mav = new ModelAndView("historyUpdate");
        mav.addObject("historyOrders", historyOrders); // Truyền danh sách historyOrders vào view

        return mav;
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
