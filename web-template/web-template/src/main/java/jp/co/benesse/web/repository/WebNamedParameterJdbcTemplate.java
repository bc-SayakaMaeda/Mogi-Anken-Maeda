package jp.co.benesse.web.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.lang.Nullable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * WEB用にカスタマイズしたNamedParameterJdbcTemplate
 * 参照系のメソッド（query...）にリトライ処理を追記している。更新系はリトライしない
 *
 * 作成日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Component
public class WebNamedParameterJdbcTemplate {

    /** SQLサーバーへのアクセス用テンプレート */
    @Autowired
    private NamedParameterJdbcTemplate jt;

    /** リトライ回数取得キー */
    private static final String RETRY_COUNT = "#{${db.retry.count}}";

    /** リトライ間隔取得キー */
    private static final String RETRY_INTERVAL = "#{${db.retry.interval}}";

    /** バッチ処理数 */
    @Value("${db.batch.execute.num}")
    private Integer batchExecuteNum;

    /** DBエラーコード */
    private static final String DB_ERROR = "XXXXX-002";

    /**
     * @param <T>
     * @param sql
     * @param paramSource
     * @param action
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    public <T> T execute(String sql, SqlParameterSource paramSource, PreparedStatementCallback<T> action)
            throws WebUnexpectedException {
        T t = null;
        try {
            t = jt.execute(sql, paramSource, action);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramMap
     * @param action
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    public <T> T execute(String sql, Map<String, ?> paramMap, PreparedStatementCallback<T> action)
            throws WebUnexpectedException {
        T t = null;
        try {
            t = jt.execute(sql, paramMap, action);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param action
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    public <T> T execute(String sql, PreparedStatementCallback<T> action) throws WebUnexpectedException {
        T t = null;
        try {
            t = jt.execute(sql, action);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramSource
     * @param rse
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> T query(String sql, SqlParameterSource paramSource, ResultSetExtractor<T> rse)
            throws WebUnexpectedException {
        T t = null;
        try {
            t = jt.query(sql, paramSource, rse);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramMap
     * @param rse
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> T query(String sql, Map<String, ?> paramMap, ResultSetExtractor<T> rse)
            throws WebUnexpectedException {
        T t = null;
        try {
            t = jt.query(sql, paramMap, rse);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param rse
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> T query(String sql, ResultSetExtractor<T> rse) throws WebUnexpectedException {
        T t = null;
        try {
            t = jt.query(sql, rse);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param paramSource
     * @param rch
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public void query(String sql, SqlParameterSource paramSource, RowCallbackHandler rch)
            throws WebUnexpectedException {
        try {
            jt.query(sql, paramSource, rch);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
    }

    /**
     * @param sql
     * @param paramMap
     * @param rch
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public void query(String sql, Map<String, ?> paramMap, RowCallbackHandler rch)
            throws WebUnexpectedException {
        try {
            jt.query(sql, paramMap, rch);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
    }

    /**
     * @param sql
     * @param rch
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public void query(String sql, RowCallbackHandler rch) throws WebUnexpectedException {
        try {
            jt.query(sql, rch);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
    }

    /**
     * @param <T>
     * @param sql
     * @param paramSource
     * @param rowMapper
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper)
            throws WebUnexpectedException {
        List<T> t = null;
        try {
            t = jt.query(sql, paramSource, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            t = Collections.emptyList();
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramMap
     * @param rowMapper
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
            throws WebUnexpectedException {
        List<T> t = null;
        try {
            t = jt.query(sql, paramMap, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            t = Collections.emptyList();
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param rowMapper
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws WebUnexpectedException {
        List<T> t = null;
        try {
            t = jt.query(sql, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            t = Collections.emptyList();
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramSource
     * @param rowMapper
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> Stream<T> queryForStream(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper)
            throws WebUnexpectedException {
        Stream<T> t = null;
        try {
            t = jt.queryForStream(sql, paramSource, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            t = Stream.empty();
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramMap
     * @param rowMapper
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> Stream<T> queryForStream(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
            throws WebUnexpectedException {
        Stream<T> t = null;
        try {
            t = jt.queryForStream(sql, paramMap, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            t = Stream.empty();
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramSource
     * @param rowMapper
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> T queryForObject(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper)
            throws WebUnexpectedException {
        T t = null;
        try {
            t = jt.queryForObject(sql, paramSource, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramMap
     * @param rowMapper
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> T queryForObject(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
            throws WebUnexpectedException {
        T t = null;
        try {
            t = jt.queryForObject(sql, paramMap, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramSource
     * @param requiredType
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> T queryForObject(String sql, SqlParameterSource paramSource, Class<T> requiredType)
            throws WebUnexpectedException {
        T t = null;
        try {
            t = jt.queryForObject(sql, paramSource, requiredType);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramMap
     * @param requiredType
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> requiredType)
            throws WebUnexpectedException {
        T t = null;
        try {
            t = jt.queryForObject(sql, paramMap, requiredType);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param paramSource
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public Map<String, Object> queryForMap(String sql, SqlParameterSource paramSource) throws WebUnexpectedException {
        Map<String, Object> t = null;
        try {
            t = jt.queryForMap(sql, paramSource);
        } catch (EmptyResultDataAccessException e) {
            t = Collections.emptyMap();
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param paramMap
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap) throws WebUnexpectedException {
        Map<String, Object> t = null;
        try {
            t = jt.queryForMap(sql, paramMap);
        } catch (EmptyResultDataAccessException e) {
            t = Collections.emptyMap();
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramSource
     * @param elementType
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> List<T> queryForList(String sql, SqlParameterSource paramSource, Class<T> elementType)
            throws WebUnexpectedException {
        List<T> t = null;
        try {
            t = jt.queryForList(sql, paramSource, elementType);
        } catch (EmptyResultDataAccessException e) {
            t = Collections.emptyList();
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param <T>
     * @param sql
     * @param paramMap
     * @param elementType
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public <T> List<T> queryForList(String sql, Map<String, ?> paramMap, Class<T> elementType)
            throws WebUnexpectedException {
        List<T> t = null;
        try {
            t = jt.queryForList(sql, paramMap, elementType);
        } catch (EmptyResultDataAccessException e) {
            t = Collections.emptyList();
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param paramSource
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public List<Map<String, Object>> queryForList(String sql, SqlParameterSource paramSource)
            throws WebUnexpectedException {
        List<Map<String, Object>> t = null;
        try {
            t = jt.queryForList(sql, paramSource);
        } catch (EmptyResultDataAccessException e) {
            t = Collections.emptyList();
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param paramMap
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public List<Map<String, Object>> queryForList(String sql, Map<String, ?> paramMap)
            throws WebUnexpectedException {
        List<Map<String, Object>> t = null;
        try {
            t = jt.queryForList(sql, paramMap);
        } catch (EmptyResultDataAccessException e) {
            t = Collections.emptyList();
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param paramSource
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public SqlRowSet queryForRowSet(String sql, SqlParameterSource paramSource) throws WebUnexpectedException {
        SqlRowSet t = null;
        try {
            t = jt.queryForRowSet(sql, paramSource);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param paramMap
     * @return 取得結果
     * @throws WebUnexpectedException
     */
    @Retryable(retryFor = WebUnexpectedException.class, maxAttemptsExpression = RETRY_COUNT, backoff = @Backoff(delayExpression = RETRY_INTERVAL))
    public SqlRowSet queryForRowSet(String sql, Map<String, ?> paramMap) throws WebUnexpectedException {
        SqlRowSet t = null;
        try {
            t = jt.queryForRowSet(sql, paramMap);
        } catch (EmptyResultDataAccessException e) {
            // 何もせずnull返却
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param paramSource
     * @return 件数
     * @throws WebUnexpectedException
     */
    public int update(String sql, SqlParameterSource paramSource) throws WebUnexpectedException {
        int t = 0;
        try {
            t = jt.update(sql, paramSource);
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param paramMap
     * @return 件数
     * @throws WebUnexpectedException
     */
    public int update(String sql, Map<String, ?> paramMap) throws WebUnexpectedException {
        int t = 0;
        try {
            t = jt.update(sql, paramMap);
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param paramSource
     * @param generatedKeyHolder
     * @return 件数
     * @throws WebUnexpectedException
     */
    public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder)
            throws WebUnexpectedException {
        int t = 0;
        try {
            t = jt.update(sql, paramSource, generatedKeyHolder);
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param paramSource
     * @param generatedKeyHolder
     * @param keyColumnNames
     * @return 件数
     * @throws WebUnexpectedException
     */
    public int update(
            String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder, @Nullable String[] keyColumnNames)
            throws WebUnexpectedException {
        int t = 0;
        try {
            t = jt.update(sql, paramSource, generatedKeyHolder, keyColumnNames);
        } catch (DataAccessException e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

    /**
     * @param sql
     * @param batchArgs
     * @return 件数
     * @throws WebUnexpectedException
     */
    public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) throws WebUnexpectedException {
        int[] t;
        try {
            if (batchArgs.length <= batchExecuteNum.intValue()) {
                t = jt.batchUpdate(sql, batchArgs);
            } else {
                int count = 0;
                t = new int[batchArgs.length];
                List<SqlParameterSource> batchExecuteNumArgs = new ArrayList<>();
                for (int index = 0; index < batchArgs.length; index++) {
                    batchExecuteNumArgs.add(batchArgs[index]);
                    if (batchExecuteNumArgs.size() == batchExecuteNum.intValue()
                            || index == batchArgs.length - 1) {
                        int[] temp = jt.batchUpdate(sql, batchExecuteNumArgs.toArray(new SqlParameterSource[] {}));
                        System.arraycopy(temp, 0, t, count, temp.length);
                        count += temp.length;
                        temp = null;
                        batchExecuteNumArgs.clear();
                    }
                }
            }
        } catch (Exception e) {
            throw new WebUnexpectedException(MessageUtil.getMessage(DB_ERROR), e);
        }
        return t;
    }

}
