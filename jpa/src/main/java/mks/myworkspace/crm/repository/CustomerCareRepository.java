package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;

public interface CustomerCareRepository extends JpaRepository<CustomerCare, Long>, JpaSpecificationExecutor<CustomerCare> {
	@Query("SELECT c FROM Customer c LEFT JOIN Interaction i ON c.id = i.customer.id " +
			"WHERE c.mainStatus.name = 'Mới' AND i.id IS NULL")
	List<Customer> findPotentialCustomers();
	
	@Query("SELECT cc FROM CustomerCare cc JOIN FETCH cc.customer")
    List<CustomerCare> findAllCustomerCares();
}
