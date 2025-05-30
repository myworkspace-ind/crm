package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.FilesUpload;
import mks.myworkspace.crm.entity.dto.FilesUploadDTO;

@Repository
public interface FilesUploadRepository extends JpaRepository<FilesUpload, Long> {
	@Query("SELECT new mks.myworkspace.crm.entity.dto.FilesUploadDTO(f.id, f.fileName, f.filePath, f.fileType) "
			+ "FROM FilesUpload f WHERE f.interaction.id = :interactionId")
	List<FilesUploadDTO> findFilesByInteractionId(@Param("interactionId") Long interactionId);
	
	boolean existsByInteraction_IdAndFileName(Long interactionId, String fileName);

}
