function deleteCustomer(customerId) {
    if (confirm("Bạn có chắc muốn xóa khách hàng này?")) {
        fetch('/crm-web/customer/delete-customers', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify([customerId]) // Truyền danh sách ID cần xóa
        })
        .then(response => response.json())
        .then(data => {
            if (data.errorMessage) {
                alert("Lỗi: " + data.errorMessage);
            } else {
                alert("Khách hàng đã được xóa thành công!");
                // Reload hoặc cập nhật giao diện sau khi xóa
                location.href = '/crm-web/customer/list';
            }
        })
        .catch(error => {
            alert("Có lỗi xảy ra. Vui lòng thử lại sau!");
            console.error("Error:", error);
        });
    }
}
