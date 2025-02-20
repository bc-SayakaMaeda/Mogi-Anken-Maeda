package jp.co.benesse.web.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.benesse.web.config.PaginationProperties;
import jp.co.benesse.web.dto.BookDataDTO;
import jp.co.benesse.web.entity.BookData;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.repository.WebCustomerMenuRepository;
import jp.co.benesse.web.util.LogUtil;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * メニュー画面サービス
 *
 * 作成日：2025/01/21
 * 更新日：2025/02/20
 * </pre>
 *
 * @auther bcｓ)maeda
 * @version 1.0
 */
@Service
public class WebCustomerMenuService {

    /** ｓ メニューリポジトリ */
    @Autowired
    private WebCustomerMenuRepository webCustomerMenuRepository;

    /** ページングプロパティ */
    @Autowired
    private PaginationProperties paginationProperties;

    /**
     * 図書一覧取得と在庫数設定
     * 
     * @return 在庫数を設定した図書一覧DTO
     * @throws WebUnexpectedException
     */
    public List<BookDataDTO> getBookListWithStock() throws WebUnexpectedException {
        try {
            // 図書一覧を取得
            List<BookData> bookList = webCustomerMenuRepository.findAllBooks();

            // entityからdtoに詰め替え
            List<BookDataDTO> bookDataDTOList = bookList.stream()
                    .map(book -> {
                        BookDataDTO dto = new BookDataDTO();
                        BeanUtils.copyProperties(book, dto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            // 在庫数を計算
            Map<String, Long> stockCountMap = calculateStock(bookList);

            // 在庫数をDTOに設定
            bookDataDTOList.forEach(dto -> {
                dto.setStockCount(stockCountMap.getOrDefault(dto.getBookID(), 0L).intValue());
            });

            return bookDataDTOList;

        } catch (WebUnexpectedException e) {
            String errorMessage = MessageUtil.getMessage("図書一覧の取得に失敗しました");
            LogUtil.infoDetail(errorMessage, e);
            throw new WebUnexpectedException(errorMessage, e);
        }
    }

    /**
     * 書籍IDごとの在庫数算出
     * 
     * @param bookList 図書一覧
     * @return 在庫数マップ
     */
    private Map<String, Long> calculateStock(List<BookData> bookList) {
        // bookListがnullの場合は空のマップを返す
        if (bookList == null) {
            return Collections.emptyMap();
        }

        // 貸出中の書籍をリストとして保持
        List<String> loanedBookIDs = bookList.stream()
                .filter(BookData::isLoanFlg)
                .map(BookData::getLibraryBookID)
                .collect(Collectors.toList());

        // 書籍IDごとの在庫数を計算
        return bookList.stream()
                .filter(book -> !loanedBookIDs.contains(book.getLibraryBookID()))
                .collect(Collectors.groupingBy(BookData::getBookID, Collectors.counting()));
    }

    /**
     * ページング機能総ページ数算出メソッド
     * 
     * @param totalBooks 総書籍数
     * @return ページング機能総ページ数
     */
    public int calculateTotalPages(int totalBooks) {
        return (int) Math.ceil((double) totalBooks / paginationProperties.getRowsPerPage());
    }
}