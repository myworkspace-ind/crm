package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.OrderStatus;
import mks.myworkspace.crm.repository.OrderStatusRepository;

public interface OrderStatusService {
	OrderStatusRepository getRepo();
	
	List<OrderStatus> findByOrderCategories_Id(Long categoryId);
	List<OrderStatus> findAllOrderStatuses ();
	List<OrderStatus> findByOrderCategories_ListId(List<Long> cartegoryIds);
	OrderStatus findStatusById(Long id);
	OrderStatus findStatusByName(String name);
}
