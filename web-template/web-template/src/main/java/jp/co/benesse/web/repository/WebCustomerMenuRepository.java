package jp.co.benesse.web.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.co.benesse.web.entity.BookData;
import jp.co.benesse.web.exception.WebUnexpectedException;

/**
 * <pre>
 * メニューリポジトリ
 *
 * 作成日：2025/01/21
 * 更新日：2025/02/05
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@EnableAutoConfiguration
@Repository
public class WebCustomerMenuRepository extends SqlGeneratorBaseRepository {

    /** JDBCテンプレート */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 図書情報取得
     * 
     * @return BookData 書籍情報
     * @throws WebUnexpectedException
     */
    public List<BookData> findAllBooks() throws WebUnexpectedException {
        // 動的なSQLの作成
        String sql = getSql(null);

        RowMapper<BookData> rowMapper = new BeanPropertyRowMapper<>(BookData.class);

        // SQLクエリを実行して結果を取得
        return jdbcTemplate.query(sql, rowMapper);

    }
}