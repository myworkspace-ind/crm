package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "crm_customer", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // system field

	@Column(name = "site_id", length = 99)
	private String siteId; // system field

	@Column(length = 99)
	private String name;

	@Column(length = 255)
	private String address;

	@Column(length = 10)
	private String phone;

	// Many-to-Many relationship with Status
//	@ManyToMany(fetch = FetchType.EAGER)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "customer_status", // name of the join table
			joinColumns = @JoinColumn(name = "customer_id"), // foreign key for Customer in the join table
			inverseJoinColumns = @JoinColumn(name = "status_id") // foreign key for Status in the join table
	)
	
	private Set<Status> statuses = new HashSet<>(); // a customer can have multiple statuses
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
	private Set<Order> orders;
	
	public Customer(Long id, String siteId, String name, String address, String phone, Set<Status> statuses,
			Set<Order> orders) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.statuses = statuses;
		this.orders = orders;
	}
	
	public Customer(Long id, String siteId, String name, String address, String phone) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.name = name;
		this.address = address;
		this.phone = phone;
	}

//	@Override
//	public String toString() {
//		return "Customer [id=" + id + ", siteId=" + siteId + ", name=" + name + ", address=" + address + ", phone="
//				+ phone + ", statuses=" + statuses + ", orders=" + orders + "]";
//	}

}
