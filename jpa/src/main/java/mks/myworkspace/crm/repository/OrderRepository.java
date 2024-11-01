package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mks.myworkspace.crm.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findAll();

//	@Query("SELECT DISTINCT o FROM Order o " + "LEFT JOIN FETCH o.goodsCategory " + " LEFT JOIN FETCH o.customer "
//			+ "LEFT JOIN FETCH o.orderCategory " + "LEFT JOIN FETCH o.orderStatus")
//	List<Order> findAllWithAssociations();
}
