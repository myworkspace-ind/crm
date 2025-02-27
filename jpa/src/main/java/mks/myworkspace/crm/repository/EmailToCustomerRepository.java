package mks.myworkspace.crm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.EmailToCustomer;

@Repository
public interface EmailToCustomerRepository extends JpaRepository<EmailToCustomer, Long>, JpaSpecificationExecutor<EmailToCustomer>{
	@Query("SELECT etc FROM EmailToCustomer etc WHERE etc.customer.id = :customerId")
	List<EmailToCustomer> getAllEmailToCustomer(@Param("customerId") Long customerId);	
	
	@Query("SELECT etc FROM EmailToCustomer etc WHERE etc.id = :id")
	Optional<EmailToCustomer> findEmailToCustomerById(@Param("id") Long id);
}
