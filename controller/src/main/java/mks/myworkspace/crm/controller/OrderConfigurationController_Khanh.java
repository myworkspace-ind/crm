
package mks.myworkspace.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles requests for the application home page.
 */
@Controller
@Slf4j

//@RequestMapping("/orders-configuration")
@RequestMapping("/ordersConfigurationCRMScreen_Khanh")
public class OrderConfigurationController_Khanh extends BaseController {

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
		ModelAndView mav = new ModelAndView("ordersConfigurationCRMScreen_Khanh");

		initSession(request, httpSession);
		return mav;
	}
//	
//	@GetMapping("/status")
//	public ModelAndView displayOrderConfigurationStatus(HttpServletRequest request, HttpSession httpSession) {
//		ModelAndView mav = new ModelAndView("ordersConfiguration-StatusCRMScreen");
//
//		initSession(request, httpSession);
//		return mav;
//	}
//
//	@GetMapping("/load-orders")
//	@ResponseBody
//	public Object getOrderConfigurationData() throws IOException {
//		log.debug("Get sample data from configuration file.");
//		int[] colWidths = { 50, 300, 300, };
//		String[] colHeaders = { "No", "Loại đơn hàng", "Ghi chú", };
//		List<Object[]> tblData = new ArrayList<>();
//		Object[] data1 = new Object[] { "1", "Mặc định", "" };
//		Object[] data2 = new Object[] { "2", "Máy móc", "" };
//		Object[] data3 = new Object[] { "3", "Thực phẩm", "" };
//		tblData.add(data1);
//		tblData.add(data2);
//		tblData.add(data3);
//
//		TableStructure tblOrderConfiguration = new TableStructure(colWidths, colHeaders, tblData);
//
//		return tblOrderConfiguration;
//	}
//	
//	@GetMapping("/load-statuses")
//	@ResponseBody
//	public Object getOrderConfigurationStatusData() throws IOException {
//		log.debug("Get sample data from configuration file.");
//		int[] colWidths = { 50, 300, 300, };
//		String[] colHeaders = { "No", "Loại đơn hàng", "Trạng thái", };
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
//
//		TableStructure tblOrderConfigurationStatus = new TableStructure(colWidths, colHeaders, tblData);
//
//		return tblOrderConfigurationStatus;
//	}

}
