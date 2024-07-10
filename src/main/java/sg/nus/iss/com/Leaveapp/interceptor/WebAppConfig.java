package sg.nus.iss.com.Leaveapp.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebAppConfig implements WebMvcConfigurer{

	@Autowired
	ActionsInterceptor actionInterceptor;
	

	@Autowired
	LoginInterceptor loginInterceptor;
	
	
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/**", "/login", "/static/**");
        registry.addInterceptor(actionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/**", "/login", "/static/**");
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(60000); // Set the default asynchronous request timeout to 60 seconds
    }
}