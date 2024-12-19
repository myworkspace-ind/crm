/**
 * 
 */
const openCreateOrderBtn = document.getElementById('openCreateOrderBtn');
const createOrderModal = document.getElementById('createOrderModal');
//const modalOverlay = document.getElementById('modalOverlay');

openCreateOrderBtn.addEventListener('click', function() {
	createOrderModal.style.display = 'block';
	//modalOverlay.style.display = 'block';
})

function closeModal() {
	createOrderModal.style.display = 'none';
	//modalOverlay.style.display = 'none';
}

function changeTitle(newTitle) {
	document.getElementById("title").innerText = newTitle;
}

function getDataIdFromDatalist(datalistId, value) {
	let dataId = null;
	$('#' + datalistId + ' option').each(function() {
		if ($(this).val() === value) {
			dataId = $(this).data('id');
		}
	});
	return dataId;
}

function checkSelectionGoodOrder() {
	var input = document.getElementById('orderCodeInputCreate');
	var datalist = document.getElementById('orderCodes');
	var options = datalist.options;
	for (var i = 0; i < options.length; i++) {
		if (options[i].value === input.value) {
			alert('Bạn đã tạo đơn hàng: ' + input.value + '. Vui lòng nhập Mã đơn hàng khác không có trong danh sách gợi ý!!!!');
			break;
		}
	}
}

document.getElementById("createOrderButton").addEventListener("click", function() {
	let orderCategoryName = $('#orderCategoryCreate').val();
    let orderCategoryId = getDataIdFromDatalist('orderCategories', orderCategoryName);

    // Lấy giá trị của loại hàng hóa
    let goodsCategoryName = $('#orderGoodsCreate').val();
    let goodsCategoryId = getDataIdFromDatalist('goodsCategories', goodsCategoryName);
	
	const orderData = {
		createDate: document.getElementById("orderCreateDateCreate").value,
		deliveryDate: document.getElementById("orderDeliveryDateCreate").value,
		orderCode: document.getElementById("orderCodeInputCreate").value,
		orderCategory: orderCategoryId ? orderCategoryId.toString() : null,
		goodsCategory: goodsCategoryId ? goodsCategoryId.toString() : null,
		orderStatus: document.getElementById("orderStatusCreate").value,
		transportMethod: document.getElementById("orderTransportCreate").value,
		sender: document.getElementById("orderSenderNameCreate").value,
		receiver: document.getElementById("orderReceiverNameCreate").value,
		requirement: document.getElementById("orderRequirementCreate").value,
		address: document.getElementById("orderAddressCreate").value
	};
	
	// Kiểm tra ngày tạo và ngày giao
	const createDate = new Date(orderData.createDate);
	const deliveryDate = new Date(orderData.deliveryDate);
	const currentDate = new Date();
	
	// Đặt giờ, phút, giây và mili giây của cả ngày tạo, ngày giao và ngày hiện tại về 0 để chỉ so sánh phần ngày tháng năm mà không so sánh tới giờ phút giây
	createDate.setHours(0, 0, 0, 0);
	currentDate.setHours(0, 0, 0, 0);
	deliveryDate.setHours(0, 0, 0, 0);

	// Kiểm tra nếu dữ liệu đã đầy đủ trước khi gửi
	if (!orderData.createDate) {
		alert("Vui lòng điền ngày tạo đơn!");
		return;
	}
	
	// Kiểm tra nếu ngày tạo đơn nhỏ hơn ngày hiện tại
	if (createDate < currentDate) {
	    alert("Ngày tạo đơn không được ở quá khứ!");
	    return;
	}
	
	if (!orderData.deliveryDate) {
		alert("Vui lòng điền ngày giao hàng!");
		return;
	}
	
	// Kiểm tra nếu ngày giao hàng nhỏ hơn ngày hiện tại
	if (deliveryDate < currentDate) {
	    alert("Ngày giao hàng không được ở quá khứ!");
	    return;
	}

	// Kiểm tra nếu ngày tạo lớn hơn ngày giao
	if (createDate > deliveryDate) {
	    alert("Ngày tạo đơn không được lớn hơn ngày giao hàng!");
	    return;
	}
	
	if (!orderData.orderCode) {
		alert("Vui lòng điền mã đơn hàng!");
		return;
	}
	if (!orderData.orderCategory) {
		alert("Vui lòng chọn danh mục đơn hàng!");
		return;
	}
	if (!orderData.goodsCategory) {
		alert("Vui lòng chọn loại hàng hóa!");
		return;
	}
	if (!orderData.orderStatus) {
		alert("Vui lòng chọn trạng thái đơn hàng!");
		return;
	}
	if (!orderData.transportMethod) {
		alert("Vui lòng chọn phương thức vận chuyển!");
		return;
	}
	if (!orderData.sender) {
		alert("Vui lòng điền tên người gửi!");
		return;
	}
	if (!orderData.receiver) {
		alert("Vui lòng điền tên người nhận!");
		return;
	}
	if (!orderData.requirement) {
		alert("Vui lòng điền yêu cầu! Nếu không có yêu cầu gì, vui lòng điền 'Không có'");
		return;
	}
	if (!orderData.address) {
		alert("Vui lòng điền địa chỉ nhận hàng!");
		return;
	}

	//const _ctx = "/crm-web/";
	// Gửi request POST tới backend để lưu đơn hàng
	fetch(`${_ctx}orders-datatable/create-order`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(orderData)
	})
		.then(response => {
			if (!response.ok) {
				// Nếu response không thành công, ném ra lỗi
				return response.json().then(err => {
					throw new Error(err.errorMessage || 'Có lỗi xảy ra.');
				});
			}
			return response.json();
		})
		.then(data => {
			alert(data.message);  // Hiển thị thông báo từ backend
			closeModal();  // Đóng modal sau khi tạo đơn hàng thành công
			location.reload();
		})
		.catch(error => {
			console.error("Lỗi khi tạo đơn hàng:", error);
			alert("Đã xảy ra lỗi: " + error.message);
		});

});


/*$(document).ready(function() {
	$('#orderCreateForm').on('submit', function(event) {
		event.preventDefault();

		const createDateCreate = $('#orderCreateDateCreate').val().trim();
		const deliveryDateCreate = $('#orderDeliveryDateCreate').val().trim();
		const orderStatusCreate = $('#orderStatusCreate').val().trim();
		const orderCodeInputCreate = $('#orderCodeInputCreate').val().trim();
		const goodsCategoryCreate = $('#orderGoodsCreate').val().trim();
		const transportMethodCreate = $('#orderTransportCreate').val().trim();
		 

		if (!createDateCreate || !deliveryDateCreate || !orderStatusCreate || !orderCodeInputCreate || !goodsCategoryCreate || !transportMethodCreate) {
			let errorMessage = 'Vui lòng điền đầy đủ thông tin:\n';

			if (!createDateCreate) errorMessage += '- Ngày tạo đơn hàng\n';
			if (!deliveryDateCreate) errorMessage += '- Ngày giao đơn hàng\n';
			if (!orderStatusCreate) errorMessage += '- Trạng thái đơn hàng\n';
			if (!orderCodeInputCreate) errorMessage += '- Mã đơn hàng\n';
			if (!goodsCategoryCreate) errorMessage += '- Loại hàng hóa của đơn hàng\n';
			if (!transportMethodCreate) errorMessage += '- Phương tiện vận chuyển cho đơn hàng\n';
			
			$('#errorMessage').text(errorMessage).show();
			setTimeout(function() {
				$('#errorMessage').fadeOut(); 
			}, 3000);

			return; 
		}

		const orderData = {
			createDateCreate: $('#orderCreateDateCreate').val(),
			deliveryDateCreate: $('#orderDeliveryDateCreate').val(),
			orderStatusCreate: $('#orderStatusCreate').val(),
			orderCodeInputCreate: $('#orderCodeInputCreate').val(),
			goodsCategoryCreate: $('#orderGoodsCreate').val(),
			transportMethodCreate: $('#orderTransportCreate').val(),
		};
		console.log(orderData);

		// Gửi yêu cầu POST tới API để lưu khách hàng
		fetch('/crm-web/orders-datatable/create-order', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(orderData),
		})
			.then(response => {
				if (!response.ok) {
					return response.json().then(err => { throw new Error(err.errorMessage); });
				}
				return response.json();
			})
			.then(data => {
				// Đóng modal và reset form sau khi lưu
				$('#orderCreateForm').modal('hide');
				$('#orderCreateForm')[0].reset();
				alert('Đơn hàng mới đã được thêm!');
				location.reload(); // Reload lại trang để cập nhật danh sách khách hàng
			})
			.catch(error => {
				console.error('Error:', error);
				$('#errorMessage').text(error.message).show();  // Hiển thị lỗi trong modal

				setTimeout(function() {
					$('#errorMessage').fadeOut(); // Ẩn thông báo
				}, 3000);
			});
	});
});*/
