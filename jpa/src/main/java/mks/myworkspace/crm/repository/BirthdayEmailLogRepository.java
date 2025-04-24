package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.BirthdayEmailLog;
import mks.myworkspace.crm.entity.Customer;

@Repository
public interface BirthdayEmailLogRepository extends JpaRepository<BirthdayEmailLog, Long>, JpaSpecificationExecutor<BirthdayEmailLog> {
//	@Query("SELECT c FROM Customer c WHERE FUNCTION('DAY', c.birthday) = :day AND FUNCTION('MONTH', c.birthday) = :month")
//	List<Customer> findCustomersWithBirthdayToday(@Param("day") int day, @Param("month") int month);
}
