// 必須チェック
function validateRequiredFields(fields, errorMessage = '入力必須項目です。') {
    for (let field of fields) {
        if (!field.value) {
            return errorMessage;
        }
    }
    return null;
}

// フォーマットチェック（半角英数字）
function validateAlphanumeric(fields, errorMessage = 'IDまたはパスワードの入力が適切ではありません。') {
    const alphanumericRegex = /^[a-zA-Z0-9]*$/;
    for (let field of fields) {
        if (!alphanumericRegex.test(field.value)) {
            return errorMessage;
        }
    }
    return null;
}