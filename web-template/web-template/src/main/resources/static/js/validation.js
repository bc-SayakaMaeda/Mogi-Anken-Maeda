// 必須チェック
function validateRequiredField(value, errorMessage = MESSAGES.REQUIRED) {
    if (typeof value !== 'string' || value.trim() === '') {
        return errorMessage;
    }
    return null;
}

// フォーマットチェック
function validateFieldFormat(value, regex, errorMessage) {
    if (typeof value !== 'string' || !regex.test(value)) {
        return errorMessage;
    }
    return null;
}