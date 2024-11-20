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
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.ProfessionService;
import mks.myworkspace.crm.service.ResponsiblePersonService;
import mks.myworkspace.crm.service.StatusService;
import mks.myworkspace.crm.service.StorageService;

@Controller
@Slf4j
public class CustomerControllerDan extends BaseController {

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

	@RequestMapping(value = { "/add-customer-dan" }, method = RequestMethod.GET)
	public ModelAndView displayAddCustomerScreen(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("addCustomer_dan");

		// Thêm đối tượng Customer mới vào Model để truyền vào form
		mav.addObject("customer", new Customer());

		// Lấy danh sách Status để đổ vào các dropdown chọn trạng thái
		List<Status> statuses = statusService.getAllStatuses();
		mav.addObject("statuses", statuses);
		
		List<ResponsiblePerson> responsiblePersons = responsiblePersonService.getAllResponsiblePersons();
		mav.addObject("responsiblePersons", responsiblePersons);
		
		List<Profession> professions = professionService.getAllProfessions();
		mav.addObject("professions", professions);

		// Thiết lập các thuộc tính của session
		initSession(request, httpSession);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
}

	
