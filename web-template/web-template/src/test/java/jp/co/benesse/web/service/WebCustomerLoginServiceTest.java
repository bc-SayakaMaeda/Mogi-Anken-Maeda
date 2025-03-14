package jp.co.benesse.web.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
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
        assertThat(result.getCustomerId(), is("testCustomer"));
        assertThat(result.getCustomerName(), is("testUser"));
    }

    /**
     * 異常系テスト - パスワードのハッシュ化失敗
     * 
     * <pre>
     * 前提：
     * - HashUtil.sha256がnullを返す場合
     * 
     * 結果：
     * - WebUnexpectedExceptionがスローされる
     * </pre>
     */
    @Test
    public void login_異常系_ハッシュ化失敗() {

        // モックの挙動を定義
        try (MockedStatic<HashUtil> mockedHashUtil = Mockito.mockStatic(HashUtil.class)) {
            mockedHashUtil.when(() -> HashUtil.sha256("testPassword")).thenReturn(null);

            // メソッド引数の準備
            WebCustomerLoginForm form = new WebCustomerLoginForm();
            form.setCustomerID("testCustomer");
            form.setPassword("testPassword");

            // メソッドの呼び出しと例外確認
            WebUnexpectedException exception = assertThrows(WebUnexpectedException.class, () -> {
                webCustomerLoginService.login(form);
            });

            // 検証
            assertThat(exception.getMessage(), is("パスワードのハッシュ化に失敗しました。"));
        }
    }

    /**
     * 異常系テスト - 引数がnull
     * 
     * <pre>
     * 前提：
     * - 引数：WebCustomerLoginFormがnullの場合
     * 
     * 
     * 結果：
     * - WebParamExceptionがスローされる
     * </pre>
     */
    @Test
    public void login_異常系_引数がnull() {

        // メソッド引数の準備
        WebCustomerLoginForm form = null;

        // メソッドの呼び出しと例外確認
        WebParamException exception = assertThrows(WebParamException.class, () -> {
            webCustomerLoginService.login(form);
        });

        // 検証
        assertThat(exception.getMessage(), is("WE03-XXXXX-001::エラー（例外）が発生しました"));
    }

    /**
     * 異常系テスト - ログイン判定情報が取得できない場合
     *
     * <pre>
     * 前提：
     * - WebCustomerLoginRepository.getLoginInfoがnullを返す場合
     *
     * 結果：
     * - ログが正しく出力される
     * </pre>
     *
     * @throws WebUnexpectedException
     * @throws WebParamException
     */
    @Test
    public void login_異常系_ログイン判定情報が取得できない() throws WebUnexpectedException, WebParamException {

        // モックの挙動を定義
        try (MockedStatic<HashUtil> mockedHashUtil = Mockito.mockStatic(HashUtil.class)) {
            mockedHashUtil.when(() -> HashUtil.sha256("testPassword")).thenReturn("hashedPassword");

            // メソッド引数の準備
            WebCustomerLoginForm form = new WebCustomerLoginForm();
            form.setCustomerID("testCustomer");
            form.setPassword("testPassword");

            // メソッド呼び出し
            webCustomerLoginService.login(form);

            // ログ出力の検証
            verify(mockAppender, Mockito.times(1)).append(logCaptor.capture());
            Level level = logCaptor.getAllValues().get(0).getLevel();
            String message = logCaptor.getAllValues().get(0).getMessage().getFormattedMessage();
            Throwable throwable = logCaptor.getAllValues().get(0).getThrown();

            // ログレベル確認
            assertThat(level, is(Level.INFO));

            // ログメッセージ確認
            assertThat(message,
                    is("WE03-XXXXX-009::ログインに失敗しました(jp.co.benesse.web.service.WebCustomerLoginService[60])"));

            // スタックトレース確認（例外はスローされない）
            assertThat(throwable, is(nullValue()));
        }
    }

}