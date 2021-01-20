package com.ne.jp.shumipro.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.ne.jp.shumipro.ShumiProjectApplication;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.ne.jp.shumipro.security.UserAuth;
import com.ne.jp.shumipro.security.UserAuthMapper;
import com.ne.jp.shumipro.util.CsvDataSetLoader;

@ExtendWith(SpringExtension.class)
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
      DependencyInjectionTestExecutionListener.class,
      TransactionalTestExecutionListener.class,
      DbUnitTestExecutionListener.class
    })
@SpringBootTest(classes = ShumiProjectApplication.class)
@SuppressWarnings("deprecation")
@Transactional
public class UsersMapperTest {

    @Autowired
    private UsersMapper usersMapper;
    
    @Autowired
    private UserAuthMapper userAuthMapper;

    // DatabaseSetupのvalueにCSVファイルのパスを設定することで、「table-ordering.txt」を参照し、
    // 順次テーブルを作成することでテスト用のテーブル群を作成する
    // このとき、valueのパスは「src/test/resources」配下を起点とし、CSVファイルのファイル名は
    // テーブルの名前と対応させることとする
    //
    // また、@Transactionalアノテーションを付与することで、テストが終わるとトランザクションをロールバックすることで
    // 環境を汚すことなくテストができる
	@Test
    @DatabaseSetup(value = "/testData/")
    public void contextLoads() {
        List<UserAuth> userList = usersMapper.getUsersAll();

        // Daoで正常にテーブルからレコードを取得できたか
        assertThat(userList.size(), is(2));
    }

    /**
     * create処理で新規レコードが作成されるか検証する
     * エンティティによってDBが想定通りに書き換えられたかExpectedDatabaseと比較することで検証
     */
    @Test
    @DatabaseSetup(value = "/testData/")
    @ExpectedDatabase(value = "/CRUD/create/", assertionMode=DatabaseAssertionMode.NON_STRICT)
    public void createメソッドでユーザが新しく生成される() {
        UserAuth user = new UserAuth();
        user.setUsername("test2");
        user.setPassword("password");
        user.setAdminflg("0");
        user.setEnabledflg("1");

        userAuthMapper.create(user);
    }

    /**
     * update処理で既存レコードがupdateされるか検証する
     * エンティティによってDBが想定通りに書き換えられたかExpectedDatabaseと比較することで検証
     */
    @Test
    @DatabaseSetup(value = "/testData/")
    @ExpectedDatabase(value = "/CRUD/update/", assertionMode=DatabaseAssertionMode.NON_STRICT)
    public void updateメソッドでユーザ1を書き換えられる() {
    	UserAuth user = new UserAuth();
        user.setUsername("test");
        user.setAdminflg("1");
        user.setEnabledflg("1");
        
        userAuthMapper.update(user);
    }

    /**
     * delete処理でレコードが削除されるか検証する
     * 処理前後のDBを用意し、削除後に想定結果となるか比較することで妥当性を検証
     */
    @Test
    @DatabaseSetup(value = "/testData/")
    @ExpectedDatabase(value = "/CRUD/delete/", assertionMode=DatabaseAssertionMode.NON_STRICT)
    public void deleteメソッドでtestユーザを削除できる() {
    	userAuthMapper.delete("test");
    }
}