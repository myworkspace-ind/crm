/*function deleteCustomer(customerId) {
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
*/


let selectedCustomerId; 

function hideCustomer(customerId) {
    selectedCustomerId = customerId; 
    const modal = document.getElementById('confirmationModal');
    const overlay = document.getElementById('overlay');

    // Hiển thị modal và overlay
    modal.style.display = 'block';
    overlay.style.display = 'block';
}


function hideCustomer_Interaction(customerId) {
    selectedCustomerId = customerId; 
    const modal = document.getElementById('confirmationModal');
    const overlay = document.getElementById('overlay');

    // Hiển thị modal và overlay
    modal.style.display = 'block';
    overlay.style.display = 'block';
}


document.getElementById('confirmHideBtn').addEventListener('click', function() {
    if (selectedCustomerId) {
        fetch(`${_ctx}customer/hide-customers`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify([selectedCustomerId]) // Truyền danh sách ID cần xóa
        })
        .then(response => response.json())
        .then(data => {
            closeModal(); // Đóng modal sau khi xử lý
            if (data.errorMessage) {
                alert("Lỗi: " + data.errorMessage);
            } else {
                alert("Khách hàng đã được ẩn thành công!");
                // Reload hoặc cập nhật giao diện sau khi xóa
                location.href = `${_ctx}customer/list`;
            }
        })
        .catch(error => {
            closeModal(); // Đóng modal nếu có lỗi
            alert("Có lỗi xảy ra. Vui lòng thử lại sau!");
            console.error("Error:", error);
        });
    }
});


document.getElementById('cancelBtn').addEventListener('click', function() {
    closeModal(); // Đóng modal
});


function closeModal() {
    const modal = document.getElementById('confirmationModal');
    const overlay = document.getElementById('overlay');

    modal.style.display = 'none';
    overlay.style.display = 'none';
}
