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

@Entity
@Table(name = "crm_reminder_features", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
public class ReminderFeatures implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
    private String description;
	
	@Column(name = "icon")
    private String icon; 

	@Column(name="enabled")
	private boolean enabled;

	public ReminderFeatures(Long id, String code, String name, String description, String icon, boolean enabled) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.enabled = enabled;
	}
  
}
