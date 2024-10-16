function fetchCustomersByStatus(statusId) {
    // Lấy form
    const form = document.getElementById('frmScrollImages');
    
    // Xóa tất cả các input ẩn có tên là 'statusId'
    const existingInputs = form.querySelectorAll('input[name="statusId"]');
    existingInputs.forEach(input => input.remove());

    // Tạo một input ẩn để chứa statusId
    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'statusId'; // Đặt tên cho input
    input.value = statusId;  // Gán giá trị là statusId

    // Thêm input vào form
    form.appendChild(input);

    // Gửi form
    form.submit();
}


// Gán sự kiện nhấp chuột cho các phần tử trạng thái
document.querySelectorAll('.child').forEach(child => {
	child.addEventListener('click', function() {
		const statusId = this.getAttribute('data-status-id'); // Lấy statusId từ thuộc tính data (nếu có)
		fetchCustomersByStatus(statusId);
	});
});