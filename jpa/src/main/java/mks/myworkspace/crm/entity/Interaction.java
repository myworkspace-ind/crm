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
@Table(name = "customer_interaction")
@Getter
@Setter
@NoArgsConstructor
public class Interaction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID tự động tăng
	
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
	
	@Column(name = "interaction_date")
    @Temporal(TemporalType.DATE)
    private Date interactAt;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;  
    
    @Column(name = "next_plan", columnDefinition = "TEXT")
    private String plan;  
    
    
	public Interaction(Long id, Date interactAt ,String content,String plan, Customer customer) {
		super();
		this.id = id;
		this.interactAt = interactAt;
		this.content = content;
		this.plan = plan;
		this.customer = customer;
	}

}
