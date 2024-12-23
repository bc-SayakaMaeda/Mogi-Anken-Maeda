import { MESSAGES } from './constants.js';

// 必須チェック
function validateRequiredField(field, errorMessage = MESSAGES.REQUIRED) {
    if (!field.value) {
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

export { validateRequiredField, validateFieldFormat };