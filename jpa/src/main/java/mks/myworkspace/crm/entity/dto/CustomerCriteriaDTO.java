package mks.myworkspace.crm.entity.dto;

import java.util.List;
import java.util.Optional;


public class CustomerCriteriaDTO {
	
	private Optional<String> keyword;
	private Optional<String> page;
	private Optional<String> statusId;
	private Optional<String> nameCompany;
	private Optional<String> phone;
	private Optional<String> contactPerson;
	private Optional<String> address;
	private Optional<String> email;
	private Optional<List<Long>> careers;
	
	public Optional<String> getKeyword() {
		return keyword;
	}
	public void setKeyword(Optional<String> keyword) {
		this.keyword = keyword;
	}
	
	public Optional<String> getPage() {
		return page;
	}
	public void setPage(Optional<String> page) {
		this.page = page;
	}
	public Optional<String> getStatusId() {
		return statusId;
	}
	public void setStatusId(Optional<String> statusId) {
		this.statusId = statusId;
	}
	public Optional<String> getNameCompany() {
		return nameCompany;
	}
	public void setNameCompany(Optional<String> nameCompany) {
		this.nameCompany = nameCompany;
	}
	public Optional<String> getPhone() {
		return phone;
	}
	public void setPhone(Optional<String> phone) {
		this.phone = phone;
	}
	public Optional<String> getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(Optional<String> contactPerson) {
		this.contactPerson = contactPerson;
	}
	public Optional<String> getAddress() {
		return address;
	}
	public void setAddress(Optional<String> address) {
		this.address = address;
	}
	public Optional<String> getEmail() {
		return email;
	}
	public void setEmail(Optional<String> email) {
		this.email = email;
	}
	public Optional<List<Long>> getCareers() {
		return careers;
	}
	public void setCareers(Optional<List<Long>> careers) {
		this.careers = careers;
	}

}
