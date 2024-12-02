package jp.co.benesse.web.log;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * ログフィルター用Configurer
 *
 * <pre>
 * 作成日：2024/08/05
 * 更新日：2024/08/05
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Configuration
public class MdcFilterConfigurer {
    /**
     * フィルタセッティング
     * 
     * @return MdcFilter
     */
    @Bean
    MdcFilter mdcFilterFilter() {
        MdcFilter filter = new MdcFilter();
        return filter;
    }

    /**
     * フィルタの優先順位を最高に設定
     * 
     * @return FilterRegistrationBean
     */
    @Bean
    FilterRegistrationBean<MdcFilter> fugaFilterConfig() {
        FilterRegistrationBean<MdcFilter> bean = new FilterRegistrationBean<>(new MdcFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
