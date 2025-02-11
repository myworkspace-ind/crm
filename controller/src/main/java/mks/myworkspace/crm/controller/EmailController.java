package mks.myworkspace.crm.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;


import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.service.impl.EmailService;

@Controller
@Slf4j
public class EmailController extends BaseController {
	@Autowired
    private EmailService emailService;
	
	@GetMapping("/trial-registration")
	public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("trialRegistration");

		initSession(request, httpSession);

		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
	
	@PostMapping("/send-email")
	public String sendEmail(
	                        @RequestParam String contactName,
	                        @RequestParam String contactNumber,
	                        @RequestParam String email,
	                        @RequestParam String companyName,
	                        @RequestParam String message,
	                        @RequestParam String address) {
	    try {
	        emailService.sendEmail("crm.mekongsolution@gmail.com", 
	                               "Thông tin đăng ký dùng thử mới từ khách hàng", 
	                                contactName, contactNumber, email, companyName, message, address);
	        return "redirect:/thank-you";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error"; // Redirect đến trang lỗi
	    }
	}
	
	@PostMapping("/send-email-to-customer")
	 public String sendEmail(
							 @RequestParam("to") String to,
				             @RequestParam("subject") String subject,
				             @RequestParam("message") String message,
				             Model model) {
		try {
				emailService.sendEmailToCustomer("crm.mekongsolution@gmail.com",to, subject, message);
				model.addAttribute("success", "Email đã được gửi thành công!");
			} catch (MessagingException e) {
				model.addAttribute("error", "Gửi email thất bại: " + e.getMessage());
			}
		return "customerDetail";//Adjust redirection to page email sent successfully/error
	}
	
	@GetMapping("/error")
	public ModelAndView displayEmailError(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("error");

		initSession(request, httpSession);

		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
	
	@GetMapping("/thank-you")
	public ModelAndView displayEmailThanks(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("thankYou");

		initSession(request, httpSession);

		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
	
}
