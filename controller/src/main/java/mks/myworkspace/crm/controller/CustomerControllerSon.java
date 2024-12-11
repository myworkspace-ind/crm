package mks.myworkspace.crm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import mks.myworkspace.crm.entity.Profession;
import mks.myworkspace.crm.entity.ResponsiblePerson;
import mks.myworkspace.crm.entity.Status;
import mks.myworkspace.crm.service.CustomerService_Son;
import mks.myworkspace.crm.service.ProfessionService;
import mks.myworkspace.crm.service.ResponsiblePersonService;
import mks.myworkspace.crm.service.StatusService;
import mks.myworkspace.crm.service.StorageService;

@Controller
@Slf4j
public class CustomerControllerSon extends BaseController {

	@Autowired
	CustomerService_Son customerService;

	@Autowired
	StorageService storageService;

	@Autowired
	StatusService statusService;

	@Autowired
	ResponsiblePersonService responsiblePersonService;

	@Autowired
	ProfessionService professionService;

	@RequestMapping(value = { "/customer-list-son" }, method = RequestMethod.GET)
	public ModelAndView displayCustomerListCRMScreen(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "statusId", required = false) Long statusId, HttpServletRequest request,
			HttpSession httpSession) {

		log.debug("Display Cusomter list with keyword= {}", keyword);
		ModelAndView mav = new ModelAndView("customer_list_son");
		initSession(request, httpSession);

		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		List<Customer> customers;

		if (statusId != null) {
			customers = customerService.findCustomersByStatus(statusId);
			mav.addObject("statusId", statusId);

		} else if (keyword != null && !keyword.isEmpty()) {
			customers = customerService.searchCustomers(keyword);
			mav.addObject("keyword", keyword);

		} else {
			customers = customerService.getAllCustomersWithStatuses();
			log.debug("No keyword or statusId provided. Fetching all customers.");
		}

		List<Status> statuses = statusService.getAllStatuses();
		List<ResponsiblePerson> responsiblePersons = responsiblePersonService.getAllResponsiblePersons();
		List<Profession> professions = professionService.getAllProfessions();

		Map<Long, Long> statusCounts = customerService.getCustomerCountsByStatus();

		if (statusCounts == null) {
			statusCounts = new HashMap<>();
		}

		long totalCustomerCount = customerService.getTotalCustomerCount();

		mav.addObject("customers", customers);
		mav.addObject("statuses", statuses);
		mav.addObject("responsiblePersons", responsiblePersons);
		mav.addObject("professions", professions);
		mav.addObject("statusCounts", statusCounts);
		mav.addObject("totalCustomerCount", totalCustomerCount);

		return mav;
	}

	@RequestMapping(value = { "/customer-list-search-son" }, method = RequestMethod.GET)
	public ModelAndView displayCustomerListCRMSearch(
			@RequestParam(value = "nameCompany", required = false) String nameCompany,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "selectedCareers", required = false) List<Long> selectedCareers,
			@RequestParam(value = "contactPerson", required = false) String contactPerson,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "email", required = false) String email, HttpServletRequest request,
			HttpSession httpSession) {

		ModelAndView mav = new ModelAndView("customer_list_son");
		initSession(request, httpSession);

		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		List<Customer> customers;

		log.debug("Selected Careers: {}", selectedCareers);


		if ((nameCompany == null || nameCompany.isEmpty()) && (phone == null || phone.isEmpty())
				&& (selectedCareers == null || selectedCareers.isEmpty())
				&& (contactPerson == null || contactPerson.isEmpty()) && (address == null || address.isEmpty())
				&& (email == null || email.isEmpty())) {
			customers = customerService.getAllCustomersWithStatuses();
			log.debug("No keyword or field provided. Fetching all customers.");
		} else if(selectedCareers == null || selectedCareers.isEmpty()){
			customers = customerService.advancedSearchCustomersNotCareer(nameCompany,phone,selectedCareers,contactPerson, address, email);
			
		}
		
		else {
			customers = customerService.findCustomersAdvanced(nameCompany,phone,selectedCareers,contactPerson, address, email);

			mav.addObject("nameCompany", nameCompany);
			mav.addObject("phone", phone);
			mav.addObject("selectedCareers", selectedCareers);
			mav.addObject("contactPerson", contactPerson);
			mav.addObject("address", address);
			mav.addObject("email", email);
		}

		List<Status> statuses = statusService.getAllStatuses();
		List<ResponsiblePerson> responsiblePersons = responsiblePersonService.getAllResponsiblePersons();
		List<Profession> professions = professionService.getAllProfessions();

		Map<Long, Long> statusCounts = customerService.getCustomerCountsByStatus();

		if (statusCounts == null) {
			statusCounts = new HashMap<>();
		}
		long totalCustomerCount = customerService.getTotalCustomerCount();

		mav.addObject("customers", customers);
		mav.addObject("statuses", statuses);
		mav.addObject("responsiblePersons", responsiblePersons);
		mav.addObject("professions", professions);
		mav.addObject("statusCounts", statusCounts);
		mav.addObject("totalCustomerCount", totalCustomerCount);

		return mav;
	}
}
