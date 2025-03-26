package jp.co.benesse.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.benesse.web.annotation.AppDescription;
import jp.co.benesse.web.constants.AppDescriptions;
import jp.co.benesse.web.constants.SessionKeysConstants; // 追加
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.entity.WebCustomerEntity;
import jp.co.benesse.web.exception.WebParamException;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.form.WebCustomerLoginForm;
import jp.co.benesse.web.service.WebCustomerLoginService;
import jp.co.benesse.web.util.MessageUtil;
import jp.co.benesse.web.validationGroups.ValidationGroups.FormatCheck;
import jp.co.benesse.web.validationGroups.ValidationGroups.LengthCheck;
import jp.co.benesse.web.validationGroups.ValidationGroups.RequiredCheck;

/**
 * <pre>
 * web利用者ログインコントローラークラス
 *
 * 作成日：2024/12/17
 * 更新日：2025/02/20
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

    /** スマートバリデータ */
    @Autowired
    private SmartValidator validator;

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

        // ログイン画面に遷移
        return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
    }

    /**
     * ログインボタン押下時処理
     * 
     * @param webCustomerLoginForm web利用者ログインフォーム
     * @param bindingResult formクラスでのバリデーション結果
     * @param model モデル
     * @return メニュー画面
     * @throws WebUnexpectedException
     * @throws WebParamException
     */
    @PostMapping(UrlConstants.VIEW_WEB_CUSTOMER_LOGIN)
    @AppDescription(id = AppDescriptions.WEB_CUSTOMER_LOGIN_ID, name = AppDescriptions.WEB_CUSTOMER_LOGIN_NAME)
    public String login(WebCustomerLoginForm webCustomerLoginForm,
            BindingResult bindingResult, Model model) throws WebUnexpectedException, WebParamException {

        // バリデーションチェック
        if (!validateForm(webCustomerLoginForm, bindingResult, model)) {
            return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
        }
        WebCustomerEntity webCustomer = webCustomerLoginService.login(webCustomerLoginForm);

        if (webCustomer == null) {
            String errorMessage = MessageUtil.getMessage("error.invalid.credentials", "IDまたはパスワード");
            model.addAttribute("errorMessage", errorMessage);
            return UrlConstants.VIEW_WEB_CUSTOMER_LOGIN;
        }

        // セッション保存
        session.setAttribute(SessionKeysConstants.CUSTOMER_ID, webCustomer.getCustomerId());
        session.setAttribute(SessionKeysConstants.CUSTOMER_NAME, webCustomer.getCustomerName());
        session.setAttribute(SessionKeysConstants.POST_CODE, webCustomer.getPostCode());
        session.setAttribute(SessionKeysConstants.ADDRESS, webCustomer.getAddress());
        session.setAttribute(SessionKeysConstants.EMAIL, webCustomer.getEmail());

        // 平常時：メニュー画面に遷移
        return "redirect:" + UrlConstants.VIEW_WEB_CUSTOMER_MENU;

    }

    /**
     * フォームのバリデーションチェックを行う
     * 
     * @param webCustomerLoginForm web利用者ログインフォーム
     * @param bindingResult formクラスでのバリデーション結果
     * @param model モデル
     * @return バリデーションエラーがない場合はtrue、エラーがある場合はfalse
     */
    private boolean validateForm(WebCustomerLoginForm webCustomerLoginForm, BindingResult bindingResult, Model model) {
        // 必須チェック
        validator.validate(webCustomerLoginForm, bindingResult, RequiredCheck.class);
        if (bindingResult.hasErrors()) {
            String errorRequired = MessageUtil.getMessage("error.required");
            model.addAttribute("errorMessage", errorRequired);
            return false;
        }

        // 文字列長チェック
        validator.validate(webCustomerLoginForm, bindingResult, LengthCheck.class);
        if (bindingResult.hasErrors()) {
            String errorLoginLength = MessageUtil.getMessage("error.login.length", "IDまたはパスワード");
            model.addAttribute("errorMessage", errorLoginLength);
            return false;
        }

        // フォーマットチェック
        validator.validate(webCustomerLoginForm, bindingResult, FormatCheck.class);
        if (bindingResult.hasErrors()) {
            String errorLoginFormat = MessageUtil.getMessage("error.login.format", "IDまたはパスワード");
            model.addAttribute("errorMessage", errorLoginFormat);
            return false;
        }

        return true;
    }
}