package com.ne.jp.shumipro.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ne.jp.shumipro.security.UserAuth;

@Mapper
public interface UsersMapper {

	List<UserAuth> getUsersAll();

}
