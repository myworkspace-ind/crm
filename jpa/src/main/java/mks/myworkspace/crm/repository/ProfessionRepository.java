package mks.myworkspace.crm.repository;

import mks.myworkspace.crm.entity.Profession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionRepository extends JpaRepository<Profession, Long> {
	
}
