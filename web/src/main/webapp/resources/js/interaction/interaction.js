var originalData = []; // Biến lưu trữ dữ liệu gốc
const urlParams = new URLSearchParams(window.location.search);
const customerId = urlParams.get('id'); // Lấy giá trị của tham số 'id'
var currentFilter = { startDate: null, endDate: null, contactPerson: null }; // Trạng thái bộ lọc


$(document).ready(function () {   
    if (customerId) {
        loadTableData(customerId); // Gọi hàm loadTableData với customerId từ URL
    } else {
        console.error('Không tìm thấy ID khách hàng trong URL.');
    }	
	
	$('#frmInteractionConfiguration').submit(function (e) {
        e.preventDefault();

        // Lấy thông tin từ Handsontable
        var colHeaders = htInteraction.getColHeader();
        var tableData = htInteraction.getData();
        var colWidths = [];

        // Thu thập độ rộng cột
        for (let i = 0; i < colHeaders.length; i++) {
            colWidths.push(htInteraction.getColWidth(i));
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
            url: `${_ctx}customer/save-interaction?customer_id=${customerId}`, // Địa chỉ API của bạn
            type: 'POST',
            data: formDataJson,
            dataType: "json",
            contentType: 'application/json',
            success: function (result) {
                console.log("Server Response:", result); // Log phản hồi từ server
                updateData(result); // Cập nhật lại bảng với dữ liệu mới				
				alert('Cập nhật thành công');
            },
            error: function (xhr) {
                console.error("AJAX Error:", xhr.responseText); // Log lỗi
            }
        });
    });
	
	
	// Gắn sự kiện tìm kiếm
    $('.btn-search-interaction').on('click', function () {
        currentFilter.startDate = $('#startDateInput').val();
        currentFilter.endDate = $('#endDateInput').val();
        currentFilter.contactPerson = $('#customerSelect').val();

        filterTableData(currentFilter.startDate, currentFilter.endDate, currentFilter.contactPerson);
    });
});

/**
 * Cập nhật Handsontable với dữ liệu mới
 */
function updateData(result) {
    if (result.data) {
        htInteraction.loadData(result.data); // Load dữ liệu mới vào Handsontable
		loadTableData(customerId);
        console.log("Data updated successfully!");	
    } else {
        console.error("No data to update in response!");
    }

    // Hiển thị thông báo thành công
    $("#success-alert").fadeTo(2000, 500).slideUp(500, function () {
        $("#success-alert").slideUp(500);
    });
}


/**
 * Lọc dữ liệu trong Handsontable dựa trên các điều kiện từ form tìm kiếm
 */
function filterTableData(startDate, endDate, contactPerson) {
	// Lấy tất cả dữ liệu gốc trong Handsontable
	var allData = originalData;

    // Chuyển đổi ngày từ chuỗi sang đối tượng Date nếu có
    var start = startDate ? new Date(startDate) : null;
    var end = endDate ? new Date(endDate) : null;

    // Mảng lưu dữ liệu đã lọc
    filteredData = allData.filter(function(row) {
        var recordDate = new Date(row[1]);

        // Kiểm tra nếu có ngày bắt đầu và ngày kết thúc
        var matchDate = true;

        if (start && end) {
            // Kiểm tra ngày trong khoảng startDate và endDate
            matchDate = recordDate >= start && recordDate <= end;
        } else if (start) {
            // Kiểm tra ngày lớn hơn hoặc bằng startDate
            matchDate = recordDate >= start;
        } else if (end) {
            // Kiểm tra ngày nhỏ hơn hoặc bằng endDate
            matchDate = recordDate <= end;
        }

        // Kiểm tra nếu có khách hàng được chọn
        var matchCustomer = !contactPerson || row[0] === contactPerson; // Giả sử khách hàng ở cột đầu tiên (index 0)

        // Nếu thỏa mãn cả điều kiện ngày và khách hàng, giữ lại dòng
        return matchDate && matchCustomer;
    });

    // Cập nhật lại dữ liệu cho Handsontable
    htInteraction.loadData(filteredData);
}

/**
 * Load dữ liệu tương tác từ server và khởi tạo Handsontable
 */
function loadTableData(customerId) {
    if (!customerId) {
        console.error('Customer ID is required to load interactions.');
        return;
    }

	// Xây dựng URL động với _ctx
	const url = `${_ctx}/customer/load-interaction?id=${customerId}`;

    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function(res) {
            console.log("Dữ liệu nhận được:", JSON.stringify(res));

            if (res) {
				originalData = res.data; // Lưu dữ liệu gốc
                initTable(res.colHeaders, res.colWidths, res.data); // Gọi hàm initTable để khởi tạo Handsontable
				
				// Cập nhật danh sách khách hàng
				updateCustomerSelect(res.contactPersons); // Gọi hàm updateCustomerSelect để cập nhật danh sách contactPerson
					
            }
        },
        error: function(e) {
            console.log("Lỗi tải dữ liệu: " + e);
        }
    });
}

/**
 * Cập nhật dropdown #customerSelect với danh sách contactPersons
 */
function updateCustomerSelect(contactPersons) {
    var customerSelect = $('#customerSelect');
    customerSelect.empty(); // Xóa tất cả các option cũ
    customerSelect.append('<option value="" selected>-- Chọn khách hàng --</option>'); // Thêm option mặc định

    contactPersons.forEach(function(contactPerson) {
        customerSelect.append('<option value="' + contactPerson + '">' + contactPerson + '</option>');
    });
}


function initTable(colHeaders, colWidths, data) {
    console.log("HST:", document.getElementById('tblInteraction'));

    var container = document.getElementById('tblInteraction');
    
    if (container) {
        htInteraction = new Handsontable(container, {
            data: data,
            colHeaders: colHeaders,
            colWidths: colWidths,
            columns: [
                { type: 'text' }, 
                { type: 'date', dateFormat: 'YYYY-MM-DD', correctFormat: true }, 
                { type: 'text' },
                { type: 'text' }, 
                { 
                    renderer: function(instance, td, row, col, prop, value, cellProperties) {
                        Handsontable.dom.empty(td); // Xóa nội dung cũ
						
                        const button = document.createElement('button');
						button.type = 'button';
                        button.className = 'delete-button';
						
						// Tạo phần tử icon FontAwesome
				        const icon = document.createElement('i');
				        icon.className = 'fas fa-trash'; // Lớp FontAwesome cho biểu tượng thùng rác
				        
				        button.appendChild(icon);
						
                        button.onclick = function() {
                            deleteRow(row, value); // Gọi hàm deleteRow khi nhấn nút
                        };
						
                        td.appendChild(button);
                        return td;
                    }
                }, // Hành động (nút xóa)
            ],
            height: 400,
            currentRowClassName: 'currentRow',
            currentColClassName: 'currentCol',
            manualColumnResize: true,
            manualRowResize: true,
            minSpareRows: 1,
            contextMenu: true,
            licenseKey: 'non-commercial-and-evaluation',			
        });
					
    } else {
        console.error("Container tblInteraction không tồn tại.");
    }
}

function deleteRow(rowIndex, interactionId) {
    if (confirm(`Bạn có chắc muốn xóa hàng số ${rowIndex + 1}?`)) {
		
        // Gọi API DELETE với interactionId là query parameter
        $.ajax({
            url: `${_ctx}/customer/delete-interaction?id=${interactionId}`,  // Đặt interactionId vào URL
            type: 'DELETE',
            success: function(response) {
                console.log('Xóa thành công:', response);			
												
				// Xóa hàng khỏi dữ liệu gốc
                const actualRowIndex = htInteraction.toPhysicalRow(rowIndex);
				
				// Xóa hàng khỏi Handsontable
				htInteraction.alter('remove_row', actualRowIndex);
				
                originalData.splice(actualRowIndex, 1);

            },
            error: function(error) {
                console.error('Lỗi khi xóa:', error);
                alert('Có lỗi xảy ra khi xóa dữ liệu. Vui lòng thử lại.');
            }
        });
    }
}