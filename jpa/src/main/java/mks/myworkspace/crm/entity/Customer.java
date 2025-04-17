package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.*;

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

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;


	@ManyToOne
    @JoinColumn(name = "profession_id")
    private Profession profession;
	
    @ManyToOne
    @JoinColumn(name = "main_status_id")
    private Status mainStatus;

    @ManyToOne
    @JoinColumn(name = "sub_status_id")
    private Status subStatus;
    
    @ManyToOne
    @JoinColumn(name = "responsible_person_id")
    private ResponsiblePerson responsiblePerson;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "note", length = 255)
    private String note;
    
    @Column(name = "account_status")
    private Boolean accountStatus;

	public Customer(Long id) {
		super();
		this.id = id;
	}


	public String getFormattedCreatedAt() {
	    if (createdAt != null) {
	        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    }
	    return "";
	}


}
