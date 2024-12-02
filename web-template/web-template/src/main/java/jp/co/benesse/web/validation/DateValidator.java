package jp.co.benesse.web.validation;

import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.co.benesse.web.annotation.DatePattern;
import jp.co.benesse.web.util.CheckUtil;

/**
 * [[PRW015]]日付チェック検証クラス.
 * 
 * @implNote 日付チェックでDateTimeFormatterを利用している。 DateTimeFormatterの年は"yyyy"ではなく"uuuu"なので注意。
 * ちなみに"yyyy"はyear-of-era（暦の年）なので"G yyyy"のように暦も同時に指定する必要がある。 "G yyyy"とした場合の指定方法は"西暦 2017"とかLocaleをENGLISHにして”AD yyyy”とか。
 * @author amano
 */
public class DateValidator implements ConstraintValidator<DatePattern, String> {
    /** 日付フォーマット. */
    private String pattern;

    /**
     * (非 Javadoc)
     * 
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(DatePattern constraintAnnotation) {
        pattern = constraintAnnotation.pattern();
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
        return CheckUtil.isDate(value, pattern);
    }
}
