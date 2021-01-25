package com.ne.jp.shumipro.service;


import com.ne.jp.shumipro.mapper.UsersMapper;
import com.ne.jp.shumipro.response.UserInfoResponse;
import com.ne.jp.shumipro.security.UserAuth;
import com.ne.jp.shumipro.security.UserAuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApiService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    public UserAuth getUserInfo(String username) {
        return userAuthMapper.findByUsername(username);
    }

}
