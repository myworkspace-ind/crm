package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.repository.OrderCategoryRepository;

public interface OrderCategoryService {
	OrderCategoryRepository getRepo();
	
	List<OrderCategory> getAllOrderCategoriesWithOrderStatuses();
	
	List<OrderCategory> findAllOrderCategory();
	
	OrderCategory findOrderCategoryById(Long id);
}
