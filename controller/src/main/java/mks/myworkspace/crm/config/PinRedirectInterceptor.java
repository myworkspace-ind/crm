package mks.myworkspace.crm.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import mks.myworkspace.crm.repository.AuthenticationRepository;
import mks.myworkspace.crm.repository.EmployeeRepository;

/**
 * Forces a redirect to /register-pin if the employee has not set up a PIN.
 */
@Component
public class PinRedirectInterceptor implements HandlerInterceptor {
	@Autowired
    private AuthenticationRepository authenticationRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String username = null;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            String uri = request.getRequestURI();

            if (username != null && !uri.contains("/auth/register-pin") && !uri.contains("/css") && !uri.contains("/js")) {
                String pinCode = employeeRepository.findPinCodeHashByUsername(username);
                if (pinCode == null || pinCode.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/auth/register-pin");
                    return false;
                }
            }
        }

        return true;
    }
}
