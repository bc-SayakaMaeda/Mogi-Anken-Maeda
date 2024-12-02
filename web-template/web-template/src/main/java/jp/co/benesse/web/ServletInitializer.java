package jp.co.benesse.web;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * <pre>
 * 基底クラス
 *
 * 作成日：2024/10/16
 * 更新日：2024/10/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebTemplateApplication.class);
    }

}
