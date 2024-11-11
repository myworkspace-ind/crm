package mks.myworkspace.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.OrderRepository;
import mks.myworkspace.crm.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository repo;
	
	@Autowired
	private AppRepository appRepository;

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
	public List<Long> saveOrUpdateOrders(List<Order> orders) {
		return appRepository.saveOrUpdateOrder(orders);
	}


}
