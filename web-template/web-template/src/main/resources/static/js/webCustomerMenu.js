document.addEventListener('DOMContentLoaded', function () {
    const rowsPerPage = 10;
    let currentPage = 1;
    const totalPages = Math.ceil(bookList.length / rowsPerPage) || 1;
    const form = document.getElementById('reservationForm');
    const selectedBooks = new Set(selectedBookIds); 

    // 現在のページの選択状態を保存
    function saveCurrentPageSelections() {
        const checkboxes = document.querySelectorAll('#book-row-block input[type="checkbox"]');
        checkboxes.forEach(checkbox => {
            if (checkbox.checked) {
                selectedBooks.add(checkbox.value);
            } else {
                selectedBooks.delete(checkbox.value);
            }
        });
    }
    
        function showCurrentPageBooks(page) {
        const table = document.getElementById('book-row-block');
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        // 現在のページの10冊のみ非表示を解除
        for (let i = 0; i < table.rows.length; i++) {
            if (i >= start && i < end) {
                table.rows[i].style.display = '';
            } else {
                table.rows[i].style.display = 'none';
            }
        }
        }

    // 指定されたページの情報をテーブルに表示する
    function renderTable(page) {
        const tableBody = document.querySelector('#book-table tbody');
        tableBody.innerHTML = '';
        const template = document.getElementById('book-row-template').content;
        const fragment = document.createDocumentFragment();
    
        // 全ての書籍をループ
        bookList.forEach((item, index) => {
            const row = document.importNode(template, true);
    
            // 書籍情報を設定
            row.querySelector('.book-id').textContent = index + 1;
            row.querySelector('.book-title').textContent = item.title;
            row.querySelector('.book-author').textContent = item.author;
            row.querySelector('.book-stock').textContent = item.stockCount;
            
            const actionCell = row.querySelector('.book-action');
            if (item.stockCount > 0) {
                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.className = 'large-checkbox';
                checkbox.id = `book-${index + 1}`;
                checkbox.name = 'bookIds';
                checkbox.value = item.bookID;

                // 選択状態を復元
                if (selectedBooks.has(item.bookID)) {
                    checkbox.checked = true;
                }

                checkbox.addEventListener('change', function () {
                    if (this.checked) {
                        selectedBooks.add(this.value);
                    } else {
                        selectedBooks.delete(this.value);
                    }
                });

                actionCell.appendChild(checkbox);
                
            } else {
                actionCell.textContent = '貸出中';
            }
    
            fragment.appendChild(row);
        });

        tableBody.appendChild(fragment);
        
        // 現在のページの書籍のみ表示
        showCurrentPageBooks(page);
        
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
            saveCurrentPageSelections(); 
            if (currentPage > 1) {
                currentPage--;
                renderTable(currentPage);
            }
        });
    }

    if (nextPageButton) {
        nextPageButton.addEventListener('click', function(event) {
            event.preventDefault();
            saveCurrentPageSelections(); 
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
        const titleGroup = document.querySelector('.title-group p');

        // 必須チェックNGの場合、文字を赤文字に変更する
        if (!isChecked) {
            event.preventDefault(); 
            titleGroup.style.color = 'red';
            return;
        } else {
            titleGroup.style.color = ''; 
        }

    });
    
});