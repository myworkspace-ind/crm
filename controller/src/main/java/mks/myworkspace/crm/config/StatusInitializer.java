package mks.myworkspace.crm.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StatusInitializer {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void init() {
		insertIfNotExists("New", "#e94b4b", 1L);
		insertIfNotExists("Potential", "#3f51b5", 2L);
		insertIfNotExists("Newly updated", "#545454", 3L);
		insertIfNotExists("Leave", "#5c5c5c", 4L);
		insertIfNotExists("New Status", "#243236", 5L);
		insertIfNotExists("Converted", "#38993c", null);
		insertIfNotExists("Back", "#ee9020", null);
	}

	private void insertIfNotExists(String name, String backgroundColor, Long seqno) {
		String checkSql = "SELECT COUNT(*) FROM crm_status WHERE name = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, name);

		if (count != null && count == 0) {
			String insertSql = "INSERT INTO crm_status (name, backgroundColor, seqno) VALUES (?, ?, ?)";
			jdbcTemplate.update(insertSql, name, backgroundColor, seqno);
			log.debug("Inserted: {}" + name);
		} else {
			log.debug("Skipped (already exists): {}" + name);
		}
	}
}
