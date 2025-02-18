//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.InitBinder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import lombok.extern.slf4j.Slf4j;
//import mks.myworkspace.crm.controller.BaseController;
//import mks.myworkspace.crm.entity.Customer;
//import mks.myworkspace.crm.entity.EmailToCustomer;
//import mks.myworkspace.crm.repository.CustomerRepository;
//import mks.myworkspace.crm.service.StorageService;
//import mks.myworkspace.crm.service.impl.EmailService;
//
//@RestController
//@Slf4j
//@RequestMapping(value = "/email-to-customer", produces = "application/json")
//public class EmailToCustomerController extends BaseController {
//	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//
//	}
//	
//	@Autowired
//	StorageService storageService;
//	
//	@Autowired
//	EmailService emailService;
//	
//	@Autowired
//	private CustomerRepository customerRepository;
//	
//	@PostMapping("/send")
//	public ResponseEntity<?> sendEmail(@RequestParam("to") String to,
//	                                   @RequestParam("subject") String subject,
//	                                   @RequestParam("message") String content,
//	                                   @RequestParam("customerId") Long customerId,
//	                                   Authentication authentication) {
//	    try {
//	        String loggedInUserEmail = authentication.getName();
//	        log.info("Sender email: {}", loggedInUserEmail);
//	        
//	        
//	        Customer customer = customerRepository.findById(customerId).orElse(null);
//	        if (customer == null) {
//	            return ResponseEntity.badRequest().body("Không tìm thấy khách hàng!");
//	        }
//	        
//	        EmailToCustomer email = new EmailToCustomer();
//	        email.setSender(loggedInUserEmail);
//	        email.setCustomer(customer);
//	        email.setSubject(subject);
//	        email.setContent(content);
//	        email.setStatus(EmailToCustomer.EmailStatus.SENT);
//	        
//	        emailService.sendEmailToCustomer(email);
//	        
//	        return ResponseEntity.ok("Gửi email thành công!");
//	    } catch (Exception e) {
//	        return ResponseEntity.status(500).body("Gửi email thất bại!");
//	    }
//}
//
//}
