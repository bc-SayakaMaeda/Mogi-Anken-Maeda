package jp.co.benesse.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jp.co.benesse.web.annotation.AppDescription;
import jp.co.benesse.web.constants.AppDescriptions;
import jp.co.benesse.web.constants.SessionKeysConstants;
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.dto.BookDataDTO;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.service.WebCustomerReservationCheckService;

/**
 * <pre>
 * 貸出確認画面コントローラークラス
 *
 * 作成日：2025/02/10
 * 更新日：2025/03/28
 * </pre>
 *
 * @version 1.0
 */
@Controller
public class WebCustomerReservationCheckController {

    /** 貸出予約確認サービス */
    @Autowired
    private WebCustomerReservationCheckService webCustomerReservationCheckService;

    /** セッション */
    @Autowired
    private HttpSession session;

    /**
     * 貸出予約確認画面：画面表示
     * 
     * @param model モデル
     * @return 貸出確認画面画面
     * @throws WebUnexpectedException
     */
    @GetMapping(UrlConstants.VIEW_WEB_CUSTOMER_RESERVATION_CHECK)
    @AppDescription(id = AppDescriptions.WEB_CUSTOMER_RESERVATIONCHECK_ID, name = AppDescriptions.WEB_CUSTOMER_RESERVATIONCHECK_NAME)
    public String showWebCustomerReservationCheck(Model model) throws WebUnexpectedException {

        // セッションから利用者情報を取得
        String customerID = (String) session.getAttribute(SessionKeysConstants.CUSTOMER_ID);
        String customerName = (String) session.getAttribute(SessionKeysConstants.CUSTOMER_NAME);
        String postalCode = (String) session.getAttribute(SessionKeysConstants.POST_CODE);
        String address = (String) session.getAttribute(SessionKeysConstants.ADDRESS);
        String email = (String) session.getAttribute(SessionKeysConstants.EMAIL);

        // 利用者情報をモデルに設定
        model.addAttribute("customerID", customerID);
        model.addAttribute("customerName", customerName);
        model.addAttribute("postalCode", postalCode);
        model.addAttribute("address", address);
        model.addAttribute("email", email);

        // 貸出希望書籍情報の取得
        Object bookRequestList = session.getAttribute(SessionKeysConstants.BOOK_REQUEST);
        model.addAttribute("bookRequestList", bookRequestList);

        // 貸出確認画面に遷移
        return UrlConstants.VIEW_WEB_CUSTOMER_RESERVATION_CHECK;
    }

    /**
     * 貸出予約確定ボタン押下時処理
     * 
     * @param redirectAttributes
     * @param model
     * @return 画面遷移
     * @throws WebUnexpectedException
     */
    @SuppressWarnings("unchecked")
    @PostMapping(UrlConstants.VIEW_WEB_CUSTOMER_RESERVATION_CHECK)
    @AppDescription(id = AppDescriptions.WEB_CUSTOMER_RESERVATIONCHECK_ID, name = AppDescriptions.WEB_CUSTOMER_RESERVATIONCHECK_NAME)
    public String reservationConfirm(RedirectAttributes redirectAttributes, Model model) throws WebUnexpectedException {

        // セッションから貸出希望書籍IDを取得
        List<String> bookRequestList = (List<String>) session
                .getAttribute(SessionKeysConstants.BOOK_IDS);

        // 貸出状況情報取得（在庫数設定済み）
        List<BookDataDTO> loanInfoBookList = webCustomerReservationCheckService
                .getBookLoanInfoWithStock(bookRequestList);
        // 貸出可能判定
        List<BookDataDTO> reservatableBookList = webCustomerReservationCheckService
                .getReservatableBooks(loanInfoBookList);

        // 貸出不可の書籍が1冊でも存在する場合
        if (reservatableBookList.size() < loanInfoBookList.size()) {
            redirectAttributes.addFlashAttribute("errorMessage", "申し訳ございません、貸出中の図書がございます。もう一度選択ください。");
            redirectAttributes.addFlashAttribute("bookRequestList", bookRequestList);
            return "redirect:" + UrlConstants.VIEW_WEB_CUSTOMER_MENU;
        }

        // 書籍情報の更新
        session.setAttribute(SessionKeysConstants.BOOK_REQUEST, reservatableBookList);

        // 貸出情報記録
        String customerId = (String) session.getAttribute(SessionKeysConstants.CUSTOMER_ID);
        webCustomerReservationCheckService.registerLoanInfo(customerId, reservatableBookList);

        // リダイレクト
        return "redirect:" + UrlConstants.VIEW_WEB_CUSTOMER_RESERVATION_CONFIRM;
    }

}