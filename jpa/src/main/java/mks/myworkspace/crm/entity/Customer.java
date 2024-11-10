package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "crm_customer", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "site_id", length = 99)
	private String siteId;

	@Column(name = "company_name", length = 99)
	private String companyName;
	
	@Column(name = "contact_person", length = 99) 
	private String contactPerson;
	
	@Column(name = "email", length = 99)
	private String email;

	@Column(name = "phone", length = 10)
	private String phone;
	
	@Column(name = "address", length = 255)
	private String address;
	
	@Column(name = "profession", length = 99)
    private String profession;
	
    @ManyToOne
    @JoinColumn(name = "main_status_id")
    private Status mainStatus;

    @ManyToOne
    @JoinColumn(name = "sub_status_id")
    private Status subStatus;
    
    @Column(name = "responsible_person", length = 99)
    private String responsiblePerson;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column(name = "note", length = 255)
    private String note;
    
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
	private Set<Order> orders;

    public Customer(Long id, String siteId, String companyName, String contactPerson, String email, String phone, String address,
			String profession, Status mainStatus, Status subStatus, String responsiblePerson, Date createdAt,
			String note, Set<Order> orders) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.companyName = companyName;
		this.contactPerson = contactPerson;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.profession = profession;
		this.mainStatus = mainStatus;
		this.subStatus = subStatus;
		this.responsiblePerson = responsiblePerson;
		this.createdAt = createdAt;
		this.note = note;
		this.orders = orders;
	}
    
    public Customer(Long id, String siteId, String companyName, String contactPerson, String email, String phone, String address,
			String profession, String responsiblePerson, Date createdAt, String note) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.companyName = companyName;
		this.contactPerson = contactPerson;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.profession = profession;
		this.responsiblePerson = responsiblePerson;
		this.createdAt = createdAt;
		this.note = note;
	}

	/*
	 * @Override public String toString() { return "Customer [id=" + id +
	 * ", siteId=" + siteId + ", companyName=" + companyName + ", contactPerson=" +
	 * contactPerson + " email=" + email + ", phone=" + phone + ", address=" +
	 * address + ", mainStatus=" + mainStatus + ", subStatus=" + subStatus +
	 * ", orders=" + orders + ", responsiblePerson=" + responsiblePerson +
	 * ", createdAt=" + createdAt + ", note=" + note + "]"; }
	 */
}
