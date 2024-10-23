package mks.myworkspace.crm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.repository.CustomerRepository;

public interface CustomerService {
	CustomerRepository getRepo();

	List<Customer> getAllCustomers();

	List<Customer> searchCustomers(String keyword);

	List<Customer> getAllCustomersWithStatuses();

	List<Customer> findCustomersByStatus(Long statusId);

	Optional<Customer> findById(Long id);

	Long getNextCustomerId();

	Customer createCustomer(Customer customer);

	void deleteCustomersByIds(Iterable<Long> ids);

}
