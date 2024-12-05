package mks.myworkspace.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/ordersConfigurationCRMStatus_Toan")
public class OrderConfigurationControllerToan extends BaseController {
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {

	}
	@GetMapping("/status")
	public ModelAndView displayOrderConfiguration(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("ordersConfigurationCRMStatus_Toan");

		initSession(request, httpSession);
		return mav;
	}
}
