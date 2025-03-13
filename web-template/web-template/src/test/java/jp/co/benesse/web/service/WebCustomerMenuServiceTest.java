package jp.co.benesse.web.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.operation.MicrosoftSqlDatabaseOperationLookup;

import jp.co.benesse.web.BaseTest;
import jp.co.benesse.web.CsvDataSetLoader;
import jp.co.benesse.web.config.PaginationProperties;
import jp.co.benesse.web.dto.BookDataDTO;
import jp.co.benesse.web.dto.BookRequestDTO;
import jp.co.benesse.web.entity.BookData;
import jp.co.benesse.web.exception.WebUnexpectedException;

/**
 * <pre>
 * web利用者メニューのテストクラス
 *
 * 作成日：2025/03/10
 * 更新日：2025/03/13
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
public class WebCustomerMenuServiceTest extends BaseTest {

    /** テスト対象 */
    @Autowired
    private WebCustomerMenuService webCustomerMenuService;

    /** ページングプロパティ */
    @SpyBean
    private PaginationProperties paginationProperties;

    /**
     * 図書一覧取得と在庫数設定メソッドのテスト
     */
    @Nested
    @Order(1)
    @Transactional
    @DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class, databaseOperationLookup = MicrosoftSqlDatabaseOperationLookup.class)
    class testGetBookListWithStock {

        /**
         * 正常系テスト
         * 
         * <pre>
         * 前提：
         * - 貸出中で貸出不可の図書1冊の書籍
         * - 返却済み・未貸出の図書計2冊の書籍
         * 
         * 結果：
         * - 例外が発生せずに処理が終了し、在庫数を設定した図書一覧DTOが返される
         * </pre>
         * 
         * @throws WebUnexpectedException
         */
        @Test
        @DatabaseSetup(value = "classpath:service/WebCustomerMenuServiceTest/getBookListWithStock/正常系/input/")
        public void getBookListWithStock_正常系() throws WebUnexpectedException {

            // 期待値の設定
            BookDataDTO expected1 = new BookDataDTO();
            expected1.setBookID("1");
            expected1.setTitle("タイトル1");
            expected1.setAuthor("著者1");
            expected1.setStockCount(0);
            expected1.setLibraryBookID("1");
            expected1.setLoanFlg(true);

            BookDataDTO expected2 = new BookDataDTO();
            expected2.setBookID("2");
            expected2.setTitle("タイトル2");
            expected2.setAuthor("著者2");
            expected2.setStockCount(2);
            expected2.setLibraryBookID("2");
            expected2.setLoanFlg(false);

            // メソッドの呼び出し
            List<BookDataDTO> result = webCustomerMenuService.getBookListWithStock();

            // 検証
            assertThat(result.size(), is(2));
            assertThat(result.get(0), is(samePropertyValuesAs(expected1)));
            assertThat(result.get(1), is(samePropertyValuesAs(expected2)));
        }

    }

    /**
     * 在庫数算出メソッドのテスト
     */
    @Nested
    @Order(2)
    class testCalculateStock {

        /**
         * 正常系テスト
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
            Map<String, Long> result = ReflectionTestUtils.invokeMethod(webCustomerMenuService, "calculateStock",
                    bookList);

            // 検証
            Map<String, Long> expectedResult = parseExpectedResultString(expectedResultStr);
            assertThat(result, is(expectedResult));
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
         * 異常系テスト
         * 
         * <pre>
         * 前提：
         * - bookListがnullの場合
         * - bookListが空の場合
         * 
         * 結果：
         * - 空のマップを返す
         * </pre>
         * 
         * @param bookListType
         * @param description
         */
        @ParameterizedTest(name = "【異常系】{1}")
        @CsvSource(value = {
                "null, 'bookListがnullの場合'",
                "empty, 'bookListが空の場合'"
        })
        public void calculateStock_異常系(String bookListType, String description) {
            // 引数の準備
            List<?> bookList = "null".equals(bookListType) ? null : Collections.emptyList();

            // メソッドの呼び出し
            Map<String, Long> result = ReflectionTestUtils.invokeMethod(webCustomerMenuService, "calculateStock",
                    bookList);

            // 検証
            assertTrue(result.isEmpty());
        }
    }

    /**
     * ページング機能総ページ数算出メソッドのテスト
     */
    @Nested
    @Order(3)
    class testCalculateTotalPages {

        /**
         * 正常系テスト
         * 
         * <pre>
         * 前提：
         * - ケース1: 総書籍数がページングプロパティの行数で割り切れる場合
         * - ケース2: 総書籍数がページングプロパティの行数で割り切れない場合
         * 
         * 結果：
         * - 正しい総ページ数を返す
         * </pre>
         * 
         * @param rowsPerPage
         * @param totalBooks
         * @param expectedPages
         * @param description
         */
        @ParameterizedTest(name = "【正常系】{3}")
        @CsvSource(value = {
                "10, 20, 2, '総書籍数がページングプロパティの行数で割り切れる場合'",
                "10, 25, 3, '総書籍数がページングプロパティの行数で割り切れない場合'"
        })
        public void calculateTotalPages_正常系(int rowsPerPage, int totalBooks, int expectedPages,
                String description) {
            // ページングプロパティの行数を設定
            paginationProperties.setRowsPerPage(rowsPerPage);

            // メソッドの呼び出し
            int result = webCustomerMenuService.calculateTotalPages(totalBooks);

            // 検証
            assertThat(result, is(expectedPages));
        }

        /**
         * 異常系テスト
         * 
         * <pre>
         * 前提：
         * - ケース1: 総書籍数が0の場合
         * - ケース2: 総書籍数が負の場合
         * 
         * 結果：
         * - 総ページ数は0を返す
         * </pre>
         * 
         * @param totalBooks
         * @param expectedPages
         * @param description
         */
        @ParameterizedTest(name = "【異常系】{2}")
        @CsvSource(value = {
                "0, 0, '総書籍数が0の場合'",
                "-5, 0, '総書籍数が負の場合'"
        })
        public void calculateTotalPages_異常系(int totalBooks, int expectedPages, String description) {
            // ページングプロパティの行数を設定
            paginationProperties.setRowsPerPage(10);

            // メソッドの呼び出し
            int result = webCustomerMenuService.calculateTotalPages(totalBooks);

            // 検証
            assertThat(result, is(expectedPages));
        }
    }

    /**
     * 貸出可能書籍判定メソッドのテスト
     */
    @Nested
    @Order(4)
    class testGetReservatableBooks {
        /**
         * 正常系テスト
         * 
         * <pre>
         * 前提：
         * - 在庫数が1以上の書籍が含まれる場合
         * 
         * 結果：
         * - 貸出可能な書籍リストを返す
         * </pre>
         */
        @Test
        public void getReservatableBooks_正常系() {
            // 引数の準備
            BookRequestDTO book1 = new BookRequestDTO();
            book1.setBookID("book1");
            book1.setTitle("タイトル1");
            book1.setAuthor("著者1");
            book1.setStockCount(2); // 貸出可能

            BookRequestDTO book2 = new BookRequestDTO();
            book2.setBookID("book2");
            book2.setTitle("タイトル2");
            book2.setAuthor("著者2");
            book2.setStockCount(1); // 貸出可能

            BookRequestDTO book3 = new BookRequestDTO();
            book3.setBookID("book3");
            book3.setTitle("タイトル3");
            book3.setAuthor("著者3");
            book3.setStockCount(0); // 貸出不可

            List<BookRequestDTO> bookRequestDTOList = Arrays.asList(book1, book2, book3);

            // メソッドの呼び出し
            List<BookRequestDTO> result = webCustomerMenuService.getReservatableBooks(bookRequestDTOList);

            // 期待値作成
            BookRequestDTO expected1 = new BookRequestDTO();
            expected1.setBookID("book1");
            expected1.setTitle("タイトル1");
            expected1.setAuthor("著者1");
            expected1.setStockCount(2);

            BookRequestDTO expected2 = new BookRequestDTO();
            expected2.setBookID("book2");
            expected2.setTitle("タイトル2");
            expected2.setAuthor("著者2");
            expected2.setStockCount(1);

            // 結果比較
            assertThat(result, containsInAnyOrder(
                    samePropertyValuesAs(expected1),
                    samePropertyValuesAs(expected2)));
        }

        /**
         * 異常系テスト
         * 
         * <pre>
         * 前提：
         * - 書籍情報リストが空の場合
         * 
         * 結果：
         * - 空のリストを返す
         * </pre>
         */
        @Test
        public void getReservatableBooks_異常系() {
            // 引数の準備
            List<BookRequestDTO> bookRequestDTOList = Collections.emptyList();

            // メソッドの呼び出し
            List<BookRequestDTO> result = webCustomerMenuService.getReservatableBooks(bookRequestDTOList);

            // 検証
            assertThat(result, empty());
        }
    }
}