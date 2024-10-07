package mks.myworkspace.crm.service.impl;

import java.util.List;

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
	public List<Customer> saveOrUpdate(List<Customer> lstCustomers) {
		List<Long> lstIds = appRepo.saveOrUpdate(lstCustomers);

		// Update the Id of saved task
		int len = (lstIds != null) ? lstIds.size() : 0;
		for (int i = 0; i < len; i++) {
			lstCustomers.get(i).setId(lstIds.get(i));
		}

		return lstCustomers;
	}

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
}