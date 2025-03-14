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
    private SmartValidator smartValidator;

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
        form.setPassword("Password123");

        Errors errors = new BeanPropertyBindingResult(form, "form");

        // バリデーション実行
        smartValidator.validate(form, errors);

        // 検証
        assertThat(errors.hasErrors()).isFalse();
    }

    /**
     * <pre>
     * 異常系テスト
     * 
     * 前提：
     * - customerIDまたはpasswordが以下の条件を満たさない場合
     *   - ケース1: customerIDがnull
     *   - ケース2: passwordがnull
     *   - ケース3: customerIDが空文字
     *   - ケース4: passwordが空文字
     *   - ケース5: customerIDが短すぎる（8文字未満）
     *   - ケース6: customerIDが長すぎる（16文字を超える）
     *   - ケース7: passwordが短すぎる（8文字未満）
     *   - ケース8: passwordが長すぎる（16文字を超える）
     *   - ケース9: customerIDに特殊文字が含まれる
     *   - ケース10: customerIDに全角文字が含まれる
     *   - ケース11: customerIDに漢字が含まれる
     *   - ケース12: passwordに全角文字が含まれる
     *   - ケース13: passwordに漢字が含まれる
     *   - ケース14: passwordに特殊文字が含まれる
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

        // Errorsオブジェクトを作成
        Errors errors = new BeanPropertyBindingResult(form, "form");

        // バリデーション実行（グループを指定）
        smartValidator.validate(form, errors, RequiredCheck.class, LengthCheck.class, FormatCheck.class);

        // ログ出力
        System.out.println("Errors: " + errors);

        // 検証
        assertThat(errors.hasErrors()).isTrue();
        errors.getFieldErrors().forEach(error -> {
            System.out.println("Field: " + error.getField() + ", Message: " + error.getDefaultMessage());
        });
    }

    /**
     * テストメソッドのパラメータ準備
     * 
     * @return Stream<Arguments>
     */
    static Stream<Arguments> webCustomerLoginForm_異常系_パラメータ() {
        String[] testCase = {
                "ケース1: customerIDがnull",
                "ケース2: passwordがnull",
                "ケース3: customerIDが空文字",
                "ケース4: passwordが空文字",
                "ケース5: customerIDが短すぎる（8文字未満）",
                "ケース6: customerIDが長すぎる（16文字を超える）",
                "ケース7: passwordが短すぎる（8文字未満）",
                "ケース8: passwordが長すぎる（16文字を超える）",
                "ケース9: customerIDに特殊文字が含まれる",
                "ケース10: customerIDに全角文字が含まれる",
                "ケース11: customerIDに漢字が含まれる",
                "ケース12: passwordに全角文字が含まれる",
                "ケース13: passwordに漢字が含まれる",
                "ケース14: passwordに特殊文字が含まれる"
        };

        // テストデータの準備
        String customerID1 = null;
        String password1 = "Password123";

        String customerID2 = "User1234";
        String password2 = null;

        String customerID3 = "";
        String password3 = "Password123";

        String customerID4 = "User1234";
        String password4 = "";

        String customerID5 = "User";
        String password5 = "Password123";

        String customerID6 = "User123456789012345";
        String password6 = "Password123";

        String customerID7 = "User1234";
        String password7 = "Pass";

        String customerID8 = "User1234";
        String password8 = "Password123456789012345";

        String customerID9 = "User@123";
        String password9 = "Password123";

        String customerID10 = "ユーザー1234";
        String password10 = "Password123";

        String customerID11 = "漢字1234";
        String password11 = "Password123";

        String customerID12 = "User1234";
        String password12 = "パスワード123";

        String customerID13 = "User1234";
        String password13 = "漢字パスワード";

        String customerID14 = "User1234";
        String password14 = "Pass@123";

        int i = 0;
        return Stream.of(
                Arguments.arguments(customerID1, password1, testCase[i++]),
                Arguments.arguments(customerID2, password2, testCase[i++]),
                Arguments.arguments(customerID3, password3, testCase[i++]),
                Arguments.arguments(customerID4, password4, testCase[i++]),
                Arguments.arguments(customerID5, password5, testCase[i++]),
                Arguments.arguments(customerID6, password6, testCase[i++]),
                Arguments.arguments(customerID7, password7, testCase[i++]),
                Arguments.arguments(customerID8, password8, testCase[i++]),
                Arguments.arguments(customerID9, password9, testCase[i++]),
                Arguments.arguments(customerID10, password10, testCase[i++]),
                Arguments.arguments(customerID11, password11, testCase[i++]),
                Arguments.arguments(customerID12, password12, testCase[i++]),
                Arguments.arguments(customerID13, password13, testCase[i++]),
                Arguments.arguments(customerID14, password14, testCase[i++]));
    }
}