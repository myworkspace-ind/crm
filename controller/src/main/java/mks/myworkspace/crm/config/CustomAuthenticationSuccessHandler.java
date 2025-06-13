package mks.myworkspace.crm.config;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import mks.myworkspace.crm.repository.AuthenticationRepository;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	private final AuthenticationRepository authenticationRepository;

	public CustomAuthenticationSuccessHandler(AuthenticationRepository authenticationRepository) {
		this.authenticationRepository = authenticationRepository;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		String username = null;
		Object principal = authentication.getPrincipal();

		if (principal instanceof UserDetails) {
		    username = ((UserDetails) principal).getUsername();
		} else {
		    username = principal.toString();
		}

        if (!authenticationRepository.existsByUsername(username)) {
            authenticationRepository.insertEmployee(
                username,	
                "", // empty spin
                LocalDateTime.now(),
                LocalDateTime.now()
            );
        }

        // Redirect to home page after login success.
        response.sendRedirect(request.getContextPath() + "/");

		
	}

}
