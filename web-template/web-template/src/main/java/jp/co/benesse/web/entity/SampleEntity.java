package jp.co.benesse.web.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * サンプルエンティティ
 *
 * 作成日：2024/10/28
 * 更新日：2024/10/28
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Getter
@Setter
public class SampleEntity {

    /** 会員ID */
    private Integer memId;

    /** オプト */
    private String opt;

    /** 論理削除フラグ */
    private String logicDelFlg;

    /** 登録日 */
    private LocalDateTime regDt;

    /** 登録者ID */
    private String regusrId;

    /** 更新日 */
    private LocalDateTime updateDt;

    /** 更新者ID */
    private String updaterId;
}
