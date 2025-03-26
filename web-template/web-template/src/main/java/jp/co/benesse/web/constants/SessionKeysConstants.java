package jp.co.benesse.web.constants;

/**
 * <pre>
 * セッションKEY値一覧
 * 
 * 作成日：2025/02/20
 * 更新日：2025/02/20
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
public class SessionKeysConstants {

    /**
     * コンストラクタ
     */
    private SessionKeysConstants() {
    }

    /** 利用者ID */
    public static final String CUSTOMER_ID = "customerID";

    /** 利用者氏名 */
    public static final String CUSTOMER_NAME = "customerName";

    /** 郵便番号 */
    public static final String POST_CODE = "postCode";

    /** 住所 */
    public static final String ADDRESS = "address";

    /** メールアドレス */
    public static final String EMAIL = "email";

    /** 貸出希望書籍情報 */
    public static final String BOOK_REQUEST = "bookRequestList";
}