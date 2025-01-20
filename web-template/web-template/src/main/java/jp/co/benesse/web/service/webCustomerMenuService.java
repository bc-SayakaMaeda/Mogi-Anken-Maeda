package jp.co.benesse.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class webCustomerMenuService {

    @Autowired
    private BookRepository bookRepository;

    public List<BookDTO> getBookList() {
        return bookRepository.findAllBooks();
    }

    public void calculateStock(List<BookDTO> bookList) {
        for (BookDTO book : bookList) {
            int stockCount = bookRepository.countStock(book.getBookID());
            book.setStockCount(stockCount);
        }
    }

    public int calculateTotalPages(int totalBooks) {
        return (int) Math.ceil((double) totalBooks / 10);
    }
}
