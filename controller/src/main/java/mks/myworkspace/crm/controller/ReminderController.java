package mks.myworkspace.crm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.ReminderFeatures;
import mks.myworkspace.crm.service.ReminderFeaturesService;
import mks.myworkspace.crm.service.StorageService;

@Controller
@Slf4j
@RequestMapping("/reminder")
public class ReminderController extends BaseController{
	@Autowired
	ReminderFeaturesService reminderFeaturesService;
	
	@Autowired
	StorageService storageService;
	
	
	
	@GetMapping("/list")
	public ModelAndView displayContactPage(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("reminderList");
		initSession(request, httpSession);
		List<ReminderFeatures> reminderFeaturesList = reminderFeaturesService.findAll();
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		mav.addObject("reminderFeatures", reminderFeaturesList);

		return mav;
	}
	
	@PostMapping("/enable/toggle")
	public String toggleFeature(@RequestParam("id") Long id) {
		storageService.toggleById(id);
	    return "redirect:/reminder/list";
	}
}
