package mks.myworkspace.crm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Interaction;
import mks.myworkspace.crm.entity.dto.TaskDTO;
import mks.myworkspace.crm.entity.dto.TaskWithCustomersDTO;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.StorageService;
import mks.myworkspace.crm.service.TaskService;

@Controller
@Slf4j
@RequestMapping("/task")
public class TaskController extends BaseController{
	@Autowired
	TaskService taskService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	StorageService storageService;
	
	@GetMapping("/task-notifications")
	@ResponseBody
	public Map<String, Object> getAllTaskNotifications() {
	    List<String> notifications = taskService.getAllTaskNotifications();
	    Map<String, Object> response = new HashMap<>();
	    response.put("count", notifications.size());
	    response.put("notifications", notifications);
	    return response;
	}
	
	@PostMapping("/update-task")
	@ResponseBody
	public ResponseEntity<String> updateTask(@RequestBody TaskDTO taskDTO) {
	    try {
	        storageService.updateTask(taskDTO);
	        return ResponseEntity
	                .ok()
	                .contentType(MediaType.valueOf("text/plain; charset=UTF-8"))
	                .body("Cập nhật công việc thành công.");
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity
	                .badRequest()
	                .contentType(MediaType.valueOf("text/plain; charset=UTF-8"))
	                .body("Lỗi dữ liệu: " + e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity
	                .status(500)
	                .contentType(MediaType.valueOf("text/plain; charset=UTF-8"))
	                .body("Lỗi hệ thống: " + e.getMessage());
	    }
	}

	
	@DeleteMapping("/{taskId}/delete")
	@ResponseBody
	public ResponseEntity<String> deleteTask(@PathVariable("taskId") Long taskId, HttpServletRequest request, HttpSession session) {
	    try {
	        storageService.deleteTaskById(taskId);
	        return ResponseEntity.ok()
	                .contentType(MediaType.valueOf("text/plain; charset=UTF-8"))
	                .body("Đã xóa 1 công việc thành công.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed");
	    }
	}
	
	@PostMapping("{taskId}/toggle-complete")
	public String toggleTaskComplete(@PathVariable("taskId") Long taskId, HttpServletRequest request, HttpSession session) {
	    storageService.toggleTaskStatus(taskId);
	    return "redirect:/task/list";
	}
	
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
	    
	    List<Customer> listCustomers;
		listCustomers = customerService.getAllCustomers();
		
		mav.addObject("listCustomers", listCustomers);
	    
	 // Tạo map: taskId -> customersJson
		ObjectMapper mapper = new ObjectMapper();
		Map<Long, String> taskCustomersMap = new HashMap<>();
		for (List<TaskWithCustomersDTO> groupList : taskGroups.values()) {
		    for (TaskWithCustomersDTO task : groupList) {
		        try {
		            String json = mapper.writeValueAsString(task.getCustomers() != null ? task.getCustomers() : new ArrayList<>());
		            taskCustomersMap.put(task.getTaskId(), json);
		        } catch (Exception e) {
		            taskCustomersMap.put(task.getTaskId(), "[]");
		        }
		    }
		}
		mav.addObject("taskCustomersMap", taskCustomersMap);
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
	
	@GetMapping("/interactions/{customerId}")
	@ResponseBody
	public List<Map<String, Object>> getInteractionsByCustomer(@PathVariable Long customerId){
		List<Interaction> interactions = customerService.getAllCustomerInteraction(customerId);
		
		return interactions.stream().map(i -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", i.getId());
			String content = i.getContent();
			if(content != null && content.split("\\s+").length > 10) {
				content = String.join(" ", Arrays.copyOf(content.split("\\s+"), 10) + "...");
			}
			map.put("content", content);
			map.put("createdAt", i.getCreatedAt() != null ? i.getCreatedAt().toString() : "");
			return map;
		}).collect(Collectors.toList());
	}
	
	@PostMapping("/add-task")
	public ResponseEntity<String> addTask(@RequestBody TaskDTO taskDTO) {
	    try {
	        taskService.addTask(taskDTO);
	        // Trả về với UTF-8
	        return ResponseEntity
	                .ok()
	                .contentType(MediaType.valueOf("text/plain; charset=UTF-8"))
	                .body("Đã thêm công việc thành công.");
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity
	                .badRequest()
	                .contentType(MediaType.valueOf("text/plain; charset=UTF-8"))
	                .body("Lỗi dữ liệu đầu vào: " + e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity
	                .status(500)
	                .contentType(MediaType.valueOf("text/plain; charset=UTF-8"))
	                .body("Lỗi hệ thống khi thêm công việc: " + e.getMessage());
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
	public ResponseEntity<Map<String, List<TaskWithCustomersDTO>>> getAllTasks() {
	    try {
	    	//List<TaskWithCustomersDTO> taskList = taskService.getTasksWithCustomersAsDTOs();
			Map<String, List<TaskWithCustomersDTO>> taskGroups = taskService.getGroupedTasks();	    
			 // Tạo map: taskId -> customersJson
				ObjectMapper mapper = new ObjectMapper();
				Map<Long, String> taskCustomersMap = new HashMap<>();
				for (List<TaskWithCustomersDTO> groupList : taskGroups.values()) {
				    for (TaskWithCustomersDTO task : groupList) {
				        try {
				            String json = mapper.writeValueAsString(task.getCustomers() != null ? task.getCustomers() : new ArrayList<>());
				            taskCustomersMap.put(task.getTaskId(), json);
				        } catch (Exception e) {
				            taskCustomersMap.put(task.getTaskId(), "[]");
				        }
				    }
				}
			return ResponseEntity.ok(taskGroups);
	        //return ResponseEntity.ok(taskList);
	    } catch (Exception e) {
	        return ResponseEntity.status(500).build(); // hoặc thêm body lỗi nếu muốn
	    }
	}
}
