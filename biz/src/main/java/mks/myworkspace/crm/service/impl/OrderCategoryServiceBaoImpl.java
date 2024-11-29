package mks.myworkspace.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.repository.OrderCategoryRepositoryBao;
import mks.myworkspace.crm.service.OrderCategoryServiceBao;

@Service
@Transactional
@Slf4j
public class OrderCategoryServiceBaoImpl implements OrderCategoryServiceBao{
	@Autowired
	private OrderCategoryRepositoryBao repo;

	@Override
	public OrderCategoryRepositoryBao getRepo() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<OrderCategory> findAll() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}
	
	
}
