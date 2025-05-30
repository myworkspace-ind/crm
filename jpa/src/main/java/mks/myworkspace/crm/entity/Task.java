package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "crm_task", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", length = 255)
	private String name;
	
	@Column(name = "description", length = 255)
	private String description;
	
//	@Lob
//	@Column(name = "description")
//    private String description;
	
	@Column(name = "status")
	private boolean status;
	
	@Column(name = "important")
	private boolean important;
	
	@Column(name = "start_date")
	private LocalDateTime start_date;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	    name = "task_customer",
	    joinColumns = @JoinColumn(name = "task_id"),
	    inverseJoinColumns = @JoinColumn(name = "customer_id")
	)
	private List<Customer> customers = new ArrayList<>();

	public Task(Long id, String name, String description, boolean status, boolean important, LocalDateTime start_date,
			List<Customer> customers) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
		this.important = important;
		this.start_date = start_date;
		this.customers = customers;
	}
	
	public String getFormattedStartDate() {
	    if (start_date != null) {
	        return start_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    }
	    return "";
	}

}
