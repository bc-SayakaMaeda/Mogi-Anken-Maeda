package jp.co.benesse.web.util;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;

import jp.co.benesse.web.BaseTest;

/**
 * <pre>
 * ハッシュ化に関するユーティリティのテストクラス
 *
 * 作成日：2025/03/06
 * 更新日：2025/03/06
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
public class HashUtilTest extends BaseTest {

    /** テスト対象 */
    @SpyBean
    private HashUtil hashUtil;

    /**
     * 正常系テスト
     * 
     * <pre>
     * 前提：
     * - 入力値が "testPassword" の場合
     * 
     * 結果：
     * - 例外が発生せずに処理が終了し、SHA-256形式のハッシュ値が返される
     * </pre>
     */
    @Test
    public void sha256_正常系() {
        try {
            // メソッド引数の準備
            String password = "testPassword";

            // メソッドの呼び出し
            String hashedPassword = HashUtil.sha256(password);

            // 検証
            assertNotNull(hashedPassword);
            assertEquals("fd5cb51bafd60f6fdbedde6e62c473da6f247db271633e15919bab78a02ee9eb", hashedPassword);
        } catch (Exception e) {
            fail("例外が発生しました: " + e.getMessage());
        }
    }

    /**
     * 異常系テスト - ハッシュ化に失敗した場合にnullが返される
     * 
     * <pre>
     * 前提：
     * - MessageDigest.getInstance("SHA-256") が NoSuchAlgorithmException をスローする場合
     * 
     * 結果: 
     * - nullが返される
     * - ログに「パスワードのハッシュ化に失敗しました。」というメッセージが出力される
     * </pre>
     */
    @Test
    public void sha256_異常系() {
        try (MockedStatic<MessageDigest> mockedStatic = mockStatic(MessageDigest.class)) {

            // モックの挙動を定義
            mockedStatic.when(() -> MessageDigest.getInstance("SHA-256"))
                    .thenThrow(new NoSuchAlgorithmException());

            try (MockedStatic<MessageUtil> messageUtilMockedStatic = mockStatic(MessageUtil.class)) {
                messageUtilMockedStatic.when(() -> MessageUtil.getMessage("error.hash.password"))
                        .thenReturn("パスワードのハッシュ化に失敗しました。");

                // メソッドの呼び出し
                String hashedPassword = HashUtil.sha256("testPassword");

                // 返り値がnullの検証
                assertNull(hashedPassword);

                // ログ出力の検証
                verify(mockAppender, Mockito.times(1)).append(logCaptor.capture());
                Level level = logCaptor.getAllValues().get(0).getLevel();
                String message = logCaptor.getAllValues().get(0).getMessage().getFormattedMessage();

                // ログレベル確認
                assertThat(level, is(Level.INFO));

                // ログメッセージ確認
                assertThat(message, is("パスワードのハッシュ化に失敗しました。"));
            }
        }
    }
}