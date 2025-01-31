package jp.co.benesse.web.form;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * web利用者ログインフォーム
 *
 * 作成日：2024/12/20
 * 更新日：2024/12/20
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Getter
@Setter
public class WebCustomerLoginForm {
    
    /** 利用者ID */
    private String customerID;
    
    /** パスワード */
    private String password;
}