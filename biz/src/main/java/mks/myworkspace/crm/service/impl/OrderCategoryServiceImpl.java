package mks.myworkspace.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.repository.OrderCategoryRepository;
import mks.myworkspace.crm.service.OrderCategoryService;

@Service
@Transactional
@Slf4j
public class OrderCategoryServiceImpl implements OrderCategoryService{
	@Autowired
	private OrderCategoryRepository repo;
	
	@Override
	public OrderCategoryRepository getRepo() {
		return repo;
	}

	@Override
	public List<OrderCategory> getAllOrderCategoriesWithOrderStatuses() {
		return repo.findAllWithOrderStatuses();
	}

	@Override
	public List<OrderCategory> findAll() {
		return repo.findAll();
	}

}
