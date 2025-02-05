package jp.co.benesse.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jp.co.benesse.web.constants.CommonConstants;
import jp.co.benesse.web.validationGroups.ValidationGroups.FormatCheck;
import jp.co.benesse.web.validationGroups.ValidationGroups.LengthCheck;
import jp.co.benesse.web.validationGroups.ValidationGroups.RequiredCheck;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * web利用者ログインフォーム
 *
 * 作成日：2024/12/20
 * 更新日：2025/01/22
 * </pre>
 *
 * @author bc)maeda
 * @version 1.0
 */
@Getter
@Setter
public class WebCustomerLoginForm {

    /** 利用者ID */
    @NotBlank(groups = RequiredCheck.class)
    @Size(groups = LengthCheck.class, min = 8, max = 16)
    @Pattern(groups = FormatCheck.class, regexp = CommonConstants.HALF_ALPHANUMERIC)
    private String customerID;

    /** パスワード */
    @NotBlank(groups = RequiredCheck.class)
    @Size(groups = LengthCheck.class, min = 8, max = 16)
    @Pattern(groups = FormatCheck.class, regexp = CommonConstants.HALF_ALPHANUMERIC)
    private String password;
}