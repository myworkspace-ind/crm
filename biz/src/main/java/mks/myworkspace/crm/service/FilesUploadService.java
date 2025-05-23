package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.dto.FilesUploadDTO;

public interface FilesUploadService {
	List<FilesUploadDTO> findFilesByInteractionId(Long interactionId);
	
	public boolean isFileExists(Long interactionId, String filename);
}
