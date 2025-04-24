package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "crm_birthday_email_log", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
public class BirthdayEmailLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	private LocalDate birthday;

	private LocalDateTime sentAt;

	private Boolean success;

	private String errorMessage;

	public BirthdayEmailLog(Long id, Customer customer, LocalDate birthday, LocalDateTime sentAt, Boolean success,
			String errorMessage) {
		super();
		this.id = id;
		this.customer = customer;
		this.birthday = birthday;
		this.sentAt = sentAt;
		this.success = success;
		this.errorMessage = errorMessage;
	}
	
}
