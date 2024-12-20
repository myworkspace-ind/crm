document.addEventListener('DOMContentLoaded', function() {
	const searchButton = document.querySelector('.btn-search-orders');
	const customerChipsContainer = document.getElementById('customer-chips-container');
	const orderTypeChipsContainer = document.getElementById('order-type-chips-container');

	searchButton.addEventListener('click', function() {
		const customerIds = Array.from(customerChipsContainer.querySelectorAll('.chip')).map(chip => chip.dataset.id);
		const customerIdsLong = customerIds.map(id => Number(id)); 
		const orderTypeIds = Array.from(orderTypeChipsContainer.querySelectorAll('.chip')).map(chip => chip.dataset.id);
		const categoryIdsLong = orderTypeIds.map(id => Number(id)); 
		
		const selectedStatuses = [];
		const checkboxes = document.querySelectorAll('input[name="orderStatus"]:checked');
		const orderDate = document.getElementById('orderDate').value;
	    const deliveryDate = document.getElementById('deliveryDate').value;
		
		// Chuyển đổi ngày sang định dạng Date object
	    const orderDateObj = orderDate ? new Date(orderDate) : null;
	    const deliveryDateObj = deliveryDate ? new Date(deliveryDate) : null;

	    if (orderDateObj && deliveryDateObj && deliveryDateObj < orderDateObj) {
	        alert('Ngày giao không được nhỏ hơn ngày lập.');
	        return; // Dừng lại
	    }
		
		const formattedOrderDate = orderDate ? new Date(orderDate).toISOString().split('T')[0] : '1970-01-01'; // Ngày nhỏ nhất
        const formattedDeliveryDate = deliveryDate ? new Date(deliveryDate).toISOString().split('T')[0] : '9999-12-31'; // Ngày lớn nhất
		checkboxes.forEach(checkbox => {
			selectedStatuses.push(parseInt(checkbox.value, 10));
		});
		//const _ctx = "/crm-web/";
		const paramsObject = {
			customerIds: customerIdsLong.length > 0 ? customerIdsLong : '',
			orderCategoryIds: categoryIdsLong.length > 0 ? categoryIdsLong : '',
			statuses: selectedStatuses.length > 0 ? selectedStatuses : '',
			create_date: formattedOrderDate ,
			delivery_date: formattedDeliveryDate 
		};

		// Xây dựng query string từ các tham số
		const params = new URLSearchParams(paramsObject).toString();
		const requestUrl = `${_ctx}orders-datatable/search-orders-list?${params}`;

		console.log("Request URL:", requestUrl);

		fetch(`${_ctx}orders-datatable/search-orders-list?${params}`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
			},
		})
			.then(response => {
				// Kiểm tra response có JSON không
				if (!response.ok) {
					throw new Error(`HTTP error! status: ${response.status}`);
				}
				return response.text(); // Lấy text để kiểm tra nội dung
			})
			.then(text => {
				const data = text ? JSON.parse(text) : [];
				const resultContainer = document.querySelector('#tblDatatable tbody');
				const messageContainer = document.querySelector('#noResultsMessage');

				// Xóa thông báo cũ nếu có
				if (messageContainer) {
					messageContainer.remove();
				}

				if (resultContainer) {
					// Xóa nội dung cũ trong bảng
					resultContainer.innerHTML = '';

					if (data && data.length > 0) {
						orderTableSearchResult(data);
					} else {
						// Tạo thông báo bên ngoài bảng
						const newMessage = document.createElement('div');
						newMessage.id = 'noResultsMessage';
						newMessage.textContent = 'Không có kết quả phù hợp.';
						newMessage.style.cssText = `
							padding: 10px;
							margin: 10px 0;
							background-color: #f8d7da;
							color: #721c24;
							border: 1px solid #f5c6cb;
							border-radius: 4px;
							text-align: center;
							font-size: 16px;
						`;
						// Đặt thông báo ngay trên bảng
						resultContainer.parentElement.parentElement.insertBefore(newMessage, resultContainer.parentElement);
					}
				} else {
					console.error('#tblDatatable tbody không tồn tại.');
				}
			});
	});
	function loadCustomerInfo(customerId, phone, name) {
		fetch(`${_ctx}orders-datatable/get-sender-receiver-details?customerId=${customerId}`)
			.then(response => response.json())
			.then(data => {
				if (data) {
					phone.value = data.phone || "";
					name.value = data.email || "";					
				}
			})
			.catch(error => {
				console.error("Error fetching sender details:", error);
			});
	}
	function orderTableSearchResult(data) {
		var tbody = document.querySelector('#tblDatatable tbody');
		tbody.innerHTML = '';

		data.forEach(order => {
			var row = document.createElement('tr');
			row.innerHTML = `
			            <td>${order[0]}</td>  <!-- ID -->
			            <td>${order[1]}</td>  <!-- Mã đơn hàng -->
			            <td>${order[2]}</td>  <!-- Ngày giao -->
			            <td>${order[3]}</td>  <!-- Loại hàng hóa -->
			            <td>${order[4]}</td>  <!-- Thông tin người gửi -->
			            <td>${order[5]}</td>  <!-- Phương tiện vận chuyển -->
			        `;
			tbody.appendChild(row);
			console.log("orderId:", order[0]);
		});

		if ($.fn.DataTable.isDataTable('#tblDatatable')) {
			$('#tblDatatable').DataTable().clear().destroy();

			var table = $('#tblDatatable').DataTable({
				data: data,
				dom: 'Bfrtip',
				paging: true,
				searching: true,
				ordering: true,
				lengthMenu: [5, 10, 25],
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
												<button class='btn btn-info detailOrderSearch-btn'>Xem chi tiết</button>
												<button class='btn btn-warning editOrderSearch-btn'>Sửa</button>
												<button class='btn btn-danger deleteOrderSearch-btn'>Xóa</button>
												<button class='btn btn-success updateOrderStatusSearch-btn'>Cập nhật trạng thái</button>
											</div>
									</div>
								</div> `
					}
				]
			});

			// Attach event listeners to buttons
			$('#tblDatatable tbody').off('click', '.editOrderSearch-btn').on('click', '.editOrderSearch-btn', function() {
				var row = table.row($(this).closest('tr')).data(); // Lấy dữ liệu của dòng được nhấp vào
				console.log("Dòng được nhấp vào: ", row);
				var orderId = row[0];
				console.log("Row[0]: ", orderId);

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
							loadCustomerInfo(currentOrderSenderId, document.getElementById("orderSenderPhoneUpdate"), document.getElementById("orderSenderEmailUpdate"));
							$('#orderSenderNameUpdate').html(options);
						}

						if (orderReceiverData && orderReceiverData.length > 0) {
							var options = orderReceiverData.map(function(receiver) {
								var selected = receiver[0] === currentOrderReceiverId ? ' selected' : '';
								return '<option value="' + receiver[0] + '"' + selected + '>' + receiver[1] + '</option>';
							}).join('');
							loadCustomerInfo(currentOrderReceiverId, document.getElementById("orderReceiverPhoneUpdate"), document.getElementById("orderReceiverEmailUpdate"));
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

			$('#tblDatatable tbody').off('click', '.detailOrderSearch-btn').on('click', '.detailOrderSearch-btn', function() {
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
							loadCustomerInfo(currentOrderSenderId, document.getElementById("orderSenderPhoneDetail"), document.getElementById("orderSenderEmailDetail"));
							$('#orderSenderNameDetail').html(options);
							$('#orderSenderNameDetail').css('pointer-events', 'none');
						}

						if (orderReceiverData && orderReceiverData.length > 0) {
							var options = orderReceiverData.map(function(receiver) {
								var selected = receiver[0] === currentOrderReceiverId ? ' selected' : '';
								return '<option value="' + receiver[0] + '"' + selected + '>' + receiver[1] + '</option>';
							}).join('');
							loadCustomerInfo(currentOrderReceiverId, document.getElementById("orderReceiverPhoneDetail"), document.getElementById("orderReceiverEmailDetail"));
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

			$('#tblDatatable tbody').off('click', '.deleteOrderSearch-btn').on('click', '.deleteOrderSearch-btn', function() {
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

			$('#tblDatatable tbody').off('click', '.updateOrderStatusSearch-btn').on('click', '.updateOrderStatusSearch-btn', function() {
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
						$('#orderIdUpdateStatus').text(response[0]);

						var orderStatusData = response[6]
						var currentOrderStatusId = response[7];

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

						if (orderStatusData && orderStatusData.length > 0) {
							var options = orderStatusData.map(function(status) {
								var selected = status[0] === currentOrderStatusId ? ' selected' : '';
								return '<option value="' + status[0] + '"' + selected + '>' + status[1] + '</option>';
							}).join('');

							$('#orderStatusUpdateStatus').html(options);
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

			$(document).on('click', '#saveOrderStatusButton', function() {
				var button = $(this);
				button.prop('disabled', true); // Disable button to prevent multiple clicks

				var orderId = $('#orderIdUpdateStatus').text();
				var orderStatus = $('#orderStatusUpdateStatus').val();

				console.log(orderId); // In ra giá trị orderId
				console.log(orderStatus); // In ra giá trị orderCode

				if (!orderId) {
					alert('ID đơn hàng không hợp lệ.');
					button.prop('disabled', false); // Enable button if id is not valid
					return;
				}

				var order = {
					id: orderId,
					orderStatus: orderStatus,
				};

				// In toàn bộ đối tượng order ra console
				console.log("Order object being sent:", order);

				$.ajax({
					url: _ctx + '/orders-datatable/saveOrderStatus/',
					method: 'POST',
					contentType: 'application/json',
					data: JSON.stringify(order),

					success: function(response) {
						console.log("UDPATE FINAL ORDER STATUS: ", order); // In lại order khi thành công
						if (response.status === "success") {
							alert(response.message);
							document.getElementById("updateOrderStatusModal").style.display = "none";
							location.reload();
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
		}
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
