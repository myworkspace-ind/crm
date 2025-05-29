package mks.myworkspace.crm.entity.dto;

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
}
