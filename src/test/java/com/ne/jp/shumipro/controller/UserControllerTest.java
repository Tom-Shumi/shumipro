package com.ne.jp.shumipro.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.ne.jp.shumipro.ShumiProjectApplication;
import com.ne.jp.shumipro.util.CsvDataSetLoader;
import com.ne.jp.shumipro.util.WithMockCustomUser;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
@SuppressWarnings("deprecation")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // getリクエストでviewを指定し、httpステータスでリクエストの成否を判定
    @Test
    @DatabaseSetup(value = "/testData/")
    @WithMockCustomUser(username="test", password="test")
    public void ユーザ画面に遷移できる() throws Exception {
        // andDo(print())でリクエスト・レスポンスを表示
        this.mockMvc.perform(get("/user"))//.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/user"));
    }
}
