document.addEventListener('DOMContentLoaded', function() {
    // Thymeleafから渡されたデータを取得
    const data = /*[[${books}]]*/ []; // サーバーからのデータをここに挿入
    const rowsPerPage = 10;
    let currentPage = 1;
    const totalPages = parseInt(document.querySelector('.webCustomerMenu-container').getAttribute('data-total-pages'), 10);

    function renderTable(page) {
        const tableBody = document.querySelector('#book-table tbody');
        tableBody.innerHTML = '';

        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        const pageData = data.slice(start, end);

        pageData.forEach((item, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${start + index + 1}</td>
                <td>${item.title}</td>
                <td>${item.author}</td>
                <td>${item.stock}</td>
                <td>${item.stock > 0 ? `<input type="checkbox" id="book-${item.id}" name="bookIds" value="${item.id}">` : '貸出中'}</td>
            `;
            tableBody.appendChild(row);
        });

        document.getElementById('page-info').textContent = `${page}/${totalPages}ページ`;

        // 「前」ボタンの表示/非表示を切り替え
        const prevPageButton = document.getElementById('prev-page');
        prevPageButton.style.display = page === 1 ? 'none' : 'inline';

        // 「次」ボタンの表示/非表示を切り替え
        const nextPageButton = document.getElementById('next-page');
        nextPageButton.style.display = page === totalPages ? 'none' : 'inline';
    }

    document.getElementById('prev-page').addEventListener('click', function(event) {
        event.preventDefault();
        if (currentPage > 1) {
            currentPage--;
            renderTable(currentPage);
        }
    });

    document.getElementById('next-page').addEventListener('click', function(event) {
        event.preventDefault();
        if (currentPage < totalPages) {
            currentPage++;
            renderTable(currentPage);
        }
    });

    renderTable(currentPage);
});