package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.repository.CustomerCareRepository;

public interface CustomerCareService {
	CustomerCareRepository getRepo();
	
	void loadPotentialCustomersIntoCustomerCare();
	List<CustomerCare> findAll();
}
