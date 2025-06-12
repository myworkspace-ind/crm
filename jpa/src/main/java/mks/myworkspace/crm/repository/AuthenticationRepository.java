package mks.myworkspace.crm.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AuthenticationRepository {
	@Autowired
	@Qualifier("jdbcTemplate0")
	private JdbcTemplate jdbcTemplate0;
		
	public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM crm_employee WHERE username = ?";
        Integer count = jdbcTemplate0.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }
	
	public void insertEmployee(String username, String pinCodeHash, LocalDateTime createdAt, LocalDateTime updatedAt) {
        String sql = "INSERT INTO crm_employee (username, pin_code_hash, created_at, updated_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate0.update(sql, username, pinCodeHash, Timestamp.valueOf(createdAt), Timestamp.valueOf(updatedAt));
    }
	 
	public void updatePinCodeHash(String username, String newHashedPin) {
		String sql = "UPDATE crm_employee SET pin_code_hash = ?, updated_at = ? WHERE username = ?";
		jdbcTemplate0.update(sql, newHashedPin, LocalDateTime.now(), username);
	}

}
