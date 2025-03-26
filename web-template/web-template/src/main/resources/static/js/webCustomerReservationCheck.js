document.addEventListener('DOMContentLoaded', function () {
    const tableBody = document.getElementById('book-row-block'); // `book-row-block`をターゲットに設定

    // テーブルに書籍情報を表示
    function renderTable() {
        // テーブルの内容をクリア
        tableBody.innerHTML = '';

        // 書籍情報をループして表示
        bookList.forEach((item, index) => {
            const row = document.createElement('tr'); // 新しい行を作成

            // 書籍情報を設定
            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${item.title}</td>
                <td>${item.author}</td>
            `;

            // 行をテーブルに追加
            tableBody.appendChild(row);
        });
    }

    // 初期化処理
    function init() {
        renderTable(); // 書籍情報を表示
    }

    // 初期化を実行
    init();
});