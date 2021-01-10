package com.ne.jp.shumipro.security;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAuthService implements UserDetailsService {

	@Autowired
	private UserAuthMapper mapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isEmpty(username)) {
			throw new UsernameNotFoundException("ユーザ名を入力してください。");
		}
		UserAuth userAuth = mapper.findByUsername(username);
		if (userAuth == null) {
			throw new UsernameNotFoundException("ユーザが見つかりません。");
		}
		List<GrantedAuthority> authority = null;
		if (userAuth.getAdminflg().equals("1")) {
			authority = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
		} else {
			authority = AuthorityUtils.createAuthorityList("ROLE_USER");
		}
		return new UserAuthAccount(userAuth, authority);
	}
	
	@Transactional
	public int registerUser(String username, String password, String adminflg) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(adminflg)) {
			return 0;
		}
		UserAuth userAuth = new UserAuth(username, password, "1", adminflg);
		if (mapper.findByUsername(username) == null) {
			mapper.create(userAuth);
			return 1;
		} else {
			mapper.update(userAuth);
			return 2;
		}
	}
}
