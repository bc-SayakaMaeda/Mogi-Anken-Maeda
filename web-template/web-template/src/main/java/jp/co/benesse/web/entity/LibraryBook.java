package jp.co.benesse.web.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 図書館書籍情報（M_MstLibraryBook）エンティティ
 *
 * 作成日：2025/01/21
 * 更新日：2025/01/21
 * </pre>
 *
 * @version 1.0
 */
@Getter
@Setter
public class LibraryBook {

    /** 図書館書籍ID */
    private int libraryBookID;

    /** 書籍ID */
    private int bookID;

    /** 論理削除フラグ */
    private boolean logicDelFlg;
}