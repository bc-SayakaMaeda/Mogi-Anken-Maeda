document.addEventListener('DOMContentLoaded', function() {
    // Thymeleafから渡されたデータを取得
    const data = /*[[${bookList}]]*/ []; // サーバーからのデータをここに挿入
    const rowsPerPage = 10;
    let currentPage = 1;
    const totalPages = parseInt(document.querySelector('.webCustomerMenu-container').getAttribute('data-total-pages'), 10) || 1;

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
                <td>${item.stockCount}</td> <!-- 在庫数を表示 -->
                <td>${item.stockCount > 0 ? `<input type="checkbox" id="book-${item.bookID}" name="bookIds" value="${item.bookID}">` : '貸出中'}</td>
            `;
            tableBody.appendChild(row);
        });

        document.getElementById('page-info').textContent = `${page}/${totalPages}ページ`;

        // 「前」ボタンの表示/非表示を切り替え
        const prevPageButton = document.getElementById('prev-page');
        if (prevPageButton) {
            prevPageButton.style.display = page === 1 ? 'none' : 'inline';
        }

        // 「次」ボタンの表示/非表示を切り替え
        const nextPageButton = document.getElementById('next-page');
        if (nextPageButton) {
            nextPageButton.style.display = page === totalPages ? 'none' : 'inline';
        }
    }

    const prevPageButton = document.getElementById('prev-page');
    const nextPageButton = document.getElementById('next-page');

    if (prevPageButton) {
        prevPageButton.addEventListener('click', function(event) {
            event.preventDefault();
            if (currentPage > 1) {
                currentPage--;
                renderTable(currentPage);
            }
        });
    }

    if (nextPageButton) {
        nextPageButton.addEventListener('click', function(event) {
            event.preventDefault();
            if (currentPage < totalPages) {
                currentPage++;
                renderTable(currentPage);
            }
        });
    }

    renderTable(currentPage);
});