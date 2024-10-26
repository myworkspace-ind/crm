package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	@Column(length = 99)
	private String id; // system field

	@Column(name = "site_id", length = 99)
	private String siteId; // system field

	@Column
	private String name;

	@Column(name = "delivery_date")
	@Temporal(TemporalType.DATE)
	private Date deliveryDate;

	@Column(name = "transportation_method")
	private String transportationMethod;

	@Column(name = "customer_requirement")
	private String customerRequirement;

	// Relation 1-1 with OrderCategoryRepository
	@OneToOne
	@JoinColumn(name = "order_category_id", referencedColumnName = "id")
	private OrderCategory orderCategory;

	// Relation 1-1 with GoodsCategory
	@OneToOne
	@JoinColumn(name = "goods_category_id", referencedColumnName = "id")
	private GoodsCategory goodsCategory;

	// Relation 1-1 with OrderStatus
	@OneToOne
	@JoinColumn(name = "order_status_id", referencedColumnName = "id")
	private OrderStatus orderStatus;

	// Relation 1-1 with Customer
	@OneToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	private Customer customer;

	public Order(String id, String siteId, String name, Date deliveryDate, String transportationMethod,
			String customerRequirement, OrderCategory orderCategory, GoodsCategory goodsCategory,
			OrderStatus orderStatus) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.name = name;
		this.deliveryDate = deliveryDate;
		this.transportationMethod = transportationMethod;
		this.customerRequirement = customerRequirement;
		this.orderCategory = orderCategory;
		this.goodsCategory = goodsCategory;
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", siteId=" + siteId + ", name=" + name + ", deliveryDate=" + deliveryDate
				+ ", transportationMethod=" + transportationMethod + ", customerRequirement=" + customerRequirement
				+ ", orderCategory=" + orderCategory + ", goodsCategory=" + goodsCategory + ", orderStatus="
				+ orderStatus + "]";
	}

	public Order(String orderId, Date deliveryDate2, GoodsCategory goodsCategory2, Customer customer2,
			String transportationMethod2) {
		// TODO Auto-generated constructor stub
	}
}
