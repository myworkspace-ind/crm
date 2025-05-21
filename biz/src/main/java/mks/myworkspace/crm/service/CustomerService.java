package mks.myworkspace.crm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Interaction;
import mks.myworkspace.crm.entity.dto.CustomerCriteriaDTO;
import mks.myworkspace.crm.entity.dto.InteractionDTO;
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
	
//	List<Interaction> getAllCustomerInteraction(Long customerID);
	List<InteractionDTO> getAllCustomerInteractionWithFiles(Long customerID);
	
	List<Interaction> saveOrUpdateInteraction(List<Interaction> entities);
	
	void deleteInteractionById(Long interactionId);

	List<Customer> findByInteractDateRange(Date startDate, Date enDate);
	
	Page<Customer> findAllWithSpecs(Pageable pageable, CustomerCriteriaDTO customerCriteriaDTO);
	
	Page<Customer> findAllWithStatuses(Pageable pageable);
	
	Page<Customer> findAllKeyword(Pageable pageable, String keyword);

	Optional<Customer> findById_ForCustomerCare(Long customerId);
	
	List<Customer> findCustomersAdvanced(String nameCompany, String phone, List<Long> selectedCareers,
			String contactPerson, String address, String email);
	
	List<Customer> advancedSearchCustomersNotCareer(String nameCompany, String phone, List<Long> selectedCareers,
			String contactPerson, String address, String email);
	
	List<Customer> findByselectedCareers(List<Long> selectedCareers);
	
//	List<Customer> findPotentialCustomers();
}
