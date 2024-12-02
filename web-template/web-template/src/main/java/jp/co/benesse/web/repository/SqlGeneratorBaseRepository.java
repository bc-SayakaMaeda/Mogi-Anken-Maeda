package jp.co.benesse.web.repository;

import java.util.Objects;

import org.mybatis.scripting.thymeleaf.SqlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.benesse.web.util.FileAccessUtil;

/**
 * <pre>
 * SqlGenerator使用のベース
 *
 * 作成日：2024/06/13
 * 更新日：2024/06/13
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Repository
public class SqlGeneratorBaseRepository {

    /** SQLサーバーへのアクセス用テンプレート */
    @Autowired
    protected WebNamedParameterJdbcTemplate kgwebjt;

    /** SQLジェネレータ */
    @Autowired
    private SqlGenerator sqlGenerator;

    /** SQLファイルフォルダ */
    private static final String SQL_FILE_FOLDER = "sql/";

    /** ファイル区切り文字 (/) */
    private static final String SQL_FILE_SPILIT = "/";

    /** SQLファイル拡張子 */
    private static final String SQL_FILE_EXTENSION = ".sql";

    /**
     * 外部SQLからSQL文字列を生成して返却するメソッド
     * 
     * @param candidateParams パラメータ候補
     * @return SQLファイル名
     */
    protected String getSql(SqlParameterSource candidateParams) {
        // このクラスではなく呼び出し元の情報を使用するので2番目取得
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];

        return createSql(candidateParams, true, getClassName(ste), getMethodName(ste));
    }

    /**
     * 外部SQLからSQL文字列を生成して返却するメソッド メソッド名を引数として受け取る場合に利用する。
     * 
     * @param candidateParams パラメータ候補
     * @param methodName メソッド名
     * @return SQLファイル名
     */
    protected String getSql(SqlParameterSource candidateParams, String methodName) {
        // このクラスではなく呼び出し元の情報を使用するので2番目取得
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];

        return createSql(candidateParams, true, getClassName(ste), methodName);
    }

    /**
     * 外部SQLからSQL文字列を生成して返却するメソッド<br>
     * エンティティから作成する かつ SQL上で展開するかどうかを制御したい場合に利用する。
     * 
     * @param candidateParams パラメータ候補
     * @param doExpose EntityをSQL上で展開するか否か
     * @return 動的に作成されたSQL
     */
    protected String getSql(SqlParameterSource candidateParams, boolean doExpose) {
        // このクラスではなく呼び出し元の情報を使用するので2番目取得
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];

        return createSql(candidateParams, doExpose, getClassName(ste), getMethodName(ste));
    }

    /**
     * SQL文を動的に作成するメソッド
     * 
     * @param candidateParams パラメータ候補
     * @param doExpose EntityをSQL上で展開するか否か
     * @param className クラス名
     * @param methodName メソッド名
     * @return 動的に作成されたSQL
     */
    private String createSql(SqlParameterSource candidateParams, boolean doExpose, String className,
            String methodName) {
        // パラメータを指定しない場合は、そのままSQLを返却
        if (Objects.isNull(candidateParams)) {
            return FileAccessUtil.load(createSqlFileName(className, methodName));
        }

        // 呼び出し側でパラメータを作成している場合は、引数のパラメータをそのまま利用し、動的SQLを生成
        if (candidateParams instanceof MapSqlParameterSource) {
            MapSqlParameterSource selectedParams = (MapSqlParameterSource) candidateParams;
            return this.sqlGenerator.generate(FileAccessUtil.load(createSqlFileName(className, methodName)),
                    selectedParams.getValues(), selectedParams::addValue);
        }

        // Entityから動的SQLを作成する場合は、Entityから動的SQLを生成するためのパラメータをこちらで作成する。
        MapSqlParameterSource exposedParams = new MapSqlParameterSource();
        String[] keys = candidateParams.getParameterNames();
        if (keys != null) {
            for (String key : keys) {
                exposedParams.addValue(key, candidateParams.getValue(key));
            }
        }

        // EntityをMapとして展開したい場合か、したくない場合かで第2引数が変化。
        return this.sqlGenerator.generate(FileAccessUtil.load(createSqlFileName(className, methodName)),
                doExpose ? exposedParams.getValues() : candidateParams, exposedParams::addValue);
    }

    /**
     * スタックトレースからクラス名を取得する。
     * 
     * @param ste スタックトレース
     * @return クラス名
     */
    private String getClassName(StackTraceElement ste) {
        String classFullName = ste.getClassName();
        return classFullName.substring(classFullName.lastIndexOf(".") + 1);
    }

    /**
     * スタックトレースからメソッド名を取得する。
     * 
     * @param ste スタックトレース
     * @return メソッド名
     */
    private String getMethodName(StackTraceElement ste) {
        return ste.getMethodName();
    }

    /**
     * クラス名とメソッド名からSQLファイル名を取得するメソッド
     * 
     * @param className クラス名
     * @param methodName メソッド名
     * @return SQLファイル名
     */
    private String createSqlFileName(String className, String methodName) {

        StringBuilder sb = new StringBuilder();

        sb.append(SQL_FILE_FOLDER)
                .append(className)
                .append(SQL_FILE_SPILIT)
                .append(methodName)
                .append(SQL_FILE_EXTENSION);

        return sb.toString();
    }

}
