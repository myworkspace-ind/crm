package mks.myworkspace.crm.repository;

import mks.myworkspace.crm.entity.Profession;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
	@Query("SELECT p FROM Profession p ORDER BY p.seqno ASC")
	List<Profession> findAllOrderBySeqno();
}
