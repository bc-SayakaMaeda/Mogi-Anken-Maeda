package jp.co.benesse.web.interceptor;

import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.exception.WebParamException;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.exception.WebViewHandlingException;
import jp.co.benesse.web.util.LogUtil;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * 共通処理用Interceptor実装クラス
 * 画面表示用コントローラークラスを実行する前に実行される
 *
 * 作成日：2024/06/18
 * 更新日：2024/06/18
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Aspect
@Order(2)
@Component
public class WebInterceptor {

    /** レスポンス */
    @Autowired
    private HttpServletResponse response;

    /** セッション */
    @Autowired
    private HttpSession session;

    /**
     * 共通処理インターセプト
     *
     * @param pjp 進行中処理
     * @return オブジェクト
     * @throws Throwable 予期しない例外
     */
    @Around("execution(public * jp.co.benesse.web.controller..*Controller.*(..))")
    public Object commonIntercept(ProceedingJoinPoint pjp) throws Throwable {

        // 戻り値
        Object result = null;

        try {
            // responseの文字エンコーディングをUTF8に設定
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            response.setHeader("Content-Security-Policy", "frame-ancestors");
            response.setHeader("X-XSS-Protection", "1; mode=block");

            // 処理を実行
            result = pjp.proceed();

            // 戻り値がHTML名の場合、頭のスラッシュをとる
            if (result != null && result instanceof String) {
                String resultStr = (String) result;
                if (resultStr.startsWith("/") && resultStr.length() > 1) {
                    result = resultStr.substring(1);
                }
            }

        } catch (WebParamException e) {
            LogUtil.warnDetail(e.getErrorCodeMessage(), e);
            session.setAttribute("error_msg", MessageUtil.getMessage("systemError.message"));
            return "forward:" + UrlConstants.VIEW_ERROR;

        } catch (WebViewHandlingException e) {
            LogUtil.infoDetail(e.getErrorCodeMessage(), e);
            session.setAttribute("error_msg", e.getErrorMessage());
            return "forward:" + UrlConstants.VIEW_ERROR;

        } catch (WebUnexpectedException e) {
            if (Objects.isNull(e.getCause())) {
                LogUtil.errorDetail(e.getErrorCodeMessage(), e);
            } else {
                LogUtil.logger.error(e.getErrorCodeMessage(), e.getCause());
            }
            session.setAttribute("error_msg", MessageUtil.getMessage("systemError.message"));
            return "forward:" + UrlConstants.VIEW_ERROR;

        } catch (Exception e) {
            LogUtil.logger.error(MessageUtil.getMessage("XXXXX-001"), e);
            session.setAttribute("error_msg", MessageUtil.getMessage("systemError.message"));
            return "forward:" + UrlConstants.VIEW_ERROR;
        }
        return result;
    }

}
