package mks.myworkspace.crm.config;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Employee;
import mks.myworkspace.crm.repository.AuthenticationRepository;
import mks.myworkspace.crm.repository.EmployeeRepository;

/**
 * Creates an employee account if it doesn't exist.
 */
@Component
@Slf4j
public class PinSetupInterceptor implements HandlerInterceptor {
	@Autowired
    private EmployeeRepository employeeRepository;
	
	@Autowired
	private AuthenticationRepository authenticationRepository;
	
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
            
            log.debug("Interceptor chạy!");
            System.out.println("=== Interceptor ===");
            if (username != null && !employeeRepository.existsByUsername(username)) {
            	authenticationRepository.insertEmployee(
                    username,
                    "", // empty pin first
                    LocalDateTime.now(),
                    LocalDateTime.now()
                );
            }
        }

        return true;
    }

}
