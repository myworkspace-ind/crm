package mks.myworkspace.crm.service;

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
}
