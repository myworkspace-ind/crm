package mks.myworkspace.crm.repository;

import java.sql.Date;
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
		       + "AND (:orderCategoryId IS NULL OR o.orderCategory.id = :orderCategoryId) "
		       + "AND (:statuses IS NULL OR o.orderStatus.id IN :statuses)")
		List<Order> findOrderByCriteria(@Param("customerId") Long customerId,
		                                @Param("orderCategoryId") Long orderCategoryId,
		                                @Param("statuses") List<Long> statuses);
	@Query("SELECT DISTINCT code FROM Order")
	List<String> findAllCodeOrders();
	
	@Query("SELECT o FROM Order o WHERE "
		       + "(COALESCE(:customerIds, NULL) IS NULL OR o.sender.id IN :customerIds OR o.receiver.id IN :customerIds) "
		       + "AND (COALESCE(:orderCategoryIds, NULL) IS NULL OR o.orderCategory.id IN :orderCategoryIds) "
		       + "AND (COALESCE(:statuses, NULL) IS NULL OR o.orderStatus.id IN :statuses)"
		       + "AND NOT(:create_date IS NOT NULL AND o.createDate > :delivery_date)"
		       + "AND NOT(:delivery_date IS NOT NULL AND o.deliveryDate < :create_date)")
		List<Order> findOrderByList(
		    @Param("customerIds") List<Long> customerIds,
		    @Param("orderCategoryIds") List<Long> orderCategoryIds,
		    @Param("statuses") List<Long> statuses,
		    @Param("create_date") Optional<Date> create_date,
		    @Param("delivery_date") Optional<Date> delivery_date
		);


}
