package mks.myworkspace.crm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {
	@Autowired
	AppRepository appRepo;

	@Autowired
	CustomerRepository customerRepo;

	@Override
	public CustomerRepository getCustomerRepo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppRepository getAppRepo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer saveOrUpdate(Customer customer) {
		Long id = appRepo.saveOrUpdate(customer);
		if (id != null) {
			customer.setId(id);
		}
		return customer;
	}
}