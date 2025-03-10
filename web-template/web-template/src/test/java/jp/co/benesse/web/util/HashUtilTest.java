package jp.co.benesse.web.util;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;

import jp.co.benesse.web.BaseTest;

/**
 * <pre>
 * ハッシュ化に関するユーティリティのテストクラス
 *
 * 作成日：2025/03/06
 * 更新日：2025/03/10
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
     * - 入力値が正常(半角英数字8文字以上16文字以内)である場合
     * - ケース1: 半角英字かつ8文字
     * - ケース2: 半角数字かつ16文字
     * - ケース3: 半角英数字（9文字）
     * 
     * 結果：
     * - 例外が発生せずに処理が終了し、SHA-256形式のハッシュ値が返される
     * </pre>
     * 
     * @param input
     * @param expectedHash
     * @param description
     */
    @ParameterizedTest(name = "【正常系】{2}")
    @CsvSource({
            "password, 5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8, 半角英字8文字",
            "1234567890123456, 7a51d064a1a216a692f753fcdab276e4ff201a01d8b66f56d50d4d719fd0dc87, 半角数字16文字",
            "password1, 0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e, 半角英数字9文字"
    })
    public void sha256_正常系(String input, String expectedHash, String description) {
        try {
            // メソッドの呼び出し
            String hashedPassword = HashUtil.sha256(input);

            // 検証
            assertNotNull(hashedPassword);
            assertEquals(expectedHash, hashedPassword);
        } catch (Exception e) {
            fail("例外が発生しました: " + e.getMessage());
        }
    }

    /**
     * 異常系テスト - 引数がnull
     * 
     * <pre>
     * 前提：
     * - 引数：インプットのパスワードがnullの場合
     * 
     * 結果：
     * - nullが返される
     * - ログに「エラー（例外）が発生しました」というメッセージが出力される
     * </pre>
     */
    @Test
    public void sha256_異常系_引数がnull() {
        try (MockedStatic<MessageDigest> mockedStatic = mockStatic(MessageDigest.class);
                MockedStatic<MessageUtil> messageUtilMockedStatic = mockStatic(MessageUtil.class)) {

            // モックの挙動を定義
            messageUtilMockedStatic.when(() -> MessageUtil.getMessage("XXXXX-001"))
                    .thenReturn("エラー（例外）が発生しました");

            // メソッドの呼び出し
            String hashedPassword = HashUtil.sha256(null);

            // 返り値がnullの検証
            assertNull(hashedPassword);

            // ログ出力の検証
            verify(mockAppender, Mockito.times(1)).append(logCaptor.capture());
            Level level = logCaptor.getAllValues().get(0).getLevel();
            String message = logCaptor.getAllValues().get(0).getMessage().getFormattedMessage();

            // ログレベル確認
            assertThat(level, is(Level.INFO));

            // 検証
            assertThat(message, is("エラー（例外）が発生しました"));
        }
    }

    /**
     * 異常系テスト - ハッシュ化失敗
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
    public void sha256_異常系_ハッシュ化失敗() {
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