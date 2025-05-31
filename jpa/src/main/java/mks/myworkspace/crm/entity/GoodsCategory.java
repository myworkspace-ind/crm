package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

//	@Column(name = "site_id", length = 99)
//	private String siteId; 

	@Column(name = "name", length = 255)
	private String name;
	
	@Column(name = "note", length = 255)
    private String note;  // Ghi chú về loại hàng hóa
	
	@Column(name = "seqno")
    private Long seqno;  // Trường seqno sẽ luôn bằng với id dùng để sắp xếp thứ tự
	
//	@OneToMany(mappedBy = "goodsCategory", fetch = FetchType.LAZY)
//	private List<Order> orders;
	
	// Constructor nhận id
    public GoodsCategory(Long id) {
        this.id = id;
    }
    
	public GoodsCategory(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public GoodsCategory(Long id, String name, String note, Long seqno) {
		super();
		this.id = id;
		this.name = name;
		this.note = note;
		this.seqno = seqno;
	}



}
