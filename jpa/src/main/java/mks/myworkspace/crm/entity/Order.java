package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

//	@Column(name = "site_id", length = 99)
//	private String siteId;

	@Column(name = "name", length = 255)
	private String name;

	@Column(name = "code", length = 255)
	private String code;
	
	@Column(name = "address", length = 255)
	private String address;
	
	@Column(name = "create_date")
	//@Temporal(TemporalType.DATE)
	private Date createDate;
	
	@Column(name = "delivery_date")
	//@Temporal(TemporalType.DATE)
	private Date deliveryDate;

	@Column(name = "transportation_method", length = 255)
	private String transportationMethod;

	@Column(name = "customer_requirement", length = 255)
	private String customerRequirement;

	// Relation with OrderCategoryRepository
	@ManyToOne
	@JoinColumn(name = "order_cate_id", referencedColumnName = "id", nullable = false)
	private OrderCategory orderCategory;

	// Relation with Customer
	@ManyToOne
	@JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
	private Customer sender;
	
	@ManyToOne
	@JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
	private Customer receiver;

	// Relation Many-to-One with OrderStatus
	@ManyToOne
	@JoinColumn(name = "order_status_id", nullable = false)
	private OrderStatus orderStatus;
	
	@ManyToOne
    @JoinColumn(name = "goods_category_id", nullable = false) 
    private GoodsCategory goodsCategory;

	public Order(Long id, String name, String code, Date createDate, Date deliveryDate,
			String transportationMethod, String customerRequirement, OrderCategory orderCategory, Customer sender,
			Customer receiver, OrderStatus orderStatus, GoodsCategory goodsCategory) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.createDate = createDate;
		this.deliveryDate = deliveryDate;
		this.transportationMethod = transportationMethod;
		this.customerRequirement = customerRequirement;
		this.orderCategory = orderCategory;
		this.sender = sender;
		this.receiver = receiver;
		this.orderStatus = orderStatus;
		this.goodsCategory = goodsCategory;
	}

}
