package jp.co.benesse.web.enums;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

/**
 * サンプル区分
 *
 * <pre>
 * 作成日：2024/10/28
 * 更新日：2024/10/28
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Getter
public enum SampleKbn {

    /** サンプル1 */
    SAMPLE_1("1", "サンプル1"),

    /** サンプル2 */
    SAMPLE_2("2", "サンプル2");

    /** 区分 */
    private String kbn;

    /** 論理名 */
    private String name;

    /**
     * コンストラクタ
     * 
     * @param kbn 区分
     * @param name 論理名
     */
    SampleKbn(String kbn, String name) {
        this.kbn = kbn;
        this.name = name;
    }

    /**
     * 対象の区分を返す処理。ない場合はnullを返す。
     * 
     * @param kbn 区分
     * @return 対象Enum / ない場合はnull
     */
    public static SampleKbn getEnumByKbn(String kbn) {
        for (SampleKbn sampleKbn : values()) {
            if (sampleKbn.getKbn().equals(kbn)) {
                return sampleKbn;
            }
        }
        return null;

    }

    /**
     * kbnとnameの組み合わせをMap形式で取得する
     * 
     * @return enumのMap
     */
    public static Map<String, String> getSampleKbnMap() {

        Map<String, String> resultMap = new LinkedHashMap<>();
        for (SampleKbn elem : SampleKbn.values()) {
            resultMap.put(elem.getKbn(), elem.getName());
        }
        return resultMap;
    }
}
