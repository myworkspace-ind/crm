/**
 * Licensed to MKS Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * MKS Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package mks.myworkspace.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles requests for the application home page.
 */
@Slf4j
@Controller
public class OldVersion_CustomerListController extends BaseController {
	// Tạo logger cho class này 
	   /**
     * This method is called when binding the HTTP parameter to bean (or model).
     * 
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Sample init of Custom Editor

//        Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//        PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//        binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

    }
    
	/**
	 * Simply selects the home view to render by returning its name.
     * @return 
	 */
	@GetMapping("/new")
	public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("new");

		initSession(request, httpSession);
		log.debug("Customer List Controller is running....");
		
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
    
    @GetMapping("/homeCRMScreen")
	public ModelAndView displayHeaderCRMCustomerList(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("homeCRMScreen");

		initSession(request, httpSession);
		log.debug("Home CRM Screen Controller is running....");
		
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
        
    
    @GetMapping("/kpiCRMScreen")
	public ModelAndView displayKPIScreen(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("kpiCRMScreen");

		initSession(request, httpSession);
		log.debug("KPI CRM Controller is running....");
		
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
    
    @GetMapping("/workCRMScreen")
	public ModelAndView displayWorkScreen(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("workCRMScreen");

		initSession(request, httpSession);
		log.debug("Work CRM Controller is running....");
		
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
    
    @GetMapping("/takeCareCustomerCRMScreen")
	public ModelAndView displayTakeCareCustomerScreen(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("takeCareCustomerCRMScreen");

		initSession(request, httpSession);
		log.debug("Take Care Customer Controller is running....");
		
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
    
    @GetMapping("/reportCRMScreen")
   	public ModelAndView displayReportScreen(HttpServletRequest request, HttpSession httpSession) {
   		ModelAndView mav = new ModelAndView("reportCRMScreen");

   		initSession(request, httpSession);
   		log.debug("Report Controller is running....");
   		
   		mav.addObject("currentSiteId", getCurrentSiteId());
   		mav.addObject("userDisplayName", getCurrentUserDisplayName());

   		return mav;
   	}
    
   
}
