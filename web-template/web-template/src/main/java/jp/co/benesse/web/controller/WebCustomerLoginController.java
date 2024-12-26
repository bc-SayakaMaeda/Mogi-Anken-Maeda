package jp.co.benesse.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.benesse.web.annotation.AppDescription;
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.form.WebCustomerLoginForm;

/**
 * <pre>
 * web利用者ログインコントローラークラス
 *
 * 作成日：2024/12/17
 * 更新日：2024/12/26
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Controller
public class WebCustomerLoginController {

    /** セッション */
    @Autowired
    private HttpSession session;

    /**
     * 初期表示：web利用者ログイン画面表示
     * <p>
     * 1. セッション情報を初期化 2. 入力フィールド（利用者ID・パスワード）を初期化
     * </p>
     * 
     * @param form フォーム
     * @return web利用者ログイン画面
     */
    @GetMapping(UrlConstants.VIEW_WEB_CUSTOMER_LOGIN)
    @AppDescription(id = "WEB_CUSTOMER_LOGIN", name = "WEB利用者ログイン")
    public String showWebCustomerLogin(WebCustomerLoginForm form) {
        // セッション情報を初期化
        session.invalidate();

        // 入力フィールド（利用者ID・パスワード）を初期化
        form.setCustomerID("");
        form.setPassword("");

        return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
    }
}