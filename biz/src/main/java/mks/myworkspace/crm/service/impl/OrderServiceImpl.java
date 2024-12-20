package mks.myworkspace.crm.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.repository.OrderRepository;
import mks.myworkspace.crm.service.OrderService;

@Slf4j
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

	@Override
	public List<Order> searchOrders(Long customerId, Long orderCategoryId, List<Long> statuses) {
		log.debug("CustomerId: {}, Type: {}", customerId, (customerId != null ? customerId.getClass().getName() : "null"));
		
	    log.debug("OrderCategoryId: {}, Type: {}", orderCategoryId, (orderCategoryId != null ? orderCategoryId.getClass().getName() : "null"));
	    if (statuses != null) {
	        statuses.forEach(status -> log.debug("Status: {}, Type: {}", status, (status != null ? status.getClass().getName() : "null")));
	    } else {
	        log.debug("Statuses: null");
	    }
	    if (statuses == null || statuses.isEmpty()) {
	        statuses = null; 
	    }
		return repo.findOrderByCriteria(customerId, orderCategoryId, statuses);
	}

	@Override
	public List<String> findAllCodeOrders() {
		// TODO Auto-generated method stub
		return repo.findAllCodeOrders();
	}

	@Override
	public List<Order> searchOrdersByList(List<Long> customerIds, List<Long> orderCategoryIds, List<Long> statuses,
			Optional<Date> create_date, Optional<Date> delivery_date) {
		// TODO Auto-generated method stub
		if (customerIds == null || customerIds.isEmpty()) {
			customerIds = null; 
	    }
		if (orderCategoryIds == null || orderCategoryIds.isEmpty()) {
			orderCategoryIds = null; 
	    }
		if (statuses == null || statuses.isEmpty()) {
	        statuses = null; 
	    }
		return repo.findOrderByList(customerIds, orderCategoryIds, statuses, create_date, delivery_date);
	}


}
