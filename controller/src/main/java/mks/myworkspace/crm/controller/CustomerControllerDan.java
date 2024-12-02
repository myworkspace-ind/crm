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
            RedirectAttributes redirectAttributes) {
//	    try {
	    	
	        customer.setCreatedAt(new Date());
	        customer.setSiteId(getCurrentSiteId());

//	        boolean hasError = false;

//	        // Kiểm tra các trường bắt buộc từ phía client
//	        if (customer.getCompanyName() == null || customer.getCompanyName().isEmpty()) {
//	            hasError = true;
//	            redirectAttributes.addFlashAttribute("alertFailMessage", "Tên công ty không được để trống");
//	            return "redirect:/customer/add";
//	        }
//	        if (customer.getContactPerson() == null || customer.getContactPerson().isEmpty()) {
//	            hasError = true;
//	            redirectAttributes.addFlashAttribute("alertFailMessage", "Người liên hệ chính không được để trống");
//	            return "redirect:/customer/add";
//	        }
//	        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
//	            hasError = true;
//	            redirectAttributes.addFlashAttribute("alertFailMessage", "Email không được để trống");
//	            return "redirect:/customer/add";
//	        }
//	        if (customer.getPhone() == null || customer.getPhone().isEmpty()) {
//	            hasError = true;
//	            redirectAttributes.addFlashAttribute("alertFailMessage", "Số điện thoại không được để trống");
//	            return "redirect:/customer/add";
//	        }
//	        if (customer.getAddress() == null || customer.getAddress().isEmpty()) {
//	            hasError = true;
//	            redirectAttributes.addFlashAttribute("alertFailMessage", "Địa chỉ không được để trống");
//	            return "redirect:/customer/add";
//	        }
//	        if (customer.getProfession() == null || customer.getProfession().getId() == null) {
//	            hasError = true;
//	            redirectAttributes.addFlashAttribute("alertFailMessage", "Ngành nghề không được để trống");
//	            return "redirect:/customer/add";
//	        }
//	        if (customer.getMainStatus() == null || customer.getMainStatus().getId() == null) {
//	            hasError = true;
//	            redirectAttributes.addFlashAttribute("alertFailMessage", "Trạng thái chính không được để trống");
//	            return "redirect:/customer/add";
//	        }
//	        if (customer.getSubStatus() == null || customer.getSubStatus().getId() == null) {
//	            hasError = true;
//	            redirectAttributes.addFlashAttribute("alertFailMessage", "Trạng thái phụ không được để trống");
//	            return "redirect:/customer/add";
//	        }
//	        if (customer.getResponsiblePerson() == null || customer.getResponsiblePerson().getId() == null) {
//	            hasError = true;
//	            redirectAttributes.addFlashAttribute("alertFailMessage", "Người phụ trách không được để trống");
//	            return "redirect:/customer/add";
//	        }

        
	        // Lưu khách hàng
	        try {
	        	Customer savedCustomer = storageService.saveOrUpdate(customer);
	        	redirectAttributes.addFlashAttribute("alertSuccessMessage", "Khách hàng đã được tạo thành công!");
	        	return "redirect:/customer/add";
	        }
	        catch (DataAccessException e) {
        // Nếu có lỗi trong quá trình lưu (lỗi database)
	        	redirectAttributes.addFlashAttribute("alertDataMessage", "Lỗi");
	        	return "redirect:/customer/add"; // Redirect đến trang thêm khách hàng mới với thông báo lỗi
	        } catch (Exception e) {
        // Nếu có bất kỳ lỗi nào khác
	        	redirectAttributes.addFlashAttribute("alertDataMessage", "Lỗi");
	        return "redirect:/customer/add"; // Redirect đến trang thêm khách hàng mới với thông báo lỗi chung
	        }

	}
}
