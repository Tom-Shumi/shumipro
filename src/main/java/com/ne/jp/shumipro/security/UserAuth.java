package com.ne.jp.shumipro.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAuth implements Serializable {
	
	public UserAuth(String username, String password, String enabledflg, String adminflg) {
		this.username = username;
		this.password = password;
		this.enabledflg = enabledflg;
		this.adminflg = adminflg;
	}
	
	public UserAuth() {
		
	}
	private Long userid;
	
	private String username;
	
	private String password;
	
	private String enabledflg;
	
	private String adminflg;

}
