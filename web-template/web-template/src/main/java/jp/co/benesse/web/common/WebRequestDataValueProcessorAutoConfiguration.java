package jp.co.benesse.web.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <pre>
 * hidden操作
 *
 * 作成日：2024/10/28
 * 更新日：2023/10/28
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Configuration
public class WebRequestDataValueProcessorAutoConfiguration {

    /**
     * formタグのhidden項目にトークンを追加する
     * 
     * @return RequestDataValueProcessor
     */
    @Bean
    RequestDataValueProcessor requestDataValueProcessor() {
        return new RequestDataValueProcessor() {

            @Override
            public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {

                // formタグのhidden項目にトークンを追加する
                Map<String, String> hiddenParams = new HashMap<>();
                String token = (String) request.getSession().getAttribute(TokenProcessor.SESSION_TOKEN_NAME);
                if (token != null) {
                    hiddenParams.put(TokenProcessor.PARAM_TOKEN_NAME, token);
                    return hiddenParams;
                }
                return null;
            }

            @Override
            public String processUrl(HttpServletRequest request, String url) {
                return url;
            }

            @Override
            public String processAction(HttpServletRequest request, String action, String httpMethod) {
                return action;
            }

            @Override
            public String processFormFieldValue(HttpServletRequest request, String name, String value, String type) {
                return value;
            }

        };
    }
}
