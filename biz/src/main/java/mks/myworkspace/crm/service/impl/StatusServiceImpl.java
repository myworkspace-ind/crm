package mks.myworkspace.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.crm.entity.Status;
import mks.myworkspace.crm.repository.StatusRepository;
import mks.myworkspace.crm.service.StatusService;

@Service
public class StatusServiceImpl implements StatusService{
	
	@Autowired
	private StatusRepository repo;

	@Override
	public StatusRepository getRepo() {
		return repo;
	}

	@Override
	public List<Status> getAllStatuses() {
		return repo.findAll();
	}

}
