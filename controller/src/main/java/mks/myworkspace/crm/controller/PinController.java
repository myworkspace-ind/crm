package mks.myworkspace.crm.controller;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.config.AuthenticationRepository;
import mks.myworkspace.crm.repository.EmployeeRepository;

@Controller
@Slf4j
@RequestMapping("/auth")
public class PinController extends BaseController{
	
	@Autowired
	AuthenticationRepository authenticationRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
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
	
		String currentUsername = null;
		if (authentication != null && authentication.isAuthenticated()) {
		    Object principal = authentication.getPrincipal();
		    if (principal instanceof UserDetails) {
		        UserDetails userDetails = (UserDetails) principal;
		        currentUsername = userDetails.getUsername();
		    } else {
		        currentUsername = principal.toString(); // fallback
		    }
		}

		String hashedPin = BCrypt.hashpw(pin, BCrypt.gensalt());
		authenticationRepository.updatePinCodeHash(currentUsername, hashedPin);

		return "redirect:/"; // hoặc trở lại dashboard
	}
	
	@PostMapping("/verify-pin")
	@ResponseBody
	public ResponseEntity<?> verifyPin(@RequestBody Map<String, String> requestBody) {
	    String pin = requestBody.get("pin");

	    if (pin == null || pin.isBlank()) {
	        return ResponseEntity.badRequest().body("PIN is missing or empty");
	    }

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || !authentication.isAuthenticated()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
	    }

	    String currentUsername = null;
	    Object principal = authentication.getPrincipal();
	    if (principal instanceof UserDetails) {
	        currentUsername = ((UserDetails) principal).getUsername();
	    } else if (principal != null) {
	        currentUsername = principal.toString(); // fallback
	    }

	    log.debug("Current username: {}", currentUsername);

	    if (currentUsername == null || currentUsername.isBlank()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unable to determine username");
	    }

	    Optional<String> storedHashedPinOpt = employeeRepository.findPinCodeHashByUsername(currentUsername);
	    if (storedHashedPinOpt.isEmpty() || storedHashedPinOpt.get().isBlank()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No PIN set for this user");
	    }

	    String storedHashedPin = storedHashedPinOpt.get();

	    try {
	        if (BCrypt.checkpw(pin, storedHashedPin)) {
	            return ResponseEntity.ok().build(); // OK
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect PIN");
	        }
	    } catch (Exception e) {
	        log.error("Error while verifying PIN for user {}: {}", currentUsername, e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("PIN verification failed");
	    }
	}
}
