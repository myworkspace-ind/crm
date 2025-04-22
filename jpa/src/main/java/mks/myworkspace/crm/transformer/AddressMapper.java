package mks.myworkspace.crm.transformer;

import mks.myworkspace.crm.entity.Address;
import mks.myworkspace.crm.entity.dto.AddressDTO;

public class AddressMapper {
	public static AddressDTO toDTO(Address address) {
        if (address == null) return null;

        return new AddressDTO(
        	address.getId(),
            address.getStreet(),
            address.getWard(),
            address.getDistrict(),
            address.getState(),
            address.getPostcode(),
            address.getCountry(),
            address.getLatitude(),
            address.getLongitude()
        );
    }
}
