package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "crm_status", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
public class Status implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // system field

	@Column(name = "site_id", length = 99)
	private String siteId; // system field

	@Column(name = "name", length = 99)
	private String name;

	@Column(name = "backgroundColor", length = 99)
	private String backgroundColor;

//	// Many-to-Many relationship with Customer
//	@ManyToMany(mappedBy = "statuses")
//	private Set<Customer> customers = new HashSet<>();
	
	//Comment One-to-Many relationship with Customer for main status and sub status tp avoid error: com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
	// One-to-Many relationship with Customer for main status
    //@OneToMany(mappedBy = "mainStatus", fetch = FetchType.EAGER)
	//private Set<Customer> mainStatusCustomers = new HashSet<>();

    // One-to-Many relationship with Customer for sub status
	// @OneToMany(mappedBy = "subStatus", fetch = FetchType.EAGER)
	// private Set<Customer> subStatusCustomers = new HashSet<>();

	public Status(Long id, String siteId, String name, String backgroundColor, Set<Customer> customers) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.name = name;
		this.backgroundColor = backgroundColor;
	}

	public Status(String id) {
		this.id = Long.parseLong(id);
	}
	
	
}
