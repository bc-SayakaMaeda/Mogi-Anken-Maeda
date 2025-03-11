package jp.co.benesse.web.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import jp.co.benesse.web.BaseTest;
import jp.co.benesse.web.CsvDataSetLoader;
import jp.co.benesse.web.dto.BookDataDTO;
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
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
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
    @DatabaseSetup(value = "classpath:repository/WebCustomerMenuRepositoryTest/input/")
    public void findAllBooks_正常系() throws WebUnexpectedException {

        // 期待値の設定
        BookDataDTO expected = new BookDataDTO();
        expected.setBookID("1");
        expected.setTitle("Book Title 1");
        expected.setAuthor("Author 1");
        expected.setLoanFlg(false);

        // メソッドの呼び出し
        List<BookData> result = webCustomerMenuRepository.findAllBooks();

        // 検証
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(samePropertyValuesAs(expected)));
    }
}
