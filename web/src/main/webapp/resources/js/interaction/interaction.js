$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const customerId = urlParams.get('id'); // Lấy giá trị của tham số 'id'

    if (customerId) {
        loadTableData(customerId); // Gọi hàm loadTableData với customerId từ URL
    } else {
        console.error('Không tìm thấy ID khách hàng trong URL.');
    }
});

/**
 * Load dữ liệu tương tác từ server và khởi tạo Handsontable
 */
function loadTableData(customerId) {
    if (!customerId) {
        console.error('Customer ID is required to load interactions.');
        return;
    }

	// Xây dựng URL động với _ctx
	const url = `${_ctx}/customer/load-interaction?id=${customerId}`;

    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function(res) {
            console.log("Dữ liệu nhận được:", JSON.stringify(res));

            if (res) {
                initTable(res.colHeaders, res.colWidths, res.data); // Gọi hàm initTable để khởi tạo Handsontable
            }
        },
        error: function(e) {
            console.log("Lỗi tải dữ liệu: " + e);
        }
    });
}


function initTable(colHeaders, colWidths, data) {
    console.log("HST:", document.getElementById('tblInteraction'));

    var container = document.getElementById('tblInteraction');
    
    if (container) {
        htInteraction = new Handsontable(container, {
            data: data,
            colHeaders: colHeaders,
            colWidths: colWidths,
            columns: [
                { type: 'text' }, 
                { type: 'date', dateFormat: 'YYYY-MM-DD', correctFormat: true }, 
                { type: 'text' },
                { type: 'text' }, 
                { 
                    renderer: function(instance, td, row, col, prop, value, cellProperties) {
                        Handsontable.dom.empty(td); // Xóa nội dung cũ
						
                        const button = document.createElement('button');
						button.type = 'button';
                        button.className = 'delete-button';
						
						// Tạo phần tử icon FontAwesome
				        const icon = document.createElement('i');
				        icon.className = 'fas fa-trash'; // Lớp FontAwesome cho biểu tượng thùng rác
				        
				        button.appendChild(icon);
						
                        button.onclick = function() {
                            deleteRow(row, value); // Gọi hàm deleteRow khi nhấn nút
                        };
						
                        td.appendChild(button);
                        return td;
                    }
                }, // Hành động (nút xóa)
            ],
            rowHeaders: true,
            minRows: 8,
            height: 400,
            currentRowClassName: 'currentRow',
            currentColClassName: 'currentCol',
            manualColumnResize: true,
            manualRowResize: true,
            minSpareRows: 1,
            contextMenu: true,
            licenseKey: 'non-commercial-and-evaluation',			
        });
					
    } else {
        console.error("Container tblInteraction không tồn tại.");
    }
}

function deleteRow(rowIndex, interactionId) {
    if (confirm(`Bạn có chắc muốn xóa hàng số ${rowIndex + 1}?`)) {
        // Gọi API DELETE với interactionId là query parameter
        $.ajax({
            url: `${_ctx}/customer/delete-interaction?id=${interactionId}`,  // Đặt interactionId vào URL
            type: 'DELETE',
            success: function(response) {
                console.log('Xóa thành công:', response);						
				// Sau khi xóa thành công, xóa hàng trong Handsontable
				htInteraction.alter('remove_row', rowIndex);		
            },
            error: function(error) {
                console.error('Lỗi khi xóa:', error);
                alert('Có lỗi xảy ra khi xóa dữ liệu. Vui lòng thử lại.');
            }
        });
    }
}
