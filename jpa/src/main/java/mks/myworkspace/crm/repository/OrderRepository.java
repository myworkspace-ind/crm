package mks.myworkspace.crm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mks.myworkspace.crm.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
//	@Query("SELECT DISTINCT o FROM Order o " + "LEFT JOIN FETCH o.goodsCategory " + " LEFT JOIN FETCH o.customer "
//			+ "LEFT JOIN FETCH o.orderCategory " + "LEFT JOIN FETCH o.orderStatus")
//	List<Order> findAllWithAssociations();
	@Query("SELECT o FROM Order o WHERE o.code = :code")
	Optional<Order> findByCode(@Param("code") String code);
	
	
	@Query("SELECT o FROM Order o WHERE o.id = :id")
	Optional<Order> findOrderById(@Param("id") Long id);
	
	@Query("SELECT o FROM Order o WHERE "
			+ "(:customerId IS NULL OR o.sender.id = :customerId OR o.receiver.id = :customerId) "
			+ "AND (:orderCategoryId IS NULL OR o.orderCategory.id = :orderCategoryId)")
	List<Order> findOrderByCriteria(@Param("customerId") Long customerId,
									@Param("orderCategoryId") Long orderCategoryId);
	
}
