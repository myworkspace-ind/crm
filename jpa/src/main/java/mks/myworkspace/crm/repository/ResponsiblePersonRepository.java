package mks.myworkspace.crm.repository;

import mks.myworkspace.crm.entity.ResponsiblePerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponsiblePersonRepository extends JpaRepository<ResponsiblePerson, Long> {
	
}
