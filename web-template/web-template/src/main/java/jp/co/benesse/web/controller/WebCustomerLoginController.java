package jp.co.benesse.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

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
    @GetMapping("/webCustomerLogin")
    public String showWebCustomerLogin(HttpSession session, Model model) {
        // セッション情報を初期化
        session.invalidate();

        // 入力フィールド（利用者ID・パスワード）を初期化
        model.addAttribute("customerID", "");
        model.addAttribute("password", "");

        return "webCustomerLogin";
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
     * @param model モデル
     * @param session セッション
     * @return メニュー画面
     */
    @PostMapping("/webCustomerLogin")
    public String login(@RequestParam("customerID") String customerID, @RequestParam("password") String password, Model model, HttpSession session) {
        // ユーザー情報取得
        // 必須チェック
        if (customerID == null || customerID.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("errorMessage", "入力必須項目です。");
            return "webCustomerLogin";
        }

        // 文字列長チェック
        if (customerID.length() < 8 || customerID.length() > 16 || password.length() < 8 || password.length() > 16) {
            model.addAttribute("errorMessage", "IDまたはパスワードの入力が適切ではありません。");
            return "webCustomerLogin";
        }

        // フォーマットチェック
        if (!customerID.matches("[a-zA-Z0-9]+") || !password.matches("[a-zA-Z0-9]+")) {
            model.addAttribute("errorMessage", "IDまたはパスワードの入力が適切ではありません。");
            return "webCustomerLogin";
        }

        // ログイン処理
        WebCustomer webCustomer = webCustomerLoginService.login(customerID, password);
        if (webCustomer == null) {
            model.addAttribute("errorMessage", "IDまたはパスワードが間違っています。");
            return "webCustomerLogin";
        }

        // セッション保存
        session.setAttribute("customerID", webCustomer.getCustomerId());
        session.setAttribute("customerName", webCustomer.getCustomerName());
        session.setAttribute("postCode", webCustomer.getPostCode());
        session.setAttribute("address", webCustomer.getAddress());
        session.setAttribute("email", webCustomer.getEmail());

        // 画面遷移
        return "forward:/webCustomerMenu";
    }
}