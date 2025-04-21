package mks.myworkspace.crm.entity.dto;

import java.io.Serializable;
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
public class CustomerDetailDTO implements Serializable{
	private static final long serialVersionUID = 1L;
//	private Long id;
//	private String companyName;
//	private String email;
//	private String phone;
//	private String address;
//	private String contactPerson;
//	private Long mainStatus;
//	private Long subStatus;
//	private Long responsiblePerson;
//	private String birthday;
//	private String note;
//	private Long profession;
	private Long id;
	private String siteId;
    private String companyName;
    private String email;
    private String phone;
    private AddressDTO address;
    private String contactPerson;
    private StatusDTO mainStatus;
    private StatusDTO subStatus;
    private ResponsiblePersonDTO responsiblePerson;
    private LocalDate birthday;
    private String note;
    private ProfessionDTO profession;

}
