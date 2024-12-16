package mks.myworkspace.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

//@Controller
//public class ContactController {
//	@GetMapping("/contact")
//    public String showContactPage() {
//        return "contact";
//    }
//}

@Controller
@Slf4j	
public class ContactController extends BaseController {
	/**
	 * Simply selects the home view to render by returning its name.
     * @return Intro view 
	 */
	@GetMapping("/contact")
	public ModelAndView displayContactPage(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("contact");

		initSession(request, httpSession);
		
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
}