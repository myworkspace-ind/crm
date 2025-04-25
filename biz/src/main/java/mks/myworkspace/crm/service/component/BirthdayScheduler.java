package mks.myworkspace.crm.service.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.service.HappyBirthdayService;

@Slf4j
@Component
public class BirthdayScheduler {
	@Autowired
	private HappyBirthdayService happyBirthdayService;

	// Chạy mỗi ngày vào 08:00 sáng
    @Scheduled(cron = "0 0 8 * * *")
    public void run() {
        happyBirthdayService.sendBirthdayEmail();
    }

	// Chạy mỗi 1 phút để test
//	@Scheduled(cron = "0 0/1 * * * *") // tức là mỗi phút
//	public void run() {
//		log.info("🔔 Đang chạy scheduler gửi email sinh nhật lúc {}", java.time.LocalDateTime.now());
//		happyBirthdayService.sendBirthdayEmail();
//	}
}
