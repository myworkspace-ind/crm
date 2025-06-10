package mks.myworkspace.crm.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;

/**
 * Repository interface for managing {@link CustomerCare} entities.
 * <p>
 * This repository and others repositories with name structure like: {@link CustomerRepository}, {@link OrderStatusRepository}, ...
 * are just used for querying and managing data (SELECT)
 *
 * @author Nguyen Hoang Phuong Ngan
 * @version 1.0 
 */
@Repository
public interface CustomerCareRepository extends JpaRepository<CustomerCare, Long>, JpaSpecificationExecutor<CustomerCare> {
	@Query("SELECT cc.careStatus, COUNT(cc) FROM CustomerCare cc GROUP BY cc.careStatus")
	List<Object[]> countByCareStatus();	
	
	//Chỉ mới xét trường hợp khách hàng Mới và chưa có Interaction hoặc có thời gian tạo mới interaction > thời gian nhắc nhở trong customer care
	
	//TODO: Lấy khách hàng "Mới"
    @Query("SELECT c FROM Customer c " +
            "LEFT JOIN CustomerCare cc ON c.id = cc.customer.id " +
            "WHERE c.mainStatus.name = 'New' " +
            "AND NOT EXISTS ( " +
            "    SELECT 1 FROM Interaction iSub WHERE iSub.customer.id = c.id " +
            ") " +
            "AND c.createdAt <= :caseDaysAgo_1")
     List<Customer> findNewCustomersWithEmptyInteraction(@Param("caseDaysAgo_1") LocalDateTime caseDaysAgo_1);
//	@Query("SELECT c FROM Customer c " + 
//		       "LEFT JOIN CustomerCare cc ON c.id = cc.customer.id " +
//		       "WHERE ( " +
//		       "    c.mainStatus.name = 'Mới' AND " +
//		       "    NOT EXISTS ( " +
//		       "        SELECT 1 FROM Interaction iSub WHERE iSub.customer.id = c.id " +
//		       "    ) AND " +
//		       "    c.createdAt <= :caseDaysAgo_1 " +
//		       ") " +
//		       "OR ( " +
//		       "    cc.remindDate IS NOT NULL AND cc.remindDate <= :now " +
//		       ")")
//	List<Customer> findNewCustomersWithEmptyInteraction(
//	    @Param("caseDaysAgo_1") LocalDateTime caseDaysAgo_1,
//	    @Param("now") LocalDateTime now
//	);
	
	 @Query("SELECT c FROM Customer c " +
	           "WHERE c.mainStatus.name = 'Potential' " +
	           "AND EXISTS ( " +
	           "    SELECT 1 FROM Interaction iSub " +
	           "    WHERE iSub.customer.id = c.id " +
	           "    GROUP BY iSub.customer.id " +
	           "    HAVING MAX(iSub.createdAt) < :caseDaysAgo_2 " +
	           ")")
	 List<Customer> findPotentialCustomers(@Param("caseDaysAgo_2") LocalDateTime caseDaysAgo_2);
	 
	 @Query("SELECT c FROM Customer c " +
		       "JOIN FETCH c.mainStatus " +
		       "WHERE c.mainStatus.name = 'New' " +
		       "AND EXISTS ( " +
		       "    SELECT 1 FROM Interaction iSub " +
		       "    WHERE iSub.customer.id = c.id " +
		       "    GROUP BY iSub.customer.id " +
		       "    HAVING MAX(iSub.createdAt) < :caseDaysAgo_3 " +
		       ")")
	List<Customer> findNewCustomersWithInteractionNotNull(@Param("caseDaysAgo_3") LocalDateTime caseDaysAgo_3);
	
//	@Query("SELECT c FROM Customer c " +
//		       "LEFT JOIN CustomerCare cc ON c.id = cc.customer.id " +
//		       "WHERE ( " +
//		       "    c.mainStatus.name = 'Tiềm năng' AND " +
//		       "    EXISTS ( " +
//		       "        SELECT 1 FROM Interaction iSub " +
//		       "        WHERE iSub.customer.id = c.id " +
//		       "        GROUP BY iSub.customer.id " +
//		       "        HAVING MAX(iSub.createdAt) <= :caseDaysAgo_2 " +
//		       "    ) " +
//		       ") " +
//		       "OR ( " +
//		       "    cc.remindDate IS NOT NULL AND cc.remindDate <= :now " +
//		       ")")
//	List<Customer> findPotentialCustomers(
//	    @Param("caseDaysAgo_2") LocalDateTime caseDaysAgo_2,
//	    @Param("now") LocalDateTime now
//	);

	
//	@Query("SELECT c FROM Customer c " +
//		       "LEFT JOIN CustomerCare cc ON c.id = cc.customer.id " +
//		       "LEFT JOIN Interaction i ON i.customer.id = c.id " +
//		       "WHERE ( " +
//		       "    c.mainStatus.name = 'Mới' AND " +
//		       "    NOT EXISTS ( " +
//		       "        SELECT 1 FROM Interaction iSub WHERE iSub.customer.id = c.id " +
//		       "    ) AND " +
//		       "    c.createdAt <= :caseDaysAgo_1 " +
//		       ") " +
//		       "OR ( " +
//		       "    c.mainStatus.name = 'Tiềm năng' AND " +
//		       "    EXISTS ( " +
//		       "        SELECT 1 FROM Interaction iSub " +
//		       "        WHERE iSub.customer.id = c.id " +
//		       "        GROUP BY iSub.customer.id " +
//		       "        HAVING MAX(iSub.createdAt) <= :caseDaysAgo_2 " +
//		       "    ) " +
//		       ") " +
//		       "OR ( " +
//		       "    cc.remindDate IS NOT NULL AND cc.remindDate <= :now " +
//		       ")")
//		List<Customer> findPotentialCustomers(
//		    @Param("caseDaysAgo_1") LocalDateTime caseDaysAgo_1,
//		    @Param("caseDaysAgo_2") LocalDateTime caseDaysAgo_2,
//		    @Param("now") LocalDateTime now
//		);


	@Query("SELECT cc FROM CustomerCare cc JOIN FETCH cc.customer")
    List<CustomerCare> findAllCustomerCares();
	
//	@Query("SELECT cc FROM CustomerCare cc " +
//		       "JOIN FETCH cc.customer c " +
//		       "LEFT JOIN Interaction i ON i.customer.id = c.id " +
//		       "WHERE c.mainStatus.name = 'Tiềm năng' " +
//		       "ORDER BY i.createdAt DESC")
//	List<CustomerCare> findAllCustomerCares();
	
	@Query("SELECT COUNT(cc) > 0 FROM CustomerCare cc WHERE cc.customer = :customer AND cc.remindDate = :remindDate")
	boolean existsByCustomerAndRemindDate(@Param("customer") Customer customer, @Param("remindDate") LocalDateTime remindDate);
	
	/*
	 * TODO: Nếu customerId đã tồn tại trong bảng crm_customer_care, phương thức sẽ trả về
	 * true. Nếu không có dữ liệu, phương thức sẽ trả về false.
	 */
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
		       "FROM CustomerCare c " +
		       "WHERE c.customer.id = :customerId " +
		       "AND NOT EXISTS (SELECT 1 FROM CustomerCare cc WHERE cc.customer.id = c.customer.id)")
	boolean existsInCustomerCares(@Param("customerId") Long customerId);
	
	@Query("SELECT COUNT(c) > 0 FROM CustomerCare c WHERE c.id = :customerCareId")
	boolean existsByCustomeCareId(@Param("customerCareId") Long customerCareId);
	
	@Query("SELECT cc FROM CustomerCare cc WHERE cc.customer.id = :customerId")
	List<CustomerCare> findByCustomerId(@Param("customerId") Long customerId);

}
