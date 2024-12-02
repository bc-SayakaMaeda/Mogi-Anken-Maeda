package jp.co.benesse.web.annotation;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.co.benesse.web.validation.DateValidator;

/**
 * <pre>
 * 日付チェック用.
 * DateTimeFormatterの年は"yyyy"ではなく"uuuu"なので注意。
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Retention(RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD })
@Constraint(validatedBy = { DateValidator.class })
public @interface DatePattern {
    /** Bean Validation として必要: ターゲットグループをカスタマイズする用 */
    Class<?>[] groups() default {};

    /** Bean Validation として必要: メタデータ情報の拡張用 */
    Class<? extends Payload>[] payload() default {};

    /** デフォルトメッセージ */
    String message() default "不正な日付が設定されています";

    /** デフォルトパターン */
    String pattern() default "uuuu-MM-dd";

}
