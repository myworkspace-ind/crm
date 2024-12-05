package mks.myworkspace.crm.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Interaction;
import mks.myworkspace.crm.repository.CustomerRepository;

public interface CustomerService {
	CustomerRepository getRepo();

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
	
	List<Interaction> getAllCustomerInteraction(Long customerID);
	
	List<Interaction> saveOrUpdateInteraction(List<Interaction> entities);
	
	void deleteInteractionById(Long interactionId);
}
