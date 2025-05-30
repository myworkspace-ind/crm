package mks.myworkspace.crm.service;

import java.util.List;
import java.util.Map;

import mks.myworkspace.crm.entity.Task;
import mks.myworkspace.crm.entity.dto.TaskDTO;
import mks.myworkspace.crm.entity.dto.TaskWithCustomersDTO;

public interface TaskService {	
	void addTask(TaskDTO dto);
	List<Task> getAllTasksWithCustomers();
	List<TaskWithCustomersDTO> getTasksWithCustomersAsDTOs();
	Map<String, List<TaskWithCustomersDTO>> getGroupedTasks();
	
}
