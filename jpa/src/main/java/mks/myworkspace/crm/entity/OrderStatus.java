package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Loại đơn hàng
@Entity
@Table(name = "crm_orderstatus", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class OrderStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // system field

	@Column(name = "site_id", length = 99)
	private String siteId; // system field

	@Column
	private String name;

//	@OneToMany(mappedBy = "orderStatus", fetch = FetchType.EAGER)
//	private Set<Order> orders;
	
	@ManyToMany(mappedBy = "orderStatuses", fetch = FetchType.EAGER)
	private Set<OrderCategory> orderCategories;
	
	@Override
	public String toString() {
		return "OrderStatus [id=" + id + ", siteId=" + siteId + ", name=" + name + ", orderCategories="
				+ orderCategories + "]";
	}

	public OrderStatus(Long id, String siteId, String name, Set<OrderCategory> orderCategories) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.name = name;
		this.orderCategories = orderCategories;
	}

}
