package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mks.myworkspace.crm.entity.EmailToCustomer.EmailStatus;

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
    
    @Column(name = "birthay")
    private LocalDate birthday; //Lưu ngày sinh nhật cá nhân hoặc ngày kỷ niệm thành lập doanh nghiệp
    
    @Column(name = "classification")
	private Classification classification; // Save status of "Send" and "Save draft"

	public enum Classification {
		BUSINESS, INDIVIDUAL;
	}
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "note", length = 255)
    private String note;
    
    @Column(name = "account_status")
    private Boolean accountStatus;
    
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Interaction> interactions;

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

	@Override
	public String toString() {
		return "Customer [id=" + id + ", siteId=" + siteId + ", companyName=" + companyName + ", contactPerson="
				+ contactPerson + ", email=" + email + ", phone=" + phone + ", address=" + address + ", profession="
				+ profession + ", mainStatus=" + mainStatus + ", subStatus=" + subStatus + ", responsiblePerson="
				+ responsiblePerson + ", birthday=" + birthday + ", classification=" + classification + ", createdAt="
				+ createdAt + ", note=" + note + ", accountStatus=" + accountStatus + ", interactions=" + interactions
				+ "]";
	}


}
