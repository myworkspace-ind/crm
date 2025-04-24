package mks.myworkspace.crm.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.service.HappyBirthdayService;

@Slf4j
@Service
public class HappyBirthdayServiceImpl implements HappyBirthdayService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private EmailService emailService;

	@Override
	public CustomerRepository getRepo() {
		return null;
	}

	@Override
	public void sendBirthdayEmail() {
		LocalDate today = LocalDate.now();
		List<Customer> customers = customerRepository.findAll();

//		for (Customer customer : customers) {
//			if (isBirthdayToday(customer.getBirthday()) && isChotStatus(customer)) {
//				emailService.sendBirthdayCard(customer);
//			}
//		}

		for (Customer customer : customers) {
			if (isBirthdayToday(customer.getBirthday())) {
				emailService.sendBirthdayCard(customer);
			}
		}
	}

	private boolean isBirthdayToday(LocalDate birthday) {
		if (birthday == null)
			return false;
		LocalDate today = LocalDate.now();
		return birthday.getDayOfMonth() == today.getDayOfMonth() && birthday.getMonth() == today.getMonth();
	}

	private boolean isChotStatus(Customer customer) {
		return customer.getMainStatus() != null && "Chốt".equalsIgnoreCase(customer.getMainStatus().getName()); // chỉnh
	}

}
