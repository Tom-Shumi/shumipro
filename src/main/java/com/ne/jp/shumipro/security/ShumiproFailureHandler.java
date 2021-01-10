package com.ne.jp.shumipro.security;

import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;

public class ShumiproFailureHandler extends ForwardAuthenticationFailureHandler {

	public ShumiproFailureHandler(String forwardUrl) {
		super(forwardUrl);
	}
}
