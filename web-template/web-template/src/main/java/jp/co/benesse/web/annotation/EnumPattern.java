package jp.co.benesse.web.annotation;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.co.benesse.web.validation.EnumValidator;

/**
 * <pre>
 * Enum用独自バリデータアノテーション
 *
 * 作成日：2024/07/12
 * 更新日：2024/07/12
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Retention(RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD })
@Constraint(validatedBy = { EnumValidator.class })
public @interface EnumPattern {
    /** Bean Validation として必要: ターゲットグループをカスタマイズする用 */
    Class<?>[] groups() default {};

    /** Bean Validation として必要: メタデータ情報の拡張用 */
    Class<? extends Payload>[] payload() default {};

    /** デフォルトメッセージ */
    String message() default "コード定義外の値が設定されています";

    /** Enum */
    Class<? extends Enum<?>> enumClass();

    /** フィールド名 */
    String fieldName();

}
