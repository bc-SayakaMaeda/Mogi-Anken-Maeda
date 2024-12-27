// 必須チェック
function validateRequiredField(field, errorMessage = MESSAGES.REQUIRED) {
    if (field == null || !field.value) {
        return errorMessage;
    }
    return null;
}

// フォーマットチェック
function validateFieldFormat(field, regex, errorMessage) {
    if (!regex.test(field.value)) {
        return errorMessage;
    }
    return null;
}