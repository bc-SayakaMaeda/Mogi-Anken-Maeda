package jp.co.benesse.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.benesse.web.entity.WebCustomerEntity;
import jp.co.benesse.web.exception.WebParamException;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.repository.WebCustomerLoginRepository;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * web利用者ログインサービス
 *
 * 作成日：2024/12/24
 * 更新日：2024/12/24
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
@Service
public class WebCustomerLoginService {

    /** WebCustomerLoginリポジトリ */
    @Autowired
    private WebCustomerLoginRepository webCustomerLoginRepository;

    /** パスワードエンコーダー */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * ログイン処理
     * 
     * @param customerID 利用者ID
     * @param password パスワード
     * @return WebCustomer ログインユーザー情報
     * @throws WebUnexpectedException
     * @throws WebParamException
     */
    public static WebCustomerEntity login(String customerID, String password) throws WebUnexpectedException, WebParamException {
        // パスワードをハッシュ化
        String hashedPassword = passwordEncoder.encode(password);

        // ログイン判定情報を取得
        WebCustomerEntity webCustomer = webCustomerLoginRepository.getLoginInfo(customerID, hashedPassword);

        if (webCustomer == null) {
            throw new WebParamException(MessageUtil.getMessage("XXXXX-001", "IDまたはパスワード"));
        }

        return webCustomer;
    }
}