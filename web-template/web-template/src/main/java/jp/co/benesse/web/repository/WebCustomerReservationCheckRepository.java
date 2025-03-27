package jp.co.benesse.web.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import jp.co.benesse.web.entity.BookData;
import jp.co.benesse.web.exception.WebUnexpectedException;

/**
 * <pre>
 * 貸出予約確認画面リポジトリ
 *
 * 作成日：2025/03/27
 * 更新日：
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@EnableAutoConfiguration
@Repository
public class WebCustomerReservationCheckRepository extends SqlGeneratorBaseRepository {

    /** RowMapper */
    private final RowMapper<BookData> rowMapper = new BeanPropertyRowMapper<>(BookData.class);

    /**
     * 貸出状況情報取得
     * 
     * @param bookRequestList
     * @return BookData 書籍情報
     * @throws WebUnexpectedException
     */
    public List<BookData> findLoanStatusInfo(List<BookData> bookRequestList) throws WebUnexpectedException {

        // BookIDのみを抽出
        List<String> bookIdList = bookRequestList.stream()
                .map(BookData::getBookID)
                .distinct()
                .collect(Collectors.toList());

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("bookRequestList", bookRequestList);

        // 動的なSQLの作成
        String sql = getSql(parameters);

        // SQLクエリを実行して結果を取得
        return kgwebjt.query(sql, parameters, rowMapper);

    }

    /**
     * 図書貸出情報登録
     * 
     * @param customerId 顧客ID
     * @return 自動発番された図書貸出ID
     * @throws WebUnexpectedException
     */
    public int regLoanInfo(String customerId) throws WebUnexpectedException {
        // パラメータを設定
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("customerId", customerId);

        // SQL文を生成
        String sql = getSql(params);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        // SQLを実行し、自動発番されたIDを取得
        kgwebjt.update(sql, params, keyHolder);

        // 自動発番されたIDを返却
        return keyHolder.getKey().intValue();

    }

    /**
     * 図書貸出明細登録
     * 
     * @param bookLoanId
     * @param libraryBookId
     * @throws WebUnexpectedException
     */
    public void regLoanDetail(String bookLoanId, String libraryBookId) throws WebUnexpectedException {

        // パラメータを設定
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bookLoanId", bookLoanId);
        params.addValue("libraryBookId", libraryBookId);

        // 動的なSQLの作成
        String sql = getSql(null);

        // SQLを実行
        kgwebjt.update(sql, params);

    }

    /**
     * web利用者履歴情報登録
     * 
     * @param bookRequestList
     * @throws WebUnexpectedException
     */
    public void regWebCustomerHistoryInfo(String bookRequestList) throws WebUnexpectedException {

        // パラメータを設定
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bookRequestList", bookRequestList);

        // 動的なSQLの作成
        String sql = getSql(null);

        // SQLクエリを実行して結果を取得
        kgwebjt.update(sql, params);

    }
}