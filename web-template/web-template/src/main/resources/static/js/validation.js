// 必須チェック
function validateRequiredFields(fields, errorMessage = MESSAGES.REQUIRED) {
    for (let field of fields) {
        if (!field.value) {
            return errorMessage;
        }
    }
    return null;
}

// フォーマットチェック（半角英数字）
function validateHalfAlphanumeric(fields, errorMessage = MESSAGES.HALF_ALPHANUMERIC) {
    const halfAlphanumericRegex = /^[a-zA-Z0-9]*$/;
    for (let field of fields) {
        if (!halfAlphanumericRegex.test(field.value)) {
            return errorMessage;
        }
    }
    return null;
}