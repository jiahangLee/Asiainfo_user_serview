package gags.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@SpringBootApplication
//@EnableResourceServer
//@EnableDiscoveryClient
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableDiscoveryClient
public class AnalyseServiceApplication extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/welcome","/v2/**","/swagger-ui.html",    "/webjars/**", "/configuration/**", "/swagger-resources/**", "/docs").permitAll()
				.anyRequest().authenticated();
	}
	public static void main(String[] args) {
		SpringApplication.run(AnalyseServiceApplication.class, args);
	}
}
