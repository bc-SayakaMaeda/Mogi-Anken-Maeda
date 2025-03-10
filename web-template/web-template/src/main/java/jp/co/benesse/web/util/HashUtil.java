package jp.co.benesse.web.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <pre>
 * ハッシュ化に関するユーティリティ
 *
 * 作成日：2025/01/07
 * 更新日：2025/03/10
 * </pre>
 *
 * @author bc)maeda
 * @version 1.0
 */
public class HashUtil {

    /**
     * 入力されたパスワードをSHA-256でハッシュ化する
     *
     * @param password ハッシュ化するパスワード
     * @return ハッシュ化されたパスワード（16進数の文字列）、ハッシュ化に失敗した場合はnull
     */
    public static String sha256(String password) {

        if (password == null) {
            String errorMessage = MessageUtil.getMessage("XXXXX-001");
            LogUtil.logger.info(errorMessage);
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            // ハッシュ値を16進数の文字列に変換
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                // 各バイトを符号なし整数として扱い、16進数の文字列に変換
                String hex = Integer.toHexString(0xff & b);

                // 1桁の場合先頭に0を追加して2桁にする
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // 16進数の文字列を返す
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            String errorMessage = MessageUtil.getMessage("error.hash.password");
            LogUtil.logger.info(errorMessage, e);

            return null;
        }
    }
}