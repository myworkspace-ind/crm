package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mks.myworkspace.crm.entity.OrderCategory;

public interface OrderCategoryRepository  extends JpaRepository<OrderCategory, Long>{
	@Query("SELECT DISTINCT o FROM OrderCategory o LEFT JOIN FETCH o.orderStatuses")
	List<OrderCategory> findAllWithOrderStatuses();
}
