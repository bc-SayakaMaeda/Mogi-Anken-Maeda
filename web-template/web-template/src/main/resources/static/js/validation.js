// 必須チェック
function validateRequiredFields(fields) {
    for (let field of fields) {
        if (!field.value) {
            return false;
        }
    }
    return true;
}