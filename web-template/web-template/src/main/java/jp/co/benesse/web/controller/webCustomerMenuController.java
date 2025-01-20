import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({ "customerID", "customerName", "BookRequestList" })
public class webCustomerMenuController {

    @Autowired
    private BookService bookService;

    @GetMapping("/webCustomerMenu")
    public String showMenu(Model model, @ModelAttribute("customerID") String customerID,
            @ModelAttribute("customerName") String customerName) {
        // 利用者情報の取得
        model.addAttribute("customerID", customerID);
        model.addAttribute("customerName", customerName);

        // 書籍情報の取得
        List<BookDTO> bookRequestList = (List<BookDTO>) model.getAttribute("BookRequestList");
        if (bookRequestList == null) {
            bookRequestList = new ArrayList<>();
        }

        // 図書一覧取得
        List<BookDTO> bookList = bookService.getBookList();

        // 在庫数算出
        bookService.calculateStock(bookList);

        // 総ページ数算出
        int totalPages = bookService.calculateTotalPages(bookList.size());

        model.addAttribute("bookList", bookList);
        model.addAttribute("totalPages", totalPages);

        return "menu";
    }
}
