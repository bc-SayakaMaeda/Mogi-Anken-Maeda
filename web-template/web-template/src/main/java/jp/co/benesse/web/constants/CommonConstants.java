package jp.co.benesse.web.constants;

/**
 * <pre>
 * 共通定数クラス
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
public class CommonConstants {

    /**
     * コンストラクタ
     */
    private CommonConstants() {
    }

    /** 機能ID */
    public static final String FUNC_ID = "funcId";

    /** エラーコード共通部 */
    public static final String ERROR_CODE_REPLACE_PART = "XXXXX";

    /** エラーコードとエラーメッセージの区切り文字 */
    public static final String ERROR_SPLIT = "::";

    /** 日付フォーマット（年月日） */
    public static final String FORMAT_YMD = "uuuu/MM/dd";

    /** 日付フォーマット（年月日時分秒） */
    public static final String FORMAT_YMDHMS = "uuuu/MM/dd HH:mm:ss";

    /** 日付フォーマット（年月日時分秒+ミリ秒7桁） */
    public static final String FORMAT_YMDHMS_7S = "uuuu/MM/dd HH:mm:ss.SSSSSSS";

    /** 日付フォーマット（年月日時分秒区切りなし） */
    public static final String FORMAT_YMDHMS_NOBREADK = "uuuuMMddHHmmss";

    /** 日付フォーマット(年月日_日本) */
    public static final String DTF_FORMAT_YMD_JC = "uuuu年M月d日";

}
