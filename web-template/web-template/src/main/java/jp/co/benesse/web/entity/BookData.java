package jp.co.benesse.web.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 書籍情報（M_MstBookData）エンティティ
 *
 * 作成日：2025/01/21
 * 更新日：2025/01/21
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

    /** タイトル */
    private String title;

    /** 著者 */
    private String author;

    /** 論理削除フラグ */
    private boolean logicDelFlg;
}