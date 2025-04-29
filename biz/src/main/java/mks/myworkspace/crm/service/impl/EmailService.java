package mks.myworkspace.crm.service.impl;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.EmailToCustomer;
import mks.myworkspace.crm.repository.AppRepository;

@Slf4j
@Service
public class EmailService {
	private final JavaMailSender mailSender;
	private final ServletContext servletContext;
	private final SpringTemplateEngine templateEngine;
	
	@Value("${email.birthday.subject}")
	private String birthdaySubject;

	@Value("${email.birthday.template}")
	private String birthdayTemplate;

	@Autowired
	private AppRepository appRepository;

	//
	/**
	 * Constructor injection for JavaMailSender
	 * @param mailSender: JavaMailSender for sending emails.
	 * @param servletContext: ServletContext for accessing web resources.
	 * @param templateEngine: SpringTemplateEngine for rendering email templates (html)//
	 */
	public EmailService(JavaMailSender mailSender, ServletContext servletContext, SpringTemplateEngine templateEngine) {
		this.mailSender = mailSender;
		this.servletContext = servletContext;
		this.templateEngine = templateEngine;
	}
	
	public void sendBirthdayCard(Customer customer) {
		Context context = new Context();
		context.setVariable("customerName", customer.getCompanyName());
//		context.setVariable("discount", "30%");
//		context.setVariable("duration", "3 tháng");
		String htmlContent = templateEngine.process(birthdayTemplate, context);

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(customer.getEmail());
			helper.setSubject(birthdaySubject);
			helper.setText(htmlContent, true);

			mailSender.send(message);

			log.info("Đã gửi thiệp sinh nhật cho: {}" + customer.getEmail());

		} catch (MessagingException e) {
			log.error("Lỗi khi gửi email sinh nhật: {}" + e.getMessage());
		}

	}

	public void sendEmail(String to, String subject, String contactName, String contactNumber, String email,
			String companyName, String message, String address) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

		// String filePath =
		// "/crm-web/src/main/webapp/WEB-INF/templates/emailTemplate.html";
		// String emailContent = new String(Files.readAllBytes(Paths.get(filePath)),
		// StandardCharsets.UTF_8);
		// Đọc file template từ thư mục WEB-INF
		String filePath = servletContext.getRealPath("/WEB-INF/templates/emailTemplate.html");
		String emailContent = new String(Files.readAllBytes(Paths.get(filePath)));

		// Thay thế các placeholder {{...}} bằng dữ liệu thực
		emailContent = emailContent.replace("{{contactName}}", contactName).replace("{{contactNumber}}", contactNumber)
				.replace("{{email}}", email).replace("{{companyName}}", companyName).replace("{{message}}", message)
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

//    public void sendEmailToCustomer(String from, String to, String subject, String text) throws MessagingException{
//    	MimeMessage message = mailSender.createMimeMessage();
//    	MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setFrom(from);
//    	helper.setTo(to);
//    	helper.setSubject(subject);
//        helper.setText(text, true);
//         
//        mailSender.send(message);
//    }

	public void sendEmailToCustomer(EmailToCustomer email) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(email.getSender());
			helper.setTo(email.getCustomer().getEmail());

			helper.setSubject(
					new String(email.getSubject().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
			helper.setText(new String(email.getContent().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8),
					true);

			mailSender.send(message);

			// Cập nhật trạng thái và thời gian gửi
			email.setStatus(EmailToCustomer.EmailStatus.SENT);
			email.setSendDate(new Date());

			appRepository.saveEmailToCustomer(email);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Gửi email thất bại!");
		}
	}
}
