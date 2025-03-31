package jp.co.benesse.web.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import jp.co.benesse.web.dto.BookDataDTO;
import jp.co.benesse.web.entity.BookData;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.repository.WebCustomerReservationCheckRepository;

/**
 * <pre>
 * 貸出予約確認画面サービス
 *
 * 作成日：2025/03/27
 * 更新日：
 * </pre>
 *
 * @auther bc)maeda
 * @version 1.0
 */
@Service
public class WebCustomerReservationCheckService {

    /** 貸出予約確認リポジトリ */
    @Autowired
    private WebCustomerReservationCheckRepository webCustomerReservationCheckRepository;

    /**
     * 貸出状況取得と在庫数設定
     * 
     * @param bookDataList
     * @return 在庫数を設定した図書貸出状況一覧DTO
     * @throws WebUnexpectedException
     */
    public List<BookDataDTO> getBookLoanInfoWithStock(List<String> bookDataList) throws WebUnexpectedException {

        // 貸出状況取得
        List<BookData> bookList = webCustomerReservationCheckRepository.findLoanStatusInfo(bookDataList);

        // 在庫数計算
        Map<String, Long> stockCountMap = calculateStock(bookList);

        // EntityからDTOに詰め替え
        List<BookDataDTO> bookDataDTOListResult = bookList.stream()
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
        bookDataDTOListResult.sort(Comparator.comparing(dto -> {
            String bookID = dto.getBookID();
            return bookID.isEmpty() ? Long.MAX_VALUE : Long.parseLong(bookID);
        }));

        return bookDataDTOListResult;
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
     * <pre>
     * 貸出可能書籍判定メソッド
     * 
     * 在庫数が1以上の書籍を抽出し、貸出可能な書籍リストを作成して返す
     * </pre>
     * 
     * @param BookDataDTOList 書籍情報リスト
     * @return 貸出可能書籍一覧
     */
    public List<BookDataDTO> getReservatableBooks(List<BookDataDTO> BookDataDTOList) {
        if (CollectionUtils.isEmpty(BookDataDTOList)) {
            return List.of();
        }

        // 在庫数が1以上の書籍を抽出
        return BookDataDTOList.stream()
                .filter(book -> book.getStockCount() > 0)
                .collect(Collectors.toList());
    }

    /**
     * 貸出情報をDBに登録する
     * 
     * @param customerId 顧客ID
     * @param confirmBookList 貸出リクエストリスト
     * @throws WebUnexpectedException
     */
    @Transactional
    public void registerLoanInfo(String customerId, List<BookDataDTO> confirmBookList)
            throws WebUnexpectedException {
        // 貸出情報登録し、自動発番された図書貸出IDを取得
        int bookLoanID = webCustomerReservationCheckRepository.regLoanInfo(customerId);

        // 貸出明細情報登録
        for (BookDataDTO bookRequest : confirmBookList) {
            webCustomerReservationCheckRepository.regLoanDetail(bookLoanID,
                    bookRequest.getLibraryBookID());
        }

        // web利用者履歴情報登録
        webCustomerReservationCheckRepository.regWebCustomerHistoryInfo(bookLoanID, customerId);
    }
}