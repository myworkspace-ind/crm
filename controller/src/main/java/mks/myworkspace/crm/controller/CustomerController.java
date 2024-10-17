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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	

	@PostMapping(value = "/api/addNewCustomers", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Customer> addNewCustomer(@RequestBody Customer customer) {
	    System.out.println("Received customer data: " + customer);  
	    try {
	        // Lưu khách hàng vào cơ sở dữ liệu
	        Customer savedCustomer = customerService.createCustomer(customer);
	        
	        // Trả về khách hàng đã lưu cùng với mã trạng thái 200 OK
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer); 
	    } catch (Exception e) {
	        e.printStackTrace();  // In ra stack trace để dễ dàng debug
	        // Nếu có lỗi xảy ra, trả về mã trạng thái 500 Internal Server Error
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	
	
	
	/*
	 * @RequestMapping(value = { "/customer" }, method = RequestMethod.GET) public
	 * ModelAndView displayCustomerDetailCRMScreen(@RequestParam(value = "id",
	 * required = false) Long id, HttpServletRequest request, HttpSession
	 * httpSession) {
	 * 
	 * log.debug("CUSTOMER DETAIL IS RUNNING..."); ModelAndView mav = new
	 * ModelAndView("customerListCRMScreen"); initSession(request, httpSession);
	 * 
	 * mav.addObject("currentSiteId", getCurrentSiteId());
	 * mav.addObject("userDisplayName", getCurrentUserDisplayName());
	 * 
	 * Optional<Customer> customerOptional = customerService.findById(id);
	 * mav.addObject("customerDetail", customerOptional);
	 * 
	 * 
	 * return mav; }
	 */

//	@RequestMapping(value = { "/customer/detail" }, method = RequestMethod.GET)
//	public ModelAndView displayCustomerDetailCRMScreen(HttpServletRequest request, HttpSession httpSession) {
//
//	    ModelAndView mav = new ModelAndView("customerDetail");
//	    initSession(request, httpSession);
//
//	    mav.addObject("currentSiteId", getCurrentSiteId());
//	    mav.addObject("userDisplayName", getCurrentUserDisplayName());
//
//	    // Truyền id = 2 vào để lấy thông tin chi tiết của khách hàng có id = 2
//	    Long customerId = 2L;  // Giả sử bạn muốn tìm khách hàng có id = 2
//	    Optional<Customer> customerOptional = customerService.findById(customerId);
//
//	    if (customerOptional.isPresent()) {
//	        Customer customer = customerOptional.get();
//	        mav.addObject("customer", customer);
//	        log.debug("Customer details: {}", customer.getName());
//	        // Bạn có thể thêm customer vào ModelAndView để hiển thị trong view
//	    } else {
//	        log.debug("Customer with id {} not found.", customerId);
//	        mav.addObject("message", "Customer not found");
//	    }
//
//	    return mav;
//	}

}
