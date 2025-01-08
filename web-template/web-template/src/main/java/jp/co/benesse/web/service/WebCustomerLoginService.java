package jp.co.benesse.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.benesse.web.constants.ErrorMessages;
import jp.co.benesse.web.entity.WebCustomerEntity;
import jp.co.benesse.web.exception.WebParamException;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.repository.WebCustomerLoginRepository;
import jp.co.benesse.web.util.HashUtil;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * web利用者ログインサービス
 *
 * 作成日：2024/12/24
 * 更新日：2025/01/07
 * </pre>
 * 
 * @author bc)maeda
 * @version 1.0
 */
@Service
public class WebCustomerLoginService {

    /** WebCustomerLoginリポジトリ */
    @Autowired
    private static WebCustomerLoginRepository webCustomerLoginRepository;

    /** パスワードエンコーダー */
    @Autowired
    private static BCryptPasswordEncoder passwordEncoder;

    /**
     * <pre>
     * ログイン認証をする
     * 1. パスワードのハッシュ化 
     * 2. DBアクセス（ログイン判定情報取得）
     * </pre>
     *
     * @param customerID
     * @param password
     * @return webCustomer
     * @throws WebUnexpectedException
     * @throws WebParamException
     */
    public static WebCustomerEntity login(String customerID, String password) throws WebUnexpectedException, WebParamException {
        // パスワードをSHA-256でハッシュ化
        String sha256HashedPassword = HashUtil.sha256(password);

        // SHA-256でハッシュ化されたパスワードをBCryptPasswordEncoderでエンコード
        String bcryptHashedPassword = passwordEncoder.encode(sha256HashedPassword);

        // DBアクセス（ログイン判定情報取得）
        WebCustomerEntity webCustomer = webCustomerLoginRepository.getLoginInfo(customerID, bcryptHashedPassword);

        if (webCustomer == null) {
            throw new WebParamException(MessageUtil.getMessage(ErrorMessages.INVALID_CREDENTIALS));
        }

        return webCustomer;
    }
}