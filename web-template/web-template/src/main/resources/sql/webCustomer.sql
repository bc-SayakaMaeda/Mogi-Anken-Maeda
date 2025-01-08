SELECT 
    webCust.customerID,
    cust.customerName,
    cust.postCode,
    cust.address,
    cust.email
FROM 
    M_MstWebCustomer AS webCust
INNER JOIN 
    M_MstCustomer AS cust ON webCust.customerID = cust.customerID
WHERE 
    webCust.customerID = /*[# mb:p="customerID"]*/ 'customerID' /*[/]*/
AND 
    webCust.password = /*[# mb:p="password"]*/ 'password' /*[/]*/
AND 
    webCust.logicDelFlg = '0' -- 削除なし
AND 
    cust.logicDelFlg = '0'; -- 削除なし