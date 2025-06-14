package mks.myworkspace.crm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import mks.myworkspace.crm.service.CustomerCareService;
import mks.myworkspace.crm.service.StorageService;
import mks.myworkspace.crm.service.TaskService;

@ControllerAdvice
public class GlobalModelAttributes {
	@Autowired
    private TaskService taskService;

    @Autowired
    private StorageService storageService;
    
    @Autowired
    private CustomerCareService customerCareService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request, HttpSession session) {
        
    	// Reminders for tasks
    	List<String> notifications = taskService.getAllTaskNotifications();
        model.addAttribute("notifications", notifications);
        model.addAttribute("taskNotificationCount", notifications.size());
        model.addAttribute("hasTaskNotification", !notifications.isEmpty());
        
        //Reminder for customer care
        List<String> customerCareNotifications = customerCareService.getCustomerCareNotifications();
        model.addAttribute("customerCareNotifications", customerCareNotifications);
        model.addAttribute("customerCareNotificationCount", customerCareNotifications.size());
        model.addAttribute("hasCustomerCareNotification", !customerCareNotifications.isEmpty());
        //Boolean toggleCCEnabled = storageService.isFeatureEnabledByCode("CC_REMINDER");
        //model.addAttribute("toggleCCEnabled", toggleCCEnabled);

        // Nếu cần hiển thị tên người dùng:
        //model.addAttribute("userDisplayName", getCurrentUserDisplayName());
    }

//    private String getCurrentUserDisplayName() {
//        return "Tên người dùng";
//    }
}
