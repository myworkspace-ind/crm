package mks.myworkspace.crm.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.repository.OrderRepository;

public interface OrderService {
	OrderRepository getRepo();

	List<Order> getAllOrders();

	Order getOrderById(Long orderId);
	
	Optional<Order> findById(Long orderId);
	
	List<Order> searchOrders(Long customerId, Long orderCategoryId, List<Long> statuses);
	
	List<String> findAllCodeOrders();
	
	List<Order> searchOrdersByList(List<Long> customerIds, List<Long> orderCategoryIds, List<Long> statuses, 
									Optional<Date> create_date, Optional<Date> delivery_date);
}
