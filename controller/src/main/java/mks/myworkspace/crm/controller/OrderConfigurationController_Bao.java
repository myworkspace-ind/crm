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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.common.model.TableStructure;

/**
 * Handles requests for the application home page.
 */
@Controller
@Slf4j
//@RequestMapping("/orders-configuration")
@RequestMapping("/ordersConfigurationCRMOrderType_Bao")
public class OrderConfigurationController_Bao extends BaseController {

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
		ModelAndView mav = new ModelAndView("ordersConfigurationCRMOrderType_Bao");

		initSession(request, httpSession);
		return mav;
	}
	
	@GetMapping("/status")
	public ModelAndView displayOrderConfigurationStatus(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("ordersConfiguration-StatusCRMScreen");

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
		Object[] data1 = new Object[] { "1", "Mặc định", "Loại đơn hàng mặc định ban đầu." };
		Object[] data2 = new Object[] { "2", "Máy móc", "Đơn hàng liên quan đến các thiết bị máy móc." };
		Object[] data3 = new Object[] { "3", "Thực phẩm", "Đơn hàng về thực phẩm tươi hoặc đông lạnh." };
		Object[] data4 = new Object[] { "4", "Mỹ phẩm", "Sản phẩm chăm sóc sắc đẹp và làm đẹp." };
		Object[] data5 = new Object[] { "5", "Điện tử", "Đơn hàng liên quan đến các thiết bị điện tử." };
		Object[] data6 = new Object[] { "6", "Hàng gia dụng", "Đơn hàng về đồ dùng gia đình như bếp, tủ lạnh." };
		Object[] data7 = new Object[] { "7", "Dược phẩm", "Thuốc và sản phẩm y tế." };
		Object[] data8 = new Object[] { "8", "Thời trang", "Đơn hàng quần áo, giày dép và phụ kiện." };
		Object[] data9 = new Object[] { "9", "Sách vở", "Đơn hàng tài liệu học tập, sách, báo." };
		Object[] data10 = new Object[] { "10", "Văn phòng phẩm", "Đồ dùng văn phòng như bút, sổ, giấy." };
		Object[] data11 = new Object[] { "11", "Đồ chơi", "Đơn hàng đồ chơi trẻ em." };
		Object[] data12 = new Object[] { "12", "Đồ thể thao", "Dụng cụ và trang phục thể thao." };
		Object[] data13 = new Object[] { "13", "Hóa chất", "Các sản phẩm hóa chất, chất tẩy rửa." };
		Object[] data14 = new Object[] { "14", "Xe cộ", "Phương tiện vận tải như xe đạp, xe máy." };
		Object[] data15 = new Object[] { "15", "Đồ nội thất", "Bàn, ghế, tủ, giường và đồ nội thất khác." };
		Object[] data16 = new Object[] { "16", "Nguyên liệu xây dựng", "Xi măng, sắt thép, gạch." };
		Object[] data17 = new Object[] { "17", "Sản phẩm nghệ thuật", "Tranh vẽ, tượng điêu khắc." };
		Object[] data18 = new Object[] { "18", "Thiết bị công nghiệp", "Máy móc công nghiệp lớn." };
		Object[] data19 = new Object[] { "19", "Sản phẩm nông nghiệp", "Hoa quả, rau củ, lúa gạo." };
		Object[] data20 = new Object[] { "20", "Thực phẩm chức năng", "Sản phẩm bổ sung dinh dưỡng hoặc hỗ trợ sức khỏe." };

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
		tblData.add(data14);
		tblData.add(data15);
		tblData.add(data16);
		tblData.add(data17);
		tblData.add(data18);
		tblData.add(data19);
		tblData.add(data20);




		TableStructure tblOrderConfiguration = new TableStructure(colWidths, colHeaders, tblData);

		return tblOrderConfiguration;
	}
	
	@GetMapping("/load-statuses")
	@ResponseBody
	public Object getOrderConfigurationStatusData() throws IOException {
		log.debug("Get sample data from configuration file.");
		int[] colWidths = { 50, 300, 300, };
		String[] colHeaders = { "No", "Loại đơn hàng", "Trạng thái", };
		List<Object[]> tblData = new ArrayList<>();
		Object[] data1 = new Object[] { "1", "Mặc định", "Nhận đơn" };
		Object[] data2 = new Object[] { "", "", "Đóng gói" };
		Object[] data3 = new Object[] { "", "", "Vận chuyển" };
		Object[] data4 = new Object[] { "", "", "Giao hàng" };
		
		Object[] data5 = new Object[] { "2", "Máy móc", "Nhận đơn" };
		Object[] data6 = new Object[] { "", "", "Đóng gói" };
		Object[] data7 = new Object[] { "", "", "Vận chuyển" };
		Object[] data8 = new Object[] { "", "", "Lưu kho" };
		Object[] data9 = new Object[] { "", "", "Giao hàng" };
		
		Object[] data10 = new Object[] { "3", "Thực phẩm", "Nhận đơn" };
		Object[] data11 = new Object[] { "", "", "Đóng gói" };
		Object[] data12 = new Object[] { "", "", "Vận chuyển" };
		Object[] data13 = new Object[] { "", "", "Lưu kho lạnh" };
		Object[] data14 = new Object[] { "", "", "Giao hàng" };
		
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
		tblData.add(data14);

		TableStructure tblOrderConfigurationStatus = new TableStructure(colWidths, colHeaders, tblData);

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
