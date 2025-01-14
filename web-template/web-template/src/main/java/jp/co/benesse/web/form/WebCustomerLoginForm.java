package jp.co.benesse.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jp.co.benesse.web.constants.CommonConstants;
import jp.co.benesse.web.constants.ErrorMessages;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * web利用者ログインフォーム
 *
 * 作成日：2024/12/20
 * 更新日：2025/1/14
 * </pre>
 *
 * @author bc)maeda
 * @version 1.0
 */
@Getter
@Setter
public class WebCustomerLoginForm {

    /** 利用者ID */
    @NotBlank(message = ErrorMessages.REQUIRED)
    @Size(min = 8, max = 16, message = ErrorMessages.LOGIN_LENGTH)
    @Pattern(regexp = CommonConstants.HALF_ALPHANUMERIC, message = ErrorMessages.LOGIN_FORMAT)
    private String customerID;

    /** パスワード */
    @NotBlank(message = ErrorMessages.REQUIRED)
    @Size(min = 8, max = 16, message = ErrorMessages.LOGIN_LENGTH)
    @Pattern(regexp = CommonConstants.HALF_ALPHANUMERIC, message = ErrorMessages.LOGIN_FORMAT)
    private String password;
}