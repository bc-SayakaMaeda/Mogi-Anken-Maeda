package jp.co.benesse.web.exception;

import jp.co.benesse.web.constants.CommonConstants;
import lombok.Getter;

/**
 * <pre>
 * WEB_ベース例外
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Getter
public abstract class WebBaseException extends Exception {

    /** エラーコード */
    private final String errorCode;

    /** エラーメッセージ */
    private final String errorMessage;

    /** エラーコード＋エラーメッセージ */
    private final String errorCodeMessage;

    /**
     * コンストラクタ
     * 
     * @param errorMessage エラーメッセージ
     */
    public WebBaseException(String errorMessage) {
        super(errorMessage);

        String[] errorInfo = errorMessage.split(CommonConstants.ERROR_SPLIT);
        this.errorCode = errorInfo[0];
        if (errorInfo.length == 2) {
            this.errorMessage = errorInfo[1];
        } else {
            this.errorMessage = errorMessage;
        }
        this.errorCodeMessage = errorMessage;
    }

    /**
     * コンストラクタ
     * 
     * @param errorMessage エラーメッセージ
     * @param e 例外
     */
    public WebBaseException(String errorMessage, Throwable e) {
        super(errorMessage, e);

        String[] errorInfo = errorMessage.split(CommonConstants.ERROR_SPLIT);
        this.errorCode = errorInfo[0];
        if (errorInfo.length == 2) {
            this.errorMessage = errorInfo[1];
        } else {
            this.errorMessage = errorMessage;
        }
        this.errorCodeMessage = errorMessage;
    }
}
