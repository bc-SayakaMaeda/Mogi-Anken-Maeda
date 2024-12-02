package jp.co.benesse.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.benesse.web.exception.WebBaseException;

/**
 * <pre>
 * ログ出力ユーティリティ.
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
public class LogUtil {

    /** Logger. */
    public static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    /** インスタンス化防止用のプライベートコンストラクタ */
    private LogUtil() {
    }

    /**
     * DEBUGログ(exceptionから呼び出し元情報取得。スタックトレースは出さない).
     * 
     * @param message メッセージ
     * @param e エラー情報
     */
    public static void debugDetail(String message, WebBaseException e) {
        logger.debug(message + callerInfo(e));
    }

    /**
     * INFOログ(exceptionから呼び出し元情報取得。スタックトレースは出さない).
     * 
     * @param message メッセージ
     * @param e エラー情報
     */
    public static void infoDetail(String message, WebBaseException e) {
        logger.info(message + callerInfo(e));
    }

    /**
     * WARNログ(exceptionから呼び出し元情報取得。スタックトレースは出さない).
     * 
     * @param message メッセージ
     * @param e エラー情報
     */
    public static void warnDetail(String message, WebBaseException e) {
        logger.warn(message + callerInfo(e));
    }

    /**
     * ERRORログ(exceptionから呼び出し元情報取得。スタックトレースは出さない).
     * 
     * @param message メッセージ
     * @param e エラー情報
     */
    public static void errorDetail(String message, WebBaseException e) {
        logger.error(message + callerInfo(e));
    }

    /**
     * 例外から呼び出し元の情報を取得してログ出力する文字列を生成する
     * 
     * @param e エラー情報
     * @return 呼び出し元情報
     */
    private static String callerInfo(WebBaseException e) {
        StackTraceElement[] ste = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(ste[0].getClassName()).append("[").append(ste[0].getLineNumber()).append("])");
        return sb.toString();
    }
}
