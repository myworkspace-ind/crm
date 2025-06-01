package mks.myworkspace.crm.entity.dto;

import java.time.LocalDateTime;
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
public class TaskDTO {
	private Long id;
	private String name;
    private String description;
    private boolean status;
    private boolean important;
    private LocalDateTime startDate;
    private List<Long> customerIds;
}
