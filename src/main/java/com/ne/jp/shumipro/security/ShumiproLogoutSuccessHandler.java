package com.ne.jp.shumipro.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class ShumiproLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        if (authentication != null && authentication.getDetails() != null) {
        	logger.info("ログアウト対象： " + authentication.getDetails().toString());
        } else {
        	logger.info("ログアウト");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        redirectStrategy.sendRedirect(request, response, "/loginForm?logout");
    }
}
