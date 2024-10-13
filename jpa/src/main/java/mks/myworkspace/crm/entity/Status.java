package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

	@Column(length = 99)
	private String name;

	@Column(length = 99)
	private String backgroundColor;

	// Many-to-Many relationship with Customer
	@ManyToMany(mappedBy = "statuses")
	private Set<Customer> customers = new HashSet<>();

	public Status(Long id, String siteId, String name, String backgroundColor, Set<Customer> customers) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.name = name;
		this.backgroundColor = backgroundColor;
		this.customers = customers;
	}

	@Override
	public String toString() {
		return "Status [id=" + id + ", siteId=" + siteId + ", name=" + name + ", backgroundColor=" + backgroundColor
				+ ", customers=" + customers + "]";
	}

	

}
