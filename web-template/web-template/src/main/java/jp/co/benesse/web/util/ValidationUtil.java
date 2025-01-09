package jp.co.benesse.web.util;

import jp.co.benesse.web.constants.CommonConstants;
import jp.co.benesse.web.constants.ErrorMessages;

/**
 * <pre>
 * バリデーション用ユーティリティ.
 *
 * 作成日：2025/01/07
 * 更新日：2025/01/07
 * </pre>
 * 
 * @version 1.0
 */
public class ValidationUtil {

    /**
     * <pre>
     * バリデーションをチェックする
     * </pre>
     *
     * @param customerID 利用者ID
     * @param password パスワード
     * @return エラーメッセージ
     */
    public static String validateCustomerIDAndPassword(String customerID, String password) {
        // 必須チェック
        if (customerID == null || customerID.isEmpty() || password == null || password.isEmpty()) {
            return ErrorMessages.REQUIRED;
        }

        // 文字列長チェック
        if (customerID.length() < 8 || customerID.length() > 16 || password.length() < 8 || password.length() > 16) {
            return ErrorMessages.LOGIN_LENGTH;
        }

        // フォーマットチェック（半角英数字）
        if (!CheckUtil.isRegex(customerID, CommonConstants.HALF_ALPHANUMERIC)
                || !CheckUtil.isRegex(password, CommonConstants.HALF_ALPHANUMERIC)) {
            return ErrorMessages.LOGIN_FORMAT;
        }

        return null;
    }
}