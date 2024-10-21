package mks.myworkspace.crm.service;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;

public interface StorageService {
	AppRepository getAppRepo();
	
	CustomerRepository getCustomerRepo();
	
	Customer saveOrUpdate(Customer customer);
}
