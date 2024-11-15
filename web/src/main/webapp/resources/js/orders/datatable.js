/*var dataSet */

$(document).ready(function() {
	if (!window.dataSetLogged) {
		console.log(dataSet);
		window.dataSetLogged = true; // Đánh dấu đã in dữ liệu
	}

	if (!$.fn.DataTable.isDataTable('#tblDatatable')) {
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
			var row = table.row($(this).closest('tr')).data(); // Lấy dữ liệu của dòng được nhấp vào
			var orderId = row[0];

			console.log('Xem chi tiết cho ID đơn hàng: ', orderId);

			$.ajax({
				url: _ctx + 'orders-datatable/viewDetails/' + orderId,
				method: 'GET',
				success: function(response) {
					console.log(response)
					//var orderStatusId = response[6];
					//var orderStatusName = response[7];
					var orderStatusData = response[6]
					var currentOrderStatusId = response[7];

					var orderGoodsCategoryId = response[8];
					var orderGoodsCategoryName = response[9];
					var orderCustomerId = response[10];
					var orderCustomerName = response[11];
					//var orderCustomerPhone = response[12];
					//var orderCustomerEmail = response[13];

					$('#orderIdUpdate').text(response[0]);
					$('#orderCodeUpdate').text(response[1]);
					$('#orderCodeUpdateInput').val(response[1]);
					$('#orderDeliveryDateUpdate').val(response[2]);
					$('#orderCreateDateUpdate').val(response[3]);
					$('#orderTransportUpdate').val(response[4]);
					$('#orderRequirementUpdate').val(response[5]);

					// Cập nhật Order Status (lấy từ response[6] - mảng 2 chiều)
					if (orderStatusData && orderStatusData.length > 0) {
						var options = orderStatusData.map(function(status) {
							// status[0] là ID và status[1] là Name của trạng thái
							var selected = status[0] === currentOrderStatusId ? ' selected' : ''; // So sánh ID trạng thái hiện tại
							return '<option value="' + status[0] + '"' + selected + '>' + status[1] + '</option>';
						}).join('');

						// Chèn các option vào #orderStatusDetail
						$('#orderStatusUpdate').html(options);
					}

					$('#orderGoodsUpdate').html('<option value="' + orderGoodsCategoryId + '">' + orderGoodsCategoryName + '</option>');
					$('#orderSenderNameUpdate').html('<option value="' + orderCustomerId + '">' + orderCustomerName + '</option>');

					$('#orderSenderPhoneUpdate').val(response[12]);
					$('#orderSenderEmailUpdate').val(response[13]);

					document.getElementById("updateOrderModal").style.display = "block";
				},
				error: function(error) {
					console.error('Có lỗi khi lấy thông tin chi tiết đơn hàng:', error);
				}
			});

		});


		$('#tblDatatable tbody').on('click', '.detail-btn', function() {
			/*var row = table.row($(this).closest('tr')).data(); // Lấy dữ liệu của dòng được nhấp vào
						var orderId = row[0];

						console.log('Xem chi tiết cho ID đơn hàng: ', orderId);

						$.ajax({
							url: _ctx + 'orders-datatable/viewDetails/' + orderId,
							method: 'GET',
							success: function(response) {
								console.log(response)
								var orderStatusId = response[6];
								var orderStatusName = response[7];
								var orderGoodsCategoryId = response[8];
								var orderGoodsCategoryName = response[9];
								var orderCustomerId = response[10];
								var orderCustomerName = response[11];
								//var orderCustomerPhone = response[12];
								//var orderCustomerEmail = response[13];

								$('#orderIdUpdate').text(response[0]);
								$('#orderCodeUpdate').text(response[1]);
								$('#orderCodeUpdateInput').val(response[1]);
								$('#orderDeliveryDateUpdate').val(response[2]);
								$('#orderCreateDateUpdate').val(response[3]);
								$('#orderTransportUpdate').val(response[4]);
								$('#orderRequirementUpdate').val(response[5]);

								$('#orderStatusUpdate').html('<option value="' + orderStatusId + '">' + orderStatusName + '</option>');
								$('#orderGoodsUpdate').html('<option value="' + orderGoodsCategoryId + '">' + orderGoodsCategoryName + '</option>');
								$('#orderSenderNameUpdate').html('<option value="' + orderCustomerId + '">' + orderCustomerName + '</option>');

								$('#orderSenderPhoneUpdate').val(response[12]);
								$('#orderSenderEmailUpdate').val(response[13]);

								document.getElementById("updateOrderModal").style.display = "block";
							},
							error: function(error) {
								console.error('Có lỗi khi lấy thông tin chi tiết đơn hàng:', error);
							}
						});*/
		});

		/*$('#tblDatatable tbody').on('click', '.detail-btn', function() {
			var data = $('#tblDatatable').DataTable().row($(this).parents('tr')).data();
			viewDetail(data);
		});*/
	}
});


$(document).on('click', '#saveOrderButton', function() {
	var button = $(this);
	button.prop('disabled', true); // Disable button to prevent multiple clicks

	var orderId = $('#orderIdUpdate').text();
	var orderCode = $('#orderCodeUpdateInput').val();
	var deliveryDate = $('#orderDeliveryDateUpdate').val();
	var createDate = $('#orderCreateDateUpdate').val();
	var status = $('#orderStatusUpdate').val();
	var goodsCategory = $('#orderGoodsUpdate').val();
	var senderName = $('#orderSenderNameUpdate').val()
	var senderPhone = $('#orderSenderPhoneUpdate').val();
	var transport = $('#orderTransportUpdate').val();
	var requirement = $('#orderRequirementUpdate').val();
	var senderEmail = $('#orderSenderEmailUpdate').val();

	console.log(orderId); // In ra giá trị orderId
	console.log(orderCode); // In ra giá trị orderCode

	if (!orderId) {
		alert('ID đơn hàng không hợp lệ.');
		button.prop('disabled', false); // Enable button if id is not valid
		return;
	}

	var order = {
		id: orderId,
		orderCode: orderCode,
		deliveryDate: deliveryDate,
		createDate: createDate,
		status: status,
		goodsCategory: goodsCategory,
		senderName: senderName,
		senderPhone: senderPhone,
		transport: transport,
		requirement: requirement,
		senderEmail: senderEmail
	};

	// In toàn bộ đối tượng order ra console
	console.log("Order object being sent:", order);

	$.ajax({
		url: _ctx + '/orders-datatable/saveOrderData/',
		method: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(order),

		success: function(response) {
			console.log("UDPATE FINAL ORDER: ", order); // In lại order khi thành công
			if (response.status === "success") {
				alert(response.message);
				// Close modal or update the UI as needed
				document.getElementById("updateOrderModal").style.display = "none";
				// Reload or refresh the data table if needed
				$('#tblDatatable').DataTable().ajax.reload();
			} else {
				alert(response.message);
			}
			button.prop('disabled', false); // Re-enable button
		},
		error: function(error) {
			console.error("Error saving order:", error);
			alert("An error occurred while saving/updating the order.");
			button.prop('disabled', false); // Re-enable button
		}
	});
});


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

