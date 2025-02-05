package jp.co.benesse.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 書籍情報DTO
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
public class BookDataDTO {

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

    /** 在庫数 */
    private int stockCount; // 新たに追加するカラム
}