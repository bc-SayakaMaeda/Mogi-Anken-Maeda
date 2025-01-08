package jp.co.benesse.web.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * <pre>
 * Hash化用ユーティリティ.
 *
 * 作成日：2025/01/07
 * 更新日：2025/01/07
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
public class HashUtil {

    /**
    * <pre>
    * SHA-256でハッシュ化する
    * </pre>
    *
    * @param input
    * @return エラーメッセージ
    */
    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}