package jp.co.benesse.web.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import jp.co.benesse.web.BaseTest;
import jp.co.benesse.web.entity.WebCustomerEntity;
import jp.co.benesse.web.exception.WebParamException;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.form.WebCustomerLoginForm;
import jp.co.benesse.web.repository.WebCustomerLoginRepository;
import jp.co.benesse.web.util.HashUtil;

/**
 * <pre>
 * web利用者ログインサービスのテストクラス
 *
 * 作成日：2025/03/05
 * 更新日：2025/03/05
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
public class WebCustomerLoginServiceTest extends BaseTest {

    /** テスト対象 */
    @SpyBean
    private WebCustomerLoginService webCustomerLoginService;

    /** モックリポジトリ */
    @MockBean
    private WebCustomerLoginRepository webCustomerLoginRepository;

    /**
     * 正常系テスト
     * 
     * <pre>
     * 前提：
     * - 正しい利用者IDとパスワードが入力される場合
     * 
     * 結果：
     * - 例外が発生せずに処理が終了する
     * </pre>
     * 
     * @throws WebUnexpectedException
     * @throws WebParamException
     */
    @Test
    public void login_正常系() throws WebUnexpectedException, WebParamException {

        // モックの挙動を定義
        String hashedPassword = HashUtil.sha256("testPassword");
        WebCustomerEntity mockEntity = new WebCustomerEntity();
        mockEntity.setCustomerId("testCustomer");
        mockEntity.setCustomerName("testUser");
        when(webCustomerLoginRepository.getLoginInfo("testCustomer", hashedPassword)).thenReturn(mockEntity);

        // メソッド引数の準備
        WebCustomerLoginForm form = new WebCustomerLoginForm();
        form.setCustomerID("testCustomer");
        form.setPassword("testPassword");

        // メソッドの呼び出し
        WebCustomerEntity result = webCustomerLoginService.login(form);

        // 検証
        assertEquals(result.getCustomerId(), "testCustomer");
        assertEquals(result.getCustomerName(), "testUser");
    }

}