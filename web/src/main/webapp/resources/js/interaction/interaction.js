const appState = {
    originalData: [], // Biến lưu trữ dữ liệu gốc
    currentFilter: { startDate: null, endDate: null, contactPerson: null }, // Trạng thái bộ lọc
};

const urlParams = new URLSearchParams(window.location.search);
const customerId = urlParams.get('id'); // Lấy giá trị của tham số 'id'

$(document).ready(function () {
    if (customerId) {
        loadTableData(customerId);
    } else {
        console.error('Không tìm thấy ID khách hàng trong URL.');
    }

    attachEventHandlers();
});

function attachEventHandlers() {
    $('#frmInteractionConfiguration').submit(handleFormSubmit);
    $('.btn-search-interaction').on('click', handleSearchInteraction);
}

function handleFormSubmit(e) {
    e.preventDefault();

	const tableData = htInteraction.getData().filter(row => 
	    row.some(cell => cell !== null && cell !== '') // Loại bỏ các dòng hoàn toàn trống
	);

	const isValid = tableData.every(row => 
	    [0, 1, 2, 3].every(colIndex => row[colIndex] !== null && row[colIndex] !== '')
	);

	if (!isValid) {
	    showErrorToast('Vui lòng nhập đầy đủ thông tin trước khi lưu.');
	    return;
	}
	
    const colHeaders = htInteraction.getColHeader();
    const colWidths = [];

    for (let i = 0; i < colHeaders.length; i++) {
        colWidths.push(htInteraction.getColWidth(i));
    }

    const formDataJson = JSON.stringify({
        colWidths: colWidths,
        colHeaders: colHeaders,
        data: tableData,
    });

    console.log("Data to send:", formDataJson);

    $.ajax({
        url: `${_ctx}customer/save-interaction?customer_id=${customerId}`,
        type: 'POST',
        data: formDataJson,
        dataType: "json",
        contentType: 'application/json',
        success: function (result) {
            console.log("Server Response:", result);
            reloadAndFilterData();
            showSuccessToast('Cập nhật thành công');
        },
        error: function (xhr) {
            console.error("AJAX Error:", xhr.responseText);
        }
    });
}

function handleSearchInteraction() {
    appState.currentFilter.startDate = $('#startDateInput').val();
    appState.currentFilter.endDate = $('#endDateInput').val();
    appState.currentFilter.contactPerson = $('#customerSelect').val();

    filterTableData(
        appState.currentFilter.startDate,
        appState.currentFilter.endDate,
        appState.currentFilter.contactPerson
    );
}

function reloadAndFilterData() {
    return loadTableData(customerId).then(() => {
        filterTableData(
            appState.currentFilter.startDate,
            appState.currentFilter.endDate,
            appState.currentFilter.contactPerson
        );
    });
}

function loadTableData(customerId) {
    return new Promise((resolve, reject) => {
        if (!customerId) {
            console.error('Customer ID is required to load interactions.');
            reject('Customer ID is missing');
            return;
        }
		
//        const url = `${_ctx}/customer/load-interaction?id=${customerId}`;
		//Không thêm / trước customer vì trong _ctx đã có /sẵn rồi
		const url = `${_ctx}customer/load-interaction?id=${customerId}`;
        $.ajax({
            url: url,
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            success: function (res) {
                console.log("Dữ liệu nhận được:", JSON.stringify(res));

                if (res) {
                    appState.originalData = res.data;
                    initTable(res.colHeaders, res.colWidths, res.data);
                    updateCustomerSelect(res.contactPersons);
                    resolve();
                }
            },
            error: function (e) {
                console.error("Lỗi tải dữ liệu:", e);
                reject(e);
            }
        });
    });
}

function updateCustomerSelect(contactPersons) {
    const customerSelect = $('#customerSelect');
    customerSelect.empty();
    customerSelect.append('<option value="" selected>Tất cả</option>');

    contactPersons.forEach(function (contactPerson) {
        customerSelect.append('<option value="' + contactPerson + '">' + contactPerson + '</option>');
    });
}

function initTable(colHeaders, colWidths, data) {
    const container = document.getElementById('tblInteraction');

    if (container) {
        htInteraction = new Handsontable(container, {
            data: data,
            colHeaders: colHeaders,
            colWidths: colWidths,
            columns: [
                { type: 'text' },
				{
                    type: 'date',
                    dateFormat: 'YYYY-MM-DD',
                    correctFormat: true,
					defaultDate: new Date(),
					datePickerConfig: { format: 'YYYY-MM-DD' }
                },
                { type: 'text' },
                { type: 'text' },
                { renderer: deleteButtonRenderer },
            ],
            height: 400,
            currentRowClassName: 'currentRow',
            currentColClassName: 'currentCol',
            manualColumnResize: true,
            manualRowResize: true,
			columnSorting: true,  // Bật tính năng sắp xếp cột
			autoColumnSize: true,  // Tự động điều chỉnh kích thước cột
            minSpareRows: 1,
            contextMenu: true,
            licenseKey: 'non-commercial-and-evaluation',
        });
    } else {
        console.error("Container tblInteraction không tồn tại.");
    }
}

function deleteButtonRenderer(instance, td, row, col, prop, value, cellProperties) {
    Handsontable.dom.empty(td);
    const button = document.createElement('button');
    button.type = 'button';
    button.innerHTML = '<i class="fas fa-trash"></i>';
    button.onclick = function () {
        deleteRow(row, value);
    };
    td.appendChild(button);
    return td;
}

function deleteRow(rowIndex, interactionId) {
    if (confirm(`Bạn có chắc muốn xóa hàng số ${rowIndex + 1}?`)) {
        $.ajax({
            url: `${_ctx}customer/delete-interaction?id=${interactionId}`,
            type: 'DELETE',
            success: function (response) {
                console.log('Xóa thành công:', response);
                reloadAndFilterData();
				showSuccessToast('Xóa thành công');
            },
            error: function (error) {
                console.error('Lỗi khi xóa:', error);
                alert('Có lỗi xảy ra khi xóa dữ liệu. Vui lòng thử lại.');
            }
        });
    }
}

function filterTableData(startDate, endDate, contactPerson) {
    const allData = appState.originalData;
    const start = startDate ? new Date(startDate) : null;
    const end = endDate ? new Date(endDate) : null;

    if ((start && isNaN(start.getTime())) || (end && isNaN(end.getTime()))) {
        console.error("Ngày không hợp lệ.");
        return;
    }
	
	// Kiểm tra nếu ngày bắt đầu lớn hơn ngày kết thúc
    if (start && end && start > end) {
        showErrorToast('Ngày bắt đầu không được lớn hơn ngày kết thúc.');
        return; // Dừng xử lý nếu điều kiện sai
    }

    const filteredData = allData.filter(function (row) {
        const recordDate = new Date(row[1]);
        let matchDate = true;

        if (start && end) {
            matchDate = recordDate >= start && recordDate <= end;
        } else if (start) {
            matchDate = recordDate >= start;
        } else if (end) {
            matchDate = recordDate <= end;
        }

        const matchCustomer = !contactPerson || row[0] === contactPerson;
        return matchDate && matchCustomer;
    });

    htInteraction.loadData(filteredData);
}

let isToastVisible = false; // Biến flag để kiểm tra trạng thái hiển thị toast

function showToast(message, type) {
    if (isToastVisible) return; // Nếu một toast đang hiển thị, không hiển thị toast mới
    
    isToastVisible = true; // Đánh dấu là toast đang hiển thị
    let toast;
    
    // Chọn thông báo dựa trên loại (success hoặc warning)
    if (type === 'success') {
        toast = $('#success-alert');
        toast.removeClass('alert-warning').addClass('alert-success');
    } else if (type === 'warning') {
        toast = $('#warning-alert');
        toast.removeClass('alert-success').addClass('alert-warning');
    }

    toast.text(message); // Đặt nội dung của toast

    // Hiển thị toast từ đầu
    toast.show().animate({
        left: '+=20px'  // Lắc sang phải
    }, 100).animate({
        left: '-=40px'  // Lắc sang trái
    }, 100).animate({
        left: '+=20px'  // Quay lại vị trí ban đầu
    }, 100);

    // Sau 3 giây (3000ms), ẩn toast (fade-out)
    setTimeout(function () {
        toast.fadeOut(100);  // 200ms để mờ dần ra
        isToastVisible = false; // Đánh dấu là toast đã ẩn
    }, 500);  // Toast sẽ hiển thị trong 0.5 giây
}

function showSuccessToast(message) {
    showToast(message, 'success');
}

function showErrorToast(message) {
    showToast(message, 'warning');
}

