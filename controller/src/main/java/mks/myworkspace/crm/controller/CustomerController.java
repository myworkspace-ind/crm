package mks.myworkspace.crm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Status;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.StatusService;
import mks.myworkspace.crm.service.StorageService;

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
	StorageService storageService;

	@Autowired
	StatusService statusService;

	@RequestMapping(value = { "/customer-list" }, method = RequestMethod.GET)
	public ModelAndView displayCustomerListCRMScreen(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "statusId", required = false) Long statusId, HttpServletRequest request,
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

	@RequestMapping(value = { "/customerDetail" }, method = RequestMethod.GET)
	public ModelAndView displaycustomerDetailScreen(@RequestParam("id") Long customerId, HttpServletRequest request,
			HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("customerDetail");

		initSession(request, httpSession);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		log.debug("Customer Detail is running....");

		Optional<Customer> customerOpt = customerService.findById(customerId);

		// Check if the customer exists and add to model
		customerOpt.ifPresentOrElse(customer -> {
			mav.addObject("customer", customer);
		}, () -> {
			mav.addObject("errorMessage", "Customer not found.");
		});

		return mav;
	}
	

	@RequestMapping(value = { "/add-customer" }, method = RequestMethod.GET)
	public ModelAndView displayAddCustomerScreen(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("createCustomer");
		
		mav.addObject("customer", new Customer());

		initSession(request, httpSession);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		
		//Long newId = customerService.getNextCustomerId(); // Lấy ID tiếp theo
	    //Customer customer = new Customer(); // Tạo đối tượng Customer mới
	    //customer.setId(newId); // Thiết lập ID mới cho khách hàng
	    //mav.addObject("customer", customer); // Thêm khách hàng vào model
		return mav;
	}
	
	@RequestMapping(value = { "/save-customer" }, method = RequestMethod.POST)
	public ModelAndView saveCustomer(@ModelAttribute("customer") Customer customer, HttpServletRequest request, HttpSession httpSession) {
	    // Khởi tạo ModelAndView
	    ModelAndView mav = new ModelAndView();

	    // Lưu khách hàng bằng cách gọi tầng service
	    storageService.saveOrUpdate(customer);

	    // Điều hướng về trang danh sách khách hàng sau khi lưu thành công
	    mav.setViewName("redirect:/customer-list"); // Giả sử bạn có trang danh sách khách hàng tại "/customers"

	    // Cập nhật session và thêm các đối tượng cần thiết
	    initSession(request, httpSession);
	    mav.addObject("currentSiteId", getCurrentSiteId());
	    mav.addObject("userDisplayName", getCurrentUserDisplayName());

	    return mav;
	}
	

	
//	@PostMapping("/save-customer")
//	public ModelAndView addCustomer(@ModelAttribute Customer customer) {
//		try {
//			customerService.createCustomer(customer);
//
//			ModelAndView mav = new ModelAndView("redirect:/customer-list");
//			mav.addObject("successMessage", "Customer has been added successfully!");
//			return mav;
//
//		} catch (Exception e) {
//			ModelAndView mav = new ModelAndView("createCustomer");
//			mav.addObject("errorMessage", "Error occurred while adding the customer.");
//			return mav;
//		}
//	}

}
