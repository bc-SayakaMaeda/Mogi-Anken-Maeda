package jp.co.benesse.web.log;

import java.io.IOException;

import org.apache.logging.log4j.ThreadContext;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * ログにセッションIDを出力するフィルター
 *
 * <pre>
 * 作成日：2024/08/05
 * 更新日：2024/08/05
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
public class MdcFilter implements Filter {
    /** フィルタコンフィグ */
    private FilterConfig filterConfig = null;

    /**
     * <pre>
     * 初期化処理
     * </pre>
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * <pre>
     * メイン処理を実行
     * </pre>
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (filterConfig == null) {
            return;
        }

        if (request instanceof HttpServletRequest) {
            HttpServletRequest servletRequest = (HttpServletRequest) request;

            // jsessionId
            String jsessionId = servletRequest.getSession().getId();
            if (jsessionId == null) {
                jsessionId = "";
            }
            ThreadContext.put("jsessionId", jsessionId);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            // 使用後は削除する
            ThreadContext.remove("jsessionId");
        }

    }

    /**
     * <pre>
     * 終了処理
     * </pre>
     */
    public void destroy() {
        this.filterConfig = null;
    }

}
