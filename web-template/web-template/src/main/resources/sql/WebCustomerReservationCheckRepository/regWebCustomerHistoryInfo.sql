INSERT INTO M_WebCustomerRecords 
(customerID, bookLoanID, logicDelFlg, createBy, createTime, updateBy, updateTime)
VALUES (
    (/*[# th:each="customerId : ${customerIds}"]*/
        /*[# mb:p="customerId"]*/ '1' /*[/]*/
    /*[(${customerIdStat.last} ? '' : ',')]*/
    /*[/]*/), -- 利用者ID
    (/*[# th:each="bookLoanId : ${bookLoanIds}"]*/
        /*[# mb:p="bookLoanId"]*/ '1' /*[/]*/
    /*[(${bookLoanIdStat.last} ? '' : ',')]*/
    /*[/]*/), -- 図書貸出ID
    '0', -- 削除フラグ
    '9999', -- 作成者(貸出担当者ID)
    FORMAT(GETDATE(),'YYYY-MM-DD HH:mm:ss'), -- 作成日時
    NULL, -- 更新者
    NULL -- 更新日時
);