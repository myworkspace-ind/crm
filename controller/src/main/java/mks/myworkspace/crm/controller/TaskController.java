package mks.myworkspace.crm.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.dto.TaskDTO;
import mks.myworkspace.crm.entity.dto.TaskWithCustomersDTO;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.TaskService;

@Controller
@Slf4j
@RequestMapping("/task")
public class TaskController extends BaseController{
	@Autowired
	TaskService taskService;
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/list")
	public ModelAndView displayTaskPage(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("taskList_v2");

		initSession(request, httpSession);
		
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		
		//List<Task> taskList = taskService.getAllTasksWithCustomers();
		//List<TaskWithCustomersDTO> taskList = taskService.getTasksWithCustomersAsDTOs();
		
		Map<String, List<TaskWithCustomersDTO>> taskGroups = taskService.getGroupedTasks();
	    mav.addObject("taskGroups", taskGroups);
		//mav.addObject("tasks", taskList);

		return mav;
	}
	
	@GetMapping("/create-task")
	public ModelAndView displayCreateTaskPage(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("createTask");

		initSession(request, httpSession);
		
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		
		List<Customer> listCustomers;
		listCustomers = customerService.getAllCustomers();
		
		mav.addObject("listCustomers", listCustomers);

		return mav;
	}
	
	@PostMapping("/add-task")
	public ResponseEntity<?> addTask(@RequestBody TaskDTO taskDTO) {
	    try {
	        taskService.addTask(taskDTO);
	        return ResponseEntity.ok("Đã thêm công việc thành công.");
	    } catch (IllegalArgumentException e) {
	        // Lỗi từ phía client, ví dụ thiếu tên công việc
	        return ResponseEntity.badRequest().body("Lỗi dữ liệu đầu vào: " + e.getMessage());
	    } catch (Exception e) {
	        // Lỗi hệ thống
	        return ResponseEntity.status(500).body("Lỗi hệ thống khi thêm công việc: " + e.getMessage());
	    }
	}
	
//	@GetMapping("/tasks-response")
//	public ResponseEntity<List<Task>> getAllTasks() {
//	    try {
//	        List<Task> tasks = taskService.getAllTasksWithCustomers();
//	        return ResponseEntity.ok(tasks);
//	    } catch (Exception e) {
//	        return ResponseEntity.status(500).build(); // hoặc thêm body lỗi nếu muốn
//	    }
//	}
	
	@GetMapping("/tasks-response")
	public ResponseEntity<List<TaskWithCustomersDTO>> getAllTasks() {
	    try {
	    	List<TaskWithCustomersDTO> taskList = taskService.getTasksWithCustomersAsDTOs();
	        return ResponseEntity.ok(taskList);
	    } catch (Exception e) {
	        return ResponseEntity.status(500).build(); // hoặc thêm body lỗi nếu muốn
	    }
	}
}
