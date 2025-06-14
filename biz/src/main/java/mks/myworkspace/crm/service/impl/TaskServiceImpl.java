package mks.myworkspace.crm.service.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Task;
import mks.myworkspace.crm.entity.dto.CustomerSimpleForTaskDTO;
import mks.myworkspace.crm.entity.dto.TaskDTO;
import mks.myworkspace.crm.entity.dto.TaskWithCustomersDTO;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.TaskRepository;
import mks.myworkspace.crm.service.TaskService;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	TaskRepository repo;
	
	@Autowired
	AppRepository appRepository;

	@Override
	public void addTask(TaskDTO dto) {
		appRepository.addTask(dto);
	}

	@Override
	public List<Task> getAllTasksWithCustomers() {
		return repo.findAllWithCustomers();
	}
	

	@Override
	public List<String> getAllTaskNotifications() {
		List<Task> tasks = repo.findAllTaskNotCompletedWithCustomers();
	    LocalDateTime now = LocalDateTime.now();
	    List<String> notifications = new ArrayList<>();

	    for (Task task : tasks) {
	        if (task == null) continue;//Chi hien thi task chua hoan thanh

	        // Remind Date đã đến
	        if (task.getRemind_date() != null && !task.getRemind_date().isAfter(now)) {
	            notifications.add("- Bạn có công việc \"" + task.getName() + "\" cần hoàn thành.");
	        }

	        // Sắp đến Start Date
	        if (task.getStart_date() != null) {
	            long daysToStart = ChronoUnit.DAYS.between(now.toLocalDate(), task.getStart_date().toLocalDate());
	            if (daysToStart == 2 || daysToStart == 1) {
	                notifications.add("- Bạn có công việc \"" + task.getName() + "\" sắp bắt đầu.");
	            } else if (daysToStart == 0) {
	                notifications.add("- Bạn có công việc \"" + task.getName() + "\" bắt đầu.");
	            }
	        }

	        // Sắp đến Due Date hoặc quá hạn
	        if (task.getDue_date() != null) {
	            long daysToDue = ChronoUnit.DAYS.between(now.toLocalDate(), task.getDue_date().toLocalDate());
	            if (daysToDue == 2 || daysToDue == 1) {
	                notifications.add("- Bạn có công việc \"" + task.getName() + "\" sắp hết hạn.");
	            } else if (daysToDue <= 0) {
	                notifications.add("- Bạn có công việc \"" + task.getName() + "\" bị quá hạn.");
	            }
	        }
	    }

	    return notifications;
		
	}
	
	@Override
	public List<TaskWithCustomersDTO> getTasksWithCustomersAsDTOs() {
		List<Object[]> rawData = repo.fetchTaskWithCustomersRaw();
	    Map<Long, TaskWithCustomersDTO> taskMap = new LinkedHashMap<>();

	    for (Object[] row : rawData) {
	        Long taskId = ((Number) row[0]).longValue();

	        TaskWithCustomersDTO task = taskMap.computeIfAbsent(taskId, id -> {
	            TaskWithCustomersDTO dto = new TaskWithCustomersDTO();
	            dto.setTaskId(taskId);
	            dto.setTaskName(handleNull(row[1]));
	            dto.setTaskDescription(handleNull(row[2]));
	            dto.setTaskStartDate(row[3] != null ? ((Timestamp) row[3]).toLocalDateTime() : null);
	            dto.setTaskDueDate(row[4] != null ? ((Timestamp) row[4]).toLocalDateTime() : null);
	            dto.setTaskRemindDate(row[5] != null ? ((Timestamp) row[5]).toLocalDateTime() : null);
	            //dto.setRemind(row[6] != null && ((Boolean) row[6]));
	            dto.setStatus(row[6] != null && ((Boolean) row[6]));
	            dto.setImportant(row[7] != null && ((Boolean) row[7]));
	            return dto;
	        });

	        if (row[8] != null) {
	            CustomerSimpleForTaskDTO customer = new CustomerSimpleForTaskDTO(
	                ((Number) row[8]).longValue(),
	                handleNull(row[9]),
	                handleNull(row[10]),
	                handleNull(row[11])
	            );
	            task.getCustomers().add(customer);
	        }
	    }

	    return new ArrayList<>(taskMap.values());
	}

	// Tiện ích xử lý null/chuỗi rỗng
	private String handleNull(Object value) {
	    String str = value != null ? value.toString().trim() : "";
	    return str.isEmpty() ? "Không xác định" : str;
	}

	@Override
	public Map<String, List<TaskWithCustomersDTO>> getGroupedTasks() {
	    List<TaskWithCustomersDTO> tasks = getTasksWithCustomersAsDTOs(); // dùng raw query + mapping DTO

	    Map<String, List<TaskWithCustomersDTO>> taskGroups = new LinkedHashMap<>();
	    taskGroups.put("Hôm nay", filterByToday(tasks));
	    taskGroups.put("Quá hạn", filterByOverdue(tasks));
	    taskGroups.put("Sắp tới", filterByUpcoming(tasks));
	    taskGroups.put("Quan trọng", filterByImportant(tasks));
	    taskGroups.put("Hoàn thành", filterByCompleted(tasks));
	    taskGroups.put("Chưa có thời gian cụ thể", filterByNoStartDate(tasks));

	    return taskGroups;
	}
	
	private List<TaskWithCustomersDTO> filterByToday(List<TaskWithCustomersDTO> tasks) {
	    LocalDate today = LocalDate.now();
	    return tasks.stream()
	        .filter(t -> t.getTaskStartDate() != null &&
	                     t.getTaskStartDate().toLocalDate().isEqual(today) &&
	                     !t.isStatus())
	        .collect(Collectors.toList());
	}

	private List<TaskWithCustomersDTO> filterByOverdue(List<TaskWithCustomersDTO> tasks) {
	    LocalDate today = LocalDate.now();
	    return tasks.stream()
	        .filter(t -> t.getTaskStartDate() != null &&
	                     t.getTaskStartDate().toLocalDate().isBefore(today) &&
	                     !t.isStatus())
	        .collect(Collectors.toList());
	}

	private List<TaskWithCustomersDTO> filterByUpcoming(List<TaskWithCustomersDTO> tasks) {
	    LocalDate today = LocalDate.now();
	    return tasks.stream()
	        .filter(t -> t.getTaskStartDate() != null &&
	                     t.getTaskStartDate().toLocalDate().isAfter(today) &&
	                     !t.isStatus())
	        .collect(Collectors.toList());
	}

	private List<TaskWithCustomersDTO> filterByImportant(List<TaskWithCustomersDTO> tasks) {
	    return tasks.stream()
	        .filter(t -> t.isImportant() && !t.isStatus())
	        .collect(Collectors.toList());
	}

	private List<TaskWithCustomersDTO> filterByCompleted(List<TaskWithCustomersDTO> tasks) {
	    return tasks.stream()
	        .filter(TaskWithCustomersDTO::isStatus)
	        .collect(Collectors.toList());
	}

	private List<TaskWithCustomersDTO> filterByNoStartDate(List<TaskWithCustomersDTO> tasks) {
	    return tasks.stream()
	        .filter(t -> t.getTaskStartDate() == null)
	        .collect(Collectors.toList());
	}
	
//	private List<TaskWithCustomersDTO> filterByToday(List<TaskWithCustomersDTO> tasks) {
//	    LocalDate today = LocalDate.now();
//	    return tasks.stream()
//	        .filter(t -> t.getTaskStartDate() != null &&
//	                     t.getTaskStartDate().toLocalDate().isEqual(today))
//	        .collect(Collectors.toList());
//	}
//
//	private List<TaskWithCustomersDTO> filterByOverdue(List<TaskWithCustomersDTO> tasks) {
//	    LocalDate today = LocalDate.now();
//	    return tasks.stream()
//	        .filter(t -> t.getTaskStartDate() != null &&
//	                     t.getTaskStartDate().toLocalDate().isBefore(today))
//	        .collect(Collectors.toList());
//	}
//
//	private List<TaskWithCustomersDTO> filterByUpcoming(List<TaskWithCustomersDTO> tasks) {
//	    LocalDate today = LocalDate.now();
//	    return tasks.stream()
//	        .filter(t -> t.getTaskStartDate() != null &&
//	                     t.getTaskStartDate().toLocalDate().isAfter(today))
//	        .collect(Collectors.toList());
//	}
//
//	private List<TaskWithCustomersDTO> filterByImportant(List<TaskWithCustomersDTO> tasks) {
//	    return tasks.stream()
//	        .filter(t -> t.isImportant())
//	        .collect(Collectors.toList());
//	}
//
//	private List<TaskWithCustomersDTO> filterByCompleted(List<TaskWithCustomersDTO> tasks) {
//	    return tasks.stream()
//	        .filter(TaskWithCustomersDTO::isStatus)
//	        .collect(Collectors.toList());
//	}
//
//	private List<TaskWithCustomersDTO> filterByNoStartDate(List<TaskWithCustomersDTO> tasks) {
//	    return tasks.stream()
//	        .filter(t -> t.getTaskStartDate() == null)
//	        .collect(Collectors.toList());
//	}


}
