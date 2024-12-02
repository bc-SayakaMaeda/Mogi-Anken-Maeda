package jp.co.benesse.web.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 日付に関するユーティリティクラス.
 * 
 * <pre>
 * 作成日 : 2024/06/16
 * 更新日 : 2024/06/16
 * </pre>
 * 
 * @author BC)yoda
 * @version 1.0
 */
public class DateUtil {

    /**
     * コンストラクタ
     */
    private DateUtil() {
    }

    /**
     * システム日付を取得
     * 
     * @return システム日付
     */
    public static LocalDateTime getNowDate() {
        return LocalDateTime.now();
    }

    /**
     * LocalDateTime型からDate型に変換
     * 
     * @param localDateTime 変換前
     * @return 変換後
     */
    public static Date toDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zone);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    /**
     * 第一引数のほうが未来または同じの場合はtrueを返す
     * 
     * @param date1 比較対象の日付
     * @param date2 比較される側の日付
     * @return 第一引数のほうが未来または同じの場合はtrue、そうでない場合はfalse
     */
    public static boolean isEqualAfter(LocalDateTime date1, LocalDateTime date2) {
        return date1.isAfter(date2) || date1.isEqual(date2);
    }

    /**
     * 第一引数のほうが過去または同じの場合はTrueを返す
     * 
     * @param date1 比較対象の日付
     * @param date2 比較される側の日付
     * @return 第一引数のほうが過去または同じの場合はtrue、そうでない場合はfalse
     */
    public static boolean isEqualBefore(LocalDateTime date1, LocalDateTime date2) {
        return date1.isBefore(date2) || date1.isEqual(date2);
    }

    /**
     * 第一引数のほうが過去の場合はTrueを返す
     * 
     * @param date1 比較対象の日付
     * @param date2 比較される側の日付
     * @return 第一引数のほうが過去場合はtrue、そうでない場合はfalse
     */
    public static boolean isBefore(LocalDateTime date1, LocalDateTime date2) {
        return date1.isBefore(date2);
    }

    /**
     * 第一引数のほうが未来の場合はTrueを返す
     * 
     * @param date1 比較対象の日付
     * @param date2 比較される側の日付
     * @return 第一引数のほうが未来の場合はtrue、そうでない場合はfalse
     */
    public static boolean isAfter(LocalDateTime date1, LocalDateTime date2) {
        return date1.isAfter(date2);
    }

    /**
     * 第一引数のほうが同じの場合はTrueを返す
     * 
     * @param date1 比較対象の日付
     * @param date2 比較される側の日付
     * @return 第一引数が第二引数と同じの場合はtrue、そうでない場合はfalse
     */
    public static boolean isEqual(LocalDateTime date1, LocalDateTime date2) {
        return date1.isEqual(date2);
    }

    /**
     * 引数の日付からNカ月後の月末の値を返却する マイナスの値が与えられた場合は、Nカ月前の月末とする
     * 
     * @param date 対象日付
     * @param month Nカ月
     * @return 対象日付のNカ月後の月末
     */
    public static LocalDateTime getEndOfMonthAfterMonths(LocalDateTime date, int month) {
        LocalDateTime fixDate = date.plusMonths(month);
        return fixDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 引数の日付が指定した2つの月の範囲内にあるかを確かめる
     * 
     * @param date 対象日付
     * @param month1 月の下限値
     * @param month2 月の上限値
     * @return trueの場合: 対象日付は2つの月の範囲内に存在 / falseの場合: 対象日付は範囲外に存在
     */
    public static boolean isBetweenMonth(LocalDateTime date, Month month1, Month month2) {
        int dateMonth = date.getMonthValue();
        return month1.getValue() <= dateMonth && dateMonth <= month2.getValue();
    }

    /**
     * 引数の日付が指定した月以降であるかを確かめる
     * 
     * @param date 対象日付
     * @param month 対象月
     * @return trueの場合: 対象日付は対象月以降である / falseの場合: 対象日付は対象月よりも前である
     */
    public static boolean isEqualAfterMonth(LocalDateTime date, Month month) {
        int dateMonth = date.getMonthValue();
        return dateMonth >= month.getValue();
    }

    /**
     * 引数の日付とオフセット値から年度を算出する
     * 
     * @param date 対象日付
     * @param offset 年度算出時のオフセット値
     * @return 年度
     */
    public static int getNendo(LocalDateTime date, int offset) {
        return date.minusMonths(offset).getYear();
    }

    /**
     * LocalDateTime型の日時を文字列型に変換する
     * 
     * @param localDateTime 日時
     * @param format フォーマット
     * @return 変換後文字列
     */
    public static String localDateTimeToStr(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(dateTimeFormatter);
    }
}
