package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.repository.OrderRepository;

public interface OrderService {
	OrderRepository getRepo();
	
	List<Order> getAllOrders();
	
}
