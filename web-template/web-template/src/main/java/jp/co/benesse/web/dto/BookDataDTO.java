package jp.co.benesse.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 書籍情報DTO
 *
 * 作成日：2025/01/21
 * 更新日：2025/02/21
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Getter
@Setter
public class BookDataDTO extends BookRequestDTO {

    /** 図書ID */
    private String libraryBookID;

    /** 貸出フラグ */
    private boolean loanFlg;

}