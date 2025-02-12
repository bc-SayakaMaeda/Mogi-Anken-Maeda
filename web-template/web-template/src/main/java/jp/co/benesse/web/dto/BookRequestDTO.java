package jp.co.benesse.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 貸出可能書籍情報DTO
 *
 * 作成日：2025/02/12
 * 更新日：2025/02/12
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Getter
@Setter
public class BookRequestDTO {

    /** 書籍ID */
    private String bookID;

    /** タイトル */
    private String title;

    /** 著者 */
    private String author;

    /** 在庫数 */
    private int stockCount;
}
