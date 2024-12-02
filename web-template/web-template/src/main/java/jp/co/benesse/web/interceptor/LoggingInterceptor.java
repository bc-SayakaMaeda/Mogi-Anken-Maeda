package jp.co.benesse.web.interceptor;

import java.lang.reflect.Method;
import java.util.Objects;

import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jp.co.benesse.web.annotation.AppDescription;
import jp.co.benesse.web.constants.CommonConstants;
import jp.co.benesse.web.util.LogUtil;

/**
 * <pre>
 * ロギング用インターセプタ―
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Aspect
@Order(1)
@Component
public class LoggingInterceptor {

    /**
     * 全てのActionを対象に開始／終了ログ出力
     * 
     * @param pjp メソッド情報
     * @return オブジェクト
     * @throws Throwable 予期しない例外
     */
    @Around("execution(public * jp.co.benesse.web.controller..*Controller.*(..))")
    public Object apiIntercept(ProceedingJoinPoint pjp) throws Throwable {

        // 戻り値
        Object result = null;

        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();

        // API説明アノテーションを取得
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        AppDescription appDescription = method.getAnnotation(AppDescription.class);
        String apiId = "";
        if (!Objects.isNull(appDescription)) {
            apiId = appDescription.id();
            // 機能IDをThreadContextに登録しておき、共通機能で使用する
            ThreadContext.put(CommonConstants.FUNC_ID, apiId);
        }

        // 開始ログ出力
        LogUtil.logger.info(className + "#" + methodName + " [start]");

        try {
            // メソッドを実行
            result = pjp.proceed();
            // 終了ログ出力
            LogUtil.logger.info(className + "#" + methodName + " [end]");
        } catch (Exception e) {
            // 終了ログ出力
            LogUtil.logger.debug(className + "#" + methodName + " [end]");
            throw e;
        } finally {
            ThreadContext.remove(CommonConstants.FUNC_ID);
        }
        return result;
    }

}
