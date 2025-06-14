package mks.myworkspace.crm.service;

import java.util.List;
import java.util.Map;

import mks.myworkspace.crm.entity.Task;
import mks.myworkspace.crm.entity.dto.TaskDTO;
import mks.myworkspace.crm.entity.dto.TaskWithCustomersDTO;

public interface TaskService {	
	public void addTask(TaskDTO dto);
	public List<Task> getAllTasksWithCustomers();
	public List<TaskWithCustomersDTO> getTasksWithCustomersAsDTOs();
	public Map<String, List<TaskWithCustomersDTO>> getGroupedTasks();
	
	public List<String> getAllTaskNotifications();
	
}
