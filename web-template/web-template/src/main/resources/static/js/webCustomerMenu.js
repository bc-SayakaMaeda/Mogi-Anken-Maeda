document.addEventListener('DOMContentLoaded', function() {
    const rowsPerPage = 10;
    let currentPage = 1;
    const totalPages = Math.ceil(bookList.length / rowsPerPage) || 1;  
    const form = document.getElementById('reservationForm'); 

    // 指定されたページの情報をテーブルに表示する
    function renderTable(page) {
        const tableBody = document.querySelector('#book-table tbody');
        tableBody.innerHTML = '';

        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        const pageData = bookList.slice(start, end);
        
        const template = document.getElementById('book-row-template').content;
        const fragment = document.createDocumentFragment();

        pageData.forEach((item, index) => {

            const row = document.importNode(template, true);
            row.querySelector('.book-id').textContent = start + index + 1;
            row.querySelector('.book-title').textContent = item.title;
            row.querySelector('.book-author').textContent = item.author;
            row.querySelector('.book-stock').textContent = item.stockCount;
            row.querySelector('.book-action').innerHTML = item.stockCount > 0 ? `<input type="checkbox" class="large-checkbox" id="book-${start + index + 1}" name="bookIds" value="${item.bookID}">` : '貸出中';
            tableBody.appendChild(row);
        });
        
        document.getElementById('book-row-block').appendChild(fragment);

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
    
    // 貸出希望確認ボタン押下時
    form.addEventListener('submit', function (event) {
        // 必須チェック
        const checkboxes = document.querySelectorAll('#book-row-block input[type="checkbox"]'); 
        const isChecked = Array.from(checkboxes).some(checkbox => checkbox.checked); 

        if (!isChecked) {
            event.preventDefault(); 
            return;
        }

    });
        
    });