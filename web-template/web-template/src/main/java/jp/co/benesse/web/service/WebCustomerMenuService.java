package jp.co.benesse.web.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * 更新日：2025/02/12
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

            // DTOのリストを作成
            List<BookDataDTO> bookDataDTOList = bookList.stream()
                    .collect(Collectors.toMap(BookData::getBookID, book -> {
                        BookDataDTO dto = new BookDataDTO();
                        dto.setBookID(book.getBookID());
                        dto.setLibraryBookID(book.getLibraryBookID());
                        dto.setTitle(book.getTitle());
                        dto.setAuthor(book.getAuthor());
                        dto.setLoanFlg(book.isLoanFlg());
                        dto.setStockCount(
                                stockCountMap.getOrDefault(book.getBookID(), 0L).intValue());
                        return dto;
                    },
                            (existing, replacement) -> existing))
                    .values().stream().collect(Collectors.toList());

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
     * @return 在庫数を設定した図書一覧DTO
     * @throws WebUnexpectedException
     */
    public List<BookRequestDTO> getBookListByIdWithStock() throws WebUnexpectedException {
        try {
            // 書籍ID指定図書一覧を取得
            List<BookData> selectBookList = webCustomerMenuRepository.findSelectBooks();

            // 在庫数を計算
            Map<String, Long> stockCountMap = calculateStock(selectBookList);

            // DTOのリストを作成
            List<BookRequestDTO> bookDataDTOList = selectBookList.stream()
                    .collect(Collectors.toMap(BookData::getBookID, book -> {
                        BookRequestDTO dto = new BookRequestDTO();
                        dto.setBookID(book.getBookID());
                        dto.setTitle(book.getTitle());
                        dto.setAuthor(book.getAuthor());
                        dto.setStockCount(
                                stockCountMap.getOrDefault(book.getBookID(), 0L).intValue());
                        return dto;
                    },
                            (existing, replacement) -> existing))
                    .values().stream().collect(Collectors.toList());

            return bookDataDTOList;

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
     * @return 在庫数マップ
     */
    private Map<String, Long> calculateStock(List<BookData> bookList) {
        // 貸出中の書籍をフィルタリング
        List<String> loanedBookIDs = bookList.stream()
                .filter(BookData::isLoanFlg)
                .map(BookData::getLibraryBookID)
                .collect(Collectors.toList());

        // 在庫数を計算
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
        return (int) Math.ceil((double) totalBooks / 10);
    }

    /**
     * <pre>
     * 貸出可能書籍判定メソッド
     * 
     * 在庫数が1以上の書籍を抽出し、貸出可能な書籍リストを作成して返す
     * 1. 入力として渡された書籍リストをストリームに変換
     * 2. 在庫数が1以上の書籍をフィルタリング
     * 3. フィルタリング結果をリスト型に変換
     * 4. 貸出可能な書籍リストを返却
     * </pre>
     * 
     * @param bookDataDTOList 書籍情報リスト
     * @return 貸出可能書籍一覧（List<BookRequestDTO>）
     */
    public List<BookRequestDTO> getReservatableBooks(List<BookRequestDTO> bookDataDTOList) {
        if (bookDataDTOList == null || bookDataDTOList.isEmpty()) {
            return List.of();
        }

        // 在庫数が1以上の書籍を抽出
        return bookDataDTOList.stream()
                .filter(book -> book.getStockCount() > 0)
                .collect(Collectors.toList());
    }
}