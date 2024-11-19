/**
 * 
 */
const openCreateOrderBtn = document.getElementById('openCreateOrderBtn');
const createOrderModal = document.getElementById('createOrderModal');
const modalOverlay = document.getElementById('modalOverlay');

openCreateOrderBtn.addEventListener('click', function() {
	createOrderModal.style.display = 'block';
	modalOverlay.style.display = 'block';
})

function closeModal() {
	createOrderModal.style.display = 'none';
	modalOverlay.style.display = 'none';
}

function changeTitle(newTitle) {
	document.getElementById("title").innerText = newTitle;
}

document.getElementById("createOrderButton").addEventListener("click", function() {
	const orderData = {
		createDate: document.getElementById("orderCreateDateCreate").value,
		deliveryDate: document.getElementById("orderDeliveryDateCreate").value,
		orderCode: document.getElementById("orderCodeInputCreate").value,
		orderCategory: document.getElementById("orderCategoryCreate").value,
		goodsCategory: document.getElementById("orderGoodsCreate").value,
		orderStatus: document.getElementById("orderStatusCreate").value,
		transportMethod: document.getElementById("orderTransportCreate").value,
		sender: document.getElementById("orderSenderNameCreate").value,
		receiver: document.getElementById("orderReceiverNameCreate").value,
		requirement: document.getElementById("orderRequirementCreate").value,
		address: document.getElementById("orderAddressCreate").value
	};

	// Kiểm tra nếu dữ liệu đã đầy đủ trước khi gửi
	if (!orderData.createDate) {
		alert("Vui lòng điền ngày tạo đơn!");
		return;
	}
	if (!orderData.deliveryDate) {
		alert("Vui lòng điền ngày giao hàng!");
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

	const _ctx = "/crm-web/";
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
			if (data.reload === "true") {
				reloadOrdersData();
			}
		})
		.catch(error => {
			console.error("Lỗi khi tạo đơn hàng:", error);
			alert("Đã xảy ra lỗi: " + error.message);
		});

	// Hàm reload lại dữ liệu
	function reloadOrdersData() {
		fetch(`${_ctx}orders-datatable/`)  // Hoặc endpoint lấy danh sách orders
			.then(response => response.json())
			.then(data => {
				// Xử lý dữ liệu và cập nhật giao diện
			})
			.catch(error => console.error('Error loading orders:', error));
	}
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
