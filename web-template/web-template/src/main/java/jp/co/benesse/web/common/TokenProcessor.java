package jp.co.benesse.web.common;

import java.util.UUID;

import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * <pre>
 * Tokenプロセッサー
 *
 * 作成日：2024/10/28
 * 更新日：2024/10/28
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
public class TokenProcessor {

    /** インスタンス */
    private static TokenProcessor instance = new TokenProcessor();

    /** セッショントークン */
    public static final String SESSION_TOKEN_NAME = "session_token";

    /** hiddenトークン */
    public static final String PARAM_TOKEN_NAME = "param_token";

    /**
     * インスタンス生成
     *
     * @return インスタンス
     */
    public static TokenProcessor getInstance() {
        return instance;
    }

    /**
     * コンストラクタ
     */
    protected TokenProcessor() {
    }

    /**
     * トークンチェック
     * 
     * @param request リクエスト
     * @return チェック結果
     */
    public synchronized boolean isTokenValid(HttpServletRequest request) {
        return isTokenValid(request, false);
    }

    /**
     * トークンチェック（モデルからトークン取得）
     * 
     * @param request リクエスト
     * @param model モデル
     * @return チェック結果
     */
    public synchronized boolean isTokenValid(HttpServletRequest request, Model model) {
        request.setAttribute(TokenProcessor.PARAM_TOKEN_NAME, model.getAttribute(TokenProcessor.PARAM_TOKEN_NAME));
        return isTokenValid(request, false);
    }

    /**
     * トークンチェック
     * 
     * @param request リクエスト
     * @param reset true:セッショントークンリセット、false：リセットしない
     * @return チェック結果
     */
    public synchronized boolean isTokenValid(HttpServletRequest request, boolean reset) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String saved = (String) session.getAttribute(SESSION_TOKEN_NAME);
        if (saved == null) {
            return false;
        }
        if (reset) {
            resetToken(request);
        }
        String token = request.getParameter(PARAM_TOKEN_NAME) == null ? (String) request.getAttribute(PARAM_TOKEN_NAME)
                : request.getParameter(PARAM_TOKEN_NAME);
        if (token == null) {
            return false;
        }
        return saved.equals(token);
    }

    /**
     * トークン削除
     * 
     * @param request リクエスト
     */
    public synchronized void resetToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(SESSION_TOKEN_NAME);
    }

    /**
     * トークン保存
     * 
     * @param request リクエスト
     */
    public synchronized void saveToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String token = generateToken();
        if (token != null) {
            session.setAttribute(SESSION_TOKEN_NAME, token);
        }
    }

    /**
     * トークン生成
     * 
     * @return 生成したトークン
     */
    public synchronized String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
