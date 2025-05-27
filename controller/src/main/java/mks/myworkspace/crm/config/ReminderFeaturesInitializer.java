package mks.myworkspace.crm.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReminderFeaturesInitializer {
	@Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        insertIfNotExists("CC_REMINDER", "Nhắc Chăm Sóc Khách Hàng Định Kỳ",
                "Hệ thống tự động nhắc nhở nhân viên tương tác lại khách hàng định kỳ dựa trên thời gian đã thiết lập. Có thể cấu hình theo nhóm thời gian tương tác gần nhất, cấu hình theo trạng thái chính của khách hàng,...",
                "https://cdn-icons-png.flaticon.com/512/3989/3989835.png", true);

        insertIfNotExists("BIRTHDAY_REMINDER", "Sinh nhật khách hàng", "Nhắc gửi lời chúc/ưu đãi sinh nhật.",
                "https://cdn-icons-png.flaticon.com/512/595/595067.png", false);

        insertIfNotExists("TASK_REMINDER", "Nhắc nhở công việc", "Thông báo công việc trong ngày, sắp tới, quá hạn.",
                "https://cdn-icons-png.flaticon.com/512/4712/4712105.png", false);

        insertIfNotExists("RENEWAL_REMINDER", "Nhắc gia hạn hợp đồng", "Thông báo sắp hết hạn hợp đồng.",
                "https://cdn-icons-png.flaticon.com/512/1521/1521213.png", false);

        insertIfNotExists("PAYMENT_REMINDER", "Nhắc thanh toán", "Nhắc các khoản cần thu hoặc chi.",
                "https://cdn-icons-png.flaticon.com/512/833/833472.png", false);

        insertIfNotExists("NEW_CARE_REMINDER", "Nhắc nhở chăm sóc mới", "Tự động nhắc chăm sóc khách hàng mới.",
                "https://cdn-icons-png.flaticon.com/512/1077/1077976.png", false);

        insertIfNotExists("FEEDBACK_REMINDER", "Theo dõi phản hồi KH", "Nhắc theo dõi xử lý phản hồi khách hàng.",
                "https://cdn-icons-png.flaticon.com/512/1946/1946488.png", false);

        insertIfNotExists("CONSULT_REMINDER", "Lịch hẹn tư vấn", "Nhắc đến lịch hẹn với khách hàng.",
                "https://cdn-icons-png.flaticon.com/512/545/545705.png", false);

        insertIfNotExists("EMAIL_REMINDER", "Gửi email định kỳ", "Nhắc gửi email chăm sóc theo lịch định kỳ.",
                "https://cdn-icons-png.flaticon.com/512/2910/2910791.png", false);
    }

    private void insertIfNotExists(String code, String name, String desc, String icon, boolean enabled) {
        String checkSql = "SELECT COUNT(*) FROM crm_reminder_features WHERE code = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, code);

        if (count != null && count == 0) {
            String insertSql = "INSERT INTO crm_reminder_features (code, name, description, icon, enabled) " +
                    "VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertSql, code, name, desc, icon, enabled);
            log.debug("Inserted: {}" + code);
        } else {
        	log.debug("Skipped (already exists): {}" + code);
        }
    }
}
