$(document).ready(function() {
	$('#addNewCustomerForm').on('submit', function(event) {
		event.preventDefault();

		const name = $('#name').val().trim();
		const address = $('#address').val().trim();
		const phone = $('#phone').val().trim();

		if (!name || !address || !phone) {
			let errorMessage = 'Vui lòng điền đầy đủ thông tin:\n';

			if (!name) errorMessage += '- Tên khách hàng\n';
			if (!address) errorMessage += '- Địa chỉ\n';
			if (!phone) errorMessage += '- Số điện thoại\n';

			$('#errorMessage').text(errorMessage).show(); // Hiển thị thông báo lỗi
			setTimeout(function() {
				$('#errorMessage').fadeOut(); // Ẩn thông báo sau 3 giây
			}, 3000);

			return; // Ngăn không cho submit form nếu còn trường trống
		}

		const customerData = {
			name: $('#name').val(),
			address: $('#address').val(),
			phone: $('#phone').val(),
		};
		console.log(customerData);

		// Gửi yêu cầu POST tới API để lưu khách hàng
		fetch('/crm-web/create-customer', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(customerData),
		})
			.then(response => {
				if (!response.ok) {
					return response.json().then(err => { throw new Error(err.errorMessage); });
				}
				return response.json();
			})
			.then(data => {
				// Đóng modal và reset form sau khi lưu
				$('#addNewCustomerModal').modal('hide');
				$('#addNewCustomerForm')[0].reset();
				alert('Khách hàng mới đã được thêm!');
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
