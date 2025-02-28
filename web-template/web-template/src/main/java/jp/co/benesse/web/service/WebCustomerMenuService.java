package jp.co.benesse.web.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jp.co.benesse.web.config.PaginationProperties;
import jp.co.benesse.web.dto.BookDataDTO;
import jp.co.benesse.web.dto.BookRequestDTO;
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
 * @auther bc)maeda
 * @version 1.0
 */
@Service
public class WebCustomerMenuService {

    /** メニューリポジトリ */
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

            // 在庫数を計算
            Map<String, Long> stockCountMap = calculateStock(bookList);

            // entityからdtoに詰め替え
            List<BookDataDTO> bookDataDTOList = bookList.stream()
                    .collect(Collectors.groupingBy(BookData::getBookID))
                    .entrySet().stream()
                    .map(entry -> {
                        BookDataDTO dto = new BookDataDTO();
                        BookData book = entry.getValue().get(0);
                        BeanUtils.copyProperties(book, dto);
                        dto.setStockCount(stockCountMap.getOrDefault(book.getBookID(), 0L).intValue());
                        return dto;
                    })
                    .collect(Collectors.toList());

            // bookIDで昇順ソート（数値として比較）
            bookDataDTOList.sort(Comparator.comparing(dto -> {
                String bookID = dto.getBookID();
                return bookID.isEmpty() ? Long.MAX_VALUE : Long.parseLong(bookID);
            }));

            return bookDataDTOList;

        } catch (WebUnexpectedException e) {
            String errorMessage = MessageUtil.getMessage("図書一覧の取得に失敗しました");
            LogUtil.infoDetail(errorMessage, e);
            throw new WebUnexpectedException(errorMessage, e);
        }
    }

    /**
     * 図書一覧取得（書籍ID指定）と在庫数設定
     * 
     * @param bookIds 貸出希望書籍IDリスト
     * @return 在庫数を設定した図書一覧DTO
     * @throws WebUnexpectedException
     */
    public List<BookRequestDTO> getBookListByIdWithStock(List<String> bookIds) throws WebUnexpectedException {
        try {
            // 書籍ID指定図書一覧取得
            List<BookData> selectBookList = webCustomerMenuRepository.findSelectBooks(bookIds);

            // 在庫数を計算
            Map<String, Long> stockCountMap = calculateStock(selectBookList);

            // 書籍IDごとにグループ化し、DTOに詰め替え
            return selectBookList.stream()
                    .collect(Collectors.groupingBy(BookData::getBookID))
                    .entrySet().stream()
                    .map(entry -> {
                        BookRequestDTO dto = new BookRequestDTO();
                        BookData book = entry.getValue().get(0);
                        BeanUtils.copyProperties(book, dto);
                        dto.setStockCount(stockCountMap.getOrDefault(book.getBookID(), 0L).intValue());
                        return dto;
                    })
                    .collect(Collectors.toList());

        } catch (WebUnexpectedException e) {
            String errorMessage = MessageUtil.getMessage("図書一覧の取得に失敗しました");
            LogUtil.infoDetail(errorMessage, e);
            throw new WebUnexpectedException(errorMessage, e);
        }
    }

    /**
     * 在庫数算出
     * 
     * @param bookList 図書一覧
     * @return 在庫数マップ キー：BookID、値：在庫数
     */
    private Map<String, Long> calculateStock(List<BookData> bookList) {
        // bookListがnullまたは空の場合は空のマップを返す
        if (CollectionUtils.isEmpty(bookList)) {
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

    /**
     * <pre>
     * 貸出可能書籍判定メソッド
     * 
     * 在庫数が1以上の書籍を抽出し、貸出可能な書籍リストを作成して返す
     * </pre>
     * 
     * @param bookRequestDTOList 書籍情報リスト
     * @return 貸出可能書籍一覧
     */
    public List<BookRequestDTO> getReservatableBooks(List<BookRequestDTO> bookRequestDTOList) {
        if (CollectionUtils.isEmpty(bookRequestDTOList)) {
            return List.of();
        }

        // 在庫数が1以上の書籍を抽出
        return bookRequestDTOList.stream()
                .filter(book -> book.getStockCount() > 0)
                .collect(Collectors.toList());
    }
}