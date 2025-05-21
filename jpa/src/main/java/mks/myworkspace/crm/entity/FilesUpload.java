package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
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
@Table(name = "crm_files_upload", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
public class FilesUpload implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interaction_id", nullable = false)
    private Interaction interaction;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_type", length = 255)
    private String fileType;

    @Column(name = "file_path", length = 255)
    private String filePath;

    @Column(name = "uploaded_at", insertable = true, updatable = false)
    private Timestamp uploadedAt;

	public FilesUpload(Long id, Interaction interaction, String fileName, String fileType, String filePath,
			Timestamp uploadedAt) {
		super();
		this.id = id;
		this.interaction = interaction;
		this.fileName = fileName;
		this.fileType = fileType;
		this.filePath = filePath;
		this.uploadedAt = uploadedAt;
	}
    
}
