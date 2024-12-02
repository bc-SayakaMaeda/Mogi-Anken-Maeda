package jp.co.benesse.web.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * 共通エラーControllerクラス
 * サービスへの振り分け、画面遷移を行う
 *
 * 作成日：2024/07/11
 * 更新日：2024/07/11
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Controller
public class ErrorController {

    /** セッション */
    @Autowired
    private HttpSession session;

    /**
     * 初期表示：セッションで受け取ったエラーメッセージを表示させる
     *
     * @param request リクエスト
     * @param response レスポンス
     * @param model データ受け渡し用モデル
     * @return 遷移先ページ
     */
    @RequestMapping(path = UrlConstants.VIEW_ERROR, method = { RequestMethod.GET, RequestMethod.POST })
    public String errorInit(HttpServletRequest request, HttpServletResponse response, Model model) {

        String errorMsg = (String) session.getAttribute("error_msg");

        if (Objects.isNull(errorMsg)) {
            errorMsg = MessageUtil.getMessage("systemError.message");
        }

        // エラー文とフラグを連携
        model.addAttribute("error_msg", errorMsg);

        return "error";
    }
}
