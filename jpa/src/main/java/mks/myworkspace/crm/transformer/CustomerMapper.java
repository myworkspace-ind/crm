package mks.myworkspace.crm.transformer;

import mks.myworkspace.crm.entity.Address;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Profession;
import mks.myworkspace.crm.entity.ResponsiblePerson;
import mks.myworkspace.crm.entity.Status;
import mks.myworkspace.crm.entity.dto.AddressDTO;
import mks.myworkspace.crm.entity.dto.CustomerDetailDTO;
import mks.myworkspace.crm.entity.dto.ProfessionDTO;
import mks.myworkspace.crm.entity.dto.ResponsiblePersonDTO;
import mks.myworkspace.crm.entity.dto.StatusDTO;

public class CustomerMapper {
//	public static CustomerDetailDTO toDTO(Customer customer) {
//        return new CustomerDetailDTO(
//            customer.getId(),
//            customer.getCompanyName(),
//            customer.getEmail(),
//            customer.getPhone(),
//            customer.getAddress(),
//            customer.getContactPerson(),
////            customer.getMainStatus().getId(),
////            customer.getSubStatus().getId(),
////            customer.getResponsiblePerson().getId(),
//            customer.getMainStatus() != null ? customer.getMainStatus().getId() : null,  
//            customer.getSubStatus() != null ? customer.getSubStatus().getId() : null,   
//            customer.getResponsiblePerson() != null ? customer.getResponsiblePerson().getId() : null, 
//            customer.getBirthday() != null ? customer.getBirthday().toString() : null,
//            customer.getNote(), 
//            customer.getProfession() != null ? customer.getProfession().getId() : null
//        );
//    }

	public static CustomerDetailDTO toDTO(Customer customer) {
		if (customer == null) {
			return null;
		}

		CustomerDetailDTO dto = new CustomerDetailDTO();
		dto.setId(customer.getId());
		dto.setCompanyName(customer.getCompanyName());
		dto.setEmail(customer.getEmail());
		dto.setPhone(customer.getPhone());
		dto.setContactPerson(customer.getContactPerson());
		dto.setBirthday(customer.getBirthday());
		dto.setNote(customer.getNote());

		// Address
		Address addr = customer.getAddress();
		if (addr != null) {
			AddressDTO addrDto = new AddressDTO();
			addrDto.setId(addr.getId());
			addrDto.setStreet(addr.getStreet());
			addrDto.setWard(addr.getWard());
			addrDto.setDistrict(addr.getDistrict());
			addrDto.setState(addr.getState());
			addrDto.setCountry(addr.getCountry());
			addrDto.setPostcode(addr.getPostcode());
			addrDto.setLatitude(addr.getLatitude());
			addrDto.setLongitude(addr.getLongitude());
			dto.setAddress(addrDto);
		}

		// Main Status
		Status ms = customer.getMainStatus();
		if (ms != null) {
			StatusDTO msDto = new StatusDTO(ms.getId(), 
					//ms.getSiteId(), 
					ms.getName(), 
					ms.getBackgroundColor(),
					ms.getSeqno());
			dto.setMainStatus(msDto);
		}

		// Sub Status
		Status ss = customer.getSubStatus();
		if (ss != null) {
			StatusDTO ssDto = new StatusDTO(ss.getId(), 
					//ss.getSiteId(), 
					ss.getName(), 
					ss.getBackgroundColor(),
					ss.getSeqno());
			dto.setSubStatus(ssDto);
		}

		// Responsible Person
		ResponsiblePerson rp = customer.getResponsiblePerson();
		if (rp != null) {
			ResponsiblePersonDTO rpDto = new ResponsiblePersonDTO(rp.getId(), rp.getName(), rp.getNote(),
					rp.getSeqno());
			dto.setResponsiblePerson(rpDto);
		}

		// Profession
		Profession prof = customer.getProfession();
		if (prof != null) {
			ProfessionDTO profDto = new ProfessionDTO(prof.getId(), prof.getName(), prof.getNote(), prof.getSeqno());
			dto.setProfession(profDto);
		}

		return dto;
	}

	public static Customer toEntity(CustomerDetailDTO dto) {
		if (dto == null) {
			return null;
		}

		Customer customer = new Customer();
		customer.setId(dto.getId());
		customer.setCompanyName(dto.getCompanyName());
		customer.setEmail(dto.getEmail());
		customer.setPhone(dto.getPhone());
		customer.setContactPerson(dto.getContactPerson());
		customer.setBirthday(dto.getBirthday());
		customer.setNote(dto.getNote());

		// Address
		AddressDTO addrDto = dto.getAddress();
		if (addrDto != null) {
			Address addr = new Address();
			addr.setId(addrDto.getId());
			addr.setStreet(addrDto.getStreet());
			addr.setWard(addrDto.getWard());
			addr.setDistrict(addrDto.getDistrict());
			addr.setState(addrDto.getState());
			addr.setCountry(addrDto.getCountry());
			addr.setPostcode(addrDto.getPostcode());
			addr.setLatitude(addrDto.getLatitude());
			addr.setLongitude(addrDto.getLongitude());
			customer.setAddress(addr);
		}

		// Main Status
		StatusDTO msDto = dto.getMainStatus();
		if (msDto != null) {
			Status ms = new Status();
			ms.setId(msDto.getId());
			//ms.setSiteId(msDto.getSiteId());
			ms.setName(msDto.getName());
			ms.setBackgroundColor(msDto.getBackgroundColor());
			ms.setSeqno(msDto.getSeqno());
			customer.setMainStatus(ms);
		}

		// Sub Status
		StatusDTO ssDto = dto.getSubStatus();
		if (ssDto != null) {
			Status ss = new Status();
			ss.setId(ssDto.getId());
			//ss.setSiteId(ssDto.getSiteId());
			ss.setName(ssDto.getName());
			ss.setBackgroundColor(ssDto.getBackgroundColor());
			ss.setSeqno(ssDto.getSeqno());
			customer.setSubStatus(ss);
		}

		// Responsible Person
		ResponsiblePersonDTO rpDto = dto.getResponsiblePerson();
		if (rpDto != null) {
			ResponsiblePerson rp = new ResponsiblePerson();
			rp.setId(rpDto.getId());
			rp.setName(rpDto.getName());
			rp.setNote(rpDto.getNote());
			rp.setSeqno(rpDto.getSeqno());
			customer.setResponsiblePerson(rp);
		}

		// Profession
		ProfessionDTO profDto = dto.getProfession();
		if (profDto != null) {
			Profession prof = new Profession();
			prof.setId(profDto.getId());
			prof.setName(profDto.getName());
			prof.setNote(profDto.getNote());
			prof.setSeqno(profDto.getSeqno());
			customer.setProfession(prof);
		}

		return customer;
	}

}
