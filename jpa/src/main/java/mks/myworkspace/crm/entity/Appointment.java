package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name = "crm_appointment", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
public class Appointment implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "site_id", length = 99)
	private String siteId;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	private Customer customer;
	
	@Column(name = "exchange_person", length = 99)
	private String exchangePerson;
	
	@Column(name = "expected_date")
	private LocalDate expectedDate; //Ngày dự kiến
	
	@Column(name = "expected_time")
    private LocalTime expectedTime; //Thời gian dự kiến
	
	@Column(name = "meeting_date")
    private LocalDate meetingDate; // Ngày gặp mặt

    @Column(name = "meeting_time")
    private LocalTime meetingTime; // Thời gian gặp mặt

    @Column(name = "notes")
    private String notes; // Ghi chú

	public Appointment(Long id, Customer customer, String exchangePerson, LocalDate expectedDate,
			LocalTime expectedTime, LocalDate meetingDate, LocalTime meetingTime, String notes) {
		super();
		this.id = id;
		this.customer = customer;
		this.exchangePerson = exchangePerson;
		this.expectedDate = expectedDate;
		this.expectedTime = expectedTime;
		this.meetingDate = meetingDate;
		this.meetingTime = meetingTime;
		this.notes = notes;
	}
    
}
