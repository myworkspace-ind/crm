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
		url: _ctx + 'orders/load',
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
	        <a href="#" onclick="viewDetail(${row})">- Xem chi tiết</a><br>
	        <a href="#" onclick="edit(${row})">- Sửa</a><br>
	        <a href="#" onclick="deleteRow(${row})">- Xóa</a><br>
	        <a href="#" onclick="openStatusModal(${row})">Cập nhật trạng thái</a>
	    `;
		td.style.textAlign = 'start';
		td.style.cursor = 'pointer';
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
			height: 2000,
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
function changeTitle(newTitle){
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
	console.log("Xóa hàng:", row);
	// Xử lý logic xóa hàng
}

let currentRow;

function openStatusModal(row) {
	currentRow = row;  // Save current row to update
	document.getElementById("statusModal").style.display = "block";
	document.getElementById("modalOverlay").style.display = "block";
}
function closeModal() {
	document.getElementById("statusModal").style.display = "none";
	document.getElementById("updateOrderModal").style.display = "none";
	document.getElementById("modalOverlay").style.display = "none";
}

function saveStatus() {
	const newStatus = document.getElementById("statusSelect").value;
	console.log("Cập nhật trạng thái cho hàng:", currentRow, "thành:", newStatus);

	// Ví dụ: Update data in Handsontable
	//htOrder.setDataAtRowProp(currentRow, 'status', newStatus);

	closeModal();  // Close modal after save
}