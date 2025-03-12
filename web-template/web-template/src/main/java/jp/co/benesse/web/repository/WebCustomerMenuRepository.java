package jp.co.benesse.web.repository;

import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.benesse.web.entity.BookData;
import jp.co.benesse.web.exception.WebUnexpectedException;

/**
 * <pre>
 * メニューリポジトリ
 *
 * 作成日：2025/01/21
 * 更新日：2025/03/12
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@EnableAutoConfiguration
@Repository
public class WebCustomerMenuRepository extends SqlGeneratorBaseRepository {

    /** RowMapper */
    private final RowMapper<BookData> rowMapper = new BeanPropertyRowMapper<>(BookData.class);

    /**
     * 図書情報取得
     * 
     * @return BookData 書籍情報
     * @throws WebUnexpectedException
     */
    public List<BookData> findAllBooks() throws WebUnexpectedException {
        // 動的なSQLの作成
        String sql = getSql(null);

        // SQLクエリを実行して結果を取得
        return kgwebjt.query(sql, rowMapper);

    }

    /**
     * 図書情報取得(書籍ID指定)
     * 
     * @param bookIds 貸出希望書籍IDリスト
     * @return BookData 書籍情報リスト
     * @throws WebUnexpectedException
     */
    public List<BookData> findSelectBooks(List<String> bookIds) throws WebUnexpectedException {

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("bookIds", bookIds);

        // 動的なSQLの作成
        String sql = getSql(parameters);

        // SQLクエリを実行して結果を取得
        return kgwebjt.query(sql, parameters, rowMapper);

    }
}