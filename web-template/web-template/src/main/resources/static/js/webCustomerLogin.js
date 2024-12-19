document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('webCustomerLoginForm');
    const customerIDInput = document.getElementById('customerID');
    const passwordInput = document.getElementById('password');
    const errorMessageDiv = document.querySelector('.error-message');
    const togglePassword = document.getElementById('togglePassword');
    
    form.addEventListener('submit', function(event) {
        event.preventDefault();
        let isValid = true;
        errorMessageDiv.innerHTML = '';
        // 必須チェック
        let errorMessage = validateRequiredFields([customerIDInput, passwordInput]);
        if (errorMessage) {
            errorMessageDiv.innerHTML = errorMessage;
            errorMessageDiv.style.visibility = 'visible';
            isValid = false;
        }
        
        // フォーマットチェック（半角英数字）
        errorMessage = validateAlphanumeric([customerIDInput, passwordInput]);
        if (errorMessage) {
            errorMessageDiv.innerHTML = errorMessage;
            errorMessageDiv.style.visibility = 'visible'; 
            isValid = false;
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
        this.src = type === 'password' ? '../static/images/icon_eye_hide.png' : '../static/images/icon_eye.png';
    });
    
    passwordInput.addEventListener('input', function() {
        if (passwordInput.value) {
            togglePassword.style.display = 'block'; 
        } else {
            togglePassword.style.display = 'none'; 
        }
    });
});