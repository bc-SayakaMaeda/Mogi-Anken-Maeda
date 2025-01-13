import { validateRequiredField, validateFieldFormat } from './validation.js';
import { MESSAGES, IMAGE_PATHS, REGEX } from './constants.js';
    
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('webCustomerLoginForm');
    const customerIDInput = document.getElementById('customerID');
    const passwordInput = document.getElementById('password');
    const errorMessageDiv = document.querySelector('.error-message');
    const togglePassword = document.getElementById('togglePassword');
    

    // 初期表示のアイコン設定
    togglePassword.src = IMAGE_PATHS.ICON_EYE_HIDE;
    
    
    function showError(message) {
        if (message) {
            errorMessageDiv.innerHTML = message;
            errorMessageDiv.style.visibility = 'visible';
        } else {
            errorMessageDiv.style.visibility = 'hidden';
        }
    }
    
    showError(errorMessage);

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        if (errorMessageDiv) {
            errorMessageDiv.innerHTML = '';
            errorMessageDiv.style.visibility = 'hidden';
        }
        // 必須チェック
        let errorMessage = validateRequiredField(customerIDInput.value, MESSAGES.REQUIRED);
        if (errorMessage) {
            showError(errorMessage);
            return;
        }

        errorMessage = validateRequiredField(passwordInput.value, MESSAGES.REQUIRED);
        if (errorMessage) {
            showError(errorMessage);
            return;
        }

        // フォーマットチェック（半角英数字）
        errorMessage = validateFieldFormat(customerIDInput.value, REGEX.HALF_ALPHANUMERIC, MESSAGES.LOGIN_FORMAT);
        if (errorMessage) {
            showError(errorMessage);
            return;
        }

        errorMessage = validateFieldFormat(passwordInput.value, REGEX.HALF_ALPHANUMERIC, MESSAGES.LOGIN_FORMAT);
        if (errorMessage) {
            showError(errorMessage);
            return;
        }

        // 入力チェックに問題がない場合フォームを送る
        form.submit();
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