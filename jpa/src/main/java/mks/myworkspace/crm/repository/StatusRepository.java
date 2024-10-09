package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
	List<Status> findAll();
}
