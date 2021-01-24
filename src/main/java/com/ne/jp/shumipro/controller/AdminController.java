package com.ne.jp.shumipro.controller;

import java.util.List;

import com.ne.jp.shumipro.component.SessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ne.jp.shumipro.security.UserAuth;
import com.ne.jp.shumipro.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminService;

	@Autowired
	SessionData sessionData;
	
	@RequestMapping("")
	public String admin(Model model) {
		
		List<UserAuth> userAuthList = adminService.getUsersAll();
		model.addAttribute("userList", userAuthList);
		model.addAttribute("loginUsername", sessionData.getUsername());
		return "admin/admin";
	}
	
	@RequestMapping("/delete")
	public String delete(RedirectAttributes redirectAttrs, Model model, @RequestParam("username") String username) {
		if (username == null || sessionData.getUsername().equals(username)){
			redirectAttrs.addFlashAttribute("message", "削除に失敗しました");
		} else {
			adminService.deleteUser(username);
			redirectAttrs.addFlashAttribute("message", "削除が完了しました");
		}

		return "redirect:/admin";
	}
}
