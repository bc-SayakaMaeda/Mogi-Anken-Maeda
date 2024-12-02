package jp.co.benesse.web.util;

import java.util.Locale;
import java.util.Objects;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jp.co.benesse.web.constants.CommonConstants;

/**
 * <pre>
 * メッセージ用ユーティリティ.
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Component
public final class MessageUtil {

    /** 使用するメッセージソース */
    private static MessageSource messageSource;

    /** メッセージソース */
    @Autowired
    private MessageSource componentMessageSource;

    /**
     * コンストラクタ
     */
    private MessageUtil() {
    }

    /**
     * コンポーネント初期化
     */
    @PostConstruct
    public void init() {
        MessageUtil.messageSource = componentMessageSource;
    }

    /**
     * <pre>
     * キーから値(メッセージ)を取得します。
     * </pre>
     *
     * @param key キー
     * @param params 置換文字列
     * @return 値(メッセージ)
     */
    public static String getMessage(String key, Object... params) {

        String message = messageSource.getMessage(key, params, Locale.JAPAN);
        // 置換文字列を機能IDに置換
        if (message.indexOf(CommonConstants.ERROR_CODE_REPLACE_PART) != -1) {
            String funcId = Objects.nonNull(ThreadContext.get(CommonConstants.FUNC_ID))
                    ? ThreadContext.get(CommonConstants.FUNC_ID)
                    : CommonConstants.ERROR_CODE_REPLACE_PART;
            message = message.replaceAll(CommonConstants.ERROR_CODE_REPLACE_PART, funcId);
        }
        return message;
    }
}