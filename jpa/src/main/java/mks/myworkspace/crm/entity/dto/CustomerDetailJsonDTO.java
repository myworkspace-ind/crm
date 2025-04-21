package mks.myworkspace.crm.entity.dto;

import mks.myworkspace.crm.entity.Address;

public class CustomerDetailJsonDTO {
	private Long id;
	private String companyName;
	private String email;
	private String phone;
	private String address;
	private String contactPerson;
	private String mainStatus;
	private String subStatus;
	private String responsiblePerson;
	private String birthday;
	private String note;
	
	public CustomerDetailJsonDTO(Long id, String companyName, String email, String phone, String address,
			String contactPerson, String mainStatus, String subStatus, String responsiblePerson, String birthday,
			String note) {
		super();
		this.id = id;
		this.companyName = companyName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.contactPerson = contactPerson;
		this.mainStatus = mainStatus;
		this.subStatus = subStatus;
		this.responsiblePerson = responsiblePerson;
		this.birthday = birthday;
		this.note = note;
	}

	public CustomerDetailJsonDTO(Long id2, String companyName2, String email2, String phone2, Address address2,
			String contactPerson2, String mainStatus2, String subStatus2, String responsiblePerson2, String birthday2,
			String note2) {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getMainStatus() {
		return mainStatus;
	}

	public void setMainStatus(String mainStatus) {
		this.mainStatus = mainStatus;
	}

	public String getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

	public String getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	

}
