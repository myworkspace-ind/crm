package mks.myworkspace.crm.transformer;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.dto.CustomerDetailDTO;

public class CustomerMapper {
	public static CustomerDetailDTO toDTO(Customer customer) {
        return new CustomerDetailDTO(
            customer.getId(),
            customer.getCompanyName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getAddress(),
            customer.getContactPerson(),
//            customer.getMainStatus().getId(),
//            customer.getSubStatus().getId(),
//            customer.getResponsiblePerson().getId(),
            customer.getMainStatus() != null ? customer.getMainStatus().getId() : null,  
            customer.getSubStatus() != null ? customer.getSubStatus().getId() : null,   
            customer.getResponsiblePerson() != null ? customer.getResponsiblePerson().getId() : null, 
            customer.getBirthday() != null ? customer.getBirthday().toString() : null,
            customer.getNote(), 
            customer.getProfession() != null ? customer.getProfession().getId() : null
        );
    }
}
