INSERT INTO M_WebCustomerRecords 
(customerID, bookLoanID, logicDelFlg, createBy, createTime, updateBy, updateTime)
VALUES (
    :customerId, -- 利用者ID
    :bookLoanID, -- 図書貸出ID
    '0', -- 削除フラグ
    '9999', -- 作成者(貸出担当者ID)
    GETDATE(), -- 作成日時
    NULL, -- 更新者
    NULL -- 更新日時
);