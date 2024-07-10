package sg.nus.iss.com.Leaveapp.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//Cross domain configuration

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1 Set access source address
        corsConfiguration.addAllowedOrigin("*");
        // 2 Set access source request header
        corsConfiguration.addAllowedHeader("*");
        // 3 Set access source request method
        corsConfiguration.addAllowedMethod("*");
        // 4 Cross domain settings for interface configuration
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
