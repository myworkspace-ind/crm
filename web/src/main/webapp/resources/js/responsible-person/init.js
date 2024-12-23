var htResponsiblePerson;

/**
 * Processing after the webpage are loaded in the browser.
 */

/**
 * Load column width, header, initTable()
 */
function loadTableData(type) {
	console.log("Biến ctx:", _ctx);
	$.ajax({
		url: _ctx + `customer/load-responsible-person?type=${type}`,
		type: 'GET',
		dataType: 'json',
		contentType: 'application/json',
		success: function(res) {
			console.log("res=" + JSON.stringify(res));

			if (res) {
				initTable(res.colHeaders, res.colWidths, res.data, type);
			}
		},
		error: function(e) {
			console.log("Error: " + e);
		}
	});
}
// Hàm hoán đổi 2 hàng
function swapRows(hotInstance, rowIndex1, rowIndex2, row1, row2) {
	const data = hotInstance.getData();

	// Đổi dữ liệu hai hàng
	const temp = data[row1];
	data[row1] = data[row2];
	data[row2] = temp;

	// Cập nhật lại bảng
	hotInstance.loadData(data);
	var container = document.getElementById('responsibleTable');
	let type;
	if(container.classList.contains("customPerson")){
		type = "customPerson";
	}
	else if(container.classList.contains("customProfession")){
		type = "customProfession";
	}
	else if(container.classList.contains("customStatus")){
		type = "customStatus"
	}
	else{
		type = "customGoodsCategory";
	}
	// Tạo một đối tượng URLSearchParams với các tham số
	const params = new URLSearchParams();
	params.append('rowIndex1', rowIndex1);
	params.append('rowIndex2', rowIndex2);
	params.append('type', type);

	// Xây dựng URL với query parameters
	const url = `${_ctx}customer/swap?${params.toString()}`;

	fetch(url, {
		method: 'POST',
	    headers: {
			'Content-Type': 'application/json',
		},
		// Không cần body khi sử dụng query parameters qua URL
	})
	.then(response => response.json())
	.then(data => {
		alert("Hoán đổi thành công");
		location.reload();
	})
	.catch(error => {
		alert("Đã xảy ra lỗi khi hoán đổi!!!!");
		console.error('Lỗi khi swap:', error);
	});
}

function initTable(colHeaders, colWidths, data, type) {
    console.log("HST:", document.getElementById('responsibleTable'));

    var container = document.getElementById('responsibleTable');
    container.classList = '';	
    container.classList.add(`${type}`);

    if (container) {
        htResponsiblePerson  = new Handsontable(container, {
            data: data,
            colHeaders: colHeaders,
            colWidths: colWidths,
            columns: [
                { type: 'text' }, // ID
                { type: 'text' }, 
                { type: 'text' },
				{ type: 'text' },
                { renderer: deleteButtonRenderer },
            ],
            rowHeaders: true,
            height: 300,
            currentRowClassName: 'currentRow',
            currentColClassName: 'currentCol',
            manualColumnResize: true,
            manualRowResize: true,
            columnSorting: true,  // Bật tính năng sắp xếp cột
            autoColumnSize: true,  // Tự động điều chỉnh kích thước cột
            minSpareRows: 1,
            contextMenu: {
				items: {
					"row_above": {
						name: 'Add Row Above',
					},
					"row_below": {
						name: 'Add Row Below',
					},
					"swap_rows": {
						name: 'Swap Rows', // Tên của menu item
						callback: function () {
						const selected = htResponsiblePerson.getSelected();

						if (selected && selected.length === 2) {
						const [row1, col1] = selected[0];
						const [row2, col2] = selected[1];

						const idRow1 = htResponsiblePerson.getDataAtCell(row1, 0);
						const idRow2 = htResponsiblePerson.getDataAtCell(row2, 0);
						// Kiểm tra nếu đây là 2 hàng khác nhau
						if (row1 !== row2) {
						 // Thực hiện hoán đổi hàng ở đây
							swapRows(htResponsiblePerson,idRow1, idRow2, row1, row2);
						} else {
							alert('Please select two different rows!');
						}
						} else {
							alert('Please select exactly two rows to swap.');
						}
					}
				},
			}
		},
            licenseKey: 'non-commercial-and-evaluation'
        });
        
        // Nếu bạn cần gọi lại autoColumnSize sau khi thay đổi dữ liệu, sử dụng updateSettings
        htResponsiblePerson.updateSettings({
            autoColumnSize: true
        });

        // Hoặc nếu bảng có sự thay đổi, bạn có thể gọi lại render để làm mới bảng
        htResponsiblePerson.render();
    } else {
        console.error("Container responsibleTable không tồn tại.");
    }
}



function deleteButtonRenderer(instance, td, row, col, prop, value, cellProperties) {
    Handsontable.dom.empty(td);

    const button = document.createElement('button');
    button.type = 'button';
    button.innerHTML = '<i class="fas fa-trash"></i>';
    button.onclick = function () {
        // Lấy ID từ cột đầu tiên (cột 0)
        const rowId = instance.getDataAtCell(row, 0);  // Cột đầu tiên chứa ID
        
        deleteRow(row, rowId);
    };
	td.style.textAlign = 'center';  // Căn giữa nội dung của ô
    td.style.verticalAlign = 'middle'; // Căn giữa theo chiều dọc
    td.appendChild(button);
    return td;
}


function deleteRow(rowIndex, id) {
	var container = document.getElementById('responsibleTable');

	let type;
	if(container.classList.contains("customPerson")){
		type = "customPerson";
	}
	else if(container.classList.contains("customProfession")){
		type = "customProfession";
	}
	else if(container.classList.contains("customStatus")){
		type = "customStatus"
	}
	else{
		type = "customGoodsCategory";
	}
    if (confirm(`Bạn có chắc muốn xóa hàng số ${rowIndex + 1}?`)) {
        $.ajax({
            url: `${_ctx}customer/delete-object?id=${id}&type=${type}`,
            type: 'DELETE',
            success: function (response) {
                console.log('Xóa thành công:', response);
				alert("Xóa thành công");
				location.reload();
            },
            error: function (error) {
                console.error('Lỗi khi xóa:', error);
                alert('Có lỗi xảy ra khi xóa dữ liệu. Vui lòng thử lại.');
            }
        });
    }
}