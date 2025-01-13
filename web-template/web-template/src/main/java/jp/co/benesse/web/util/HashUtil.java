package jp.co.benesse.web.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <pre>
 * ハッシュ化に関するユーティリティ
 *
 * 作成日：2025/01/07
 * 更新日：2025/01/14
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
public class HashUtil {

    /**
     * <pre>
     * 入力されたパスワードをSHA-256でハッシュ化する
     * </pre>
     *
     * @param password ハッシュ化するパスワード
     * @return ハッシュ化されたパスワード（16進数の文字列）
     * @throws RuntimeException
     */
    public static String sha256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {

                // 各バイトを符号なし整数として扱い、StringBuilderで16進数の文字列に変換する
                String hex = Integer.toHexString(0xff & b);

                // 1桁の場合先頭に0を足して2桁にする
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            // 16進数の文字列を返す
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}