package mks.myworkspace.crm.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.ProfessionService;
import mks.myworkspace.crm.service.ResponsiblePersonService;
import mks.myworkspace.crm.service.StatusService;
import mks.myworkspace.crm.service.StorageService;

@Controller
@Slf4j
public class CustomerControllerKhoa extends BaseController {

	@Autowired
	CustomerService customerService;

	@Autowired
	StorageService storageService;

	@Autowired
	StatusService statusService;
	
	@Autowired
	ResponsiblePersonService responsiblePersonService;
	
	@Autowired
	ProfessionService professionService;

	@RequestMapping(value = { "/customerEdit" }, method = RequestMethod.GET)
	public ModelAndView displaycustomerDetailScreen(@RequestParam("id") Long customerId, HttpServletRequest request,
			HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("editCustomerStatus_khoa");

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
}

	
