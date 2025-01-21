package jp.co.benesse.web.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 貸出記録詳細（M_BookLoanRecordsDetail）エンティティ
 *
 * 作成日：2025/01/21
 * 更新日：2025/01/21
 * </pre>
 *
 * @version 1.0
 */
@Getter
@Setter
public class LoanRecordsDetail {

    /** 図書館書籍ID */
    private int libraryBookID;

    /** 返却フラグ */
    private boolean returnFlg;

    /** 論理削除フラグ */
    private boolean logicDelFlg;
}