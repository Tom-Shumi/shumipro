package com.ne.jp.shumipro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.ne.jp.shumipro.security.ShumiproFailureHandler;
import com.ne.jp.shumipro.security.ShumiproLogoutSuccessHandler;
import com.ne.jp.shumipro.security.ShumiproSuccessHandler;
import com.ne.jp.shumipro.security.UserAuthService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserAuthService userDetailsService;
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }
    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    		.csrf().disable()
    		.authorizeRequests()
    		.antMatchers("/admin/**").hasRole("ADMIN")
    		.antMatchers("/user/**").authenticated()
    		.antMatchers("/loginForm/**").permitAll()
    		.anyRequest().authenticated()
    		.and()
    		.formLogin()
            .loginPage("/loginForm")
            .usernameParameter("username")
            .passwordParameter("password")
            .loginProcessingUrl("/login")
            .successHandler(customSuccessHandler())
            //.defaultSuccessUrl("/menu", true)
            .failureHandler(customAuthenticationFailureHandler())
            //.failureUrl("/loginForm?error=true")
    		.and()
    		.sessionManagement()
    			.maximumSessions(1)
    			.maxSessionsPreventsLogin(true);
    	http.logout()
    		//.logoutSuccessUrl("/loginForm?logout")
    		.logoutUrl("/logout")
    		.logoutSuccessHandler(logoutSuccessHandler())
    		.invalidateHttpSession(true)
    		.deleteCookies("JSESSIONID");
    }
    
    @Override
     public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/css/**", "/static/image/**", "/static/js/**");
    }
    
    @Bean
    AuthenticationSuccessHandler customSuccessHandler() {
    	return new ShumiproSuccessHandler();
    }
    
    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new ShumiproFailureHandler("/loginForm?error=true");
    }
    
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        // ハンドラを返す
        return new ShumiproLogoutSuccessHandler();
    }
}
