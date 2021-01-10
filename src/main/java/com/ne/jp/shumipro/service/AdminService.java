package com.ne.jp.shumipro.service;

import java.util.List;

import com.ne.jp.shumipro.security.UserAuth;

public interface AdminService {

	public List<UserAuth> getUsersAll();
	
	public void deleteUser(String username);
}
