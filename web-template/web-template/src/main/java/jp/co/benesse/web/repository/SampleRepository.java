package jp.co.benesse.web.repository;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.benesse.web.entity.SampleEntity;
import jp.co.benesse.web.exception.WebUnexpectedException;

/**
 * <pre>
 * サンプルリポジトリ
 *
 * 作成日：2024/10/29
 * 更新日：2024/10/29
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@EnableAutoConfiguration
@Repository
public class SampleRepository extends SqlGeneratorBaseRepository {

    /**
     * サンプルテーブル取得
     * 
     * @param memId 会員ID
     * @return オプト
     * @throws WebUnexpectedException
     */
    public String selectSampleOpt(Integer memId) throws WebUnexpectedException {

        // SQLに渡すパラメータを設定
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memId", memId);
        // 動的なSQLの作成
        String sql = getSql(params);
        return kgwebjt.queryForObject(sql, params, String.class);
    }

    /**
     * サンプルテーブル更新
     * 
     * @param entity エンティティ
     * @return 処理件数
     * @throws WebUnexpectedException
     */
    public int updateSampleOpt(SampleEntity entity) throws WebUnexpectedException {

        // パラメータの準備
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        // 動的なSQLの作成
        String sql = getSql(params);

        return kgwebjt.update(sql, params);
    }
}
