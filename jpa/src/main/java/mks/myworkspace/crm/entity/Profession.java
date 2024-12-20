package mks.myworkspace.crm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "crm_profession")
@Getter
@Setter
@NoArgsConstructor
public class Profession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID tự động tăng

    @Column(name = "name", length = 99, nullable = false)
    private String name;  // Tên ngành nghề

    @Column(name = "note", length = 255)
    private String note;  // Ghi chú về ngành nghề

    @Column(name = "seqno")
    private Long seqno;  // Trường seqno sẽ luôn bằng với id dùng để sắp xếp thứ tự
    public Profession(Long id, String name, String note) {
        super();
        this.id = id;
        this.name = name;
        this.note = note;
    }
    
	public Profession(String id) {
		this.id = Long.parseLong(id);
	}

	@PrePersist
	public void setSeqno() {
		if (this.id != null) {
			this.seqno = this.id;  // Đảm bảo seqno luôn bằng với id
		}
	}

}
