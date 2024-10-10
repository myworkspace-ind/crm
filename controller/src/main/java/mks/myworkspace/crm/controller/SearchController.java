package mks.myworkspace.crm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.StatusService;

/**
 * Handles requests for Tasks.
 */
@Controller
@Slf4j
public class SearchController extends BaseController {
	/**
	 * This method is called when binding the HTTP parameter to bean (or model).
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// Sample init of Custom Editor

//     Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//     PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//     binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

	}

	/**
	 * Simply selects the home view to render by returning its name.
	 * 
	 * @return
	 */

	/*
	 * @Autowired CustomerService customerService;
	 * 
	 * @Autowired StatusService statusService;
	 * 
	 * @RequestMapping(value = {"/customer-list"}, method = RequestMethod.GET)
	 * public ModelAndView searchCustomers(@RequestParam(value = "keyword", required
	 * = false) String keyword, HttpServletRequest request, HttpSession httpSession)
	 * { ModelAndView mav = new ModelAndView("customerListCRMScreen");
	 * initSession(request, httpSession);
	 * 
	 * mav.addObject("currentSiteId", getCurrentSiteId());
	 * mav.addObject("userDisplayName", getCurrentUserDisplayName());
	 * 
	 * // Tìm kiếm khách hàng theo từ khóa List<Customer> customers =
	 * customerService.searchCustomers(keyword);
	 * 
	 * // Thêm vào ModelAndView mav.addObject("customers", customers);
	 * mav.addObject("searchKeyword", keyword); // Lưu từ khóa tìm kiếm để hiển thị
	 * nếu cần
	 * 
	 * for (Customer customer : customers) {
	 * log.debug("Thông tin khách hàng tìm thấy: {}", customer); }
	 * 
	 * if (customers.isEmpty()) {
	 * log.debug("Không tìm thấy khách hàng nào với từ khóa: {}", keyword); }
	 * 
	 * return mav; }
	 */
	
	/*
	 * @GetMapping("/search/customers") public void searchCustomers(@RequestParam
	 * String keyword) { List<Customer> customers =
	 * customerService.searchCustomers(keyword);
	 * 
	 * log.debug("Tìm kiếm khách hàng với tên: {}", keyword);
	 * 
	 * if (customers.isEmpty()) {
	 * log.debug("Không tìm thấy khách hàng nào với tên: {}", keyword); } else { for
	 * (Customer customer : customers) {
	 * log.debug("Thông tin khách hàng tìm thấy: {}", customer); } } }
	 */


}
