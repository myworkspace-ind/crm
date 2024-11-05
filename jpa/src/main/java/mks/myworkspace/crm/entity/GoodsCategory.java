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

//Loại hàng hóa
@Entity
@Table(name = "crm_goodscategory", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class GoodsCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // system field

	@Column(name = "site_id", length = 99)
	private String siteId; // system field

	@Column
	private String name;

//	// Relation Many-to-Many with Order
//    @ManyToMany(mappedBy = "goodsCategories")
//    private Set<Order> orders;

	public GoodsCategory(Long id, String siteId, String name) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.name = name;
	}

	@Override
	public String toString() {
		return "GoodsCategory [id=" + id + ", siteId=" + siteId + ", name=" + name + "]";
	}

}
