package mks.myworkspace.crm.entity.dto;

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
public class FilesUploadDTO {
	private Long id;
    private String fileName;
    private String filePath;
    private String fileType;
}
