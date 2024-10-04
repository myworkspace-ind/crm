package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mks.myworkspace.crm.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	List<Customer> findAll();
}
