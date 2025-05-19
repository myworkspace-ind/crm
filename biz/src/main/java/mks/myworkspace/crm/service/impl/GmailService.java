package mks.myworkspace.crm.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Profile;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.EmailToCustomer;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;

@Slf4j
@Service
public class GmailService {
	
	@Autowired
	private AppRepository appRepository;
	@Autowired
	private CustomerRepository customerRepo;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    
    public String getSenderEmail(Credential credential) throws IOException {
    	Gmail gmail = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
    			.setApplicationName("MKS CRM")
    			.build();
    	Profile profile = gmail.users().getProfile("me").execute();
    	return profile.getEmailAddress();
    }

    // Gửi email có hỗ trợ CC, BCC, đính kèm
	public void sendEmailWithOptionalParams(String customerIdStr, Credential credential, String sender, String to, String cc, String bcc,
			String subject, String body, MultipartFile[] attachments) throws Exception {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);
		email.setFrom(new InternetAddress(sender));
		email.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		if (cc != null && !cc.isBlank()) {
			email.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
		}

		if (bcc != null && !bcc.isBlank()) {
			email.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
		}

		email.setSubject(subject, "UTF-8");

		// Tạo phần thân email có thể chứa đính kèm
		Multipart multipart = new MimeMultipart();

		// Body (HTML)
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(body, "text/html; charset=UTF-8");
		multipart.addBodyPart(htmlPart);

		// Attachment nếu có
		List<String> attachmentNames = new ArrayList<>();
		if (attachments != null) {
		    for (MultipartFile attachment : attachments) {
		        if (attachment != null && !attachment.isEmpty()) {
		            MimeBodyPart attachmentPart = new MimeBodyPart();
		            attachmentPart.setFileName(attachment.getOriginalFilename());
		            attachmentPart.setDataHandler(new DataHandler(
		                new ByteArrayDataSource(attachment.getBytes(), attachment.getContentType())));
		            multipart.addBodyPart(attachmentPart);

		            attachmentNames.add(attachment.getOriginalFilename());
		        }
		    }
		}

		email.setContent(multipart);

		// Gửi bằng Gmail API
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		email.writeTo(buffer);
		byte[] rawMessageBytes = buffer.toByteArray();
		String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

		com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
		message.setRaw(encodedEmail);

		Gmail gmailService = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName("MKS CRM").build();

		gmailService.users().messages().send("me", message).execute();
		
		EmailToCustomer emailToCustomer = new EmailToCustomer();
		Long customerId = Long.parseLong(customerIdStr);
		Customer customer = customerRepo.findById(customerId)
		        .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
		emailToCustomer.setSender(sender);
		emailToCustomer.setCustomer(customer);
		emailToCustomer.setSubject(subject);
		emailToCustomer.setContent(body);
//		emailToCustomer.setCc(cc);
//		emailToCustomer.setBcc(bcc);
//		emailToCustomer.setSubject(new String(subject.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
//		emailToCustomer.setContent(new String(body.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
//		emailToCustomer.setAttachmentName(attachment != null ? attachment.getOriginalFilename() : null);
		emailToCustomer.setSendDate(new Date());
		emailToCustomer.setStatus(EmailToCustomer.EmailStatus.SENT);

		appRepository.saveEmailToCustomerUsingGmail(emailToCustomer);
	}

   
}