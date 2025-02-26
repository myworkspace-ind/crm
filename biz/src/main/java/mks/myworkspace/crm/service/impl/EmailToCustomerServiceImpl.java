package mks.myworkspace.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.EmailToCustomer;
import mks.myworkspace.crm.repository.EmailToCustomerRepository;
import mks.myworkspace.crm.service.EmailToCustomerService;

@Service
@Transactional
@Slf4j
public class EmailToCustomerServiceImpl implements EmailToCustomerService {
	@Autowired
	EmailToCustomerRepository emailToCustomerRepository;

	@Override
	public EmailToCustomerRepository getRepo() {
		return emailToCustomerRepository;
	}

	@Override
	public List<EmailToCustomer> getAllEmailToCustomer(Long customerID) {
		return emailToCustomerRepository.getAllEmailToCustomer(customerID);
	}

}
