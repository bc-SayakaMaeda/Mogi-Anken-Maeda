package jp.co.benesse.web.exception;

/**
 * <pre>
 * WEB_予期しない例外
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
public class WebUnexpectedException extends WebBaseException {

    /**
     * コンストラクタ
     * 
     * @param errorMessage エラーメッセージ
     */
    public WebUnexpectedException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * コンストラクタ
     * 
     * @param errorMessage エラーメッセージ
     * @param e 例外
     */
    public WebUnexpectedException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}
