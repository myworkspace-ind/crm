package mks.myworkspace.crm.service;

import java.util.List;
import java.util.Optional;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.repository.CustomerRepository;

public interface CustomerService {
	CustomerRepository getRepo();
	
	List<Customer> getAllCustomers();
	List<Customer> searchCustomers(String keyword);
	List<Customer> getAllCustomersWithStatuses();
	List<Customer> findCustomersByStatus(Long statusId);
	Optional<Customer> findById(Long id);
}
