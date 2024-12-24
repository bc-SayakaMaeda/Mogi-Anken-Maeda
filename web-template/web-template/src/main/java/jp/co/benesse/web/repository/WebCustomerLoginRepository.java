package jp.co.benesse.web.repository;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.benesse.web.entity.WebCustomerEntity;
import jp.co.benesse.web.exception.WebUnexpectedException;

/**
 * <pre>
 * WebCustomerLoginリポジトリ
 *
 * 作成日：2024/12/24
 * 更新日：2024/12/24
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
@EnableAutoConfiguration
@Repository
public class WebCustomerLoginRepository extends SqlGeneratorBaseRepository {

    /**
     * ログイン判定情報を取得するメソッド
     * 
     * @param customerID 利用者ID
     * @param hashedPassword ハッシュ化されたパスワード
     * @return WebCustomer ログイン判定情報
     * @throws WebUnexpectedException
     */
    public WebCustomerEntity getLoginInfo(String customerID, String hashedPassword) throws WebUnexpectedException {

        // SQLに渡すパラメータを設定
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("customerID", customerID)
                .addValue("hashedPassword", hashedPassword);

        // 動的なSQLの作成
        String sql = getSql(params);

        // クエリを実行して結果を取得
        return kgwebjt.queryForObject(sql, params, (rs, rowNum) -> {
            WebCustomerEntity webCustomer = new WebCustomerEntity();
            webCustomer.setCustomerId(rs.getString("customerID"));
            webCustomer.setCustomerName(rs.getString("customerName"));
            webCustomer.setPostCode(rs.getString("postCode"));
            webCustomer.setAddress(rs.getString("address"));
            webCustomer.setEmail(rs.getString("email"));
            return webCustomer;
        });
    }
}