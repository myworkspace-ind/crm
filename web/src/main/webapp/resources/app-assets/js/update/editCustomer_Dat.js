function saveCustomerChanges() {
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
	const customerId = document.getElementById('customer_id').value.trim(); // Lấy giá trị ID từ input hidden
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
		id: customerId,  // Thêm ID vào đối tượng customerData
        companyName: companyName,
        contactPerson: contactPerson,
        email: email,
        phone: phone,
        address: address,
        responsiblePerson: responsiblePerson || null,
        note: note || '',
        profession: profession || '',
        mainStatus: mainStatus || '',
        subStatus: subStatus || '',
    };

    const _ctx = "/crm-web/";
    // Gửi yêu cầu PUT để cập nhật thông tin khách hàng
    fetch(`${_ctx}customer/newedit-customer`, {
        method: 'PUT', // Phải là PUT để cập nhật dữ liệu
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
			// Kiểm tra xem phản hồi có chứa thông điệp thành công hay lỗi
			    if (data.message) {
			        alert(data.message); // Nếu có thông điệp thành công
			    } else if (data.errorMessage) {
			        alert(data.errorMessage); // Nếu có thông điệp lỗi
			}
        })
        .catch(error => {
            // Xử lý lỗi
            console.error('Có lỗi xảy ra:', error);
            alert('Có lỗi xảy ra khi cập nhật khách hàng.');
        });
}
