package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.repository.CustomerRepository;

public interface StorageService {
	

	
	CustomerRepository getCustomerRepo();
	

	List<Customer> saveOrUpdate(List<Customer> lstCustomer);
}