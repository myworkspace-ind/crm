package mks.myworkspace.crm.repository;

import java.util.Date;
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

    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.mainStatus ms LEFT JOIN FETCH c.subStatus ss " +
           "WHERE c.accountStatus = true AND (" +
           "LOWER(c.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.contactPerson) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(ms.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(ss.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.responsiblePerson.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.profession.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.note) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Customer> searchCustomers(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.mainStatus ms LEFT JOIN FETCH c.subStatus ss WHERE c.accountStatus = true")
    List<Customer> findAllWithStatuses();

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.mainStatus ms LEFT JOIN FETCH c.subStatus ss WHERE (c.mainStatus.id = :statusId OR c.subStatus.id = :statusId) AND c.accountStatus = true")
    List<Customer> findByStatusId(@Param("statusId") Long statusId);
    
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.mainStatus ms LEFT JOIN FETCH c.subStatus ss WHERE c.id = :id AND c.accountStatus = true")
    Optional<Customer> findById(@Param("id") Long id);
    
    @Query("SELECT COALESCE(MAX(c.id), 0) FROM Customer c")
    Long findMaxId();
    
    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Optional<Customer> findByEmail(@Param("email") String email);
    
    @Query("SELECT c FROM Customer c WHERE c.phone = :phone")
    Optional<Customer> findByPhone(@Param("phone") String phone);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Customer c WHERE c.id IN :customerIds")
    void deleteAllByIds(@Param("customerIds") List<Long> customerIds);
    
    @Query("SELECT ms.id, COUNT(c.id) " +
            "FROM Customer c LEFT JOIN c.mainStatus ms " +
            "WHERE ms IS NOT NULL AND c.accountStatus = true " +
            "GROUP BY ms.id")
     List<Object[]> countCustomersByMainStatus();
		
	@Query("SELECT ci FROM Interaction ci WHERE ci.customer.id = :customerId")
	List<Interaction> getAllCustomerInteraction(@Param("customerId") Long customerId);	

    @Query("SELECT ss.id, COUNT(c.id) " +
           "FROM Customer c LEFT JOIN c.subStatus ss " +
           "WHERE ss IS NOT NULL AND c.accountStatus = true " +
           "GROUP BY ss.id")
    List<Object[]> countCustomersBySubStatus();
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.accountStatus = true")
    long countAllCustomers();
    
	@Query("SELECT c FROM Customer c "
	        + "LEFT JOIN Interaction ci ON c.id = ci.customer.id "
	        + "WHERE ci.interaction_date BETWEEN :startDate AND :endDate")
	List<Customer> findByInteractDateRange(@Param("startDate") Date startDate,@Param("endDate") Date endDate);
}
