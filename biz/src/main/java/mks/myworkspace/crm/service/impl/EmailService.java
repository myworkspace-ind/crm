package mks.myworkspace.crm.service.impl;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;

import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail(String to, String subject, String template, String location, String contactName, 
            String contactNumber, String email, String companyName, String message, String address) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		
		// Đọc file template HTML
		ClassPathResource resource = new ClassPathResource("emailTemplate.html");
		String emailContent = new String(Files.readAllBytes(resource.getFile().toPath()));
		
		// Thay thế các placeholder {{...}} bằng dữ liệu thực
		emailContent = emailContent.replace("{{location}}", location)
					                .replace("{{contactName}}", contactName)
					                .replace("{{contactNumber}}", contactNumber)
					                .replace("{{email}}", email)
					                .replace("{{companyName}}", companyName)
					                .replace("{{message}}", message)
					                .replace("{{address}}", address);
		
		//Cấu hình email;
		helper.setTo(to);
		helper.setSubject(subject);
	    helper.setText(emailContent, true); // Gửi email dưới dạng HTML

	    mailSender.send(mimeMessage);
	}
	
}
