package mks.myworkspace.crm.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.GmailScopes;

@Controller
public class GmailOAuthController {
	
	//CLIENT_ID, CLIENT_SECRET and REDIRECT_URI should be in .env (do not share)
	private static final String CLIENT_ID = "5578493352-susptasa5447cs39kp8r5tdnhk5qo8bu.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "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";
	private static final String REDIRECT_URI = "http://localhost:8080/crm-web/oauth2callback";

	private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	private GoogleAuthorizationCodeFlow flow;

	public GmailOAuthController() throws Exception {
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES)
				.setAccessType("offline").build();
	}

	// Bước 1: Redirect người dùng đến Google để cấp quyền
	@GetMapping("/authorize")
	public void authorize(@RequestParam("customerId") Long customerId, HttpSession session, HttpServletResponse response) throws IOException {
		session.setAttribute("customerId", customerId); // lưu lại ID khách
		String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
		response.sendRedirect(url);
	}

	// Bước 2: Xử lý callback Google gửi về, lấy token
	@GetMapping("/oauth2callback")
	public String oauth2callback(@RequestParam("code") String code, HttpSession session) throws IOException {
		GoogleTokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
		Credential credential = flow.createAndStoreCredential(tokenResponse, "userID");

		// Lưu Credential vào session hoặc DB (tuỳ bạn)
		session.setAttribute("credential", credential);
		
		// Lấy customerId đã lưu từ session để redirect lại trang chi tiết
		Long customerId = (Long) session.getAttribute("customerId");
		
		if (customerId != null) {
			return "redirect:/customer/customerDetail?id=" + customerId;
		}
		
		return "redirect:/"; // chuyển tới trang gửi email
	}
}
