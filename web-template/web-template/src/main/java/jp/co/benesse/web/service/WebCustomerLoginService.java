package jp.co.benesse.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.benesse.web.entity.WebCustomerEntity;
import jp.co.benesse.web.exception.WebParamException;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.form.WebCustomerLoginForm;
import jp.co.benesse.web.repository.WebCustomerLoginRepository;
import jp.co.benesse.web.util.HashUtil;
import jp.co.benesse.web.util.LogUtil;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * web利用者ログインサービス
 *
 * 作成日：2024/12/24
 * 更新日：2025/01/27
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
     * ログイン認証メソッド
     *
     * @param webCustomerLoginForm web利用者ログインフォーム
     * @return webCustomer 利用者情報
     * @throws WebUnexpectedException
     * @throws WebParamException
     */
    public WebCustomerEntity login(WebCustomerLoginForm webCustomerLoginForm)
            throws WebUnexpectedException, WebParamException {
        String customerID = webCustomerLoginForm.getCustomerID();
        String password = webCustomerLoginForm.getPassword();

        // パスワードのハッシュ化
        String sha256HashedPassword = HashUtil.sha256(password);
        if (sha256HashedPassword == null) {
            String errorMessage = MessageUtil.getMessage("パスワードのハッシュ化に失敗しました");
            WebUnexpectedException exception = new WebUnexpectedException(errorMessage);
            LogUtil.infoDetail(errorMessage, exception);
            throw exception;
        }

        // DBアクセス（ログイン判定情報取得）
        WebCustomerEntity webCustomer = webCustomerLoginRepository.getLoginInfo(customerID, sha256HashedPassword);

        if (webCustomer == null) {
            String errorMessage = MessageUtil.getMessage("XXXXX-009");
            WebParamException exception = new WebParamException(errorMessage);
            LogUtil.infoDetail(errorMessage, exception);
        }

        return webCustomer;
    }
}