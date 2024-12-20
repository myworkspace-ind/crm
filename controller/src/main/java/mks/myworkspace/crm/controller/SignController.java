package mks.myworkspace.crm.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@Slf4j
public class SignController extends BaseController {
	@Value("${caslogout.url}")
	private String casLogoutUrl;

	// Logout
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
			redirectAttributes.addFlashAttribute("message", "Bạn đã đăng xuất thành công!");
		}
		return "redirect:/";
	}

	@GetMapping("/logout-handler")
	public String handleLogout(HttpServletRequest request, Model model) {
		// Kiểm tra nếu có session attribute CAS (giả sử CAS login)
		boolean isCasLogin = request.getUserPrincipal() != null;
		if (isCasLogin) {
			// Xóa session của ứng dụng
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			SecurityContextHolder.clearContext();
			// Nếu đăng nhập qua CAS, chuyển hướng đến CAS Logout
			return "redirect:" + casLogoutUrl;
		} else {
			// Nếu không, thực hiện logout nội bộ
			SecurityContextHolder.clearContext();
			request.getSession().invalidate();
			return "redirect:/logout"; // Chuyển đến logout nội bộ
		}
	}
}
