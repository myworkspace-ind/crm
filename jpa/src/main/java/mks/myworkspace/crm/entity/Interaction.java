package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "customer_interaction")
@Getter
@Setter
@NoArgsConstructor
public class Interaction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID tự động tăng

	@Column(name = "interaction_date")
    @Temporal(TemporalType.DATE)
    private Date interaction_date;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;  
    
    @Column(name = "next_plan", columnDefinition = "TEXT")
    private String next_plan;  
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(name = "customer_id", insertable = false, updatable = false)
    private Long customerId;

    public Long getCustomerId() {
        return (customer != null) ? customer.getId() : customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
        if (this.customer == null) {
            this.customer = new Customer();
        }
        this.customer.setId(customerId);
    }
    
    public Interaction(Long id, Date interaction_date, String content, String plan, Customer customer) {
        this.id = id;
        this.interaction_date = interaction_date;
        this.content = content;
        this.next_plan = plan;
        this.customer = customer;
    }
    
    public Interaction(Long id, Date interaction_date, String content, String plan) {
        this.id = id;
        this.interaction_date = interaction_date;
        this.content = content;
        this.next_plan = plan;
    }
    
    public Interaction(Long id, Date interaction_date, String content, String plan, Long customerId) {
        this.id = id;
        this.interaction_date = interaction_date;
        this.content = content;
        this.next_plan = plan;
        this.customerId = customerId;
    }
    
    public Interaction(Long id, Date interaction_date, String content, String plan, Long customerId, Customer customer) {
        this.id = id;
        this.interaction_date = interaction_date;
        this.content = content;
        this.next_plan = plan;
        this.customerId = customerId;
        this.customer = customer;
    }      
}
