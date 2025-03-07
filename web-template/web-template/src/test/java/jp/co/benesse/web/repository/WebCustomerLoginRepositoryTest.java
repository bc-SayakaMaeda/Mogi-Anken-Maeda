package jp.co.benesse.web.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import jp.co.benesse.web.BaseTest;
import jp.co.benesse.web.CsvDataSetLoader;
import jp.co.benesse.web.entity.WebCustomerEntity;
import jp.co.benesse.web.exception.WebUnexpectedException;

/**
 * <pre>
 * WebCustomerLoginRepositoryのテストクラス
 *
 * 作成日：2025/03/07
 * 更新日：2025/03/07
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
@Transactional
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
public class WebCustomerLoginRepositoryTest extends BaseTest {

    /** テスト対象 */
    @Autowired
    private WebCustomerLoginRepository webCustomerLoginRepository;

    /**
     * 正常系テスト
     * 
     * <pre>
     * 前提：
     * - 正しい利用者IDとハッシュ化されたパスワードが入力される場合
     * 
     * 結果：
     * - 正しいWebCustomerEntityが返される
     * </pre>
     * 
     * @throws WebUnexpectedException
     */
    @Test
    @DatabaseSetup(value = "classpath:repository/WebCustomerLoginRepositoryTest/正常系/input/")
    public void getLoginInfo_正常系() throws WebUnexpectedException {

        // 期待値の設定
        WebCustomerEntity expected = new WebCustomerEntity();
        expected.setCustomerId("0000000000000001");
        expected.setCustomerName("山田太郎");
        expected.setPostCode("7000001");
        expected.setAddress("岡山市北区1丁目");
        expected.setEmail("山田太郎@benesse.mail");

        // メソッドの呼び出し
        WebCustomerEntity result = webCustomerLoginRepository.getLoginInfo(
                "0000000000000001",
                "0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e");

        // 検証
        assertThat(result, is(samePropertyValuesAs(expected)));
    }
}