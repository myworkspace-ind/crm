package mks.myworkspace.crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	@Autowired
    private PinSetupInterceptor pinSetupInterceptor;
	
	@Autowired
	private PinRedirectInterceptor pinRedirectInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(pinSetupInterceptor).addPathPatterns("/**");
		registry.addInterceptor(pinRedirectInterceptor).addPathPatterns("/**");
	}
}
