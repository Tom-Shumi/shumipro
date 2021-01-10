package com.ne.jp.shumipro.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAuthAccount implements UserDetails {
	
	private UserAuth userAuth;
	private Collection<GrantedAuthority> authorities;
	
	public UserAuthAccount(UserAuth userAuth, Collection<GrantedAuthority> authorities) {
		this.userAuth = userAuth;
		this.authorities = authorities;
	}
	
	public Collection<? extends GrantedAuthority>  getAuthorities(){
		return this.authorities;
	}
	
	public String getPassword() {
		return this.userAuth.getPassword();
	}
	
	public String getUsername() {
		return this.userAuth.getUsername();
	}
	
	public boolean isEnabled() {
		return userAuth.getEnabledflg().equals("1");
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}
