package mks.myworkspace.crm.entity.dto;

public class CustomerDetailDTO {
	private Long id;
	private String companyName;
	private String email;
	private String phone;
	private String address;
	private String contactPerson;
	private Long mainStatus;
	private Long subStatus;
	private Long responsiblePerson;
	private String birthday;
	private String note;
	private Long profession;

	public CustomerDetailDTO(Long id, String companyName, String email, String phone, String address,
			String contactPerson, Long mainStatus, Long subStatus, Long responsiblePerson, String birthday,
			String note, Long profession) {
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
		this.profession = profession;
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

	public Long getMainStatus() {
		return mainStatus;
	}

	public void setMainStatus(Long mainStatus) {
		this.mainStatus = mainStatus;
	}

	public Long getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(Long subStatus) {
		this.subStatus = subStatus;
	}

	public Long getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(Long responsiblePerson) {
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
	

	public Long getProfession() {
		return profession;
	}

	public void setProfession(Long profession) {
		this.profession = profession;
	}

	@Override
	public String toString() {
		return "CustomerDetailDTO [id=" + id + ", companyName=" + companyName + ", email=" + email + ", phone=" + phone
				+ ", address=" + address + ", contactPerson=" + contactPerson + ", mainStatus=" + mainStatus
				+ ", subStatus=" + subStatus + ", responsiblePerson=" + responsiblePerson + ", birthday=" + birthday
				+ ", note=" + note + ", profession=" + profession + "]";
	}

}
