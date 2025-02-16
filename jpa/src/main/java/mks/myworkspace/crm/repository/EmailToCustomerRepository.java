package mks.myworkspace.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import mks.myworkspace.crm.entity.EmailToCustomer;

public interface EmailToCustomerRepository extends JpaRepository<EmailToCustomer, Long>, JpaSpecificationExecutor<EmailToCustomer>{
	
}
