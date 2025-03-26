package jp.co.benesse.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.benesse.web.annotation.AppDescription;
import jp.co.benesse.web.constants.AppDescriptions;
import jp.co.benesse.web.constants.SessionKeysConstants;
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.exception.WebUnexpectedException;

/**
 * <pre>
 * 貸出確認画面コントローラークラス
 * 
 * 貸出希望ボタン押下時の画面遷移のために仮作成
 *
 * 作成日：2025/02/10
 * 更新日：2025/03/26
 * </pre>
 *
 * @version 1.0
 */
@Controller
public class WebCustomerReservationCheckController {

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

        // 利用者情報をモデルに設定
        model.addAttribute("customerID", customerID);
        model.addAttribute("customerName", customerName);

        // 貸出希望書籍情報の取得
        Object bookRequestList = session.getAttribute(SessionKeysConstants.BOOK_REQUEST);
        model.addAttribute("bookRequestList", bookRequestList);

        // 貸出確認画面に遷移
        return UrlConstants.VIEW_WEB_CUSTOMER_RESERVATION_CHECK;
    }
}