document.addEventListener('DOMContentLoaded', function() {
    const tableBody = document.getElementById('book-row-block');

    // テーブルに書籍情報を表示
    function renderTable() {

        tableBody.innerHTML = '';

        bookList.forEach((item, index) => {
            const row = document.createElement('tr');

            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${item.title}</td>
                <td>${item.author}</td>
                <td>${item.stockCount}</td>
                <td>
                    <input type="checkbox" class="large-checkbox" checked>
                </td>      
            `;
            tableBody.appendChild(row);
        });
    }

    // 初期化処理
    function init() {
        renderTable();
    }

    init();
});