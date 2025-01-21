package jp.co.benesse.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;

/**
 * <pre>
 * 共通コントローラークラス
 *
 * 作成日：2025/01/21
 * 更新日：2025/01/21
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Controller
@SessionAttributes({ "customerID", "customerName" })
public class CommonController {

    /** セッション */
    @Autowired
    private HttpSession session;

    /**
     * セッションからweb利用者IDを取得し、モデルに追加する
     * 
     * @return セッションから取得したweb利用者ID
     */
    @ModelAttribute("customerID")
    public String setCustomerID() {
        return (String) session.getAttribute("customerID");
    }

    /**
     * セッションから利用者名を取得し、モデルに追加する
     * 
     * @return セッションから取得した利用者名
     */
    @ModelAttribute("customerName")
    public String setCustomerName() {
        return (String) session.getAttribute("customerName");
    }
}