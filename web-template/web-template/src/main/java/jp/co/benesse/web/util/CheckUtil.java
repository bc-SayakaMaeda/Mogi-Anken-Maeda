package jp.co.benesse.web.util;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Objects;

/**
 * <pre>
 * チェックに関するユーティリティクラス.
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
public class CheckUtil {

    /**
     * コンストラクタ
     */
    private CheckUtil() {
    }

    /**
     * 正規表現チェック(nullはtrue返却)
     * 
     * @param target 対象文字列
     * @param regex 正規表現
     * @return チェック結果
     */
    public static boolean isRegex(String target, String regex) {
        // targetがnullの場合はtrueを返却する
        if (Objects.isNull(target)) {
            return true;
        }
        if (target.matches(regex)) {
            return true;
        }
        return false;
    }

    /**
     * 日付チェック(nullはtrue返却)
     * 
     * @param date 日付
     * @param format フォーマット(【年月日】か【年月日時分秒】に対応)
     * @return チェック結果
     */
    public static boolean isDate(String date, String format) {
        // dateがnullの場合はtrueを返却する
        if (Objects.isNull(date)) {
            return true;
        }
        // フォーマットと、日付存在チェックを行う
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format).withResolverStyle(ResolverStyle.STRICT);
        try {
            dtf.parse(date);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
