package jp.co.benesse.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * <pre>
 * SecurityConfigクラス
 *
 * 作成日：2025/01/07
 * 更新日：2025/01/07
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Configuration
public class SecurityConfig {

    /**
     * <pre>
     * BCryptPasswordEncoderをインスタンス化する
     * </pre>
     * 
     * @return BCryptPasswordEncoderインスタンス
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}