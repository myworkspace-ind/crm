package mks.myworkspace.crm.entity.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskWithCustomersDTO {
	private Long taskId;
    private String taskName;
    private String taskDescription;
    private LocalDateTime taskStartDate;
    private boolean status;
    private boolean important;
    private List<CustomerSimpleForTaskDTO> customers = new ArrayList<>();
    
    public String getFormattedStartDate() {
	    if (taskStartDate != null) {
	        return taskStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    }
	    return "";
	}
}
