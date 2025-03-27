INSERT INTO M_BookLoanRecords 
(customerID, adminID, loanDateTime, returnFlg, returnDateTime, memo, logicDelFlg, createBy, createTime, updateBy, updateTime)
VALUES (
    (/*[# th:each="customerId : ${customerIds}"]*/
        /*[# mb:p="customerId"]*/ '1' /*[/]*/
    /*[(${customerIdStat.last} ? '' : ',')]*/
    /*[/]*/), -- 利用者ID
    '9999', -- 貸出担当者ID
    FORMAT(GETDATE(),'YYYY-MM-DD HH:mm:ss'), -- 貸出日時
    '0', -- 未返却フラグ
    NULL, -- 返却日時
    NULL, -- 返却メモ
    '0', -- 削除フラグ
    '9999', -- 作成者(貸出担当者ID)
    FORMAT(GETDATE(),'YYYY-MM-DD HH:mm:ss'), -- 作成日時
    NULL, -- 更新者
    NULL -- 更新日時
);