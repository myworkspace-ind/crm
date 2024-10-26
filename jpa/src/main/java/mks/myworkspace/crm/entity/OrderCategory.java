package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Loại đơn hàng
@Entity
@Table(name = "crm_ordercategory", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class OrderCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 99)
	private String id; // system field

	@Column(name = "site_id", length = 99)
	private String siteId; // system field

	@Column
	private String name;

	@OneToMany(mappedBy = "orderCategory", fetch = FetchType.EAGER)	
	private Set<Order> orders;
	
	public OrderCategory(String id, String siteId, String name, Set<Order> orders) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.name = name;
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "OrderCategoryRepository [id=" + id + ", siteId=" + siteId + ", name=" + name + ", orders=" + orders + "]";
	}
}
