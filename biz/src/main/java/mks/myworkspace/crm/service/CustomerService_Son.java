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
	
	long getTotalCustomerCount();

	List<Customer> findCustomersAdvanced(String nameCompany, String phone, List<Long> selectedCareers,
			String contactPerson, String address, String email);
}
