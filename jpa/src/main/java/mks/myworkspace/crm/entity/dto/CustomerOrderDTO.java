package mks.myworkspace.crm.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mks.myworkspace.crm.entity.Customer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerOrderDTO {
	private Long id;
    private String companyName;
    private String contactPerson;
    private String email;
    private String phone;
    
	public CustomerOrderDTO(Customer customer) {
		this.id = customer.getId();
        this.companyName = customer.getCompanyName();
        this.contactPerson = customer.getContactPerson();
        this.email = customer.getEmail();
        this.phone = customer.getPhone();
	}
}
