package mks.myworkspace.crm.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;

public interface CustomerCareRepository extends JpaRepository<CustomerCare, Long>, JpaSpecificationExecutor<CustomerCare> {
	//Chỉ mới xét trường hợp khách hàng Mới và chưa có Interaction hoặc có thời gian tạo mới interaction > thời gian nhắc nhở trong customer care
	@Query("SELECT DISTINCT c FROM Customer c " +
		       "LEFT JOIN FETCH c.interactions i " + //fetch luôn interactions
		       "LEFT JOIN CustomerCare cc ON c.id = cc.customer.id " +
		       "WHERE ( " +
		       "   c.mainStatus.name = 'Mới' AND " +
		       "   (i.id IS NULL OR i.createdAt > cc.remindDate) AND " +
		       "   c.createdAt <= :caseDaysAgo_1 " +
		       ") OR ( " +
		       "   c.mainStatus.name = 'Tiềm năng' AND " +
		       "   EXISTS ( " +
		       "       SELECT 1 FROM Interaction i2 " +
		       "       WHERE i2.customer.id = c.id " +
		       "       GROUP BY i2.customer.id " +
		       "       HAVING MAX(i2.createdAt) <= :caseDaysAgo_2 " +
		       "   ) " +
		       ") OR (" +
		       "   cc.remindDate IS NOT NULL " + 
		       ")")
	List<Customer> findPotentialCustomers(
	    @Param("caseDaysAgo_1") LocalDateTime caseDaysAgo_1,
	    @Param("caseDaysAgo_2") LocalDateTime caseDaysAgo_2
	);
	
	@Query("SELECT cc FROM CustomerCare cc JOIN FETCH cc.customer")
    List<CustomerCare> findAllCustomerCares();
	
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
}
