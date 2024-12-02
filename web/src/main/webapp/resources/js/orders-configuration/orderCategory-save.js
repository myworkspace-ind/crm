$(document).ready(function () {
    $('#frmOrdersConfiguration').submit(function (e) {
        e.preventDefault();

        // Lấy thông tin từ Handsontable
        var colHeaders = htOrderCategory.getColHeader();
        var tableData = htOrderCategory.getData();
        var colWidths = [];

        // Thu thập độ rộng cột
        for (let i = 0; i < colHeaders.length; i++) {
            colWidths.push(htOrderCategory.getColWidth(i));
        }

        // Chuẩn bị dữ liệu JSON
        var formDataJson = JSON.stringify({
            colWidths: colWidths,
            colHeaders: colHeaders,
            data: tableData,
        });

        console.log("Data to send:", formDataJson); // Log dữ liệu để kiểm tra

        // Gửi AJAX request
        $.ajax({
            url: _ctx + 'ordersConfigurationCRMOrderType_Bao/save',
            type: 'POST',
            data: formDataJson,
            dataType: "json",
            contentType: 'application/json',
            success: function (result) {
                console.log("Server Response:", result); // Log phản hồi từ server
                updateData(result); // Cập nhật lại bảng với dữ liệu mới
            },
            error: function (xhr) {
                console.error("AJAX Error:", xhr.responseText); // Log lỗi
            }
        });
    });
});

/**
 * Cập nhật Handsontable với dữ liệu mới
 */
function updateData(result) {
    if (result.data) {
        htOrderCategory.loadData(result.data); // Load dữ liệu mới vào Handsontable
        console.log("Data updated successfully!");
    } else {
        console.error("No data to update in response!");
    }

    // Hiển thị thông báo thành công
    $("#success-alert").fadeTo(2000, 500).slideUp(500, function () {
        $("#success-alert").slideUp(500);
    });
}
