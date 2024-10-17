package mks.myworkspace.crm.service.impl;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.service.CustomerService;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	private CustomerRepository repo;
	

	@Override
	public CustomerRepository getRepo() {
		return repo;
	}

	@Override
	public List<Customer> getAllCustomers() {	
		return repo.findAll();
	}

	@Override
	public List<Customer> searchCustomers(String keyword) {
		return repo.searchCustomers(keyword, keyword, keyword, keyword);
	}

	@Override
	public List<Customer> getAllCustomersWithStatuses() {
		 return repo.findAllWithStatuses();
	}

	@Override
	public List<Customer> findCustomersByStatus(Long statusId) {
		return repo.findByStatusId(statusId);
	}

	@Override
	public Optional<Customer> findById(Long id) {
		return repo.findById(id);
	}
	
	
	@Transactional
	@Override
    public Customer createCustomer(Customer customer) {
        log.debug("Received customer data: {}", customer);
        Customer savedCustomer = repo.save(customer);
        log.debug("Saved customer: {}", savedCustomer);
        return savedCustomer;
    }
	

}
