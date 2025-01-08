package jp.co.benesse.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.benesse.web.annotation.AppDescription;
import jp.co.benesse.web.constants.AppDescriptions;
import jp.co.benesse.web.constants.CommonConstants;
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.entity.WebCustomerEntity;
import jp.co.benesse.web.exception.WebParamException;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.form.WebCustomerLoginForm;
import jp.co.benesse.web.service.WebCustomerLoginService;

/**
 * <pre>
 * web利用者ログインコントローラークラス
 *
 * 作成日：2024/12/17
 * 更新日：2025/01/07
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
    @AppDescription(id = AppDescriptions.WEB_CUSTOMER_LOGIN_ID, name = AppDescriptions.WEB_CUSTOMER_LOGIN_NAME)
    public String showWebCustomerLogin(WebCustomerLoginForm form) {
        // セッション情報を初期化
        session.invalidate();

        // 入力フィールド（利用者ID・パスワード）を初期化
        form.setCustomerID("");
        form.setPassword("");

        return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
    }
    
    /**
     * ログインボタン押下時：ログイン処理 
     * 1. ユーザー情報取得 
     * 2. パスワードのハッシュ化 
     * 3. DBアクセス（ログイン判定情報取得）
     * 4. セッション保存 
     * 5. 画面遷移
     * 
     * @param customerID インプットの利用者ID
     * @param password インプットのパスワード
     * @param session セッション
     * @param model モデル
     * @return メニュー画面
     */
    @PostMapping(UrlConstants.VIEW_WEB_CUSTOMER_LOGIN)
    @AppDescription(id = AppDescriptions.WEB_CUSTOMER_LOGIN_ID, name = AppDescriptions.WEB_CUSTOMER_LOGIN_NAME)
    public String login(@RequestParam String customerID, @RequestParam String password, HttpSession session, Model model) {
        // ユーザー情報取得
        // 必須チェック
        if (customerID == null || customerID.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("errorMessage", "入力必須項目です。");
            return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
        }

        // 文字列長チェック
        if (customerID.length() < 8 || customerID.length() > 16 || password.length() < 8 || password.length() > 16) {
            model.addAttribute("errorMessage", "IDまたはパスワードの入力が適切ではありません。");
            return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
        }

        // フォーマットチェック
        if (!customerID.matches(CommonConstants.HALF_ALPHANUMERIC) || !password.matches(CommonConstants.HALF_ALPHANUMERIC)) {
            model.addAttribute("errorMessage", "IDまたはパスワードの入力が適切ではありません。");
            return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
        }

        // ログイン処理
        WebCustomerEntity webCustomer = null;
        try {
            webCustomer = WebCustomerLoginService.login(customerID, password);
        } catch (WebUnexpectedException e) {
            e.printStackTrace();
        } catch (WebParamException e) {
            e.printStackTrace();
        }
        if (webCustomer == null) {
            model.addAttribute("errorMessage", "IDまたはパスワードが間違っています。");
            return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
        }

        // セッション保存
        session.setAttribute("customerID", webCustomer.getCustomerId());
        session.setAttribute("customerName", webCustomer.getCustomerName());
        session.setAttribute("postCode", webCustomer.getPostCode());
        session.setAttribute("address", webCustomer.getAddress());
        session.setAttribute("email", webCustomer.getEmail());

        // 画面遷移
        return "forward:" + UrlConstants.VIEW_WEB_CUSTOMER_MENU;
    }
}