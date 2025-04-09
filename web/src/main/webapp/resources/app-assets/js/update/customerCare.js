document.getElementById("okButton").addEventListener("click", function() {
    // Lấy danh sách radio buttons
    const options = document.getElementsByName("option");
    
    // Kiểm tra option được chọn
    if (options[0].checked) { 
        // Nếu chọn "Nhắc Nhở Sinh Nhật & Ngày Kỷ Niệm", chuyển trang
		window.location.href = _ctx + 'customer-care/calendar';
    }
});