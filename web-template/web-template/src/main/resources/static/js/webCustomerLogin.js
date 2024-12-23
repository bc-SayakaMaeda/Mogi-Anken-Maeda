import { validateRequiredFields, validateHalfAlphanumeric } from './validation.js';
import { IMAGE_PATHS } from './constants.js';

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('webCustomerLoginForm');
    const customerIDInput = document.getElementById('customerID');
    const passwordInput = document.getElementById('password');
    const errorMessageDiv = document.querySelector('.error-message');
    const togglePassword = document.getElementById('togglePassword');

    // 初期表示のアイコン設定
    togglePassword.src = IMAGE_PATHS.ICON_EYE_HIDE;

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        let isValid = true;
        errorMessageDiv.innerHTML = '';
        
        // 必須チェック
        let errorMessage = validateRequiredField(customerIDInput);
        if (!errorMessage) {
            errorMessage = validateRequiredField(passwordInput);
        }
        if (errorMessage) {
            errorMessageDiv.innerHTML = errorMessage;
            errorMessageDiv.style.visibility = 'visible';
            isValid = false;
        }
        
        // 必須チェックに問題がない場合にフォーマットチェックを行う
        if (isValid) {
            // フォーマットチェック（半角英数字）
            const halfAlphanumericRegex = /^[a-zA-Z0-9]*$/;
            errorMessage = validateFieldFormat(customerIDInput, halfAlphanumericRegex, MESSAGES.LOGIN_FORMAT);
            if (!errorMessage) {
                errorMessage = validateFieldFormat(passwordInput, halfAlphanumericRegex, MESSAGES.LOGIN_FORMAT);
            }
            if (errorMessage) {
                errorMessageDiv.innerHTML = errorMessage;
                errorMessageDiv.style.visibility = 'visible';
                isValid = false;
            }
        }
        
        // 入力チェックに問題がない場合フォームを送る
        if (isValid) {
            form.submit();
        }
    });
            
    // パスワード表示機能
    togglePassword.addEventListener('click', function() {
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);
        this.src = type === 'password' ? IMAGE_PATHS.ICON_EYE_HIDE : IMAGE_PATHS.ICON_EYE_SHOW;
    });
    passwordInput.addEventListener('input', function() {
        if (passwordInput.value) {
            togglePassword.style.display = 'block';
        } else {
            togglePassword.style.display = 'none';
        }
    });
});