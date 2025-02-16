import java.security.Principal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.controller.BaseController;
import mks.myworkspace.crm.entity.EmailToCustomer;
import mks.myworkspace.crm.entity.EmailToCustomer.EmailStatus;
import mks.myworkspace.crm.service.StorageService;

@Controller
@Slf4j
@RequestMapping("email-to-customer")
public class EmailToCustomerController extends BaseController {
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// Sample init of Custom Editor

//     Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//     PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//     binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

	}
	
	@Autowired
	StorageService storageService;
	
	@PostMapping("/send")
	@ResponseBody
	public ResponseEntity<?> sendEmail(@RequestBody EmailToCustomer emailToCustomer, Principal principal) {
	    try {
	        emailToCustomer.setSendDate(new Date());
	        emailToCustomer.setSender(principal.getName()); // Lấy email người gửi từ Spring Security
	        emailToCustomer.setSiteId(getCurrentSiteId());
	        emailToCustomer.setStatus(EmailStatus.SENT);

	        EmailToCustomer savedEmailToCustomer = storageService.saveEmailToCustomer(emailToCustomer);

	        return ResponseEntity.ok().body(Map.of("message", "Email đã được gửi thành công!", "customer", savedEmailToCustomer));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("errorMessage", "Có lỗi xảy ra khi gửi email. Vui lòng thử lại sau!"));
	    }
	}

}
