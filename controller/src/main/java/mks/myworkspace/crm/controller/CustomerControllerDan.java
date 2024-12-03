package mks.myworkspace.crm.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

/**
 * Handles requests for Tasks.
 */
@Controller
@Slf4j
@RequestMapping("/customer")
public class CustomerControllerDan extends BaseController {
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
	
	@Autowired
	ResponsiblePersonService responsiblePersonService;
	
	@Autowired
	ProfessionService professionService;

	@PostMapping("/create-customer_dan")
	public String createCustomer(@ModelAttribute("customer") Customer customer,
            RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession httpSession) {

		customer.setCreatedAt(new Date());
	    customer.setSiteId(getCurrentSiteId());

	    try {
	        Customer savedCustomer = storageService.saveOrUpdate(customer);
	        redirectAttributes.addFlashAttribute("alertSuccessMessage", "Khách hàng đã được tạo thành công!");
	        redirectAttributes.addFlashAttribute("customer", customer);
	        return "redirect:/customer/add_dan"; // Chuyển hướng thành công
	    } catch (DataAccessException e) {
	        redirectAttributes.addFlashAttribute("alertDataMessage", "Lỗi khi lưu khách hàng vào cơ sở dữ liệu.");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("alertDataMessage", "Vui lòng kiểm tra lại thông tin.");
	    }

	    redirectAttributes.addFlashAttribute("customer", customer); // Lưu lại dữ liệu đã nhập
	    return "redirect:/customer/add_dan"; // Chuyển hướng kèm dữ liệu
	}
	
	@GetMapping("/add_dan")
	public ModelAndView displayAddCustomerScreen(HttpServletRequest request, HttpSession httpSession, Model model) {
		
		//ModelAndView mav = new ModelAndView("addCustomer");
		ModelAndView mav = new ModelAndView("addCustomer_v2");
		
		// Kiểm tra nếu có dữ liệu customer từ FlashAttributes (khi chuyển hướng từ POST)
		Customer customer = (Customer) model.asMap().get("customer");
		Customer customerMap= new Customer();
		
	    if (customer != null) {
	    	customerMap.setCompanyName(customer.getCompanyName());
	    	customerMap.setContactPerson(customer.getContactPerson());
	    	customerMap.setPhone(customer.getPhone());
	    	customerMap.setAddress(customer.getAddress());
	    	customerMap.setEmail(customer.getEmail());
	    	customerMap.setNote(customer.getNote());
//	    	customerMap.setProfession(customer.getProfession());
	    	mav.addObject("professionSelect", customer.getProfession().getId());
	    	
	    }
	    else {
	    	mav.addObject("professionSelect",1);
	    }
	    
		// Thêm đối tượng Customer mới vào Model để truyền vào form
		mav.addObject("customer", customerMap);

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
