package jp.co.benesse.web.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.benesse.web.entity.BookData;
import jp.co.benesse.web.repository.WebCustomerMenuRepository;

/**
 * <pre>
 * メニュー画面サービス
 *
 * 作成日：2025/01/21
 * 更新日：2025/01/21
 * </pre>
 *
 * @auther bc)maeda
 * @version 1.0
 */
@Service
public class WebCustomerMenuService {

    /** レポジトリ */
    @Autowired
    private WebCustomerMenuRepository webCustomerMenuRepository;

    /**
     * 図書一覧取得
     * 
     * @return 図書一覧
     */
    public List<BookData> getBookList() {
        return webCustomerMenuRepository.findAllBooks();
    }

    /**
     * 在庫数算出
     * 
     * @param bookList 図書一覧
     * @return 在庫数マップ
     */
    public Map<String, Long> calculateStock(List<BookData> bookList) {
        // 貸出中の書籍をフィルタリング
        List<String> loanedBookIDs = bookList.stream()
                .filter(BookData::isLoanFlag)
                .map(BookData::getLibraryBookID)
                .collect(Collectors.toList());

        // 在庫数を計算
        Map<String, Long> stockCountMap = bookList.stream()
                .filter(book -> !loanedBookIDs.contains(book.getLibraryBookID()))
                .collect(Collectors.groupingBy(BookData::getBookID, Collectors.counting()));

        return stockCountMap;
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
}