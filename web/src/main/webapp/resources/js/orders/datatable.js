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
				{
					extend: 'copyHtml5',
					text: '<i class="fa fa-copy"></i> Sao chép',
					className: 'btn-copy'
				},
				{
					extend: 'excelHtml5',
					text: '<i class="fa fa-file-excel"></i> Xuất Excel',
					className: 'btn-excel'
				},
				{
					extend: 'csvHtml5',
					text: '<i class="fa fa-file-csv"></i> Xuất CSV',
					className: 'btn-csv'
				},
				{
					extend: 'pdfHtml5',
					text: '<i class="fa fa-file-pdf"></i> Xuất PDF',
					className: 'btn-pdf'
				},
			],
			columnDefs: [
				{
					targets: -1,  // Chọn cột cuối cùng để thêm nút
					data: null,
					defaultContent: `
					<div class="row">
						<div class="col-lg-12">
								<div class="btn-group" role="group" aria-label="...">
									<button class='btn btn-info detail-btn'>Xem chi tiết</button>
									<button class='btn btn-warning edit-btn'>Sửa</button>
									<button class='btn btn-danger delete-btn'>Xóa</button>
									<button class='btn btn-success updateOrderStatus-btn'>Cập nhật trạng thái</button>
								</div>
						</div>
					</div> `
				}
			]
		});

		$('#tblDatatable tbody').on('click', '.updateOrderStatus-btn', function() {
			var row = table.row($(this).closest('tr')).data();
			var orderId = row[0];

			console.log('Cập nhật trạng thái đơn hàng ');
			//document.getElementById("updateOrderStatusModal").style.display = "block";
			$('#updateOrderStatusModal').modal('show');
			$('#updateOrderStatusModal').removeAttr('inert');



			console.log('Xem chi tiết cho ID đơn hàng: ', orderId);

			$.ajax({
				url: _ctx + 'orders-datatable/viewDetails/' + orderId,
				method: 'GET',
				success: function(response) {
					console.log(response);
					
					var orderCategoryData = response[15];
					var currentOrderCategoryId = response[16];
					$('#orderCodeUpdateStatus').val(response[1]);
					if (orderCategoryData && orderCategoryData.length > 0) {
						var options = orderCategoryData.map(function(ordercategory) {
							var selected = ordercategory[0] === currentOrderCategoryId ? ' selected' : '';
							return '<option value="' + ordercategory[0] + '"' + selected + '>' + ordercategory[1] + '</option>';
						}).join('');

						$('#orderCategoryUpdateStatus').html(options);
						$('#orderCategoryUpdateStatus').css('pointer-events', 'none');
					}

				},
				error: function(error) {
					console.error('Có lỗi khi lấy thông tin chi tiết đơn hàng:', error);
				}
			});

		});
		$('#updateOrderStatusModal').on('hidden.bs.modal', function() {
			$('#updateOrderStatusModal').attr('inert', true);
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
					var orderStatusData = response[6]
					var currentOrderStatusId = response[7];

					var orderGoodsCategoryData = response[8];
					var currentOrderGoodsCategoryId = response[9];

					var orderSenderData = response[10];
					var currentOrderSenderId = response[11];

					var orderReceiverData = response[12];
					var currentOrderReceiverId = response[13];

					$('#orderIdUpdate').text(response[0]);
					$('#orderCodeUpdate').text(response[1]);
					$('#orderCodeUpdateInput').val(response[1]);
					$('#orderDeliveryDateUpdate').val(response[2]);
					$('#orderCreateDateUpdate').val(response[3]);
					$('#orderTransportUpdate').val(response[4]);
					$('#orderRequirementUpdate').val(response[5]);
					$('#orderAddressUpdate').val(response[14]);

					if (orderStatusData && orderStatusData.length > 0) {
						var options = orderStatusData.map(function(status) {
							var selected = status[0] === currentOrderStatusId ? ' selected' : '';
							return '<option value="' + status[0] + '"' + selected + '>' + status[1] + '</option>';
						}).join('');

						$('#orderStatusUpdate').html(options);
					}

					if (orderGoodsCategoryData && orderGoodsCategoryData.length > 0) {
						var options = orderGoodsCategoryData.map(function(goodscategory) {
							var selected = goodscategory[0] === currentOrderGoodsCategoryId ? ' selected' : '';
							return '<option value="' + goodscategory[0] + '"' + selected + '>' + goodscategory[1] + '</option>';
						}).join('');

						$('#orderGoodsUpdate').html(options);
					}

					if (orderSenderData && orderSenderData.length > 0) {
						var options = orderSenderData.map(function(sender) {
							var selected = sender[0] === currentOrderSenderId ? ' selected' : '';
							return '<option value="' + sender[0] + '"' + selected + '>' + sender[1] + '</option>';
						}).join('');
						$('#orderSenderNameUpdate').html(options);
					}

					if (orderReceiverData && orderReceiverData.length > 0) {
						var options = orderReceiverData.map(function(receiver) {
							var selected = receiver[0] === currentOrderReceiverId ? ' selected' : '';
							return '<option value="' + receiver[0] + '"' + selected + '>' + receiver[1] + '</option>';
						}).join('');
						$('#orderReceiverNameUpdate').html(options);
					}


					//$('#orderGoodsUpdate').html('<option value="' + orderGoodsCategoryId + '">' + orderGoodsCategoryName + '</option>');
					//$('#orderSenderNameUpdate').html('<option value="' + orderCustomerId + '">' + orderCustomerName + '</option>');

					document.getElementById("updateOrderModal").style.display = "block";
				},
				error: function(error) {
					console.error('Có lỗi khi lấy thông tin chi tiết đơn hàng:', error);
				}
			});

		});

		$('#tblDatatable tbody').on('click', '.detail-btn', function() {
			var row = table.row($(this).closest('tr')).data(); // Lấy dữ liệu của dòng được nhấp vào
			var orderId = row[0];

			console.log('Xem chi tiết cho ID đơn hàng: ', orderId);

			$.ajax({
				url: _ctx + 'orders-datatable/viewDetails/' + orderId,
				method: 'GET',
				success: function(response) {
					console.log(response)
					var orderStatusData = response[6]
					var currentOrderStatusId = response[7];

					var orderGoodsCategoryData = response[8];
					var currentOrderGoodsCategoryId = response[9];

					var orderSenderData = response[10];
					var currentOrderSenderId = response[11];

					var orderReceiverData = response[12];
					var currentOrderReceiverId = response[13];


					$('#orderIdDetail').text(response[0]);
					$('#orderCodeDetail').text(response[1]);
					$('#orderCodeDetailInput').val(response[1]);
					$('#orderDeliveryDateDetail').val(response[2]);
					$('#orderCreateDateDetail').val(response[3]);
					$('#orderTransportDetail').val(response[4]);
					$('#orderRequirementDetail').val(response[5]);
					$('#orderAddressDetail').val(response[14]);

					if (orderStatusData && orderStatusData.length > 0) {
						var options = orderStatusData.map(function(status) {
							var selected = status[0] === currentOrderStatusId ? ' selected' : '';
							return '<option value="' + status[0] + '"' + selected + '>' + status[1] + '</option> readonly';
						}).join('');

						$('#orderStatusDetail').html(options);
						$('#orderStatusDetail').css('pointer-events', 'none');
					}

					if (orderGoodsCategoryData && orderGoodsCategoryData.length > 0) {
						var options = orderGoodsCategoryData.map(function(goodscategory) {
							var selected = goodscategory[0] === currentOrderGoodsCategoryId ? ' selected' : '';
							return '<option value="' + goodscategory[0] + '"' + selected + '>' + goodscategory[1] + '</option>';
						}).join('');

						$('#orderGoodsDetail').html(options);
						$('#orderGoodsDetail').css('pointer-events', 'none');
					}

					if (orderSenderData && orderSenderData.length > 0) {
						var options = orderSenderData.map(function(sender) {
							var selected = sender[0] === currentOrderSenderId ? ' selected' : '';
							return '<option value="' + sender[0] + '"' + selected + '>' + sender[1] + '</option>';
						}).join('');
						$('#orderSenderNameDetail').html(options);
						$('#orderSenderNameDetail').css('pointer-events', 'none');
					}

					if (orderReceiverData && orderReceiverData.length > 0) {
						var options = orderReceiverData.map(function(receiver) {
							var selected = receiver[0] === currentOrderReceiverId ? ' selected' : '';
							return '<option value="' + receiver[0] + '"' + selected + '>' + receiver[1] + '</option>';
						}).join('');
						$('#orderReceiverNameDetail').html(options);
						$('#orderReceiverNameDetail').css('pointer-events', 'none');
					}


					document.getElementById("orderDetailModal").style.display = "block";
				},
				error: function(error) {
					console.error('Có lỗi khi lấy thông tin chi tiết đơn hàng:', error);
				}
			});
		});

		$('#tblDatatable tbody').on('click', '.delete-btn', function() {
			var row = table.row($(this).closest('tr')).data(); // Lấy dữ liệu dòng
			var orderId = row[0]; // ID của đơn hàng
			console.log('Xóa đơn hàng với ID:', orderId);

			if (confirm("Bạn có chắc chắn muốn xóa đơn hàng này?")) {
				$.ajax({
					url: _ctx + 'orders-datatable/delete-order',
					method: 'DELETE',
					contentType: 'application/json',
					data: JSON.stringify({ id: orderId }), // Truyền dữ liệu JSON
					success: function(response) {
						if (response.status === "success") {
							alert(response.message);
							location.reload();
							// Reload lại DataTable
							table.row($(this).parents('tr')).remove().draw();
						} else {
							alert(response.message);
						}
					},
					error: function(error) {
						console.error('Lỗi khi xóa đơn hàng:', error);
						alert('Có lỗi xảy ra khi xóa đơn hàng.');
					}
				});
			}
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
	var senderEmail = $('#orderSenderEmailUpdate').val();
	var receiverName = $('#orderReceiverNameUpdate').val();
	var receiverPhone = $('#orderReceiverPhoneUpdate').val();
	var receiverEmail = $('#orderReceiverEmailUpdate').val();
	var transport = $('#orderTransportUpdate').val();
	var requirement = $('#orderRequirementUpdate').val();
	var address = $('#orderAddressUpdate').val();


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
		senderEmail: senderEmail,

		receiverName: receiverName,
		receiverPhone: receiverPhone,
		receiverEmail: receiverEmail,

		transport: transport,
		requirement: requirement,
		address: address,
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
				location.reload();
				//$('#tblDatatable').DataTable().ajax.reload();
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

function closeUpdateOrderStatusModal() {
	/*document.getElementById("updateOrderStatusModal").style.display = "none";
	document.getElementsByClassName("modal fade").style.display = "none";*/
	$('#updateOrderStatusModal').modal('hide');
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

