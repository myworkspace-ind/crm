
function showHidedCustomers() {
	selectedCustomerId = customerId;
	const modal = document.getElementById('showHidedCustomersConfirmationModal');
	const overlay = document.getElementById('overlay');

	// Hiển thị modal và overlay
	modal.style.display = 'block';
	overlay.style.display = 'block';
}


document.getElementById('confirmShowBtn').addEventListener('click', function() {
	fetch(`${_ctx}customer/show-hidedcustomers`, {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
	})
		.then(response => response.json())
		.then(data => {
			closeModal(); // Đóng modal sau khi xử lý
			if (data.errorMessage) {
				alert("Lỗi: " + data.errorMessage);
			} else {
				alert("Khách hàng đã được khôi phục thành công!");
				// Reload hoặc cập nhật giao diện sau khi xóa
				location.href = `${_ctx}customer/list`;
			}
		})
		.catch(error => {
			closeModal(); // Đóng modal nếu có lỗi
			alert("Có lỗi xảy ra. Vui lòng thử lại sau!");
			console.error("Error:", error);
		});

});


document.getElementById('cancelShowBtn').addEventListener('click', function() {
	closeModal(); // Đóng modal
});


function closeModal() {
	const modal = document.getElementById('showHidedCustomersConfirmationModal');
	const overlay = document.getElementById('overlay');

	modal.style.display = 'none';
	overlay.style.display = 'none';
}
