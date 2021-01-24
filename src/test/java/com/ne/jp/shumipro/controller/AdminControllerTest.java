package com.ne.jp.shumipro.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.ne.jp.shumipro.component.SessionData;
import com.ne.jp.shumipro.mapper.UsersMapper;
import com.ne.jp.shumipro.security.UserAuth;
import com.ne.jp.shumipro.security.UserAuthMapper;

import com.ne.jp.shumipro.util.WebContextTestExecutionListener;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.ne.jp.shumipro.ShumiProjectApplication;
import com.ne.jp.shumipro.util.CsvDataSetLoader;
import com.ne.jp.shumipro.util.WithMockCustomUser;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class,
        WebContextTestExecutionListener.class
})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = ShumiProjectApplication.class)
@Transactional
@SuppressWarnings("deprecation")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    SessionData sessionData;

    @BeforeEach
    public void setUp() throws Exception {
        sessionData.setUsername("shumiya");
    }

    @AfterEach
    public void doAfter() throws Exception {
        // 後処理
    }

    @Test
    @DatabaseSetup(value = "/testData/")
    @WithMockCustomUser(username="shumiya", password="shumiya")
    public void test1_管理者ユーザで管理者画面がviewとして渡される() throws Exception {
        this.mockMvc.perform(post("/admin")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin"));
        }

    @Test
    @DatabaseSetup(value = "/testData/")
    @WithMockCustomUser(username="shumiya", password="shumiya")
    public void test2_管理者画面でユーザ一覧が表示される() throws Exception {
        this.mockMvc.perform(post("/admin")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(model().attribute("userList", containsInAnyOrder(
                        hasProperty("username", is("shumiya"))
                        , hasProperty("username", is("test")))))
                .andExpect(model().attribute("userList", hasSize(2)))
                .andExpect(model().attribute("loginUsername", "shumiya"));
    }

    @Test
    @DatabaseSetup(value = "/testData/")
    public void test3_未ログインユーザはユーザトップ画面へURL直打ちで遷移できない() throws Exception {
        this.mockMvc.perform(post("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/loginForm"));
    }

    @Test
    @DatabaseSetup(value = "/testData/")
    @WithMockCustomUser(username="test", password="test")
    public void test4_一般のユーザは管理者画面へURL直打ちで遷移できない() throws Exception {
        this.mockMvc.perform(post("/admin"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DatabaseSetup(value = "/testData/")
    @WithMockCustomUser(username="shumiya", password="shumiya")
    public void test5_ログアウト処理でログイン画面へ遷移する() throws Exception {
        this.mockMvc.perform(post("/logout")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/loginForm?logout"));
    }

    @Test
    @DatabaseSetup(value = "/testData/")
    @WithMockCustomUser(username="shumiya", password="shumiya")
    public void test6_削除処理後も管理者画面へ遷移する() throws Exception {
        this.mockMvc.perform(post("/admin/delete?username=test")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "削除が完了しました"))
                .andExpect(redirectedUrl("/admin"));

        List<UserAuth> userList = usersMapper.getUsersAll();
        // 削除が正しくされているか
        assertThat(userList.size(), is(1));
        // 削除されたレコードが正しいか
        assertThat(userList.get(0).getUsername(), is("shumiya"));
    }

    @Test
    @DatabaseSetup(value = "/testData/")
    @WithMockCustomUser(username="shumiya", password="shumiya")
    public void test7_ログインユーザのユーザ削除処理は失敗する() throws Exception {
        this.mockMvc.perform(post("/admin/delete?username=shumiya")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "削除に失敗しました"))
                .andExpect(redirectedUrl("/admin"));

        List<UserAuth> userList = usersMapper.getUsersAll();
        // 削除が行われていないことを確認
        assertThat(userList.size(), is(2));
    }


}