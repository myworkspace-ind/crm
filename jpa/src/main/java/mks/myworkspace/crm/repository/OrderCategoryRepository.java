package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.OrderCategory;

@Repository
public interface OrderCategoryRepository  extends JpaRepository<OrderCategory, Long>{
	@Query("SELECT DISTINCT o FROM OrderCategory o LEFT JOIN FETCH o.orderStatuses")
	List<OrderCategory> findAllWithOrderStatuses();
	
	List<OrderCategory> findAll();
}
