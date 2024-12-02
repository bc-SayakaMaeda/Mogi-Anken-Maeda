package jp.co.benesse.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 機能情報注釈.
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AppDescription {
    /**
     * 機能ID.
     */
    public String id();

    /**
     * 機能名称.
     */
    public String name();
}
