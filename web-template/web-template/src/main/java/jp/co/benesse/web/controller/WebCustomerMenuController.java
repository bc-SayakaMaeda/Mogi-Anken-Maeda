package jp.co.benesse.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.benesse.web.annotation.AppDescription;
import jp.co.benesse.web.constants.AppDescriptions;
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.dto.BookDataDTO;
import jp.co.benesse.web.dto.BookRequestDTO;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.service.WebCustomerMenuService;
import jp.co.benesse.web.util.LogUtil;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * メニュー画面コントローラークラス
 *
 * 作成日：2025/01/21
 * 更新日：2025/02/13
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Controller
public class WebCustomerMenuController {

    /** メニューサービス */
    @Autowired
    private WebCustomerMenuService webCustomerMenuService;

    /** セッション */
    @Autowired
    private HttpSession session;

    /**
     * メニュー画面 : 画面表示
     * 
     * @param model モデル
     * @return メニュー画面
     */
    @GetMapping(UrlConstants.VIEW_WEB_CUSTOMER_MENU)
    @AppDescription(id = AppDescriptions.WEB_CUSTOMER_MENU_ID, name = AppDescriptions.WEB_CUSTOMER_MENU_NAME)
    public String showMenu(Model model) {

        // セッションから利用者情報を取得
        String customerID = (String) session.getAttribute("customerID");
        String customerName = (String) session.getAttribute("customerName");

        // 利用者情報をモデルに設定
        model.addAttribute("customerID", customerID);
        model.addAttribute("customerName", customerName);

        try {
            // セッションから貸出希望書籍情報を取得
            @SuppressWarnings("unchecked")
            List<BookDataDTO> sessionBookList = (List<BookDataDTO>) session.getAttribute("bookList");

            if (sessionBookList != null && !sessionBookList.isEmpty()) {
                model.addAttribute("bookList", sessionBookList);

            } else {
                // 図書一覧取得（在庫数設定済み）
                List<BookDataDTO> bookList = webCustomerMenuService.getBookListWithStock();
                model.addAttribute("bookList", bookList);
                session.setAttribute("bookList", bookList);

                // 総ページ数算出
                int totalPages = webCustomerMenuService.calculateTotalPages(bookList.size());
                model.addAttribute("totalPages", totalPages);
            }

        } catch (WebUnexpectedException e) {
            String errorMessage = MessageUtil.getMessage("XXXXX-002");
            LogUtil.errorDetail(errorMessage, e);

            // エラー画面に遷移
            return UrlConstants.VIEW_ERROR;
        }

        return UrlConstants.VIEW_WEB_CUSTOMER_MENU;
    }

    /**
     * 貸出希望確認ボタン押下時処理
     * 
     * @param bookIds 貸出希望書籍IDリスト
     * @param model モデル
     * @return 貸出予約確認画面
     */
    @PostMapping(UrlConstants.VIEW_WEB_CUSTOMER_MENU)
    @AppDescription(id = AppDescriptions.WEB_CUSTOMER_MENU_ID, name = AppDescriptions.WEB_CUSTOMER_MENU_NAME)
    public String reservationCheck(@RequestParam(required = false) List<String> bookIds, Model model) {
        // 必須チェック
        if (bookIds == null || bookIds.isEmpty()) {
            model.addAttribute("error", true);
            return UrlConstants.VIEW_WEB_CUSTOMER_MENU;
        }

        try {

            // 書籍ID指定図書一覧取得（在庫数設定済み）
            List<BookRequestDTO> selectBookList = webCustomerMenuService.getBookListByIdWithStock(bookIds);

            // 貸出可能判定
            List<BookRequestDTO> reservatableBookList = webCustomerMenuService.getReservatableBooks(selectBookList);
            model.addAttribute("reservatableBookList", reservatableBookList);

            if (reservatableBookList.size() == selectBookList.size()) {

                // すべての書籍IDに対して貸出可の場合：貸出希望確認画面に遷移
                return "redirect:" + UrlConstants.VIEW_WEB_CUSTOMER_RESERVATION_CHECK;

            } else {
                // 貸出不可の書籍IDが存在する場合
                model.addAttribute("errorMessage", "申し訳ございません、貸出中の図書がございます。もう一度選択ください。");
                // メニュー画面に留まる
                return UrlConstants.VIEW_WEB_CUSTOMER_MENU;
            }

        } catch (WebUnexpectedException e) {
            String errorMessage = MessageUtil.getMessage("XXXXX-002");
            LogUtil.errorDetail(errorMessage, e);

            // エラー発生時：エラー画面に遷移
            return UrlConstants.VIEW_ERROR;
        }

    }
}