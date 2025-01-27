// 数値変換処理
function getNumericAttribute(element, attributeName, defaultValue) {
    if (!element) {
        return defaultValue;
    }
    const value = element.getAttribute(attributeName);
    const numericValue = parseInt(value, 10);
    return isNaN(numericValue) ? defaultValue : numericValue;
}