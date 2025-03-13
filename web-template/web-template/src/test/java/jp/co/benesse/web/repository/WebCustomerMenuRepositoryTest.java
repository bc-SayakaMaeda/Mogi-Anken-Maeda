package jp.co.benesse.web.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.operation.MicrosoftSqlDatabaseOperationLookup;

import jp.co.benesse.web.BaseTest;
import jp.co.benesse.web.CsvDataSetLoader;
import jp.co.benesse.web.entity.BookData;
import jp.co.benesse.web.exception.WebUnexpectedException;

/**
 * <pre>
 * WebCustomerMenuRepositoryのテストクラス
 *
 * 作成日：2025/03/11
 * 更新日：2025/03/11
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
@Transactional
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class, databaseOperationLookup = MicrosoftSqlDatabaseOperationLookup.class)
public class WebCustomerMenuRepositoryTest extends BaseTest {

    /** テスト対象 */
    @Autowired
    private WebCustomerMenuRepository webCustomerMenuRepository;

    /**
     * 正常系テスト
     * 
     * <pre>
     * 前提：
     * - 不足なく図書情報がDBに登録されている場合
     * 
     * 結果：
     * - 正しい図書情報が取得される
     * </pre>
     * 
     * @throws WebUnexpectedException
     */
    @Test
    @DatabaseSetup(value = "classpath:repository/WebCustomerMenuRepositoryTest/findAllBooks/input/")
    public void findAllBooks_正常系() throws WebUnexpectedException {

        // 期待値の設定
        BookData expected1 = new BookData();
        expected1.setBookID("1");
        expected1.setLibraryBookID("1");
        expected1.setTitle("タイトル1");
        expected1.setAuthor("著者1");
        expected1.setLoanFlg(true);

        BookData expected2 = new BookData();
        expected2.setBookID("2");
        expected2.setLibraryBookID("2");
        expected2.setTitle("タイトル2");
        expected2.setAuthor("著者2");
        expected2.setLoanFlg(true);

        // メソッドの呼び出し
        List<BookData> result = webCustomerMenuRepository.findAllBooks();

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
     * - 不足なく図書情報がDBに登録されている場合
     * 
     * 結果：
     * - 正しい図書情報が取得される
     * </pre>
     * 
     * @throws WebUnexpectedException
     */
    @Test
    @DatabaseSetup(value = "classpath:repository/WebCustomerMenuRepositoryTest/findSelectBooks/input/")
    public void findSelectBooks_正常系() throws WebUnexpectedException {

        // 期待値の設定
        BookData expected = new BookData();
        expected.setBookID("1");
        expected.setLibraryBookID("1");
        expected.setTitle("タイトル1");
        expected.setAuthor("著者1");
        expected.setLoanFlg(true);

        // 貸出希望書籍IDリストの設定
        List<String> bookIds = new ArrayList<>();
        bookIds.add("1");

        // メソッドの呼び出し
        List<BookData> result = webCustomerMenuRepository.findSelectBooks(bookIds);

        // 検証
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(samePropertyValuesAs(expected)));
    }
}
