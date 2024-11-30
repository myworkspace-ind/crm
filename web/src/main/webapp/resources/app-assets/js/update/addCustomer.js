/*document.getElementById('addNewCustomerForm').addEventListener('submit', function(event) {
	event.preventDefault(); // Ngừng gửi form mặc định

	// Lấy giá trị từ các trường nhập liệu
	const companyName = document.getElementById('company_name').value.trim();
	const contactPerson = document.getElementById('contact_person').value.trim();
	const email = document.getElementById('email').value.trim();
	const phone = document.getElementById('phone').value.trim();
	const address = document.getElementById('address').value.trim();
	const responsiblePerson = document.getElementById('responsible_person').value.trim();
	const note = document.getElementById('note').value.trim();
	const profession = document.getElementById('profession').value.trim();
	const mainStatus = document.getElementById('main_status').value.trim();
	const subStatus = document.getElementById('sub_status').value.trim();

	// Kiểm tra các trường bắt buộc
	let errorMessage = '';
	if (!companyName) errorMessage += '- Tên công ty không được để trống\n';
	if (!contactPerson) errorMessage += '- Người liên hệ chính không được để trống\n';
	if (!email) errorMessage += '- Email không được để trống\n';
	if (!phone) errorMessage += '- Số điện thoại không được để trống\n';
	if (!address) errorMessage += '- Địa chỉ không được để trống\n';

	if (errorMessage) {
		alert('Vui lòng điền đầy đủ thông tin:\n' + errorMessage);
		return;
	}

	// Tạo object dữ liệu từ các trường nhập liệu
	const customerData = {
		companyName: companyName,
		contactPerson: contactPerson,
		email: email,
		phone: phone,
		address: address,
		responsiblePerson: responsiblePerson || null, // Nếu không có người phụ trách thì gửi null
		note: note || '', // Ghi chú có thể trống
		profession: profession || '', // Chuyên môn có thể trống
		mainStatus: mainStatus || '', // Trạng thái chính
		subStatus: subStatus || '', // Trạng thái phụ
	};
	const _ctx = "/crm-web/";
	// Gửi yêu cầu POST đến server với dữ liệu dạng JSON
	fetch(`${_ctx}customer/create-customer`, {
		method: 'POST', // Phải là POST để gửi dữ liệu
		headers: {
			'Content-Type': 'application/json', // Đảm bảo Content-Type là application/json
		},
		body: JSON.stringify(customerData) // Chuyển dữ liệu thành JSON
	})
	.then(response => {
		console.log("Response status: ", response.status); // Kiểm tra mã trạng thái HTTP
		return response.json(); // Chuyển phản hồi sang JSON
	})
	.then(data => {
		// Xử lý kết quả thành công
		alert('Khách hàng đã được thêm!');
		document.getElementById('addNewCustomerForm').reset(); // Reset form sau khi lưu
	})
	.catch(error => {
		// Xử lý lỗi
		console.error('Có lỗi xảy ra:', error);
		alert('Có lỗi xảy ra khi lưu khách hàng.');
	});
});
*/

function saveNewCustomer() {
	// Lấy giá trị từ các trường nhập liệu
		const companyName = document.getElementById('company_name').value.trim();
		const contactPerson = document.getElementById('contact_person').value.trim();
		const email = document.getElementById('email').value.trim();
		const phone = document.getElementById('phone').value.trim();
		const address = document.getElementById('address').value.trim();
		const responsiblePerson = document.getElementById('responsible_person').value.trim();
		const note = document.getElementById('note').value.trim();
		const profession = document.getElementById('profession').value.trim();
		const mainStatus = document.getElementById('main_status').value.trim();
		const subStatus = document.getElementById('sub_status').value.trim();

		// Kiểm tra các trường bắt buộc
		let errorMessage = '';
		if (!companyName) errorMessage += '- Tên công ty không được để trống\n';
		if (!contactPerson) errorMessage += '- Người liên hệ chính không được để trống\n';
		if (!email) errorMessage += '- Email không được để trống\n';
		if (!phone) errorMessage += '- Số điện thoại không được để trống\n';
		if (!address) errorMessage += '- Địa chỉ không được để trống\n';

		if (errorMessage) {
			alert('Vui lòng điền đầy đủ thông tin:\n' + errorMessage);
			return;
		}

		// Tạo object dữ liệu từ các trường nhập liệu
		const customerData = {
			companyName: companyName,
			contactPerson: contactPerson,
			email: email,
			phone: phone,
			address: address,
			responsiblePerson: responsiblePerson || null, // Nếu không có người phụ trách thì gửi null
			note: note || '', // Ghi chú có thể trống
			profession: profession || '', // Chuyên môn có thể trống
			mainStatus: mainStatus || '', // Trạng thái chính
			subStatus: subStatus || '', // Trạng thái phụ
		};
		const _ctx = "/crm-web/";
		// Gửi yêu cầu POST đến server với dữ liệu dạng JSON
		fetch(`${_ctx}customer/create-customer`, {
			method: 'POST', // Phải là POST để gửi dữ liệu
			headers: {
				'Content-Type': 'application/json', // Đảm bảo Content-Type là application/json
			},
			body: JSON.stringify(customerData) // Chuyển dữ liệu thành JSON
		})
		.then(response => {
			console.log("Response status: ", response.status); // Kiểm tra mã trạng thái HTTP
			return response.json(); // Chuyển phản hồi sang JSON
		})
		.then(data => {
			// Xử lý kết quả thành công
			alert('Khách hàng đã được thêm!');
			document.getElementById('addNewCustomerForm').reset(); // Reset form sau khi lưu
		})
		.catch(error => {
			// Xử lý lỗi
			console.error('Có lỗi xảy ra:', error);
			alert('Có lỗi xảy ra khi lưu khách hàng.');
		});
}

// Hàm quay lại (có thể được thêm vào nếu cần)
function goBack() {
	
}