package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.repository.CustomerCareRepository;

public interface CustomerCareService {
	CustomerCareRepository getRepo();
	
	void loadPotentialCustomersIntoCustomerCare();
	
	void createRemindersForCustomer(Customer customer);
	
	List<CustomerCare> findAll();
	
	// Hiện lên danh sách khách hàng cần chăm sóc thì lấy từ Customer
	// Lưu lại/nạp vào customer care thì mới dùng dữ liệu từ customer care
	
	List<Customer> findAllCustomerCare();
	
	boolean existsInCustomerCares(Long customerId);
	
	boolean checkCustomerCareIDExists(Long customerCareId);
}
