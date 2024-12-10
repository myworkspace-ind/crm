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
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer_interaction", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
public class Interaction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID tự động tăng

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
  
    public Interaction(Long id, Date interactionDate, String content, String plan, Customer customer) {
        this.id = id;
        this.interactionDate = interactionDate;
        this.content = content;
        this.nextPlan = plan;
        this.customer = customer;
    }
    
    public Interaction(Long id, Date interactionDate, String content, String plan) {
        this.id = id;
        this.interactionDate = interactionDate;
        this.content = content;
        this.nextPlan = plan;
    }    
}
