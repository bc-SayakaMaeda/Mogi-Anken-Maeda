SELECT 
    bookData.bookID,
    libraryBook.libraryBookID,
    bookData.title,
    bookData.author,
    CASE 
        WHEN loanDetail.returnFlg = '0' -- 未返却
        AND loanDetail.logicDelFlg = '0' -- 削除なし
        THEN 1 -- 貸出中
        ELSE 0 -- 在庫
    END AS loanFlag
FROM 
    M_MstBookData AS bookData
LEFT JOIN M_MstLibraryBook AS libraryBook
    ON bookData.bookID = libraryBook.bookID
LEFT JOIN M_BookLoanRecordsDetail AS loanDetail
    ON libraryBook.libraryBookID = loanDetail.libraryBookID
WHERE 
    bookData.logicDelFlg = '0' -- 削除なし
    AND libraryBook.logicDelFlg = '0' -- 削除なし
ORDER BY 
    bookData.bookID; -- bookIDで昇順ソート
