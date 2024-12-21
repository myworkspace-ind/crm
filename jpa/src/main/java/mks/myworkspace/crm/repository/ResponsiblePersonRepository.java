package mks.myworkspace.crm.repository;

import mks.myworkspace.crm.entity.ResponsiblePerson;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsiblePersonRepository extends JpaRepository<ResponsiblePerson, Long> {
	@Query("SELECT r FROM ResponsiblePerson r ORDER BY r.seqno ASC")
	List<ResponsiblePerson> findAllOrderBySeqno();
}
