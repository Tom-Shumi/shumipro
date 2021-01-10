package com.ne.jp.shumipro.security;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuthMapper {
	
	UserAuth findByUsername(String username);
	
	void create(UserAuth userAuth);
	
	void update(UserAuth userAuth);
	
	void delete(String username);
}
