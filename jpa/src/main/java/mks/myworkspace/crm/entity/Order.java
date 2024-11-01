package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "crm_order", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // system field
	
	@Column(name = "site_id", length = 99)
	private String siteId; // system field
	
	@Column
	private String name;
	
	@Column
	private String code;

	@Column(name = "delivery_date")
	@Temporal(TemporalType.DATE)
	private Date deliveryDate;

	@Column(name = "transportation_method")
	private String transportationMethod;

	@Column(name = "customer_requirement")
	private String customerRequirement;

	// Relation 1-1 with OrderCategoryRepository
	@OneToOne
	@JoinColumn(name = "order_cate_id", referencedColumnName = "id")
	private OrderCategory orderCategory;

	// Relation 1-1 with GoodsCategory
//	@OneToOne
//	@JoinColumn(name = "goods_category_id", referencedColumnName = "id")
//	private GoodsCategory goodsCategory;

	// Relation 1-1 with OrderStatus
//	@OneToOne
//	@JoinColumn(name = "order_status_id", referencedColumnName = "id")
//	private OrderStatus orderStatus;

	// Relation 1-1 with Customer
	@OneToOne
	@JoinColumn(name = "cus_id", referencedColumnName = "id")
	private Customer customer;
	
	public Order(Long orderId, Date deliveryDate2, GoodsCategory goodsCategory2, Customer customer2,
			String transportationMethod2) {
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", siteId=" + siteId + ", name=" + name + ", code=" + code + ", deliveryDate=" + deliveryDate
				+ ", transportationMethod=" + transportationMethod + ", customerRequirement=" + customerRequirement
				+ ", orderCategory=" + orderCategory + ", customer=" + customer + "]";
	}

	public Order(Long id, String siteId, String name, String code, Date deliveryDate, String transportationMethod,
			String customerRequirement, OrderCategory orderCategory, Customer customer) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.name = name;
		this.code = code;
		this.deliveryDate = deliveryDate;
		this.transportationMethod = transportationMethod;
		this.customerRequirement = customerRequirement;
		this.orderCategory = orderCategory;
		this.customer = customer;
	}
}
