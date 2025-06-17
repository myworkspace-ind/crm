package mks.myworkspace.crm.entity.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSimpleForTaskDTO {
	private Long customerId;
    private String companyName;
    private String contactPerson;
    private String phone;
    
    //Interaction tuong ung
    private Long interactionId;
    private String interactionContent;
    private LocalDateTime interactionCreatedAt;
    
    public String getFormattedInteractionCreatedAt() {
	    if (interactionCreatedAt != null) {
	        return interactionCreatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    }
	    return "";
	}
}
