package jp.co.benesse.web.form;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import jp.co.benesse.web.BaseTest;
import jp.co.benesse.web.validationGroups.ValidationGroups.FormatCheck;
import jp.co.benesse.web.validationGroups.ValidationGroups.LengthCheck;
import jp.co.benesse.web.validationGroups.ValidationGroups.RequiredCheck;

/**
 * <pre>
 * WebCustomerLoginFormのテストクラス
 *
 * 作成日：2025/03/14
 * 更新日：2025/03/14
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
class WebCustomerLoginFormTest extends BaseTest {

    /** スマートバリデーター */
    @Autowired
    private SmartValidator validator;

    /** フォーム */
    private WebCustomerLoginForm form;

    /** フォームのセットアップ */
    @BeforeEach
    void setUp() {
        form = new WebCustomerLoginForm();
    }

    /**
     * 正常系テスト
     * 
     * <pre>
     * 前提：
     * - customerIDとpasswordが半角英数字で、文字数が8～16文字の場合
     * 
     * 結果：
     * - バリデーションエラーが発生しない
     * </pre>
     */
    @Test
    void webCustomerLoginForm_正常系() {
        // テストデータ
        form.setCustomerID("User1234");
        form.setPassword("Password12345678");

        Errors errors = new BeanPropertyBindingResult(form, "form");

        // バリデーション実行
        validator.validate(form, errors);

        // 検証
        assertThat(errors.hasErrors()).isFalse();

        // 検証（getter）
        assertThat(form.getCustomerID()).isEqualTo("User1234");
        assertThat(form.getPassword()).isEqualTo("Password12345678");

    }

    /**
     * <pre>
     * 異常系テスト
     * 
     * 前提：
     * - customerIDまたはpasswordが以下の条件を満たさない場合
     * customerID
     *   - ケース1: null
     *   - ケース2: 空文字
     *   - ケース3: 短すぎる（8文字未満）
     *   - ケース4: 長すぎる（16文字を超える）
     *   - ケース5: 特殊文字が含まれる（@）
     *   - ケース6: 全角文字が含まれる
     *   - ケース7: かな・漢字が含まれる
     *  password
     *   - ケース8: null
     *   - ケース9: 空文字
     *   - ケース10: 短すぎる（8文字未満）
     *   - ケース11: 長すぎる（16文字を超える）
     *   - ケース12: 特殊文字が含まれる（@）
     *   - ケース13: 全角文字が含まれる
     *   - ケース14: かな・漢字が含まれる
     * 
     * 結果：
     * - バリデーションエラーが発生する
     * </pre>
     * 
     * @param customerID テスト対象の利用者ID
     * @param password テスト対象のパスワード
     * @param description テストケースの説明
     */
    @ParameterizedTest(name = "【異常系】{2}")
    @MethodSource("webCustomerLoginForm_異常系_パラメータ")
    void webCustomerLoginForm_異常系(String customerID, String password, String description) {
        form.setCustomerID(customerID);
        form.setPassword(password);

        Errors errors = new BeanPropertyBindingResult(form, "form");

        // バリデーション実行（グループを指定）
        validator.validate(form, errors, RequiredCheck.class, LengthCheck.class, FormatCheck.class);

        // 検証
        assertThat(errors.hasErrors()).isTrue();

        // 検証（getter）
        assertThat(form.getCustomerID()).isEqualTo(customerID);
        assertThat(form.getPassword()).isEqualTo(password);

    }

    /**
     * テストメソッドのパラメータ準備
     * 
     * @return Stream<Arguments>
     */
    static Stream<Arguments> webCustomerLoginForm_異常系_パラメータ() {
        String[] testCase = {
                "ケース1: customerIDがnull",
                "ケース2: customerIDが空文字",
                "ケース3: customerIDが短すぎる（8文字未満）",
                "ケース4: customerIDが長すぎる（16文字を超える）",
                "ケース5: customerIDに特殊文字が含まれる（@）",
                "ケース6: customerIDに全角文字が含まれる",
                "ケース7: customerIDにかな・漢字が含まれる",
                "ケース8: passwordがnull",
                "ケース9: passwordが空文字",
                "ケース10: passwordが短すぎる（8文字未満）",
                "ケース11: passwordが長すぎる（16文字を超える）",
                "ケース12: passwordに特殊文字が含まれる（@）",
                "ケース13: passwordに全角文字が含まれる",
                "ケース14: passwordにかな・漢字が含まれる"
        };

        // テストデータの準備
        String customerID = "User1234";
        String password = "Password123";

        String customerID1 = null;
        String customerID2 = "";
        String customerID3 = "User123";
        String customerID4 = "User1234567890123";
        String customerID5 = "User@1234";
        String customerID6 = "Ｕｓｅｒ１２３４５";
        String customerID7 = "あいうえおかき漢字";

        String password1 = null;
        String password2 = "";
        String password3 = "Pass123";
        String password4 = "Password123456789";
        String password5 = "Pass@word123";
        String password6 = "Ｐａｓｓｗｏｒｄ１２３";
        String password7 = "あいうえおかき漢字";

        return Stream.of(
                Arguments.of(customerID1, password, testCase[0]),
                Arguments.of(customerID2, password, testCase[1]),
                Arguments.of(customerID3, password, testCase[2]),
                Arguments.of(customerID4, password, testCase[3]),
                Arguments.of(customerID5, password, testCase[4]),
                Arguments.of(customerID6, password, testCase[5]),
                Arguments.of(customerID7, password, testCase[6]),
                Arguments.of(customerID, password1, testCase[7]),
                Arguments.of(customerID, password2, testCase[8]),
                Arguments.of(customerID, password3, testCase[9]),
                Arguments.of(customerID, password4, testCase[10]),
                Arguments.of(customerID, password5, testCase[11]),
                Arguments.of(customerID, password6, testCase[12]),
                Arguments.of(customerID, password7, testCase[13]));
    }
}