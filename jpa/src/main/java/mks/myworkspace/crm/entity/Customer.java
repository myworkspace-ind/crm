package mks.myworkspace.crm.entity;

import java.io.Serializable;

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
@Table(name = "crm_customer", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //system field

	@Column(name = "site_id", length = 99)
	private String siteId; //system field
	
	@Column(length = 99)
	private String name;
	
	@Column(length = 255)
	private String address;
	
	@Column(length = 10)
	private String phone;

	public Customer(Long id, String siteId, String name, String address, String phone) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.name = name;
		this.address = address;
		this.phone = phone;
	}
}