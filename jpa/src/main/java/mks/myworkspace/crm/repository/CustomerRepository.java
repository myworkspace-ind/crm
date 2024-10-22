package mks.myworkspace.crm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findAll();

//	List<Customer> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCaseOrPhoneContainingIgnoreCase(String name, String address, String phone);

	@Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.statuses s WHERE "
			+ "(LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) OR "
			+ "LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%')) OR "
			+ "LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%')) OR "
			+ "LOWER(s.name) LIKE LOWER(CONCAT('%', :status, '%')))")
	List<Customer> searchCustomers(@Param("name") String name, @Param("address") String address,
			@Param("phone") String phone, @Param("status") String status);

	@Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.statuses")
	List<Customer> findAllWithStatuses();
	
	@Query("SELECT c FROM Customer c LEFT JOIN FETCH c.statuses s WHERE s.id =:statusId")
	List<Customer> findByStatusId(@Param("statusId") Long statusId);
	
	@Query("SELECT c FROM Customer c LEFT JOIN FETCH c.statuses s WHERE c.id = :id")
	Optional<Customer> findById(@Param("id") Long id);
	
	@Query("SELECT COALESCE(MAX(c.id), 0) FROM Customer c")
    Long findMaxId();
	
	@Query("SELECT c FROM Customer c WHERE c.phone = :phone")
	//Database khong co sdt trung nhau
	Optional<Customer> findByPhone(@Param("phone") String phone);
	
	//Database co sdt trung lap
	//List<Customer> findByPhone(@Param("phone") String phone);
}
