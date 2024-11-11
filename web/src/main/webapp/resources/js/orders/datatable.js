/*var dataSet */

$(document).ready(function() {
	console.log(dataSet);

	var table = $('#tblDatatable').DataTable({
		data: dataSet,  // Sử dụng dataSet đã được truyền vào
		dom: 'Bfrtip',
		paging: true, // Phân trang
		searching: true, // Tìm kiếm
		ordering: true, // Sắp xếp
		lengthMenu: [5, 10, 25], // Số lượng đơn hàng trên mỗi trang
		buttons: [
			'copyHtml5',
			'excelHtml5',
			'csvHtml5',
			'pdfHtml5'
		],
		columnDefs: [
			{
				targets: -1,  // Chọn cột cuối cùng để thêm nút
				data: null,
				defaultContent: `
					<button class='btn btn-info detail-btn'>Xem chi tiết</button>
					<button class='btn btn-warning edit-btn'>Sửa</button>
					<button class='btn btn-danger'>Xóa</button>
					<button class='btn btn-success'>Cập nhật trạng thái</button>`
			}
		]
	});

	$('#tblDatatable tbody').on('click', '.edit-btn', function() {
		var data = $('#tblDatatable').DataTable().row($(this).parents('tr')).data();
		edit(data);
	});
	$('#tblDatatable tbody').on('click', '.detail-btn', function() {
		var row = table.row($(this).closest('tr')).data(); // Lấy dữ liệu của dòng được nhấp vào
		var orderId = row[0];

		console.log('Xem chi tiết cho ID đơn hàng: ', orderId);

		$.ajax({
			url: _ctx + '/orders-datatable/viewDetails/' + orderId,
			method: 'GET',
			success: function(response) {
				console.log(response)
				var orderStatus = response[4];
				var goodsCategory = response[5];
				var sender = response[6];
				
				$('#orderIdDetail').text(response[0]);
				$('#orderCodeDetail').text(response[1]);
				$('#orderCodeDetailInput').val(response[1]);
				$('#orderDeliveryDateDetail').val(response[2]);
				$('#orderCreateDateDetail').val(response[3]);
				$('#orderStatusDetail').html('<option value="' + orderStatus + '">' + orderStatus + '</option>');
				$('#orderGoodsDetail').html('<option value="' + goodsCategory + '">' + goodsCategory + '</option>');
				$('#orderSenderName').html('<option value="' + sender + '">' + sender + '</option>');
				$('#orderSenderPhone').val(response[7]);
				$('#orderTransportDetail').val(response[8]);
				$('#orderRequirementDetail').val(response[9]);
				$('#orderSenderEmail').val(response[10]);

				document.getElementById("orderDetailModal").style.display = "block";
			},
			error: function(error) {
				console.error('Có lỗi khi lấy thông tin chi tiết đơn hàng:', error);
			}
		});
	});

	/*$('#tblDatatable tbody').on('click', '.detail-btn', function() {
		var data = $('#tblDatatable').DataTable().row($(this).parents('tr')).data();
		viewDetail(data);
	});*/
});

let currentRow;

function changeTitle(newTitle) {
	document.getElementById("title").innerText = newTitle;
}

function edit(row) {
	changeTitle('Cập nhật đơn hàng');
	currentRow = row;
	document.getElementById("updateOrderModal").style.display = "block";
	document.getElementById("modalOverlay").style.display = "block";
}

function viewDetail(row) {
	currentRow = row;
	document.getElementById("orderDetailModal").style.display = "block";
	document.getElementById("modalOverlay").style.display = "block";
}

function closeUpdateOrderModal() {
	document.getElementById("updateOrderModal").style.display = "none";
	document.getElementById("modalOverlay").style.display = "none";
}

function closeModalOrderDetail() {
	document.getElementById("orderDetailModal").style.display = "none";
	document.getElementById("modalOverlay").style.display = "none";
}

function closeModalOrderStatus() {
	document.getElementById("statusModal").style.display = "none";
	document.getElementById("modalOverlay").style.display = "none";
}

/*$(document).ready(function() {
	$('#tblDatatable').DataTable({
		dom: 'Bfrtip',
		paging: true, // Phân trang
		searching: true, // Tìm kiếm
		ordering: true, // Sắp xếp
		lengthMenu: [5, 10, 25], // Số lượng đơn hàng trên mỗi trang
		buttons: [
			'copyHtml5',
			'excelHtml5',
			'csvHtml5',
			'pdfHtml5'
		]
	});
});*/


/*$(document).ready(function () {
	$.ajax({
		url : _ctx + 'orders-datatable/loaddata',
		type : 'GET',
		dataType : 'json',
		contentType : 'application/json',
		success : function(res) {
			// Debug
			console.log("res=" + JSON.stringify(res));
			if (res) {
				dataSet = res.data;
				initTable();
			}                
		},
		error : function (e) {
			console.log("Error: " + e);
		}
	});
    
});

function initTable() {
	$("#tblDatatable").DataTable({
		data: dataSet,
		dom: 'Bfrtip',
		buttons: [
			'copyHtml5',
			'excelHtml5',
			'csvHtml5',
			'pdfHtml5'
		]
	});
}*/


/*$(document).ready(function() {
	loadTableData();
});*/


/*function loadTableData() {
	$.ajax({
		url: _ctx + 'orders-datatable/get-orders',
		type: 'GET',
		dataType: 'json',
		contentType: 'application/json',
		success: function(res) {
			console.log("res=" + JSON.stringify(res));
			if (res) {
				// Xử lý data nhận được từ response
				let dataSet = res.data.map(order => {
					return [
						order[0],  // ID
						order[1],  // Code
						order[2],  // Delivery Date
						order[3],  // Order Category
						order[4],  // Customer
						order[5],  // Transportation Method
						'<button class="btn btn-primary">Thao tác</button>' // Thao tác
					];
				});
			    
				// Gọi initTable để hiển thị bảng
				initTable(dataSet, res.colWidths, res.colHeaders);
			}
		},
		error: function(e) {
			console.log("Error: " + e);
		}
	});   
}
*/

/*function initTable(dataSet, colWidths, colHeaders) {
	// Khởi tạo DataTable với dữ liệu và cấu hình cột
	$("#tblDatatable").DataTable({
		data: dataSet,
		dom: 'Bfrtip',
		buttons: [
			'copyHtml5',
			'excelHtml5',
			'csvHtml5',
			'pdfHtml5'
		],
		columnDefs: colWidths.map((width, index) => {
			return {
				targets: index,
				width: width + 'px'  // Gán chiều rộng cho cột tương ứng
			};
		}),
		columns: colHeaders.map(header => {
			return {
				title: header  // Gán tiêu đề cột
			};
		})
	});
}*/

