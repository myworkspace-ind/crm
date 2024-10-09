package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	List<Customer> findAll();
	List<Customer> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCaseOrPhoneContainingIgnoreCase(String name, String address, String phone);
}
