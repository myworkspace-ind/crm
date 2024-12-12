var htOrder;

/**
 * Processing after the webpage are loaded in the browser.
 */
$(document).ready(function() {
	loadTableData();
});


/**
 * Load column width, header, initTable()
 */
function loadTableData() {

	$.ajax({
		url: _ctx + 'orders-vinh/load',
		type: 'GET',
		dataType: 'json',
		contentType: 'application/json',
		success: function(res) {
			console.log("res=" + JSON.stringify(res));

			if (res) {
				initTable(res.colHeaders, res.colWidths, res.data, res.columnTypes);
			}
		},
		error: function(e) {
			console.log("Error: " + e);
		}
	});
}

function initTable(colHeaders, colWidths, data) {
	console.log("HST:", document.getElementById('tblOrder'));

	var container = document.getElementById('tblOrder');

	//render for column containing action button
	function buttonRenderer(instance, td, row, col, prop, value, cellProperties) {
		td.innerHTML = `
		        <div class="btn-group" role="group" aria-label="...">
		            <button class="btn btn-info btn-view" onclick="viewDetail(${row})" title="Xem chi tiết">
		                <i class="fas fa-eye"></i> Xem chi tiết
		            </button>
		            <button class="btn btn-warning" onclick="edit(${row})" title="Sửa">
		                <i class="fas fa-edit"></i> Sửa
		            </button>
		            <button class="btn btn-danger" onclick="deleteRow(${row})" title="Xóa">
		                <i class="fas fa-trash"></i> Xóa
		            </button>
		            <button class="btn btn-success btn-status" onclick="openStatusModal(${row})" title="Cập nhật trạng thái">
		                <i class="fas fa-sync-alt"></i> Cập nhật trạng thái
		            </button>
		        </div>
	    `;

		return td;
	}
	
	
	if (container) {
		htOrder = new Handsontable(container, {
			data: data,
			colHeaders: colHeaders,
			colWidths: colWidths,
			columns: [
				{ type: 'text' }, // ID
				{ type: 'date', dateFormat: 'YYYY-MM-DD', correctFormat: true }, // Delivery Date
				{ type: 'text' }, // Goods Category
				{ type: 'text' }, // Info of customer
				{ type: 'text' }, // Transportation method
				{ renderer: buttonRenderer }
			],
			rowHeaders: true,
			minRows: 8,
			height: 1000,
			currentRowClassName: 'currentRow',
			currentColClassName: 'currentCol',
			manualColumnResize: true,
			manualRowResize: true,
			minSpareRows: 1,
			contextMenu: true,
			licenseKey: 'non-commercial-and-evaluation'
		});
	} else {
		console.error("Container tblOrder không tồn tại.");
	}
}
function changeTitle(newTitle) {
	document.getElementById("title").innerText = newTitle;
}

function viewDetail(row) {
	console.log("Xem chi tiết cho hàng:", row);
	// Xử lý logic xem chi tiết
}

function edit(row) {
	changeTitle('Cập nhật đơn hàng');
	currentRow = row;  // Save current row to update
	document.getElementById("updateOrderModal").style.display = "block";
	document.getElementById("modalOverlay").style.display = "block";
}

function deleteRow(row) {
    // Lấy ID đơn hàng từ hàng cần xóa
    const orderId = htOrder.getSourceDataAtRow(row)[0]; // Giả sử cột ID là cột đầu tiên

    // Hiển thị thông báo xác nhận
    if (confirm(`Bạn có chắc chắn muốn xóa đơn hàng ID: ${orderId}?`)) {
        // Gửi yêu cầu xóa đến server
        $.ajax({
            url: `${_ctx}/orders-vinh/delete?id=${orderId}`, // URL API xóa (thay đổi theo API của bạn)
            type: 'DELETE',
            success: function(response) {
                console.log("Xóa thành công:", response);

                // Xóa hàng trên Handsontable
                htOrder.alter('remove_row', row);

                // Đảm bảo đồng bộ dữ liệu
                htOrder.render(); // Cập nhật lại bảng

                // Hiển thị thông báo thành công
                alert(`Đơn hàng ID: ${orderId} đã được xóa thành công!`);
            },
            error: function(error) {
                console.error("Xóa thất bại:", error);

                // Hiển thị thông báo lỗi
                alert("Có lỗi xảy ra khi xóa đơn hàng. Vui lòng thử lại sau.");
            }
        });
    }
}


let currentRow;

function openStatusModal(row) {
	currentRow = row;  // Save current row to update
	document.getElementById("statusModal").style.display = "block";
	document.getElementById("modalOverlay").style.display = "block";
}

function closeUpdateOrderModal() {
	document.getElementById("updateOrderModal").style.display = "none";
	document.getElementById("modalOverlay").style.display = "none";
}

function closeModalOrderStatus() {
	document.getElementById("statusModal").style.display = "none";
	document.getElementById("modalOverlay").style.display = "none";
}

function saveStatus() {
	const newStatus = document.getElementById("statusSelect").value;
	console.log("Cập nhật trạng thái cho hàng:", currentRow, "thành:", newStatus);

	// Ví dụ: Update data in Handsontable
	//htOrder.setDataAtRowProp(currentRow, 'status', newStatus);

	closeModal();  // Close modal after save
}