package mks.myworkspace.crm.entity.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InteractionDTO {
	private Long id;
    private String contactPerson;
    private String content;
    private String nextPlan;
    private Date interactionDate;
    private LocalDateTime createdAt;
    private List<FilesUploadDTO> files; 
}
