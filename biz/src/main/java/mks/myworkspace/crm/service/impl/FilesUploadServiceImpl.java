package mks.myworkspace.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.dto.FilesUploadDTO;
import mks.myworkspace.crm.repository.FilesUploadRepository;
import mks.myworkspace.crm.service.FilesUploadService;
@Slf4j
@Service
public class FilesUploadServiceImpl implements FilesUploadService{
	
	@Autowired 
	private FilesUploadRepository repo;
	
	@Override
	public List<FilesUploadDTO> findFilesByInteractionId(Long interactionId) {
		return repo.findFilesByInteractionId(interactionId);
	}

}
