package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mks.myworkspace.crm.entity.OrderStatus;

public interface OrderStatusRepository  extends JpaRepository<OrderStatus, String>{
	List<OrderStatus> findAll();
}
