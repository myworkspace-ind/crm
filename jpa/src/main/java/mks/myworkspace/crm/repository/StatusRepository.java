package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
	List<Status> findAll();
	
	@Query("DELETE FROM Status s WHERE s.id IN (SELECT cs.id FROM Customer cs WHERE cs.id IN :customerIds)")
	void deleteByCustomerIdIn(@Param("customerIds") Iterable<Long> ids);
	
}
