$(document).ready(function() {
	$('#addNewCustomerForm').on('submit', function(event) {
		event.preventDefault();

		const customerData = {
			name: $('#name').val(),
			address: $('#address').val(),
			phone: $('#phone').val(),
		};

		// Gửi yêu cầu POST tới API để lưu khách hàng
		fetch('/crm-web/api/addNewCustomers', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(customerData),
		})
			.then(response => response.json())
			.then(data => {
				// Đóng modal và reset form sau khi lưu
				$('#addNewCustomerModal').modal('hide');
				$('#addNewCustomerForm')[0].reset();
				alert('Khách hàng mới đã được thêm!');
			})
			.catch(error => {
				console.error('Error:', error);
				alert('Đã có lỗi xảy ra, vui lòng thử lại!');
			});
	});
});


//$(document).ready(function() {
//    $('#addNewCustomerModal').modal('show');
//});