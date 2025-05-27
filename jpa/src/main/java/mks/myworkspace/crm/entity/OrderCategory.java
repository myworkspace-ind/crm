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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // system field

//	@Column(name = "site_id", length = 99)
//	private String siteId; 

	@Column(name = "name", length = 255)
	private String name;
	
	@Column(name = "note", length = 255)
	private String note;

	@OneToMany(mappedBy = "orderCategory", fetch = FetchType.EAGER)	
//	private Set<Order> orders;
	private List<Order> orders;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "order_category_status",
			joinColumns = @JoinColumn(name = "order_category_id"),
			inverseJoinColumns = @JoinColumn(name = "order_status_id")
	)
	 private Set<OrderStatus> orderStatuses;

	public OrderCategory(Long id, String name, String note, List<Order> orders,
			Set<OrderStatus> orderStatuses) {
		super();
		this.id = id;
		this.name = name;
		this.note = note;
		this.orders = orders;
		this.orderStatuses = orderStatuses;
	}

	public OrderCategory(Long id, String name, String note) {
		super();
		this.id = id;
		this.name = name;
		this.note = note;
	}

	
}
