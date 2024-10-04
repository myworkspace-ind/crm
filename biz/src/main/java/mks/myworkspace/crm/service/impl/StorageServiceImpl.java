package mks.myworkspace.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.service.StorageService;

public class StorageServiceImpl implements StorageService {

	@Autowired
	// @Getter
	CustomerRepository getCustomerRepo;

	@Override
	public List<Customer> saveOrUpdate(List<Customer> lstCustomer) {
		return null;
	}

	@Override
	public CustomerRepository getCustomerRepo() {
		// TODO Auto-generated method stub
		return null;
	}
}