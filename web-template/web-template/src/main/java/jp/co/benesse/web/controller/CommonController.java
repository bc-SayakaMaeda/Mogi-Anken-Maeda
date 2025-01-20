import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({ "customerID", "customerName" })
public class CommonController {

    @ModelAttribute("customerID")
    public String setCustomerID() {
        return "";
    }

    @ModelAttribute("customerName")
    public String setCustomerName() {
        return "";
    }
}
