package mks.myworkspace.crm.entity.dto;

import java.util.Date;

import mks.myworkspace.crm.entity.EmailToCustomer;

public class EmailToCustomerDTO {
	private Long id;
    //private String siteId;
    private Date sendDate;
    private String subject;
    private String content;
    private Long customerId;
    private String customerEmail;
    private String sender;
    private String status;
    
	public EmailToCustomerDTO(Long id, Date sendDate, String subject, String content, Long customerId,
			String customerEmail, String sender, String status) {
		super();
		this.id = id;
		this.sendDate = sendDate;
		this.subject = subject;
		this.content = content;
		this.customerId = customerId;
		this.customerEmail = customerEmail;
		this.sender = sender;
		this.status = status;
	}
	
	public EmailToCustomerDTO(EmailToCustomer email) {
        this.id = email.getId();
        this.sendDate = email.getSendDate();
        this.subject = email.getSubject();
        this.content = email.getContent();
        this.sender = email.getSender();
        this.status = email.getStatus().toString();
        
        if (email.getCustomer() != null) {
            this.customerId = email.getCustomer().getId();
            // Thêm các trường khác từ Customer nếu cần
            this.customerEmail = email.getCustomer().getEmail();
        }
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
