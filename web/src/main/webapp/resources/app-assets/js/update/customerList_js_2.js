//document.getElementById('select-all').addEventListener('change', function() {
//    // Lấy tất cả các checkbox khách hàng
//    const customerCheckboxes = document.querySelectorAll('.customer-checkbox');
//    
//    // Thiết lập trạng thái của tất cả checkbox khách hàng dựa trên trạng thái của checkbox chọn tất cả
//    customerCheckboxes.forEach(checkbox => {
//        checkbox.checked = this.checked;
//    });
//});

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('select-all').addEventListener('change', function() {
        // Lấy tất cả các checkbox khách hàng
        const customerCheckboxes = document.querySelectorAll('.customer-checkbox');
        
        // Thiết lập trạng thái của tất cả checkbox khách hàng dựa trên trạng thái của checkbox chọn tất cả
        customerCheckboxes.forEach(checkbox => {
            checkbox.checked = this.checked;
        });
    });
});