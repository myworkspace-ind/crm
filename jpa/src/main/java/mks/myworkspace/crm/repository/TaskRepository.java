package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.Task;

/**
 * @author Nguyen Hoang Phuong Ngan
 * @version 1.0 
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
	@Query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.customers")
    List<Task> findAllWithCustomers();
	
	@Query(value = "SELECT t.id AS task_id, t.name AS task_name, t.description, " +
            "t.start_date, t.status, t.important, " +
            "c.id AS customer_id, c.company_name, " +
            "c.contact_person, c.phone " +
            "FROM crm_task t " +
            "LEFT JOIN task_customer tc ON t.id = tc.task_id " +
            "LEFT JOIN crm_customer c ON tc.customer_id = c.id " +
            "ORDER BY t.id",nativeQuery = true)
	List<Object[]> fetchTaskWithCustomersRaw();
}
