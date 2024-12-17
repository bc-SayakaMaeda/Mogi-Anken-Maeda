package jp.co.benesse.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

/**
 *  <pre>
 * web利用者ログインコントローラークラス
 *
 * 作成日：2024/12/17
 * 更新日：2024/12/17
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Controller
public class WebCustomerLoginController {

    /**
     * @param session
     * @param model
     * @return web利用者ログイン画面
     */
    @GetMapping("/webCustomerLogin")
    public String showWebCustomerLogin(HttpSession session, Model model) {
        // セッション情報を初期化
        session.invalidate();
        
        // 入力フィールドを初期化
        model.addAttribute("userId", "");
        model.addAttribute("password", "");
        
        return "webCustomerLogin"; 
    }
}
