package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mks.myworkspace.crm.transformer.EmailStatusConverter;
@Entity
@Table(name = "crm_emailtocustomer", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
public class EmailToCustomer implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@Column(name = "site_id", length = 99)
//	private String siteId;
	
	@Column(name = "send_date")
    //@Temporal(TemporalType.DATE)
    private Date sendDate;
	
	@Column(name = "subject", length = 255)
    private String subject;
	
	@Lob
	@Column(name = "content")
    private String content;  
//	ALTER TABLE crm_emailtocustomer MODIFY content LONGTEXT;
//	ALTER DATABASE crm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
//	ALTER TABLE crm_emailtocustomer CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
//	ALTER TABLE crm_emailtocustomer MODIFY content TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
//	--> Chạy 4 dòng lệnh này trong mysql
	
	@ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private Customer customer;
    
    @Column(name = "sender", length = 99) 
	private String sender; //Column sender will be displayed by username's employee when employee login successfully to the system
    
    @Convert(converter = EmailStatusConverter.class)
    @Column(name = "status")
    private EmailStatus status; //Save status of "Send" and "Save draft"
    
    public enum EmailStatus {
    	SENT, DRAFT;
    }

	public EmailToCustomer(Long id, Date sendDate, String subject, String content, Customer customer, String sender,
			EmailStatus status) {
		super();
		this.id = id;
		this.sendDate = sendDate;
		this.subject = subject;
		this.content = content;
		this.customer = customer;
		this.sender = sender;
		this.status = status;
	}
    
}
