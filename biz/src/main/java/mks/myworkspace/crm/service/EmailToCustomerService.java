package mks.myworkspace.crm.service;

import java.util.List;
import java.util.Optional;

import mks.myworkspace.crm.entity.EmailToCustomer;
import mks.myworkspace.crm.repository.EmailToCustomerRepository;

public interface EmailToCustomerService {
	EmailToCustomerRepository getRepo();
		
	List<EmailToCustomer> getAllEmailToCustomer(Long customerID);
	
	Optional<EmailToCustomer> GetEmailToCustomerByID (Long emailToCustomerID);
		
}
