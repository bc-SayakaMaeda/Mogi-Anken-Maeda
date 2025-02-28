package jp.co.benesse.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * ページング設定管理
 * 
 * 
 * 作成日：2025/02/20
 * 更新日：2025/02/20
 * </pre>
 *
 * @author BC)maeda
 * @version 1.0
 */
@Component
@ConfigurationProperties(prefix = "pagination")
public class PaginationProperties {

    /** 1ページあたりの表示件数 */
    private int rowsPerPage;

    /**
     * 表示件数取得
     *
     * @return rowsPerPage 1ページあたりの表示件数
     */
    public int getRowsPerPage() {
        return rowsPerPage;
    }

    /**
     * 表示件数設定
     *
     * @param rowsPerPage 1ページあたりの表示件数
     */
    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }
}