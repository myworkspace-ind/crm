package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.repository.OrderCategoryRepositoryBao;

public interface OrderCategoryServiceBao {
	OrderCategoryRepositoryBao getRepo();
		
	List<OrderCategory> findAll();
}
