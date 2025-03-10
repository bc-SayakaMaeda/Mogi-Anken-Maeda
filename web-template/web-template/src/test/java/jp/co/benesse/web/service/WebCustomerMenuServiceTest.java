package jp.co.benesse.web.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import jp.co.benesse.web.BaseTest;
import jp.co.benesse.web.entity.BookData;

/**
 * <pre>
 * web利用者メニューのテストクラス
 *
 * 作成日：2025/03/10
 * 更新日：2025/03/10
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
public class WebCustomerMenuServiceTest extends BaseTest {

    /** テスト対象 */
    @Autowired
    private WebCustomerMenuService webCustomerMenuService;

    /**
     * CalculateStock正常系テスト
     * 
     * <pre>
     * 前提：
     * - ケース1: 貸出中の書籍が含まれない場合
     * - ケース2: 貸出中の書籍が含まれる場合
     * 
     * 結果：
     * - 例外が発生せずに処理が終了し、在庫数マップが返される（キー：BookID, 値：在庫数）
     * </pre>
     * 
     * @param bookDataStr
     * @param expectedResultStr
     * @param description
     */
    @ParameterizedTest(name = "【正常系】{2}")
    @CsvSource(value = {
            "'book1,lib1,false;book1,lib2,false;book2,lib3,false', 'book1=2,book2=1', '貸出中の書籍が含まれない場合'",
            "'book1,lib1,false;book1,lib2,true;book2,lib3,false;book2,lib4,true', 'book1=1,book2=1', '貸出中の書籍が含まれる場合'"
    })
    public void calculateStock_正常系(String bookDataStr, String expectedResultStr, String description) {
        // 引数の準備
        List<BookData> bookList = parseBookDataString(bookDataStr);

        // メソッドの呼び出し
        Map<String, Long> result = ReflectionTestUtils.invokeMethod(webCustomerMenuService, "calculateStock", bookList);

        // 検証
        assertNotNull(result);
        Map<String, Long> expectedResult = parseExpectedResultString(expectedResultStr);
        assertEquals(expectedResult, result);
    }

    /**
     * 書籍データを文字列から BookData のリストに変換
     * 
     * @param bookDataStr
     * @return List<BookData>
     */
    private List<BookData> parseBookDataString(String bookDataStr) {
        String[] bookDataArray = bookDataStr.split(";");
        return Arrays.stream(bookDataArray)
                .map(data -> {
                    String[] parts = data.split(",");
                    if (parts.length < 3) {
                        throw new IllegalArgumentException("不正なデータ形式: " + data);
                    }
                    BookData bookData = new BookData();
                    bookData.setBookID(parts[0]);
                    bookData.setLibraryBookID(parts[1]);
                    bookData.setLoanFlg(Boolean.parseBoolean(parts[2]));
                    return bookData;
                })
                .collect(Collectors.toList());
    }

    /**
     * 期待される結果を文字列から Map<String, Long> に変換
     * 
     * @param expectedResultStr
     * @return Map<String, Long>
     */
    private Map<String, Long> parseExpectedResultString(String expectedResultStr) {
        String[] resultPairs = expectedResultStr.split(",");
        return Arrays.stream(resultPairs)
                .map(pair -> pair.split("="))
                .collect(Collectors.toMap(
                        parts -> parts[0],
                        parts -> Long.parseLong(parts[1])));
    }

    /**
     * CalculateStock異常系テスト - bookListがnullまたは空
     * 
     * <pre>
     * 前提：
     * - bookListがnullの場合
     * 
     * 結果：
     * - 空のマップを返す
     * </pre>
     */
    @Test
    public void calculateStock_異常系_bookListがnull() {

        // メソッドの呼び出し
        Map<String, Long> result = ReflectionTestUtils.invokeMethod(webCustomerMenuService, "calculateStock",
                Collections.emptyList());

        // 検証
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}