package jp.co.benesse.web.exception;

/**
 * <pre>
 * WEB_パラメータエラー用Exception
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
public class WebParamException extends WebBaseException {

    /**
     * コンストラクタ
     * 
     * @param errorMessage エラーメッセージ
     */
    public WebParamException(String errorMessage) {
        super(errorMessage);

    }
}
