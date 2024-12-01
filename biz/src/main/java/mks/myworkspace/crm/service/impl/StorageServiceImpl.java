package mks.myworkspace.crm.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.repository.OrderCategoryRepository;
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
	OrderCategoryRepository orderCategoryRepository;

	@Autowired
	@Getter
	OrderRepository orderRepo;

	@Autowired
	@Getter
	CustomerRepository customerRepo;

//	@Override
//	public CustomerRepository getCustomerRepo() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public AppRepository getAppRepo() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public Customer saveOrUpdate(Customer customer) {
		Optional<Customer> existingEmail = customerRepo.findByEmail(customer.getEmail());
	    if (existingEmail.isPresent()) {
	        throw new IllegalArgumentException("Email đã được đăng ký trước đó. Vui lòng thử lại!");
	    }
		
		Optional<Customer> existingPhone = customerRepo.findByPhone(customer.getPhone());
	    if (existingPhone.isPresent()) {
	        throw new IllegalArgumentException("Số điện thoại đã được đăng ký trước đó. Vui lòng thử lại!");
	    }
	    
	    if (!isValidPhoneNumber(customer.getPhone())) {
	        throw new IllegalArgumentException("Số điện thoại không đúng định dạng. Vui lòng nhập lại!");
	    }

	    if (!isValidEmail(customer.getEmail())) {
	        throw new IllegalArgumentException("Email không đúng định dạng. Vui lòng nhập lại!");
	    }

	    Long id = appRepo.saveOrUpdate(customer);
	    if (id != null) {
	        customer.setId(id);
	    }
	    return customer;
	}

	private boolean isValidPhoneNumber(String phoneNumber) {
	    String phoneRegex = "^[0-9]{10}$";
	    return phoneNumber != null && phoneNumber.matches(phoneRegex);
	}

	private boolean isValidEmail(String email) {
	    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";  
	    return email != null && email.matches(emailRegex);
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

	@Override
	public void deleteOrderById(Long orderId) {
		appRepo.deleteOrderById(orderId);
	}

	@Override
	public Order updateOrderStatus(Order order) {
		log.debug("Processing Order with ID: {}", order.getId());

		Long id = appRepo.updateOrderStatus(order);
		if (id != null) {
			order.setId(id);
		}

		log.debug("Final Order ID after saveOrUpdate: {}", order.getId());
		return order;
	}

	@Override
	public List<OrderCategory> saveOrUpdateOrderCategory(List<OrderCategory> lstOrderCategories) {
		List<Long> lstIds = appRepo.saveOrUpdateOrderCategory(lstOrderCategories);

		// Update the Id of saved task
		int len = (lstIds != null) ? lstIds.size() : 0;
		for (int i = 0; i < len; i++) {
			lstOrderCategories.get(i).setId(lstIds.get(i));
		}

		return lstOrderCategories;
	}
}