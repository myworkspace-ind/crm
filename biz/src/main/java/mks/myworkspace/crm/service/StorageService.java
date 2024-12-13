package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.repository.OrderCategoryRepository;
import mks.myworkspace.crm.repository.OrderRepository;

public interface StorageService {
	AppRepository getAppRepo();
	
	OrderRepository getOrderRepo();
	
	OrderCategoryRepository getOrderCategoryRepository();
	
	CustomerRepository getCustomerRepo();
	
	public Customer saveOrUpdate(Customer customer);
	
	List<Customer> saveOrUpdate(List<Customer> lstCustomers);
	
	void hideCustomersByIds(List<Long> customerIds);
	
	void showHidedCustomers();
	
	public Order saveOrUpdateOrder(Order order);
	
	void deleteOrderById(Long orderId);
	
	public Order updateOrderStatus(Order order);
	public Customer updateCustomerStatus (Customer customer);
	
	List<OrderCategory> saveOrUpdateOrderCategory(List<OrderCategory> lstOrderCategories);

}
