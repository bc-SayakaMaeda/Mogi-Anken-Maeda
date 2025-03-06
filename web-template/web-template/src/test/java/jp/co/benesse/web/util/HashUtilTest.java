package jp.co.benesse.web.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
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
     * - 入力値が "password123" の場合
     * 
     * 結果：
     * - 例外が発生せずに処理が終了し、SHA-256形式のハッシュ値が返される
     * </pre>
     */
    @Test
    public void testSha256_正常系() {
        try {
            // メソッド引数の準備
            String password = "password123";

            // メソッドの呼び出し
            String hashedPassword = HashUtil.sha256(password);

            // 検証
            assertNotNull(hashedPassword);
            assertEquals("ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f", hashedPassword);
        } catch (Exception e) {
            fail("例外が発生しました: " + e.getMessage());
        }
    }
}