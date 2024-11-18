package mks.myworkspace.crm.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.repository.OrderRepository;
import mks.myworkspace.crm.service.StorageService;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {
	@Autowired
	@Getter
	AppRepository appRepo;

	@Autowired
	@Getter
	OrderRepository orderRepo;

	@Autowired
	CustomerRepository customerRepo;

	@Override
	public CustomerRepository getCustomerRepo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppRepository getAppRepo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer saveOrUpdate(Customer customer) {
		// Kiểm tra số điện thoại đã tồn tại chưa
		Optional<Customer> existingCustomer = customerRepo.findByPhone(customer.getPhone());

		if (existingCustomer.isPresent()) {
			throw new IllegalArgumentException("Số điện thoại đã được đăng ký trước đó. Vui lòng thử lại!");
		}

		if (customer.getPhone().length() != 10) {
			throw new IllegalArgumentException("Số điện thoại chưa đúng định dạng. Vui lòng nhập lại!");
		}

		Long id = appRepo.saveOrUpdate(customer);
		if (id != null) {
			customer.setId(id);
		}
		return customer;
	}

	@Override
	public List<Customer> saveOrUpdate(List<Customer> lstCustomer) {
		List<Long> lstIds = appRepo.saveOrUpdate(lstCustomer);

		// Update the Id of saved task
		int len = (lstIds != null) ? lstIds.size() : 0;
		for (int i = 0; i < len; i++) {
			lstCustomer.get(i).setId(lstIds.get(i));
		}

		return lstCustomer;
	}

	@Override
	public void deleteCustomersByIds(List<Long> customerIds) {
		appRepo.deleteCustomersByIds(customerIds);

	}

	@Override
	public Order saveOrUpdateOrder(Order order) {

		log.debug("Processing Order with ID: {}", order.getId()); // Check the ID before saving

		Long id = appRepo.saveOrUpdateOrder(order); // This should return the ID of the saved or updated order
		if (id != null) {
			order.setId(id);
		}

		log.debug("Final Order ID after saveOrUpdate: {}", order.getId()); // Log the final ID after save/update
		return order;
	}
}