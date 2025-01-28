document.addEventListener('DOMContentLoaded', function() {
    const data = [
        { id: 1, title: 'xxx', author: 'xx', stock: 1 },
        { id: 2, title: 'yyy', author: 'yy', stock: 0 },
        { id: 3, title: 'zzz', author: 'zz', stock: 9 },
        { id: 4, title: 'aaa', author: 'aa', stock: 1 },
        { id: 5, title: 'bbb', author: 'bb', stock: 1 },
        { id: 6, title: 'xxx', author: 'xx', stock: 1 },
        { id: 7, title: 'yyy', author: 'yy', stock: 0 },
        { id: 8, title: 'zzz', author: 'zz', stock: 9 },
        { id: 9, title: 'aaa', author: 'aa', stock: 1 },
        { id: 10, title: 'bbb', author: 'bb', stock: 1 },
        { id: 11, title: 'xxx', author: 'xx', stock: 1 },
        { id: 12, title: 'yyy', author: 'yy', stock: 0 },
        { id: 13, title: 'zzz', author: 'zz', stock: 9 },
        { id: 14, title: 'aaa', author: 'aa', stock: 1 },
        { id: 15, title: 'bbb', author: 'bb', stock: 1 },
    ];

    const rowsPerPage = 10;
    let currentPage = 1;
    const container = document.querySelector('.webCustomerMenu-container');
    const totalPages = toNumeric(container.getAttribute('data-total-pages'), Math.ceil(data.length / rowsPerPage));
    
    container.setAttribute('data-total-pages', totalPages);
    
    function renderTable(page) {
        const tableBody = document.querySelector('#book-table tbody');
        tableBody.innerHTML = '';

        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        const pageData = data.slice(start, end);
        const template = document.getElementById('book-row-template').content;

        pageData.forEach((item, index) => {
            const row = document.importNode(template, true);
            row.querySelector('.book-id').textContent = start + index + 1;
            row.querySelector('.book-title').textContent = item.title;
            row.querySelector('.book-author').textContent = item.author;
            row.querySelector('.book-stock').textContent = item.stock;
            row.querySelector('.book-action').innerHTML = item.stock > 0 ? '<input type="checkbox" class="large-checkbox">' : '貸出中';
            tableBody.appendChild(row);
        });

        document.getElementById('page-info').textContent = `${page}/${totalPages}ページ`;

        // 「前」ボタンの表示/非表示を切り替え
        const prevPageButton = document.getElementById('prev-page');
        if (page === 1) {
            prevPageButton.style.display = 'none';
        } else {
            prevPageButton.style.display = 'inline';
        }

        // 「次」ボタンの表示/非表示を切り替え
        const nextPageButton = document.getElementById('next-page');
        if (page === totalPages) {
            nextPageButton.style.display = 'none';
        } else {
            nextPageButton.style.display = 'inline';
        }
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