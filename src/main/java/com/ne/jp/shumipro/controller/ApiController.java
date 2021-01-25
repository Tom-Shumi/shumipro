package com.ne.jp.shumipro.controller;

import com.ne.jp.shumipro.component.BeanUtil;
import com.ne.jp.shumipro.component.SessionData;
import com.ne.jp.shumipro.response.UserInfoResponse;
import com.ne.jp.shumipro.security.UserAuth;
import com.ne.jp.shumipro.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    ApiService apiService;

    @Autowired
    SessionData sessionData;

    @GetMapping("/user/{username}")
    public UserInfoResponse getUserInfo(@PathVariable("username") String username) throws Exception {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        UserAuth userAuth = apiService.getUserInfo(username);

        BeanUtil.copyProperties(userInfoResponse, userAuth);
        return userInfoResponse;
    }

}
