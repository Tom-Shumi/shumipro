package com.ne.jp.shumipro.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ne.jp.shumipro.component.SessionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class ShumiproSuccessHandler implements AuthenticationSuccessHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    SessionData sessionData;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		logger.info("ログインに成功しました。");
        UserAuthAccount principal = (UserAuthAccount)authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        sessionData.setUsername(principal.getUsername());
        sessionData.setAdminflg(isAdmin ? "1" : "0");
		handle(request, response, isAdmin);
		clearAuthenticationAttributes(request);
	}
	protected void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            boolean isAdmin)
            throws IOException {
        String targetUrl = determineTargetUrl(isAdmin);
        if (response.isCommitted()) {
        	logger.debug("レスポンスがすでにコミットされています。次のURLへリダイレクトできません。 :" + targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

	protected String determineTargetUrl(boolean isAdmin) {
        if (isAdmin) {
            return "/admin";
        } else {
            return "/user";
        }
    }
    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return;
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
    
    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
