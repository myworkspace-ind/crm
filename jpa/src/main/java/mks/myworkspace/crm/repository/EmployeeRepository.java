package mks.myworkspace.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	 Optional<Employee> findByUsername(String username);
	 boolean existsByUsername(String username);
	 
	 @Query("SELECT e.pinCodeHash FROM Employee e WHERE e.username = :username")
	 String findPinCodeHashByUsername(@Param("username") String username);
}
