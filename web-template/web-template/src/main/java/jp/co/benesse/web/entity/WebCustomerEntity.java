package jp.co.benesse.web.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * WebCustomerエンティティ
 *
 * 作成日：2024/12/24
 * 更新日：2024/12/24
 * </pre>
 * 
 * @author BC)maeda
 * @version 1.0
 */
@Getter
@Setter
public class WebCustomerEntity {

    /** 利用者ID */
    private String customerId;

    /** 利用者氏名 */
    private String customerName;

    /** 郵便番号 */
    private String postCode;

    /** 住所 */
    private String address;

    /** 電話番号 */
    private String telephone;

    /** メールアドレス */
    private String email;

    /** パスワード */
    private String password;

    /** 論理削除フラグ */
    private String logicDelFlg;

    /** 作成者 */
    private String createBy;

    /** 作成日時 */
    private LocalDateTime createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新日時 */
    private LocalDateTime updateTime;
}