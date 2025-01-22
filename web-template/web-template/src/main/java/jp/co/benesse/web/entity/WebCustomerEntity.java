package jp.co.benesse.web.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * web利用者ログインエンティティ
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

    /** メールアドレス */
    private String email;
}