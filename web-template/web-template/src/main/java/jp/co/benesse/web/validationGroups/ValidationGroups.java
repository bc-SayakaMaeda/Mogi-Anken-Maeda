package jp.co.benesse.web.validationGroups;

/**
 * <pre>
 * バリデーショングループを定義するクラス
 * 異なるバリデーションチェックの順序を制御するためのインターフェイスを実装
 *
 * 作成日：2025/01/16
 * 更新日：2025/01/21
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
public class ValidationGroups {

    /**
     * <pre>
     * 必須チェック用のバリデーショングループインターフェイス
     * </pre>
     */
    public interface RequiredCheck {
    }

    /**
     * <pre>
     * 文字列長チェック用のバリデーショングループインターフェイス
     * </pre>
     */
    public interface LengthCheck {
    }

    /**
     * <pre>
     * フォーマットチェック用のバリデーショングループインターフェイス
     * </pre>
     */
    public interface FormatCheck {
    }
}
