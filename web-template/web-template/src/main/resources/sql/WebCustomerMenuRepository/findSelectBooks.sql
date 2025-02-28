SELECT 
    bookData.bookID,
    libraryBook.libraryBookID,
    bookData.title,
    bookData.author,
    CASE 
        WHEN loanDetail.returnFlg = '0' -- 貸出中
        AND loanDetail.logicDelFlg = '0' -- 削除なし
        THEN 1
        ELSE 0 
    END AS loanFlg
FROM 
    M_MstBookData AS bookData
LEFT JOIN M_MstLibraryBook AS libraryBook
    ON bookData.bookID = libraryBook.bookID
LEFT JOIN M_BookLoanRecordsDetail AS loanDetail
    ON libraryBook.libraryBookID = loanDetail.libraryBookID
WHERE 
    bookData.logicDelFlg = '0' -- 削除なし
    AND libraryBook.logicDelFlg = '0' -- 削除なし
    AND bookData.bookID IN 
    (/*[# th:each="bookId : ${bookIds}"]*/
            /*[# mb:p="bookId"]*/ '1' /*[/]*/
        /*[(${bookIdStat.last} ? '' : ',')]*/
        /*[/]*/)