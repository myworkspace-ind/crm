package mks.myworkspace.crm.service;

import mks.myworkspace.crm.entity.EmailToCustomer;
import mks.myworkspace.crm.repository.EmailToCustomerRepository;

public interface EmailToCustomerService {
	EmailToCustomerRepository getRepo();
	
	EmailToCustomer createEmailToCustomer(EmailToCustomer emailToCustomer);
}
