package com.ne.jp.shumipro.form;

import java.util.List;

import com.ne.jp.shumipro.security.UserAuth;

import lombok.Data;


@Data
public class LoginForm{
	
	private String username;
	private String password;
	private String adminflg;
	
	private List<UserAuth> userList;
	
}