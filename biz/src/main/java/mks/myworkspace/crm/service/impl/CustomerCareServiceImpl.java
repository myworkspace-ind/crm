package mks.myworkspace.crm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerCareRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.service.CustomerCareService;

@Service
@Transactional
@Slf4j
public class CustomerCareServiceImpl implements CustomerCareService{
	@Autowired 
	CustomerCareRepository repo;
	
	@Autowired
	AppRepository appRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	

	@Override
	public CustomerCareRepository getRepo() {
		return repo;
	}

	@Override
	public void loadPotentialCustomersIntoCustomerCare() {
		try {
            List<Customer> potentialCustomers = customerRepository.findPotentialCustomers();

            if (potentialCustomers.isEmpty()) {
                throw new RuntimeException("Không có khách hàng tiềm năng nào để nạp vào CustomerCare.");
            }

            List<CustomerCare> customerCares = potentialCustomers.stream()
                .map(customer -> new CustomerCare(null, customer, null, null, null)) // ID tự động tăng, các trường còn lại null
                .collect(Collectors.toList());

            appRepository.insertCustomerCare(customerCares);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi nạp khách hàng tiềm năng vào CustomerCare: " + e.getMessage());
        }
		
	}

	@Override
	public List<CustomerCare> findAll() {
		return repo.findAllCustomerCares();
	}

}
