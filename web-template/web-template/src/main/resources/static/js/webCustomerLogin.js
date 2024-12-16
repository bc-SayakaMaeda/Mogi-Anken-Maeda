document.addEventListener('DOMContentLoaded', function() {
	const form = document.getElementById('webCustomerLoginForm');
	const userIdInput = document.getElementById('userId');
	const passwordInput = document.getElementById('password');
	const errorMessageDiv = document.querySelector('.error-message');
	const loginButton = document.getElementById('webCustomerLoginButton');

	form.addEventListener('submit', function(event) {
		event.preventDefault();
		let isValid = true;
		errorMessageDiv.innerHTML = '';

		// 必須チェック
		if (!userIdInput.value || !passwordInput.value) {
			errorMessageDiv.innerHTML = '入力必須項目です。';
			isValid = false;
		}

		// 文字列長チェック
		// 入力欄の最大入力文字数が16文字のため実際に表示されることはない
		if (userIdInput.value.length > 16 || passwordInput.value.length > 16) {
			errorMessageDiv.innerHTML = 'ID、パスワードは16文字以下です。';
			isValid = false;
		}

		// フォーマットチェック
        const alphanumericRegex = /^[a-zA-Z0-9]*$/;
        if (!alphanumericRegex.test(userIdInput.value) || !alphanumericRegex.test(passwordInput.value)) {
            errorMessageDiv.innerHTML = '入力形式が間違っています。';
            isValid = false;
        }

		if (isValid) {
			form.submit(); // フォームを送信
		}
	});
});