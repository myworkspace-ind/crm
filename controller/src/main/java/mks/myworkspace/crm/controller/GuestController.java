package mks.myworkspace.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class GuestController extends BaseController {
	@GetMapping("/guest")
	public ModelAndView displayGuestScreen(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("guestCRMScreen");
		
		initSession(request, httpSession);
		
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		
		return mav;
	}
	
}
