package jp.co.benesse.web.exception;

/**
 * <pre>
 * WEB_画面ハンドリングが必要エラー用Exception
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
public class WebViewHandlingException extends WebBaseException {

    /**
     * コンストラクタ
     * 
     * @param errorMessage エラーメッセージ
     */
    public WebViewHandlingException(String errorMessage) {
        super(errorMessage);
    }
}
