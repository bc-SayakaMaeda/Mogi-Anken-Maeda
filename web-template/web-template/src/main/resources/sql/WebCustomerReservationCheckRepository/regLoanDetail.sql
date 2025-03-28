INSERT INTO M_BookLoanRecordsDetail 
(bookLoanID, libraryBookID, returnFlg, returnDateTime, memo, logicDelFlg, createBy, createTime, updateBy, updateTime)
VALUES (
    :bookLoanID, -- 図書貸出ID
    :libraryBookID, -- 貸出図書ID
    '0', -- 未返却フラグ
    NULL, -- 返却日時
    NULL, -- 返却メモ
    '0', -- 削除フラグ
    '9999', -- 作成者(貸出担当者ID)
    GETDATE(), -- 作成日時
    NULL, -- 更新者
    NULL -- 更新日時
);