// 数値変換処理
function toNumeric(value, defaultValue) {
    const numericValue = parseInt(value, 10);
    return isNaN(numericValue) ? defaultValue : numericValue;
}