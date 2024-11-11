package mks.myworkspace.crm.repository;

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
}
