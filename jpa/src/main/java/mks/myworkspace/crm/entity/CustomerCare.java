package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "crm_customer_care", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
public class CustomerCare implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
	
//	@Column(name = "site_id", length = 99)
//	private String siteId;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
	private Customer customer;
	
	@Column(name = "priority")
	private String priority;
	
	@Column(name = "care_status")
    private String careStatus; 

	@Column(name = "remind_date")
    //@Temporal(TemporalType.DATE)
    private LocalDateTime remindDate;
	
	@ManyToOne
	@JoinColumn(name = "previous_main_status_id")
	private Status previousMainStatus;

	@ManyToOne
	@JoinColumn(name = "current_main_status_id")
	private Status currentMainStatus;

	public CustomerCare(Long id, Customer customer, String priority, String careStatus, LocalDateTime remindDate) {
		super();
		this.id = id;
		this.customer = customer;
		this.priority = priority;
		this.careStatus = careStatus;
		this.remindDate = remindDate;
	}
	
}
