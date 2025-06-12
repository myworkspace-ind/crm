package mks.myworkspace.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.repository.AuthenticationRepository;

@Controller
@Slf4j
@RequestMapping("/auth")
public class PinController extends BaseController{
	
	@Autowired
	AuthenticationRepository authenticationRepository;
	
	@GetMapping("/register-pin")
	public ModelAndView showPinForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("pinRegister");
        
        mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		
        return mav;
    }
	
	@PostMapping("/register-pin")
	public String submitPin(@RequestParam("pin") String pin) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		String hashedPin = BCrypt.hashpw(pin, BCrypt.gensalt());
		authenticationRepository.updatePinCodeHash(username, hashedPin);

		return "redirect:/"; // hoặc trở lại dashboard
	}
}
