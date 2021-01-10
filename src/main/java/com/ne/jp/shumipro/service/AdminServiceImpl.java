package com.ne.jp.shumipro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ne.jp.shumipro.mapper.UsersMapper;
import com.ne.jp.shumipro.security.UserAuth;
import com.ne.jp.shumipro.security.UserAuthMapper;

@Service
@Transactional
public class AdminServiceImpl implements AdminService{

	@Autowired
	private UsersMapper usersMapper;
	
	@Autowired
	private UserAuthMapper userAuthMapper;
	
	@Override
	public List<UserAuth> getUsersAll() {
		return usersMapper.getUsersAll();
	}

	@Override
	public void deleteUser(String username) {
		userAuthMapper.delete(username);
	}
}
