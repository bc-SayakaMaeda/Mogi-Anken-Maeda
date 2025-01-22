package jp.co.benesse.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.benesse.web.constants.UrlConstants;

/**
 * <pre>
 * web利用者メニューコントローラークラス
 *
 * 作成日：2025/01/14
 * 更新日：2025/01/14
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Controller
public class WebCustomerMenuController {

    /**
     * 初期表示：web利用者メニュー画面表示
     * 
     * @return web利用者メニュー画面
     */
    @GetMapping(UrlConstants.VIEW_WEB_CUSTOMER_MENU)
    public String showWebCustomerMenu() {
        return UrlConstants.VIEW_WEB_CUSTOMER_MENU;
    }
}
