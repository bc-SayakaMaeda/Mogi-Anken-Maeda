package jp.co.benesse.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.benesse.web.constants.UrlConstants;

/**
 * <pre>
 * web利用者ログインコントローラークラス
 *
 * 作成日：2024/12/17
 * 更新日：2024/12/18
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Controller
public class WebCustomerLoginController {

    /**
     * 初期表示：web利用者ログイン画面表示 1. セッション情報を初期化 2. 入力フィールド（利用者ID・パスワード）を初期化
     * 
     * @param session セッション
     * @param model モデル
     * @return web利用者ログイン画面
     */
    @GetMapping(UrlConstants.VIEW_WEB_CUSTOMER_LOGIN)
    public String showWebCustomerLogin(HttpSession session, Model model) {
        // セッション情報を初期化
        session.invalidate();

        // 入力フィールド（利用者ID・パスワード）を初期化
        model.addAttribute("customerID", "");
        model.addAttribute("password", "");

        return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
    }
}
