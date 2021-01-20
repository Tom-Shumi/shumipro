package com.ne.jp.shumipro.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.ne.jp.shumipro.ShumiProjectApplication;
import com.ne.jp.shumipro.util.CsvDataSetLoader;


@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
      DependencyInjectionTestExecutionListener.class,
      TransactionalTestExecutionListener.class,
      DbUnitTestExecutionListener.class
    })
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = ShumiProjectApplication.class)
@Transactional
public class LoginControllerTest {
    //mockMvc TomcatサーバへデプロイすることなくHttpリクエスト・レスポンスを扱うためのMockオブジェクト
    @Autowired
    private MockMvc mockMvc;

    // getリクエストでviewを指定し、httpステータスでリクエストの成否を判定
    @Test
    public void ログイン画面表示処理が走って200が返る() throws Exception {
        // andDo(print())でリクエスト・レスポンスを表示
        this.mockMvc.perform(get("/loginForm"))//.andDo(print())
            .andExpect(status().isOk());
    }
    @Test
    public void ログイン画面表示処理でモデルのメッセージにhelloが渡される() throws Exception {
        this.mockMvc.perform(get("/loginForm"))
            .andExpect(model().attribute("message", "hello!"));
    }
    
    @Test
    public void ログイン画面表示処理でモデルへユーザEntityが格納される() throws Exception {
        this.mockMvc.perform(get("/loginForm"))
            .andExpect(model().attribute("user", hasProperty("username", is("test0"))));
    }
    
    @Test
    public void ログイン画面表示処理でモデルのフォームへユーザリストが格納される() throws Exception {
        this.mockMvc.perform(get("/loginForm")).andExpect(model()
        		.attribute("dbForm", hasProperty("userList", hasItem(hasProperty("username", is("test1"))))));
    }
    
    @Test
    public void ログイン画面表示処理でviewとしてloginFormが渡される() throws Exception {
        this.mockMvc.perform(get("/loginForm"))
            .andExpect(status().isOk())
            .andExpect(view().name("loginForm"));
    }
    
    @Test
    @DatabaseSetup(value = "/testData/")
    public void init処理で既存のタスクがモデルへ渡される() throws Exception {
        // mockMvcで「/todo/init」へgetリクエストを送信
        this.mockMvc.perform(get("/loginForm"))
        // モデルへDBのレコードがリストとして渡される
            .andExpect(model().attribute("loginForm", hasProperty("userList", hasItem(hasProperty("username", is("shumiya"))))));
    }
    
    @Test
    @DatabaseSetup(value = "/testData/")
    @ExpectedDatabase(value = "/POST/create/", assertionMode=DatabaseAssertionMode.NON_STRICT)
//    @ExpectedDatabase(value = "/POST/create/", table="users")
    public void 登録処理で新規ユーザがDBへ登録される() throws Exception {

        this.mockMvc.perform(post("/loginForm/registUser")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "test2")
            .param("password", "test")
            .param("adminflg", "0"));
    }
    
    @Test
    @DatabaseSetup(value = "/testData/")
    public void DB上に存在する管理者ユーザでログインできる() throws Exception {
        this.mockMvc.perform(formLogin("/login")
                .user("shumiya")
                .password("shumiya"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }
    
    @Test
    @DatabaseSetup(value = "/testData/")
    public void DB上に存在するユーザでパスワードを間違えると失敗画面へリダイレクトされる() throws Exception {
        this.mockMvc.perform(formLogin("/login")
                .user("shumiya")
                .password("aaaaa"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/loginForm?error=true"));
    }
    
    @Test
    @DatabaseSetup(value = "/testData/")
    public void DB上に存在しないユーザだと失敗画面へリダイレクトされる() throws Exception {
        this.mockMvc.perform(formLogin("/login")
                .user("aaaaa")
                .password("shumiya"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/loginForm?error=true"));
    }
}
