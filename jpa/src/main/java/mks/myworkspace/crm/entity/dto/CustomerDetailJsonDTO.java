package mks.myworkspace.crm.entity.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDetailJsonDTO {

	private Long id;
	//private String siteId;
    private String companyName;
    private String email;
    private String phone;
    private AddressDTO address;
    private String contactPerson;
    private String mainStatus;
    private String subStatus;
    private String responsiblePerson;
    private LocalDate birthday;
    private String note;
    private String profession;
}
