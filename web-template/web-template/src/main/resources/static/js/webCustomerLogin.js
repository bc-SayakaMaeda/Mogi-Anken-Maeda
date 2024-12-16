document.addEventListener('DOMContentLoaded', function() {
	const form = document.getElementById('webCustomerLoginForm');
	const userIdInput = document.getElementById('userId');
	const passwordInput = document.getElementById('password');
	const errorMessageDiv = document.querySelector('.error-message');
	const togglePassword = document.getElementById('togglePassword');

	form.addEventListener('submit', function(event) {
		event.preventDefault();
		let isValid = true;
		errorMessageDiv.innerHTML = '';

		// 必須チェック
		if (!userIdInput.value || !passwordInput.value) {
			errorMessageDiv.innerHTML = '入力必須項目です。';
			errorMessageDiv.style.visibility = 'visible'; 
			isValid = false;
		}
		
		// フォーマットチェック
        const alphanumericRegex = /^[a-zA-Z0-9]*$/;
        if (!alphanumericRegex.test(userIdInput.value) || !alphanumericRegex.test(passwordInput.value)) {
            errorMessageDiv.innerHTML = '入力形式が間違っています。';
            errorMessageDiv.style.visibility = 'visible'; 
            isValid = false;
        }

		//入力チェックに問題がない場合ファームを送る
		if (isValid) {
			form.submit(); 
		}
	});


	//パスワード表示機能
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