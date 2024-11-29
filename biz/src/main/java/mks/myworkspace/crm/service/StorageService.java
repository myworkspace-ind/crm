package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.repository.OrderRepository;

public interface StorageService {
	AppRepository getAppRepo();
	
	OrderRepository getOrderRepo();
	
	CustomerRepository getCustomerRepo();
	
	public Customer saveOrUpdate(Customer customer);
	
	List<Customer> saveOrUpdate(List<Customer> lstCustomers);
	
	void deleteCustomersByIds(List<Long> customerIds);
	
	public Order saveOrUpdateOrder(Order order);
	
	void deleteOrderById(Long orderId);
	
	public Order updateOrderStatus(Order order);

}
