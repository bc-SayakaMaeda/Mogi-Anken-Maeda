package jp.co.benesse.web;

import java.util.Collections;

import org.mybatis.scripting.thymeleaf.SqlGenerator;
import org.mybatis.scripting.thymeleaf.SqlGeneratorConfig;
import org.mybatis.scripting.thymeleaf.processor.BindVariableRender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;

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
@SpringBootApplication
@EnableRetry
public class WebTemplateApplication {

    /**
     * メイン処理
     * 
     * @param args 引数
     */
    public static void main(String[] args) {
        SpringApplication.run(WebTemplateApplication.class, args);
    }

    /**
     * Cookieにセキュアを付与
     * 
     * @return ServletContextInitializer
     */
    @Bean
    ServletContextInitializer servletContextInitializer() {

        ServletContextInitializer servletContextInitializer = new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {

                servletContext.getSessionCookieConfig().setSecure(true);
                servletContext.setSessionTrackingModes(
                        Collections.singleton(SessionTrackingMode.COOKIE));
            }
        };
        return servletContextInitializer;
    }

    /**
     * SQLを作成するエンジンのBeanを登録
     * 
     * @return SQLを作成するエンジン
     */
    @Bean
    SqlGenerator sqlGenerator() {
        final SqlGeneratorConfig config = SqlGeneratorConfig.newInstanceWithCustomizer(
                c -> c.getDialect().setBindVariableRenderInstance(BindVariableRender.BuiltIn.SPRING_NAMED_PARAMETER));
        return new SqlGenerator(config);
    }
}
