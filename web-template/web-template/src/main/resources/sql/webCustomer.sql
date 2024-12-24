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
    webCust.customerID = :customerID
AND 
    webCust.password = :hashedPassword
AND 
    webCust.logicDelFlg = '0'
AND 
    cust.logicDelFlg = '0';