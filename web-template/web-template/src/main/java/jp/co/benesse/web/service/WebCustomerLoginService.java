package jp.co.benesse.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.co.benesse.web.entity.WebCustomerEntity;
import jp.co.benesse.web.exception.WebParamException;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.form.WebCustomerLoginForm;
import jp.co.benesse.web.repository.WebCustomerLoginRepository;
import jp.co.benesse.web.util.HashUtil;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * web利用者ログインサービス
 *
 * 作成日：2024/12/24
 * 更新日：2025/01/14
 * </pre>
 * 
 * @author bc)maeda
 * @version 1.0
 */
@Service
public class WebCustomerLoginService {

    /** WebCustomerLoginリポジトリ */
    @Autowired
    private WebCustomerLoginRepository webCustomerLoginRepository;

    /**
     * <pre>
     * ログイン認証をする
     * 1. パスワードのハッシュ化 
     * 2. DBアクセス（ログイン判定情報取得）
     * </pre>
     *
     * @param webCustomerLoginForm WEb利用者ログインフォーム
     * @return webCustomer
     * @throws WebUnexpectedException
     * @throws WebParamException
     */
    public WebCustomerEntity login(@ModelAttribute WebCustomerLoginForm webCustomerLoginForm)
            throws WebUnexpectedException, WebParamException {
        String customerID = webCustomerLoginForm.getCustomerID();
        String password = webCustomerLoginForm.getPassword();

        // パスワードをSHA-256でハッシュ化
        String sha256HashedPassword = HashUtil.sha256(password);

        // DBアクセス（ログイン判定情報取得）
        WebCustomerEntity webCustomer = webCustomerLoginRepository.getLoginInfo(customerID, sha256HashedPassword);

        if (webCustomer == null) {
            throw new WebParamException(MessageUtil.getMessage("XXXXX-009"));
        }

        return webCustomer;
    }
}