function saveCustomerChanges() {
    const companyName = document.getElementById('company_name').value.trim();
    const contactPerson = document.getElementById('contact_person').value.trim();
    const email = document.getElementById('email').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const responsiblePerson = document.getElementById('responsible_person').value.trim();
    const note = document.getElementById('note').value.trim();
    const profession = document.getElementById('profession').value.trim();
    const mainStatus = document.getElementById('main_status').value.trim();
    const subStatus = document.getElementById('sub_status').value.trim();
    const isVietnamForm = document.getElementById("vietnam-address-form").style.display !== "none";
    const customerId = document.getElementById('customer_id').value.trim();
	
//    const addressId = document.getElementById("address_id")?.value?.trim(); 
//--> optional chaining (?.) không được hỗ trợ trong một số môi trường JavaScript cũ
// Có thể thay bằng 2 dòng dưới đây:
	const addressElement = document.getElementById("address_id");
	const addressId = addressElement ? addressElement.valute.trim() : ""


    let errorMessage = '';
    if (!companyName) errorMessage += '- Tên công ty không được để trống\n';
    if (!contactPerson) errorMessage += '- Người liên hệ chính không được để trống\n';
    if (!email) errorMessage += '- Email không được để trống\n';
    if (!phone) errorMessage += '- Số điện thoại không được để trống\n';

    if (errorMessage) {
        alert('Vui lòng điền đầy đủ thông tin:\n' + errorMessage);
        return;
    }

    const addressObj = isVietnamForm
        ? {
            id: addressId || null,
            country: "Vietnam",
            street: document.getElementById("street-address").value.trim(),
            ward: document.getElementById("vn-ward").value.trim(),
            district: document.getElementById("vn-district").value.trim(),
            state: document.getElementById("vn-city").value.trim(),
            latitude: parseFloat(document.getElementById("latitude").value.trim()) || null,
            longitude: parseFloat(document.getElementById("longitude").value.trim()) || null
        }
        : {
            id: addressId || null,
            country: document.getElementById("country").value.trim(),
            street: document.getElementById("street-address").value.trim(),
            district: document.getElementById("suburb").value.trim(),
            state: document.getElementById("state").value.trim(),
            postcode: document.getElementById("postcode").value.trim(),
            latitude: parseFloat(document.getElementById("latitude").value.trim()) || null,
            longitude: parseFloat(document.getElementById("longitude").value.trim()) || null
        };

    console.log("Updating Address Sending: " + addressObj);

    const customerData = {
        id: customerId,
        companyName: companyName,
        contactPerson: contactPerson,
        email: email,
        phone: phone,
        address: addressObj,
        responsiblePerson: responsiblePerson || null,
        note: note || null,
        profession: profession || null,
        mainStatus: mainStatus || null,
        subStatus: subStatus || null,
    };


    fetch(`${_ctx}customer/newedit-customer`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(customerData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.message) {
                alert(data.message);
                console.log("Payload sending to backend: ", data)
            } else if (data.errorMessage) {
                alert(data.errorMessage);
            }
        })
        .catch(error => {
            console.error('Có lỗi xảy ra:', error);
            alert('Có lỗi xảy ra khi cập nhật khách hàng.');
        });
}
