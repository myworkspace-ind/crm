package mks.myworkspace.crm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Interaction;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAll();

    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.mainStatus ms LEFT JOIN FETCH c.subStatus ss WHERE "
            + "(LOWER(c.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(ms.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(ss.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(c.responsiblePerson) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(c.note) LIKE LOWER(CONCAT('%', :keyword, '%')))") 
    List<Customer> searchCustomers(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.mainStatus ms LEFT JOIN FETCH c.subStatus ss")
    List<Customer> findAllWithStatuses();

    @Query("SELECT c FROM Customer c WHERE c.mainStatus.id = :statusId OR c.subStatus.id = :statusId")
    List<Customer> findByStatusId(@Param("statusId") Long statusId);
    
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.mainStatus ms LEFT JOIN FETCH c.subStatus ss WHERE c.id = :id")
    Optional<Customer> findById(@Param("id") Long id);
    
    @Query("SELECT COALESCE(MAX(c.id), 0) FROM Customer c")
    Long findMaxId();
    
    @Query("SELECT c FROM Customer c WHERE c.phone = :phone")
    Optional<Customer> findByPhone(@Param("phone") String phone);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Customer c WHERE c.id IN :customerIds")
    void deleteAllByIds(@Param("customerIds") List<Long> customerIds);
    
    @Query("SELECT ms.id, COUNT(c.id) " +
	       "FROM Customer c LEFT JOIN c.mainStatus ms " +
	       "WHERE ms IS NOT NULL " +
	       "GROUP BY ms.id")
	List<Object[]> countCustomersByMainStatus();
	
	@Query("SELECT ss.id, COUNT(c.id) " +
		       "FROM Customer c LEFT JOIN c.subStatus ss " +
		       "WHERE ss IS NOT NULL " +
		       "GROUP BY ss.id")
	List<Object[]> countCustomersBySubStatus();
	
	@Query("SELECT COUNT(c) FROM Customer c")
	long countAllCustomers();
	
	@Query("SELECT ci FROM Interaction ci WHERE ci.customer.id = :customerId")
	List<Interaction> getAllCustomerInteraction(@Param("customerId") Long customerId);	

}
