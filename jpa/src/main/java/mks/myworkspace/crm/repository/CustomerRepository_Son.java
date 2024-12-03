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

@Repository
public interface CustomerRepository_Son extends JpaRepository<Customer, Long> {

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

    
    @Query("SELECT DISTINCT c FROM Customer c " +
    	       "LEFT JOIN FETCH c.mainStatus ms " +
    	       "LEFT JOIN FETCH c.subStatus ss " +
    	       "WHERE " +
    	       "(LOWER(c.companyName) LIKE LOWER(CONCAT('%', :nameCompany, '%')) OR :nameCompany IS NULL) AND " +
    	       "(LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%')) OR :phone IS NULL) AND " +
    	       "((c.profession.id) IN (:selectedCareers)) AND " +
    	       "(LOWER(c.contactPerson) LIKE LOWER(CONCAT('%', :contactPerson, '%')) OR :contactPerson IS NULL) AND " +
    	       "(LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%')) OR :address IS NULL) AND " +
    	       "(LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')) OR :email IS NULL)")
    List<Customer> advancedSearchCustomers(
    	       @Param("nameCompany") String nameCompany,
    	       @Param("phone") String phone,
    	       @Param("selectedCareers") List<Long> selectedCareers,
    	       @Param("contactPerson") String contactPerson,
    	       @Param("address") String address,
    	       @Param("email") String email);


    @Query("SELECT DISTINCT c FROM Customer c " +
 	       "LEFT JOIN FETCH c.mainStatus ms " +
 	       "LEFT JOIN FETCH c.subStatus ss " +
 	       "WHERE " +
 	       "(LOWER(c.companyName) LIKE LOWER(CONCAT('%', :nameCompany, '%')) OR :nameCompany IS NULL) AND " +
 	       "(LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%')) OR :phone IS NULL) AND " +
 	       "((c.profession.id) IN (:selectedCareers) OR :selectedCareers IS NULL OR :selectedCareers = '') AND " +
 	       "(LOWER(c.contactPerson) LIKE LOWER(CONCAT('%', :contactPerson, '%')) OR :contactPerson IS NULL) AND " +
 	       "(LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%')) OR :address IS NULL) AND " +
 	       "(LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')) OR :email IS NULL)")
    List<Customer> advancedSearchCustomersNotCareer(
 	       @Param("nameCompany") String nameCompany,
 	       @Param("phone") String phone,
 	       @Param("selectedCareers") List<Long> selectedCareers,
 	       @Param("contactPerson") String contactPerson,
 	       @Param("address") String address,
 	       @Param("email") String email);
    
    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.mainStatus ms LEFT JOIN FETCH c.subStatus ss WHERE ((c.profession.id) IN (:selectedCareers))")
    List<Customer> findByselectedCareers(@Param("selectedCareers") List<Long> selectedCareers);
    
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

}
