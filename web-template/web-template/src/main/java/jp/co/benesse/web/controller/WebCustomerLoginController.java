package jp.co.benesse.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.benesse.web.annotation.AppDescription;
import jp.co.benesse.web.constants.AppDescriptions;
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.entity.WebCustomerEntity;
import jp.co.benesse.web.exception.WebParamException;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.form.WebCustomerLoginForm;
import jp.co.benesse.web.service.WebCustomerLoginService;
import jp.co.benesse.web.util.MessageUtil;
import jp.co.benesse.web.validationGroups.ValidationGroups.ValidationOrder;

/**
 * <pre>
 * web利用者ログインコントローラークラス
 *
 * 作成日：2024/12/17
 * 更新日：2025/01/14
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Controller
public class WebCustomerLoginController {

    /** セッション */
    @Autowired
    private HttpSession session;

    /** web利用者ログインサービス */
    @Autowired
    private WebCustomerLoginService webCustomerLoginService;

    /** メッセージソース（エラーメッセージ格納） */
    @Autowired
    private MessageSource messageSource;

    /**
     * 初期表示：web利用者ログイン画面表示
     * <p>
     * 1. セッション情報を初期化 2. 入力フィールド（利用者ID・パスワード）を初期化
     * </p>
     * 
     * @param webCustomerLoginForm web利用者ログインフォーム
     * @return web利用者ログイン画面
     */
    @GetMapping(UrlConstants.VIEW_WEB_CUSTOMER_LOGIN)
    @AppDescription(id = AppDescriptions.WEB_CUSTOMER_LOGIN_ID, name = AppDescriptions.WEB_CUSTOMER_LOGIN_NAME)
    public String showWebCustomerLogin(WebCustomerLoginForm webCustomerLoginForm) {
        // セッション情報を初期化
        session.invalidate();

        // 入力フィールド（利用者ID・パスワード）を初期化
        webCustomerLoginForm.setCustomerID("");
        webCustomerLoginForm.setPassword("");

        return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
    }

    /**
     * ログインボタン押下時：ログイン処理 1. ユーザー情報取得 2. パスワードのハッシュ化 3. DBアクセス（ログイン判定情報取得） 4. セッション保存 5. 画面遷移
     * 
     * @param webCustomerLoginForm web利用者ログインフォーム
     * @param bindingResult formクラスでのバリデーション結果
     * @param model モデル
     * @return メニュー画面
     */
    @PostMapping(UrlConstants.VIEW_WEB_CUSTOMER_LOGIN)
    @AppDescription(id = AppDescriptions.WEB_CUSTOMER_LOGIN_ID, name = AppDescriptions.WEB_CUSTOMER_LOGIN_NAME)
    public String login(@Validated(ValidationOrder.class) WebCustomerLoginForm webCustomerLoginForm,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("webCustomerLoginForm", webCustomerLoginForm);
            return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
        }

        try {
            WebCustomerEntity webCustomer = webCustomerLoginService.login(webCustomerLoginForm);

            // セッション保存
            session.setAttribute("customerID", webCustomer.getCustomerId());
            session.setAttribute("customerName", webCustomer.getCustomerName());
            session.setAttribute("postCode", webCustomer.getPostCode());
            session.setAttribute("address", webCustomer.getAddress());
            session.setAttribute("email", webCustomer.getEmail());

            // 平常時：メニュー画面に遷移
            return "redirect:" + UrlConstants.VIEW_WEB_CUSTOMER_MENU;

        } catch (WebUnexpectedException e) {

            String errorMessage = MessageUtil.getMessage("XXXXX-001");
            model.addAttribute("errorMessage", errorMessage);

            // エラー発生時：エラー画面に遷移
            return UrlConstants.VIEW_ERROR;

        } catch (WebParamException e) {
            String errorMessage = messageSource.getMessage(
                    "error.invalid.credentials",
                    new Object[] { "IDまたはパスワード" },
                    LocaleContextHolder.getLocale());
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("webCustomerLoginForm", webCustomerLoginForm);
            return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
        }

    }
}