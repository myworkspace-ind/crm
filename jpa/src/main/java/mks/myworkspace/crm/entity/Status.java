package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
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

//	@Column(name = "site_id", length = 99)
//	private String siteId;

	@Column(name = "name", length = 255)
	private String name;

	@Column(name = "backgroundColor", length = 99)
	private String backgroundColor;

	@Column(name = "seqno")
	private Long seqno;  // Trường seqno sẽ luôn bằng với id dùng để sắp xếp thứ tự
	
	@OneToMany(mappedBy = "mainStatus", fetch = FetchType.LAZY)
	private List<Customer> customersWithMainStatus;

	@OneToMany(mappedBy = "subStatus", fetch = FetchType.LAZY)
	private List<Customer> customersWithSubStatus;

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

	public Status(Long id, String name, String backgroundColor, Set<Customer> customers) {
		super();
		this.id = id;
		this.name = name;
		this.backgroundColor = backgroundColor;
	}

	public Status(String id) {
		this.id = Long.parseLong(id);
	}

	public Status(Long id, String name, String backgroundColor) {
		super();
		this.id = id;
		this.name = name;
		this.backgroundColor = backgroundColor;
	}
	
	@PrePersist
	public void setSeqno() {
		if (this.id != null) {
			this.seqno = this.id;  // Đảm bảo seqno luôn bằng với id
		}
	}

	public Status(Long id, String name, String backgroundColor, Long seqno) {
		super();
		this.id = id;
		this.name = name;
		this.backgroundColor = backgroundColor;
		this.seqno = seqno;
	}

	@Override
	public String toString() {
		return "Status [id=" + id + ", name=" + name + ", backgroundColor=" + backgroundColor + ", seqno=" + seqno
				+ "]";
	}

}
