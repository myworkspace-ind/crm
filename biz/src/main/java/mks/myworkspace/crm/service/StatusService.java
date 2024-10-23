package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.Status;
import mks.myworkspace.crm.repository.StatusRepository;

public interface StatusService {
	StatusRepository getRepo();
	
	List<Status> getAllStatuses();
	
	void deleteByCustomerIdIn(Iterable<Long> ids);
}
