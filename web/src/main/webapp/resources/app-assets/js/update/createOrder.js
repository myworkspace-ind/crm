/**
 * 
 */
const openCreateOrderBtn = document.getElementById('openCreateOrderBtn');
const createOrderModal = document.getElementById('createOrderModal');
const modalOverlay = document.getElementById('modalOverlay');

openCreateOrderBtn.addEventListener('click', function(){
	createOrderModal.style.display = 'block';
	modalOverlay.style.display = 'block';
})

function closeModal() {
	createOrderModal.style.display = 'none';
	modalOverlay.style.display = 'none';
}

function changeTitle(newTitle){
	document.getElementById("title").innerText = newTitle;
}

$(document).ready(function() {
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
});
