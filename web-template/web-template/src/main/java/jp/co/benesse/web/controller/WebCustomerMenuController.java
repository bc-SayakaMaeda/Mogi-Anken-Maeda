package jp.co.benesse.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import jp.co.benesse.web.annotation.AppDescription;
import jp.co.benesse.web.constants.AppDescriptions;
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.dto.BookDataDTO;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.service.WebCustomerMenuService;
import jp.co.benesse.web.util.LogUtil;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * メニュー画面コントローラークラス
 *
 * 作成日：2025/01/21
 * 更新日：2025/02/06
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Controller
@SessionAttributes({ "customerID", "customerName", "BookRequestList" })
public class WebCustomerMenuController {

    /** メニューサービス */
    @Autowired
    private WebCustomerMenuService webCustomerMenuService;

    /**
     * メニュー画面 : 画面表示
     * 
     * @param model モデル
     * @param customerID web利用者ID
     * @param customerName 利用者名
     * @return メニュー画面
     */
    @GetMapping(UrlConstants.VIEW_WEB_CUSTOMER_MENU)
    @AppDescription(id = AppDescriptions.WEB_CUSTOMER_MENU_ID, name = AppDescriptions.WEB_CUSTOMER_MENU_NAME)
    public String showMenu(Model model, @ModelAttribute("customerID") String customerID,
            @ModelAttribute("customerName") String customerName) {

        // 利用者情報の取得
        model.addAttribute("customerID", customerID);
        model.addAttribute("customerName", customerName);

        try {
            // 図書一覧取得（在庫数設定済み）
            List<BookDataDTO> bookList = webCustomerMenuService.getBookListWithStock();

            // 総ページ数算出
            int totalPages = webCustomerMenuService.calculateTotalPages(bookList.size());

            // モデルにデータを設定
            model.addAttribute("bookList", bookList);
            model.addAttribute("totalPages", totalPages);

        } catch (WebUnexpectedException e) {
            String errorMessage = MessageUtil.getMessage("XXXXX-002");
            LogUtil.errorDetail(errorMessage, e);

            // エラー画面に遷移
            return UrlConstants.VIEW_ERROR;
        }

        return UrlConstants.VIEW_WEB_CUSTOMER_MENU;
    }

}