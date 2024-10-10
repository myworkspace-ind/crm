
//CÓ VẤN ĐỀ

function handleKeyPress(event) {
    // Kiểm tra nếu phím nhấn là Enter (key code 13)
    if (event.key === 'Enter') {
        // Lấy giá trị nhập vào từ ô input
        const keyword = document.getElementById('iconLeft').value.trim();
        
        // Kiểm tra xem keyword có rỗng không
        if (keyword !== '') {
            // Chuyển hướng đến URL tìm kiếm
            const baseUrl = 'http://localhost:8081/crm-web/customer/search';
            const url = `${baseUrl}?keyword=${encodeURIComponent(keyword)}`;
            window.location.href = url; // Chuyển hướng đến URL
        } else {
            // Nếu không có giá trị, có thể hiển thị thông báo hoặc xử lý theo cách khác
            alert('Vui lòng nhập từ khóa tìm kiếm.');
        }
    }
}	
