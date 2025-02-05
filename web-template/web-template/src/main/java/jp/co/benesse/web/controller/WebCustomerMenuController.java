package jp.co.benesse.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import jp.co.benesse.web.annotation.AppDescription;
import jp.co.benesse.web.constants.AppDescriptions;
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.entity.BookData;
import jp.co.benesse.web.service.WebCustomerMenuService;

/**
 * <pre>
 * メニュー画面コントローラークラス
 *
 * 作成日：2025/01/21
 * 更新日：2025/01/30
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

        // 書籍情報の取得
        List<BookData> bookRequestList = getBookRequestList(model);
        if (bookRequestList == null) {
            bookRequestList = new ArrayList<>();
        }

        // 図書一覧取得
        List<BookData> bookList = webCustomerMenuService.getBookList();

        // 在庫数算出
        Map<String, Long> stockCountMap = webCustomerMenuService.calculateStock(bookList);

        List<BookData> returnBookList = new ArrayList<BookData>();
        Map<String, BookData> bookMap = new HashMap<String, BookData>();

        // 在庫数を各書籍に設定
        for (BookData book : bookList) {
            // 同じ書籍IDが登録されていない場合に行う
            if (!bookMap.containsKey(book.getBookID())) {
                book.setStockCount(stockCountMap.getOrDefault(book.getBookID(), 0L).intValue());
                // 画面に渡す書籍一覧
                returnBookList.add(book);
                bookMap.put(book.getBookID(), book);
            }
        }

        // 総ページ数算出
        int totalPages = webCustomerMenuService.calculateTotalPages(bookList.size());

        model.addAttribute("bookList", returnBookList);
        model.addAttribute("totalPages", totalPages);

        return UrlConstants.VIEW_WEB_CUSTOMER_MENU;
    }

    /**
     * セッションから書籍リクエストリストを取得
     * 
     * @param model モデル
     * @return 書籍リクエストリスト
     */
    @SuppressWarnings("unchecked")
    private List<BookData> getBookRequestList(Model model) {
        return (List<BookData>) model.getAttribute("BookRequestList");
    }
}