package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.OrderStatus;

@Repository
public interface OrderStatusRepository  extends JpaRepository<OrderStatus, Long>{
	@Query("SELECT os FROM OrderStatus os JOIN os.orderCategories oc WHERE oc.id = :orderCategoryId")
	List<OrderStatus> findByOrderCategoryId (@Param("orderCategoryId") Long orderCategoryId);
	
	@Query("SELECT os FROM OrderStatus os JOIN os.orderCategories oc WHERE oc.id IN :orderCategoryIds")
	List<OrderStatus> findByOrderCategoryListId (@Param("orderCategoryIds") List<Long> orderCategoryIds);
	
}
