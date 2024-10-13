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
import mks.myworkspace.crm.entity.Status;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.StatusService;

/**
 * Handles requests for Tasks.
 */
@Controller
@Slf4j
public class CustomerController extends BaseController {
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

	@Autowired
	CustomerService customerService;

	@Autowired
	StatusService statusService;

	@RequestMapping(value = { "/customer", "/customer-list"}, method = RequestMethod.GET)
	public ModelAndView displayCustomerListCRMScreen(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "statusId", required = false) Long statusId,
			HttpServletRequest request,
			HttpSession httpSession) {

		log.debug("Display Cusomter list with keyword= {}", keyword);
		ModelAndView mav = new ModelAndView("customerListCRMScreen");
		initSession(request, httpSession);
		/*
		 * log.debug("Customer List CRM Screen Controller is running....");
		 */
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		List<Customer> customers;

		if (statusId != null) {
			customers = customerService.findCustomersByStatus(statusId);
			mav.addObject("statusId", statusId);
			log.debug("Fetching customers for status ID: {}", statusId);

			
		} else if (keyword != null && !keyword.isEmpty()) {
			customers = customerService.searchCustomers(keyword);
			mav.addObject("keyword", keyword);
			log.debug("Searching customers with keyword: {}", keyword);
			
		} else {
			customers = customerService.getAllCustomersWithStatuses();
			log.debug("No keyword or statusId provided. Fetching all customers.");
		}

		List<Status> statuses = statusService.getAllStatuses();
		mav.addObject("customers", customers);
		mav.addObject("statuses", statuses);

		for (Customer customer : customers) {
			log.debug("Thông tin khách hàng: {}", customer.getName());
		}
		for (Status status : statuses) {
			log.debug("Thông tin trạng thái KH: {}", status.getName());
		}

		for (Customer customer : customers) {
			log.debug("Customer: {} (ID: {})", customer.getName(), customer.getId());

			for (Status status : customer.getStatuses()) {
				log.debug("  - Status: {}", status.getName());
			}

		}

		return mav;
	}

//	@RequestMapping(value = "/customer/status/{statusId}", method = RequestMethod.GET)
//	public ModelAndView getCustomersByStatus(@PathVariable("statusId") Long statusId, HttpServletRequest request, HttpSession httpSession) {
//	    log.debug("Fetching customers for status ID: {}", statusId);
//	    
//	    List<Customer> customers = customerService.findCustomersByStatus(statusId);
//	    
//	    ModelAndView mav = new ModelAndView("customerListCRMScreen");
//	    initSession(request, httpSession);
//	    
//	    // Cung cấp danh sách khách hàng vào model
//	    mav.addObject("customers", customers);
//	    mav.addObject("statuses", statusService.getAllStatuses());
//
//	    return mav;
//	}

}
