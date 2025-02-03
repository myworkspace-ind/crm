package mks.myworkspace.crm.service.impl;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final ServletContext servletContext;

    // Constructor injection for JavaMailSender
    public EmailService(JavaMailSender mailSender, ServletContext servletContext) {
        this.mailSender = mailSender;
        this.servletContext = servletContext;
    }

    public void sendEmail(String to, String subject, String contactName, 
                          String contactNumber, String email, String companyName, String message, String address) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        
    	//String filePath = "/crm-web/src/main/webapp/WEB-INF/templates/emailTemplate.html";
    	//String emailContent = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    	// Đọc file template từ thư mục WEB-INF
        String filePath = servletContext.getRealPath("/WEB-INF/templates/emailTemplate.html");
        String emailContent = new String(Files.readAllBytes(Paths.get(filePath)));

        // Thay thế các placeholder {{...}} bằng dữ liệu thực
        emailContent = emailContent .replace("{{contactName}}", contactName)
                                    .replace("{{contactNumber}}", contactNumber)
                                    .replace("{{email}}", email)
                                    .replace("{{companyName}}", companyName)
                                    .replace("{{message}}", message)
                                    .replace("{{address}}", address);

        // Cấu hình email
        helper.setTo(to);
        helper.setFrom(email);
        helper.setReplyTo(email);
        helper.setSubject(subject);
        helper.setText(emailContent, true); // Gửi email dưới dạng HTML

        // Gửi email
        mailSender.send(mimeMessage);
    }
}
