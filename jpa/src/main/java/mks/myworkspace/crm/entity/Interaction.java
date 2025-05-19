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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "crm_customer_interaction", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
public class Interaction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID tự động tăng
	
	@Column(name = "site_id", length = 99)
	private String siteId;

	@Column(name = "interaction_date")
    //@Temporal(TemporalType.DATE)
    private Date interactionDate;

    @Column(name = "content")
    private String content;  
    
    @Column(name = "next_plan")
    private String nextPlan;  
    
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    
    @Column(name = "contact_person", length = 99) 
	private String contactPerson;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

	public Interaction(Long id, Date interactionDate, String content, String nextPlan, Customer customer,
			String contactPerson, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.interactionDate = interactionDate;
		this.content = content;
		this.nextPlan = nextPlan;
		this.customer = customer;
		this.contactPerson = contactPerson;
		this.createdAt = createdAt;
	}
    

//    @PrePersist
//    protected void onCreate() {
//    	this.createdAt = LocalDateTime.now(); // Gán thời gian khi tạo mới
//    }


  
    
}
