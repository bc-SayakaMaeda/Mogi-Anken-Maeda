package jp.co.benesse.web.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 書籍情報エンティティ
 *
 * 作成日：2025/01/21
 * 更新日：2025/02/05
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Getter
@Setter
public class BookData {

    /** 書籍ID */
    private String bookID;

    /** 図書ID */
    private String libraryBookID;

    /** タイトル */
    private String title;

    /** 著者 */
    private String author;

    /** 貸出フラグ */
    private boolean loanFlg;
}