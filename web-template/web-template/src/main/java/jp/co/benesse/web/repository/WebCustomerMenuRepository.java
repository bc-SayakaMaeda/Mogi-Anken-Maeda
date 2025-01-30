package jp.co.benesse.web.repository;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.entity.BookData;

/**
 * <pre>
 * メニューリポジトリ
 *
 * 作成日：2025/01/21
 * 更新日：2025/01/21
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
     * @return DBから取得した書籍情報
     */
    public List<BookData> findAllBooks() {
        try {
            // SQLファイルの内容を読み込む
            String sql = new String(Files.readAllBytes(Paths.get(UrlConstants.SQL_FIND_ALL_BOOKS)),
                    StandardCharsets.UTF_8);

            // SQLクエリを実行
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BookData.class));

        } catch (Exception e) {
            throw new RuntimeException("SQLファイルを読み込めませんでした", e);

        }
    }
}