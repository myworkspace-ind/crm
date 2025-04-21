package mks.myworkspace.crm.entity.dto;

import java.io.Serializable;

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
public class AddressDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
    private String street;
    private String ward;
    private String district;
    private String state;
    private String country;
    private String postcode;
    private Double latitude;
    private Double longitude;
}
