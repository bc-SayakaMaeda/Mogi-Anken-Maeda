package jp.co.benesse.web.constants;

/**
 * <pre>
 * エラーメッセージ定義クラス
 *
 * 作成日：2025/01/07
 * 更新日：2025/01/07
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
public class ErrorMessages {
    
    /**
     * コンストラクタ
     */
    private ErrorMessages() {
    }
    
    /** 必須チェック */
    public static final String REQUIRED = "入力必須項目です。";

    /** 文字列長チェック */
    public static final String LOGIN_LENGTH = "IDまたはパスワードの入力が適切ではありません。";

    /** フォーマットチェック */
    public static final String LOGIN_FORMAT = "IDまたはパスワードの入力が適切ではありません。";
    
    /** 認証失敗 */
    public static final String INVALID_CREDENTIALS = "IDまたはパスワードが間違っています。";
}