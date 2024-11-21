package mks.myworkspace.crm.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.repository.CustomerRepository_Son;

public interface CustomerService_Son {
	CustomerRepository_Son getRepo();

	List<Customer> getAllCustomers();

	List<Customer> searchCustomers(String keyword);

	List<Customer> getAllCustomersWithStatuses();

	List<Customer> findCustomersByStatus(Long statusId);

	Optional<Customer> findById(Long id);

	Customer createCustomer(Customer customer);

	void deleteCustomersByIds(Iterable<Long> ids);
	
	void deleteAllByIds(List<Long> customerIds);
	
	Map<Long, Long> getCustomerCountsByStatus();
	
	List<Customer> findCustomerByCompanyName(String cpname);
	
	List<Customer> findByCompanyName(String keyword);
	
	List<Customer> findByContactPerson(String keyword);
	
	List<Customer> findByEmail(String keyword);
	
	List<Customer> findByPhoneNew(String keyword);

	List<Customer> findByAddress(String keyword);

	
	long getTotalCustomerCount();
}
