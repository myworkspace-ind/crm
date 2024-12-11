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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.common.model.TableStructure;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.entity.OrderStatus;
import mks.myworkspace.crm.service.OrderCategoryService;
import mks.myworkspace.crm.service.OrderService;

/**
 * Handles requests for the application home page.
 */
@Controller
@Slf4j
@RequestMapping("/orders-configuration-ky")
public class OrderConfigurationController_Ky extends BaseController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderCategoryService categoryService;
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
		tblData.add(data1);
		tblData.add(data2);
		tblData.add(data3);

		TableStructure tblOrderConfiguration = new TableStructure(colWidths, colHeaders, tblData);

		return tblOrderConfiguration;
	}
	
	@GetMapping("/load-statuses")
	@ResponseBody
	public Object getOrderConfigurationStatusData() throws IOException {
		log.debug("Get sample data from configuration file.");
		int[] colWidths = { 50, 300, 300, };
		String[] colHeaders = { "No", "Loại đơn hàng", "Trạng thái", };
		
		List<Order> orderDataList=orderService.getAllOrders();
		//Test
		List<Object[]> orderData=new ArrayList<>();
		for(int i=0;i<orderDataList.size();i++) {
			Object[] myData=new Object[] {orderDataList.get(i).getId(),orderDataList.get(i).getName(),orderDataList.get(i).getSiteId()};
			orderData.add(myData);
		}
		
		List<OrderCategory> orderCategoryStatus=categoryService.getAllOrderCategoriesWithOrderStatuses();
		List<Object[]> orderStatusData=new ArrayList<>();
		OrderCategory category=new OrderCategory();
		Long id;
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
		        Object[] data;
		        
		        if (isFirst) {
		            data = new Object[] { id, nameCategory, nameStatus };
		            isFirst = false; // Sau phần tử đầu tiên, đặt biến này thành false
		        } else {
		            data = new Object[] { id, "", nameStatus }; // nameCategory rỗng
		        }
		        
		        orderStatusData.add(data);
		    }
		}
		
//		List<Object[]> tblData = new ArrayList<>();
//		Object[] data1 = new Object[] { "1", "Mặc định", "Nhận đơn" };
//		Object[] data2 = new Object[] { "", "", "Đóng gói" };
//		Object[] data3 = new Object[] { "", "", "Vận chuyển" };
//		Object[] data4 = new Object[] { "", "", "Giao hàng" };
//		
//		Object[] data5 = new Object[] { "2", "Máy móc", "Nhận đơn" };
//		Object[] data6 = new Object[] { "", "", "Đóng gói" };
//		Object[] data7 = new Object[] { "", "", "Vận chuyển" };
//		Object[] data8 = new Object[] { "", "", "Lưu kho" };
//		Object[] data9 = new Object[] { "", "", "Giao hàng" };
//		
//		Object[] data10 = new Object[] { "3", "Thực phẩm", "Nhận đơn" };
//		Object[] data11 = new Object[] { "", "", "Đóng gói" };
//		Object[] data12 = new Object[] { "", "", "Vận chuyển" };
//		Object[] data13 = new Object[] { "", "", "Lưu kho lạnh" };
//		Object[] data14 = new Object[] { "", "", "Giao hàng" };
//		
//		tblData.add(data1);
//		tblData.add(data2);
//		tblData.add(data3);
//		tblData.add(data4);
//		tblData.add(data5);
//		tblData.add(data6);
//		tblData.add(data7);
//		tblData.add(data8);
//		tblData.add(data9);
//		tblData.add(data10);
//		tblData.add(data11);
//		tblData.add(data12);
//		tblData.add(data13);
//		tblData.add(data14);

		TableStructure tblOrderConfigurationStatus = new TableStructure(colWidths, colHeaders, orderStatusData);

		return tblOrderConfigurationStatus;
	}

//	private String getDefaultOrderData() throws IOException {
//		return IOUtils.toString(resOrderDemo.getInputStream(), StandardCharsets.UTF_8);
//	}

//	@GetMapping("/load")
//	@ResponseBody
//	public Object getTaskData() throws IOException {
//		log.debug("Get sample data from configuration file.");
//		String jsonOrdersTable = getDefaultOrdersData();
//
//		List<Task> lstTasks = storageService.getTaskRepo().findAll();
//
//		if (lstTasks == null || lstTasks.isEmpty()) {
//			return jsonTaskTable;
//		} else {
//			JSONObject jsonObjTableTask = new JSONObject(jsonTaskTable);
//
//			JSONArray jsonObjColWidths = jsonObjTableTask.getJSONArray("colWidths");
//			int len = (jsonObjColWidths != null) ? jsonObjColWidths.length() : 0;
//			int[] colWidths = new int[len];
//			for (int i = 0; i < jsonObjColWidths.length(); i++) {
//				colWidths[i] = jsonObjColWidths.getInt(i);
//			}
//
//			JSONArray jsonObjColHeaders = jsonObjTableTask.getJSONArray("colHeaders");
//			len = (jsonObjColHeaders != null) ? jsonObjColHeaders.length() : 0;
//			String[] colHeaders = new String[len];
//			for (int i = 0; i < jsonObjColHeaders.length(); i++) {
//				colHeaders[i] = jsonObjColHeaders.getString(i);
//			}
//
//			List<Object[]> tblData = JpaTransformer.convert2D(lstTasks);
//
//			TableStructure tblTask = new TableStructure(colWidths, colHeaders, tblData);
//
//			return tblTask;
//		}
//	}
}
