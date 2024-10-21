package mks.myworkspace.crm.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.service.CustomerService;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository repo;
	
	@Autowired
	private AppRepository apprepo;

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
		log.debug("Saving customer: " + customer.toString());
		Customer savedCustomer = repo.save(customer);
		log.debug("Customer saved with ID: " + savedCustomer.getId()); // Sau khi lưu
		return savedCustomer;
	}

	@Override
	public Long getNextCustomerId() {
		Long maxId = repo.findMaxId();
		return maxId + 1; // Trả về ID mới
	}

}
