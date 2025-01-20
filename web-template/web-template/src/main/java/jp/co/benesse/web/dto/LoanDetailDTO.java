package jp.co.benesse.web.dto;

@Entity
public class LoanDetail {
    @Id
    private String libraryBookID;
    private boolean returnFlg;
    private boolean logicDelFlg;
    // getters and setters
}