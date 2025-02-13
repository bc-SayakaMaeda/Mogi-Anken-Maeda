package jp.co.benesse.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.benesse.web.constants.UrlConstants;

/**
 * <pre>
 * 貸出確認画面コントローラークラス
 * 
 * 貸出希望ボタン押下時の画面遷移のために仮作成
 *
 * 作成日：2025/02/10
 * 更新日：2025/02/10
 * </pre>
 *
 * @version 1.0
 */
@Controller
public class WebCustomerReservationCheckController {

    /**
     * 初期表示：貸出確認画面表示
     * 
     * @return 貸出確認画面画面
     */
    @GetMapping(UrlConstants.VIEW_WEB_CUSTOMER_RESERVATION_CHECK)
    public String showWebCustomerReservationCheck() {

        // 貸出確認画面に遷移
        return UrlConstants.VIEW_WEB_CUSTOMER_RESERVATION_CHECK;
    }
}