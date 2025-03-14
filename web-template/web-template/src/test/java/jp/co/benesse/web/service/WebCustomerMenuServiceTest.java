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
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
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
         * 書籍が1種類で貸出中の場合のテスト
         * 
         * <pre>
         * 前提：
         * - 貸出中で貸出不可の図書1冊のみが存在する
         * 
         * 結果：
         * - 例外が発生せずに処理が終了する
         * - 在庫数0で貸出中フラグがtrueの図書一覧DTOが1件返される
         * </pre>
         * 
         * @throws WebUnexpectedException
         */
        @Test
        @DatabaseSetup(value = "classpath:service/WebCustomerMenuServiceTest/getBookListWithStock/本が1種類_貸出中/input/")
        public void getBookListWithStock_本が1種類_貸出中() throws WebUnexpectedException {
            // 期待値の設定
            BookDataDTO expected = new BookDataDTO();
            expected.setBookID("1");
            expected.setTitle("タイトル1");
            expected.setAuthor("著者1");
            expected.setStockCount(0);
            expected.setLibraryBookID("1");
            expected.setLoanFlg(true);

            // メソッドの呼び出し
            List<BookDataDTO> result = webCustomerMenuService.getBookListWithStock();

            // 検証
            assertThat(result.size(), is(1));
            assertThat(result.get(0), is(samePropertyValuesAs(expected)));
        }

        /**
         * 書籍が1種類で貸出可能な場合のテスト
         * 
         * <pre>
         * 前提：
         * - 貸出可能な図書1冊のみが存在する
         * 
         * 結果：
         * - 例外が発生せずに処理が終了する
         * - 在庫数1で貸出中フラグがfalseの図書一覧DTOが1件返される
         * </pre>
         * 
         * @throws WebUnexpectedException
         */
        @Test
        @DatabaseSetup(value = "classpath:service/WebCustomerMenuServiceTest/getBookListWithStock/本が1種類_貸出可能/input/")
        public void getBookListWithStock_本が1種類_貸出可能() throws WebUnexpectedException {
            // 期待値の設定
            BookDataDTO expected = new BookDataDTO();
            expected.setBookID("1");
            expected.setTitle("タイトル1");
            expected.setAuthor("著者1");
            expected.setStockCount(1);
            expected.setLibraryBookID("1");
            expected.setLoanFlg(false);

            // メソッドの呼び出し
            List<BookDataDTO> result = webCustomerMenuService.getBookListWithStock();

            // 検証
            assertThat(result.size(), is(1));
            assertThat(result.get(0), is(samePropertyValuesAs(expected)));
        }

        /**
         * 書籍が2種類以上ですべて貸出中の場合のテスト
         * 
         * <pre>
         * 前提：
         * - 貸出中で貸出不可の図書が2冊以上存在する
         * 
         * 結果：
         * - 例外が発生せずに処理が終了する
         * - すべての本の在庫数が0で貸出中フラグがtrueの図書一覧DTOが返される
         * </pre>
         * 
         * @throws WebUnexpectedException
         */
        @Test
        @DatabaseSetup(value = "classpath:service/WebCustomerMenuServiceTest/getBookListWithStock/本が2種類以上_すべて貸出中/input/")
        public void getBookListWithStock_本が2種類以上_すべて貸出中() throws WebUnexpectedException {
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
            expected2.setStockCount(0);
            expected2.setLibraryBookID("2");
            expected2.setLoanFlg(true);

            // メソッドの呼び出し
            List<BookDataDTO> result = webCustomerMenuService.getBookListWithStock();

            // 検証
            assertThat(result.size(), is(2));
            assertThat(result.get(0), is(samePropertyValuesAs(expected1)));
            assertThat(result.get(1), is(samePropertyValuesAs(expected2)));
        }

        /**
         * 書籍2種類以上で一部貸出可能な場合のテスト
         * 
         * <pre>
         * 前提：
         * - 貸出可能な図書が一部存在する
         * 
         * 結果：
         * - 例外が発生せずに処理が終了する
         * - 在庫数が0で貸出フラグがtrueと1以上の本で貸出中フラグがfalseの図書一覧DTOが返される
         * </pre>
         * 
         * @throws WebUnexpectedException
         */
        @Test
        @DatabaseSetup(value = "classpath:service/WebCustomerMenuServiceTest/getBookListWithStock/本が2種類以上_一部貸出中/input/")
        public void getBookListWithStock_本が2種類以上_一部貸出中() throws WebUnexpectedException {
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
            expected2.setStockCount(1);
            expected2.setLibraryBookID("2");
            expected2.setLoanFlg(false);

            // メソッドの呼び出し
            List<BookDataDTO> result = webCustomerMenuService.getBookListWithStock();

            // 検証
            assertThat(result.size(), is(2));
            assertThat(result.get(0), is(samePropertyValuesAs(expected1)));
            assertThat(result.get(1), is(samePropertyValuesAs(expected2)));
        }

        /**
         * 書籍が2種類以上ですべて貸出可能な場合のテスト
         * 
         * <pre>
         * 前提：
         * - 貸出可能な図書が2冊以上存在する
         * - 返却済み、未貸出が含まれる
         * 
         * 結果：
         * - 例外が発生せずに処理が終了する
         * - すべての本の在庫数が1以上で貸出中フラグがfalseの図書一覧DTOが返される
         * </pre>
         * 
         * @throws WebUnexpectedException
         */
        @Test
        @DatabaseSetup(value = "classpath:service/WebCustomerMenuServiceTest/getBookListWithStock/本が2種類以上_すべて貸出可能/input/")
        public void getBookListWithStock_本が2種類以上_すべて貸出可能() throws WebUnexpectedException {
            // 期待値の設定
            BookDataDTO expected1 = new BookDataDTO();
            expected1.setBookID("1");
            expected1.setTitle("タイトル1");
            expected1.setAuthor("著者1");
            expected1.setStockCount(1);
            expected1.setLibraryBookID("1");
            expected1.setLoanFlg(false);

            BookDataDTO expected2 = new BookDataDTO();
            expected2.setBookID("2");
            expected2.setTitle("タイトル2");
            expected2.setAuthor("著者2");
            expected2.setStockCount(1);
            expected2.setLibraryBookID("2");
            expected2.setLoanFlg(false);

            // メソッドの呼び出し
            List<BookDataDTO> result = webCustomerMenuService.getBookListWithStock();

            // 検証
            assertThat(result.size(), is(2));
            assertThat(result.get(0), is(samePropertyValuesAs(expected1)));
            assertThat(result.get(1), is(samePropertyValuesAs(expected2)));
        }

        /**
         * 正常系テスト
         * 
         * <pre>
         * 前提：
         * - 図書が1冊_貸出中
         * - 図書が1冊_貸出可能_返却済み
         * - 図書が2冊以上_すべて貸出中
         * - 図書が2冊以上_一部貸出中・未貸出
         * - 図書が2冊以上_すべて貸出可能_未貸出
         * 
         * 結果：
         * - 例外が発生せずに処理が終了する
         * </pre>
         * 
         * @throws WebUnexpectedException
         */
        @Test
        @DatabaseSetup(value = "classpath:service/WebCustomerMenuServiceTest/getBookListWithStock/正常系/input/")
        public void getBookListWithStock_正常系() throws WebUnexpectedException {
            // 期待値の設定
            // 図書が1冊_貸出中
            BookDataDTO expected1 = new BookDataDTO();
            expected1.setBookID("1");
            expected1.setTitle("タイトル1");
            expected1.setAuthor("著者1");
            expected1.setStockCount(0);
            expected1.setLibraryBookID("1");
            expected1.setLoanFlg(true);

            // 図書が1冊_貸出可能_返却済み
            BookDataDTO expected2 = new BookDataDTO();
            expected2.setBookID("2");
            expected2.setTitle("タイトル2");
            expected2.setAuthor("著者2");
            expected2.setStockCount(1);
            expected2.setLibraryBookID("2");
            expected2.setLoanFlg(false);

            // 図書が2冊以上_すべて貸出中
            BookDataDTO expected3 = new BookDataDTO();
            expected3.setBookID("3");
            expected3.setTitle("タイトル3");
            expected3.setAuthor("著者3");
            expected3.setStockCount(0);
            expected3.setLibraryBookID("3");
            expected3.setLoanFlg(true);

            BookDataDTO expected4 = new BookDataDTO();
            expected4.setBookID("3");
            expected4.setTitle("タイトル3");
            expected4.setAuthor("著者3");
            expected4.setStockCount(0);
            expected4.setLibraryBookID("4");
            expected4.setLoanFlg(true);

            // 図書が2冊以上_一部貸出中・未貸出
            BookDataDTO expected5 = new BookDataDTO();
            expected5.setBookID("4");
            expected5.setTitle("タイトル4");
            expected5.setAuthor("著者4");
            expected5.setStockCount(0);
            expected5.setLibraryBookID("5");
            expected5.setLoanFlg(true);

            BookDataDTO expected6 = new BookDataDTO();
            expected6.setBookID("4");
            expected6.setTitle("タイトル4");
            expected6.setAuthor("著者4");
            expected6.setStockCount(1);
            expected6.setLibraryBookID("6");
            expected6.setLoanFlg(false);

            // 図書が2冊以上_すべて貸出可能_未貸出
            BookDataDTO expected7 = new BookDataDTO();
            expected7.setBookID("5");
            expected7.setTitle("タイトル5");
            expected7.setAuthor("著者5");
            expected7.setStockCount(1);
            expected7.setLibraryBookID("7");
            expected7.setLoanFlg(false);

            BookDataDTO expected8 = new BookDataDTO();
            expected8.setBookID("5");
            expected8.setTitle("タイトル5");
            expected8.setAuthor("著者5");
            expected8.setStockCount(1);
            expected8.setLibraryBookID("8");
            expected8.setLoanFlg(false);

            // メソッドの呼び出し
            List<BookDataDTO> result = webCustomerMenuService.getBookListWithStock();

            // 検証
            assertThat("結果のサイズが期待値と一致しません", result.size(), is(5));
            assertThat("図書が1冊_貸出中の検証に失敗しました", result.get(0), is(samePropertyValuesAs(expected1)));
            assertThat("図書が1冊_貸出可能の検証に失敗しました", result.get(1), is(samePropertyValuesAs(expected2)));
            assertThat("図書が2冊以上_すべて貸出中の検証に失敗しました (1冊目)", result.get(2), is(samePropertyValuesAs(expected3)));
            assertThat("図書が2冊以上_すべて貸出中の検証に失敗しました (2冊目)", result.get(3), is(samePropertyValuesAs(expected4)));
            assertThat("図書が2冊以上_一部貸出中の検証に失敗しました (貸出中)", result.get(4), is(samePropertyValuesAs(expected5)));
            assertThat("図書が2冊以上_一部貸出中の検証に失敗しました (貸出可能)", result.get(5), is(samePropertyValuesAs(expected6)));
            assertThat("図書が2冊以上_すべて貸出可能の検証に失敗しました (1冊目)", result.get(6), is(samePropertyValuesAs(expected7)));
            assertThat("図書が2冊以上_すべて貸出可能の検証に失敗しました (2冊目)", result.get(7), is(samePropertyValuesAs(expected8)));
        }

    }

    /**
     * 図書一覧取得と在庫数設定メソッドのテスト
     */
    @Nested
    @Order(2)
    @Transactional
    @DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class, databaseOperationLookup = MicrosoftSqlDatabaseOperationLookup.class)
    class testGetBookListByIdWithStock {

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
         * @param bookIds
         * @throws WebUnexpectedException
         */
        @ParameterizedTest(name = "【{0}】")
        @MethodSource("bookIdsProvider")
        @DatabaseSetup(value = "classpath:service/WebCustomerMenuServiceTest/getBookListByIdWithStock/正常系/input/")
        public void getBookListByIdWithStock_正常系(List<String> bookIds) throws WebUnexpectedException {

            // 期待値の設定
            BookRequestDTO expected1 = new BookRequestDTO();
            expected1.setBookID("1");
            expected1.setTitle("タイトル1");
            expected1.setAuthor("著者1");
            expected1.setStockCount(0);

            BookRequestDTO expected2 = new BookRequestDTO();
            expected2.setBookID("2");
            expected2.setTitle("タイトル2");
            expected2.setAuthor("著者2");
            expected2.setStockCount(2);

            // メソッドの呼び出し
            List<BookRequestDTO> result = webCustomerMenuService.getBookListByIdWithStock(bookIds);

            // 検証
            assertThat(result.size(), is(2));
            assertThat(result.get(0), is(samePropertyValuesAs(expected1)));
            assertThat(result.get(1), is(samePropertyValuesAs(expected2)));
        }

        /**
         * @return 書籍IDリスト
         */
        static Stream<Arguments> bookIdsProvider() {
            return Stream.of(
                    Arguments.of(List.of("1", "2")));
        }

    }

    /**
     * 在庫数算出メソッドのテスト
     */
    @Nested
    @Order(3)
    class testCalculateStock {

        /**
         * 正常系テスト
         * 
         * <pre>
         * 前提：
         * - ケース1: 本が1種類、貸出中の書籍が含まれない場合
         * - ケース2: 本が1種類、貸出中の書籍が含まれる場合
         * - ケース3: 本が2種類以上、貸出中の書籍が含まれない場合
         * - ケース4: 本が2種類以上、貸出中の書籍が含まれる場合
         * - ケース5: 本が2種類以上、1冊貸出中、1冊貸出中ではない場合
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
                "'book1,lib1,false;book1,lib2,false', 'book1=2', '本が1種類、貸出中の書籍が含まれない場合'",
                "'book1,lib1,false;book1,lib2,true', 'book1=1', '本が1種類、貸出中の書籍が含まれる場合'",
                "'book1,lib1,false;book1,lib2,false;book2,lib3,false', 'book1=2,book2=1', '本が2種類以上、貸出中の書籍が含まれない場合'",
                "'book1,lib1,false;book1,lib2,true;book2,lib3,false;book2,lib4,true', 'book1=1,book2=1', '本が2種類以上、貸出中の書籍が含まれる場合'",
                "'book1,lib1,true;book1,lib2,false;book2,lib3,true;book2,lib4,false', 'book1=1,book2=1', '本が2種類以上、1冊貸出中、1冊貸出中ではない場合'"
        })
        public void calculateStock_正常系(String bookDataStr, String expectedResultStr, String description) {
            // テスト実施準備
            List<BookData> bookList = parseBookDataString(bookDataStr);
            Map<String, Long> expectedResult = parseExpectedResultString(expectedResultStr);

            // メソッドの呼び出し
            Map<String, Long> result = ReflectionTestUtils.invokeMethod(webCustomerMenuService, "calculateStock",
                    bookList);

            // 検証
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
    @Order(4)
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
    @Order(5)
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