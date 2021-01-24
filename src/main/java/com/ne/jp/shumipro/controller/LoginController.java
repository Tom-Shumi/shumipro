package com.ne.jp.shumipro.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ne.jp.shumipro.form.LoginForm;
import com.ne.jp.shumipro.security.UserAuth;
import com.ne.jp.shumipro.security.UserAuthService;
import com.ne.jp.shumipro.service.AdminService;

@Controller
@RequestMapping("/loginForm")
public class LoginController {
	
	@Autowired
	private UserAuthService userAuthService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	AdminService adminService;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("")
    public String loginForm(Model model) {
		// test用　start
        // ユーザリスト まずは手動で生成
        List<UserAuth> userList = new ArrayList<UserAuth>();

        UserAuth user = new UserAuth();
        user.setUserid(0L);
        user.setUsername("test0");

        UserAuth user2 = new UserAuth();
        user2.setUserid(1L);
        user2.setUsername("test1");

        userList.add(user);
        userList.add(user2);

        // フォームにユーザのリストを設定し、モデルへ追加することでモデルへ正常に追加されたか検証するための土台を整える
        LoginForm form = new LoginForm();
        form.setUserList(userList);

        model.addAttribute("message", "hello!");// 1

        model.addAttribute("user", user);// 2

        model.addAttribute("dbForm", form);// 3

		List<UserAuth> userAuthList = adminService.getUsersAll();
		
		// フォームにユーザのリストを設定し、モデルへ追加することでモデルへ正常に追加されたか検証するための土台を整える
        LoginForm loginForm = new LoginForm();
        loginForm.setUserList(userAuthList);
        model.addAttribute("loginForm", loginForm);

		// test用 end
        return "loginForm";
    }
    
    @RequestMapping("/registUser")
    public String registUser(RedirectAttributes redirectAttrs, @ModelAttribute("loginForm") LoginForm form,  Model model) {
    	
    	logger.info("[ユーザ名:" + form.getUsername() + "] [パスワード:" + form.getPassword() + "] [管理者フラグ:" + form.getAdminflg() + "]");
    	
    	String password = passwordEncoder.encode(form.getPassword());
    	String adminflg = form.getAdminflg() == null ? "0" : "1";
    	int result = userAuthService.registerUser(form.getUsername(), password, adminflg);
    	String message;
    	if (result == 1) message = "登録が完了しました";
    	else if (result == 2) message = "更新が完了しました";
    	else message = "失敗しました";
    	
    	List<UserAuth> userList = adminService.getUsersAll();
    	for (UserAuth user : userList) {
    		logger.info("[ユーザ名:" + user.getUsername() + "]");
    	}
    	
    	redirectAttrs.addFlashAttribute("result_message", message);
        return "redirect:/loginForm";
    }
    
}
