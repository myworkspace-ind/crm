package mks.myworkspace.crm.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationRepository {
	@Autowired
	@Qualifier("jdbcTemplate0")
	private JdbcTemplate jdbcTemplate0;

	// Nếu bạn dùng XML thì vẫn nên có setter
	public void setJdbcTemplate0(JdbcTemplate jdbcTemplate0) {
		this.jdbcTemplate0 = jdbcTemplate0;
	}

	public boolean existsByUsername(String username) {
		String sql = "SELECT COUNT(*) FROM crm_employee WHERE username = ?";
		Integer count = jdbcTemplate0.queryForObject(sql, Integer.class, username);
		return count != null && count > 0;
	}
	
	public boolean isPinCodeMissing(String username) {
	    String sql = "SELECT pin_code_hash FROM crm_employee WHERE username = ?";
	    String result = jdbcTemplate0.queryForObject(sql, String.class, username);
	    return result == null || result.trim().isEmpty();
	}

//	public void insertEmployee(String username, String pinCodeHash, LocalDateTime createdAt, LocalDateTime updatedAt) {
//		String sql = "INSERT INTO crm_employee (username, pin_code_hash, created_at, updated_at) VALUES (?, ?, ?, ?)";
//		jdbcTemplate0.update(sql, username, pinCodeHash, Timestamp.valueOf(createdAt), Timestamp.valueOf(updatedAt));
//	}

	public void updatePinCodeHash(String username, String newHashedPin) {
		String sql = "UPDATE crm_employee SET pin_code_hash = ?, updated_at = ? WHERE username = ?";
		jdbcTemplate0.update(sql, newHashedPin, LocalDateTime.now(), username);
	}
}
