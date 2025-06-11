package mks.myworkspace.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.dto.CustomerCareStatsDTO;
import mks.myworkspace.crm.service.CustomerCareDashboardService;

@Controller
@Slf4j
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {
	@Autowired
	CustomerCareDashboardService customerCareDashboardService;
	
	@GetMapping(value = "")
	public ModelAndView displayDashboard (HttpServletRequest httpRequest, HttpSession httpSession ) {
		ModelAndView mav = new ModelAndView("customerCareDashboard_Final");
		CustomerCareStatsDTO stats = customerCareDashboardService.getCustomerCareStatistics();
		
		initSession(httpRequest, httpSession);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		
		mav.addObject("stats", stats);
		return mav;
	}
}
