package com.ne.jp.shumipro.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.ne.jp.shumipro.ShumiProjectApplication;
import com.ne.jp.shumipro.util.CsvDataSetLoader;
import com.ne.jp.shumipro.util.WithMockCustomUser;


@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class
})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = ShumiProjectApplication.class)
@Transactional
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DatabaseSetup(value = "/testData/")
    @WithMockCustomUser(username="shumiya", password="shumiya")
    public void 管理者ユーザで管理者画面がviewとして渡される() throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(view().name("admin/admin"));
    }

    @Test
    @DatabaseSetup(value = "/testData/")
    public void 未ログインユーザはユーザトップ画面へURL直打ちで遷移できない() throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/loginForm"));
    }

    @Test
    @DatabaseSetup(value = "/testData/")
    @WithMockCustomUser(username="test", password="test")
    public void 一般のユーザは管理者画面へURL直打ちで遷移できない() throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/loginForm"));
    }

    @Test
    @DatabaseSetup(value = "/testData/")
    @WithMockCustomUser(username="admin", password="admin")
    public void ログアウト処理でログイン画面へ遷移する() throws Exception {
        this.mockMvc.perform(post("/logout")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/loginForm?logout"));
    }
}
