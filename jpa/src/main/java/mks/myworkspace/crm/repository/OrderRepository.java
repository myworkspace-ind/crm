package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mks.myworkspace.crm.entity.Order;

public interface OrderRepository  extends JpaRepository<Order, String>{
	List<Order> findAll();
}
