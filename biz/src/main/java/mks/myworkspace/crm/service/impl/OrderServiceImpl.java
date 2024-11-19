package mks.myworkspace.crm.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.repository.OrderRepository;
import mks.myworkspace.crm.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository repo;

	@Override
	public OrderRepository getRepo() {
		return repo;
	}

	@Override
	public List<Order> getAllOrders() {
		return repo.findAll();
	}

	@Override
	public Order getOrderById(Long orderId) {
		return repo.findById(orderId).orElse(null);
	}

	@Override
	public Optional<Order> findById(Long orderId) {
		return repo.findOrderById(orderId);
	}


}
