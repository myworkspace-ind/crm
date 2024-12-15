package mks.myworkspace.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.OrderStatus;
import mks.myworkspace.crm.repository.OrderStatusRepository;
import mks.myworkspace.crm.service.OrderStatusService;

@Service
@Transactional
@Slf4j
public class OrderStatusServiceImpl implements OrderStatusService{
	@Autowired
	OrderStatusRepository repo;

	@Override
	public OrderStatusRepository getRepo() {
		return repo;
	}

	@Override
	public List<OrderStatus> findByOrderCategories_Id(Long categoryId) {
		return repo.findByOrderCategoryId(categoryId);
	}

	@Override
	public List<OrderStatus> findAllOrderStatuses() {
		return repo.findAll();
	}

	@Override
	public OrderStatus findStatusById(Long id) {
		// TODO Auto-generated method stub
		return repo.findById(id).get();
	}

	@Override
	public OrderStatus findStatusByName(String name) {
		// TODO Auto-generated method stub
		return repo.findByNameIgnoreCase(name);
	}

}
