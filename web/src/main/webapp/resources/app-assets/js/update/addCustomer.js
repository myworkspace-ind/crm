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
	if (!companyName) errorMessage += 'Tên công ty không được để trống\n';
	if (!contactPerson) errorMessage += 'Người liên hệ chính không được để trống\n';
	if (!email) errorMessage += 'Email không được để trống\n';
	if (!phone) errorMessage += 'Số điện thoại không được để trống\n';
	if (!address) errorMessage += 'Địa chỉ không được để trống\n';

	// Kiểm tra định dạng email và số điện thoại
	if (!isValidPhoneNumber(phone)) errorMessage += 'Số điện thoại không đúng định dạng\n';
	if (!isValidEmail(email)) errorMessage += 'Email không đúng định dạng\n';

	if (errorMessage) {
		alert(errorMessage);
		return;
	}

	// Tạo object dữ liệu từ các trường nhập liệu
	const customerData = {
		companyName: companyName,
		contactPerson: contactPerson,
		email: email,
		phone: phone,
		address: address,
		profession: profession || null,
		mainStatus: mainStatus || null,
		subStatus: subStatus || null,
		responsiblePerson: responsiblePerson || null,
		note: note || null,
	};
	//const _ctx = "/crm-web/";
	// Gửi yêu cầu POST đến server với dữ liệu dạng JSON
	fetch(`${_ctx}customer/create-customer`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(customerData) // Chuyển dữ liệu thành JSON
	})
		.then(response => {
			if (!response.ok) {
				return response.json().then(errorData => {
					throw new Error(errorData.errorMessage || "Có lỗi xảy ra");
				});
			}
			return response.json();
		})
		.then(data => {
			// Xử lý kết quả thành công
			alert('Khách hàng đã được thêm!');
			console.log('Khách hàng đã được thêm thành công:', data.customer); 
			document.getElementById('addNewCustomerForm').reset();
			window.location.href = _ctx + 'customer/list';
		})
		.catch(error => {
			// Xử lý lỗi
			console.error('Có lỗi xảy ra:', error);
			alert(error.message);
		});
}

function isValidPhoneNumber(phoneNumber) {
	const phoneRegex = /^[0-9]{10}$/;
	return phoneNumber.match(phoneRegex);
}

function isValidEmail(email) {
	const emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;
	return email.match(emailRegex);
}

function goBack() {
	//window.location.href = '/crm-web/customer/list';
	window.location.href = _ctx + 'customer/list';

}