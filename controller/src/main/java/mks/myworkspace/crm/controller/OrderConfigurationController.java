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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.common.model.TableStructure;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.entity.OrderStatus;
import mks.myworkspace.crm.service.OrderCategoryService;
import mks.myworkspace.crm.service.OrderService;
import mks.myworkspace.crm.service.OrderStatusService;
import mks.myworkspace.crm.service.StorageService;
import mks.myworkspace.crm.transformer.JpaTransformer_OrderCate_Handsontable;
import mks.myworkspace.crm.validate.OrderCategoryValidator;

/**
 * Handles requests for the application home page.
 */
@Controller
@Slf4j
@RequestMapping("/orders-configuration")
public class OrderConfigurationController extends BaseController {

	/**
	 * This method is called when binding the HTTP parameter to bean (or model).
	 * 
	 * @param binder
	 */
	
	@Value("classpath:order-category/order-category-demo.json")
	private Resource resOrderCategoryDemo;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderCategoryService categoryService;
	
	@Autowired
	private OrderStatusService statusService;
	
	@Autowired
	private StorageService storageService;
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// Sample init of Custom Editor

//        Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//        PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//        binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

	}
	@GetMapping("/orders")
	public ModelAndView displayOrderConfiguration(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("ordersConfigurationCRMScreen");

		initSession(request, httpSession);
		return mav;
	}
	
	@GetMapping("/status")
	public ModelAndView displayOrderConfigurationStatus(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("ordersConfiguration-StatusCRMScreen_ky");

		initSession(request, httpSession);
		return mav;
	}
	
	@PostMapping(value = "/save-category-status")
	@ResponseBody
	public Object saveCategoryStatus(@RequestBody Map<String, Object> requestBody) throws IOException{
	    // Lấy danh sách các thay đổi (update)
		boolean saveOrUpdate = storageService.saveOrUpdateOrderCategoryStatus(requestBody);
	    return getOrderConfigurationStatusData();
	}
	
	private String getDefaultOrderCateData() throws IOException {
		return IOUtils.toString(resOrderCategoryDemo.getInputStream(), StandardCharsets.UTF_8);
	}
	
	//Add this method to view data on handsontable
		@GetMapping("/load")
		@ResponseBody
		public Object getOrderCategoryData() throws IOException {
			log.debug("Get sample data from configuration file.");
			String jsonOrderCateTable = getDefaultOrderCateData();

			List<OrderCategory> lstOrderCates = storageService.getOrderCategoryRepository().findAll();

			if (lstOrderCates == null || lstOrderCates.isEmpty()) {
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

				List<Object[]> tblData = JpaTransformer_OrderCate_Handsontable.convert2D(lstOrderCates);

				TableStructure tblOrderCate = new TableStructure(colWidths, colHeaders, tblData);

				return tblOrderCate;
			}
			
		}
		
	@GetMapping("/load-orders")
	@ResponseBody
	public Object getOrderConfigurationData() throws IOException {
		log.debug("Get sample data from configuration file.");
		int[] colWidths = { 50, 300, 300, };
		String[] colHeaders = { "No", "Loại đơn hàng", "Ghi chú", };
		List<Object[]> tblData = new ArrayList<>();
		 Object[] data1 = new Object[] { "1", "Mặc định", "" };
		    Object[] data2 = new Object[] { "2", "Máy móc", "" };
		    Object[] data3 = new Object[] { "3", "Thực phẩm", "" };
		    Object[] data4 = new Object[] { "4", "Điện tử", "" };
		    Object[] data5 = new Object[] { "5", "Thời trang", "" };
		    Object[] data6 = new Object[] { "6", "Nội thất", "" };
		    Object[] data7 = new Object[] { "7", "Văn phòng phẩm", "" };
		    Object[] data8 = new Object[] { "8", "Dịch vụ", "" };
		    Object[] data9 = new Object[] { "9", "Y tế", "" };
		    Object[] data10 = new Object[] { "10", "Hóa chất", "" };
		    Object[] data11 = new Object[] { "11", "Xây dựng", "" };
		    Object[] data12 = new Object[] { "12", "Nông nghiệp", "" };
		    Object[] data13 = new Object[] { "13", "Giải trí", "" };

		    tblData.add(data1);
		    tblData.add(data2);
		    tblData.add(data3);
		    tblData.add(data4);
		    tblData.add(data5);
		    tblData.add(data6);
		    tblData.add(data7);
		    tblData.add(data8);
		    tblData.add(data9);
		    tblData.add(data10);
		    tblData.add(data11);
		    tblData.add(data12);
		    tblData.add(data13);

		TableStructure tblOrderConfiguration = new TableStructure(colWidths, colHeaders, tblData);

		return tblOrderConfiguration;
	}
	
	@GetMapping("/load-statuses")
	@ResponseBody
	public Object getOrderConfigurationStatusData() throws IOException {
		log.debug("Get sample data from configuration file.");
		int[] colWidths = { 50, 300, 300, 200, 200};
		String[] colHeaders = { "No", "Loại đơn hàng", "Trạng thái", "Id Trạng Thái", "Id Loại Đơn Hàng"};
		
		List<OrderCategory> orderCategoryStatus=categoryService.getAllOrderCategoriesWithOrderStatuses();
		List<Object[]> orderStatusData=new ArrayList<>();
		OrderCategory category=new OrderCategory();
		Long id,idTrangThai;
		String nameCategory,nameStatus;
		Set<OrderStatus> orderStatuses=new HashSet<OrderStatus>();

		for (int i = 0; i < orderCategoryStatus.size(); i++) {
		    category = orderCategoryStatus.get(i);
		    id = category.getId();
		    nameCategory = category.getName();
		    orderStatuses = category.getOrderStatuses();

		    boolean isFirst = true; // Biến kiểm tra phần tử đầu tiên
		    for (OrderStatus os : orderStatuses) {
		        nameStatus = os.getName();
		        idTrangThai=os.getId();
		        Object[] data;
		        
		        if (isFirst) {
		            data = new Object[] { id, nameCategory, nameStatus,idTrangThai,id };
		            isFirst = false; // Sau phần tử đầu tiên, đặt biến này thành false
		        } else {
		            data = new Object[] { "", "", nameStatus,idTrangThai,id }; // Để trống id và nameCategory
		        }

		        orderStatusData.add(data);
		    } 
		    orderStatusData.add(new Object[] {"","","Thêm trạng thái mới","",""});
		}
		orderStatusData.add(new Object[] {".",".",".","",""});
		TableStructure tblOrderConfigurationStatus = new TableStructure(colWidths, colHeaders, orderStatusData);

		return tblOrderConfigurationStatus;
	}

	@PostMapping(value = "/save")
	@ResponseBody
	public TableStructure saveOrderCategory(@RequestBody TableStructure tableData) {
		log.debug("saveOrderCategory...{}", tableData);

		try {
			List<OrderCategory> lstOrderCategories = OrderCategoryValidator.validateAndCleasing(tableData.getData());
			lstOrderCategories = storageService.saveOrUpdateOrderCategory(lstOrderCategories);

			List<Object[]> tblData = JpaTransformer_OrderCate_Handsontable.convert2D(lstOrderCategories);
			tableData.setData(tblData);
		} catch (Exception ex) {
			log.error("Could not save order category.", ex);
		}

		return tableData;
	}
}
