package jp.co.benesse.web.validation;

import java.lang.reflect.Field;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.co.benesse.web.annotation.EnumPattern;

/**
 * <pre>
 * Enum用独自バリデータ
 *
 * 作成日：2024/07/12
 * 更新日：2024/07/12
 * </pre>
 *
 * @author BC)itano
 * @version 1.0
 */
public class EnumValidator implements ConstraintValidator<EnumPattern, String> {
    /** Enumクラス. */
    private Class<? extends Enum<?>> enumClass;
    /** フィールド名. */
    private String fieldName;

    /**
     * (非 Javadoc)
     * 
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(EnumPattern constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
        fieldName = constraintAnnotation.fieldName();
    }

    /**
     * (非 Javadoc)
     * 
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }
        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            try {
                Field field = enumValue.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                if (field.get(enumValue).equals(value)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
