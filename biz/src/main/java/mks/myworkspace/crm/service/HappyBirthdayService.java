package mks.myworkspace.crm.service;

import mks.myworkspace.crm.repository.CustomerRepository;

public interface HappyBirthdayService {
	CustomerRepository getRepo();
	
	void sendBirthdayEmail();
}
