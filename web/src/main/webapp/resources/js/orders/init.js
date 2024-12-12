var htOrder;

/**
 * Processing after the webpage are loaded in the browser.
 */
$(document).ready(function() {
	loadTableData();
});


/**
 * Load column width, header, initTable()
 */
function loadTableData() {

	$.ajax({
		url: _ctx + 'orders-vinh/load',
		type: 'GET',
		dataType: 'json',
		contentType: 'application/json',
		success: function(res) {
			console.log("res=" + JSON.stringify(res));

			if (res) {
				initTable(res.colHeaders, res.colWidths, res.data, res.columnTypes);
			}
		},
		error: function(e) {
			console.log("Error: " + e);
		}
	});
}

function initTable(colHeaders, colWidths, data) {
	console.log("HST:", document.getElementById('tblOrder'));

	var container = document.getElementById('tblOrder');

	//render for column containing action button
	function buttonRenderer(instance, td, row, col, prop, value, cellProperties) {
		td.innerHTML = `
		        <div class="btn-group" role="group" aria-label="...">
		            <button class="btn btn-info btn-view" onclick="viewDetail(${row})" title="Xem chi tiết">
		                <i class="fas fa-eye"></i> Xem chi tiết
		            </button>
		            <button class="btn btn-warning" onclick="edit(${row})" title="Sửa">
		                <i class="fas fa-edit"></i> Sửa
		            </button>
		            <button class="btn btn-danger" onclick="deleteRow(${row})" title="Xóa">
		                <i class="fas fa-trash"></i> Xóa
		            </button>
		            <button class="btn btn-success btn-status" onclick="openStatusModal(${row})" title="Cập nhật trạng thái">
		                <i class="fas fa-sync-alt"></i> Cập nhật trạng thái
		            </button>
		        </div>
	    `;

		return td;
	}
	
	
	if (container) {
		htOrder = new Handsontable(container, {
			data: data,
			colHeaders: colHeaders,
			colWidths: colWidths,
			columns: [
				{ type: 'text' }, // ID
				{ type: 'date', dateFormat: 'YYYY-MM-DD', correctFormat: true }, // Delivery Date
				{ type: 'text' }, // Goods Category
				{ type: 'text' }, // Info of customer
				{ type: 'text' }, // Transportation method
				{ renderer: buttonRenderer }
			],
			rowHeaders: true,
			minRows: 8,
			height: 1000,
			currentRowClassName: 'currentRow',
			currentColClassName: 'currentCol',
			manualColumnResize: true,
			manualRowResize: true,
			minSpareRows: 1,
			contextMenu: true,
			licenseKey: 'non-commercial-and-evaluation'
		});
	} else {
		console.error("Container tblOrder không tồn tại.");
	}
}
function changeTitle(newTitle) {
	document.getElementById("title").innerText = newTitle;
}

function viewDetail(row) {
    const orderId = htOrder.getSourceDataAtRow(row)[0];
    console.log('Xem chi tiết cho ID đơn hàng: ', orderId);

    $.ajax({
        url: _ctx + 'orders-vinh/viewDetails/' + orderId,
        method: 'GET',
        success: function(response) {
            console.log(response);

            var orderStatusData = response[6];
            var currentOrderStatusId = response[7];

            var orderGoodsCategoryData = response[8];
            var currentOrderGoodsCategoryId = response[9];

            var orderSenderData = response[10];
            var currentOrderSenderId = response[11];

            var orderReceiverData = response[12];
            var currentOrderReceiverId = response[13];

            $('#orderIdDetail').text(response[0]);
            $('#orderCodeDetail').text(response[1]);
            $('#orderCodeDetailInput').val(response[1]);
            $('#orderDeliveryDateDetail').val(response[2]);
            $('#orderCreateDateDetail').val(response[3]);
            $('#orderTransportDetail').val(response[4]);
            $('#orderRequirementDetail').val(response[5]);
            $('#orderAddressDetail').val(response[14]);

            if (orderStatusData && orderStatusData.length > 0) {
                var options = orderStatusData.map(function(status) {
                    var selected = status[0] === currentOrderStatusId ? ' selected' : '';
                    return '<option value="' + status[0] + '"' + selected + '>' + status[1] + '</option>';
                }).join('');
                $('#orderStatusDetail').html(options).css('pointer-events', 'none');
            }

            if (orderGoodsCategoryData && orderGoodsCategoryData.length > 0) {
                var options = orderGoodsCategoryData.map(function(goodscategory) {
                    var selected = goodscategory[0] === currentOrderGoodsCategoryId ? ' selected' : '';
                    return '<option value="' + goodscategory[0] + '"' + selected + '>' + goodscategory[1] + '</option>';
                }).join('');
                $('#orderGoodsDetail').html(options).css('pointer-events', 'none');
            }

            if (orderSenderData && orderSenderData.length > 0) {
                var options = orderSenderData.map(function(sender) {
                    var selected = sender[0] === currentOrderSenderId ? ' selected' : '';
                    return '<option value="' + sender[0] + '"' + selected + '>' + sender[1] + '</option>';
                }).join('');
                $('#orderSenderNameDetail').html(options).css('pointer-events', 'none');
            }

            if (orderReceiverData && orderReceiverData.length > 0) {
                var options = orderReceiverData.map(function(receiver) {
                    var selected = receiver[0] === currentOrderReceiverId ? ' selected' : '';
                    return '<option value="' + receiver[0] + '"' + selected + '>' + receiver[1] + '</option>';
                }).join('');
                $('#orderReceiverNameDetail').html(options).css('pointer-events', 'none');
            }

            document.getElementById("orderDetailModal").style.display = "block";
        },
        error: function(error) {
            console.error('Có lỗi khi lấy thông tin chi tiết đơn hàng:', error);
        }
    });
}

function edit(row) {
	changeTitle('Cập nhật đơn hàng');
	currentRow = row;  // Save current row to update
	document.getElementById("updateOrderModal").style.display = "block";
	document.getElementById("modalOverlay").style.display = "block";
}

function deleteRow(row) {
    // Lấy ID đơn hàng từ hàng cần xóa
    const orderId = htOrder.getSourceDataAtRow(row)[0]; // Giả sử cột ID là cột đầu tiên

    // Hiển thị thông báo xác nhận
    if (confirm(`Bạn có chắc chắn muốn xóa đơn hàng ID: ${orderId}?`)) {
        // Gửi yêu cầu xóa đến server
        $.ajax({
            url: `${_ctx}/orders-vinh/delete?id=${orderId}`, // URL API xóa (thay đổi theo API của bạn)
            type: 'DELETE',
            success: function(response) {
                console.log("Xóa thành công:", response);

                // Xóa hàng trên Handsontable
                htOrder.alter('remove_row', row);

                // Đảm bảo đồng bộ dữ liệu
                htOrder.render(); // Cập nhật lại bảng

                // Hiển thị thông báo thành công
                alert(`Đơn hàng ID: ${orderId} đã được xóa thành công!`);
            },
            error: function(error) {
                console.error("Xóa thất bại:", error);

                // Hiển thị thông báo lỗi
                alert("Có lỗi xảy ra khi xóa đơn hàng. Vui lòng thử lại sau.");
            }
        });
    }
}


let currentRow;

function openStatusModal(row) {
    // Lấy dữ liệu hàng từ DataTable
    const orderId = htOrder.getSourceDataAtRow(row)[0];

    console.log('Cập nhật trạng thái đơn hàng: ', orderId);

    // Hiển thị modal
    $('#updateOrderStatusModal').modal('show');
    $('#updateOrderStatusModal').removeAttr('inert');

    // Gửi yêu cầu AJAX để lấy thông tin chi tiết đơn hàng
    $.ajax({
        url: _ctx + 'orders-vinh/viewDetails/' + orderId,
        method: 'GET',
        success: function(response) {
            console.log('Thông tin chi tiết đơn hàng:', response);

            // Gán giá trị vào các trường trong modal
            $('#orderIdUpdateStatus').text(response[0]);
            $('#orderCodeUpdateStatus').val(response[1]);

            // Gán dữ liệu cho danh mục đơn hàng
            var orderCategoryData = response[15];
            var currentOrderCategoryId = response[16];
            if (orderCategoryData && orderCategoryData.length > 0) {
                var options = orderCategoryData.map(function(ordercategory) {
                    var selected = ordercategory[0] === currentOrderCategoryId ? ' selected' : '';
                    return '<option value="' + ordercategory[0] + '"' + selected + '>' + ordercategory[1] + '</option>';
                }).join('');
                $('#orderCategoryUpdateStatus').html(options).css('pointer-events', 'none');
            }

            // Gán dữ liệu cho trạng thái đơn hàng
            var orderStatusData = response[6];
            var currentOrderStatusId = response[7];
            if (orderStatusData && orderStatusData.length > 0) {
                var options = orderStatusData.map(function(status) {
                    var selected = status[0] === currentOrderStatusId ? ' selected' : '';
                    return '<option value="' + status[0] + '"' + selected + '>' + status[1] + '</option>';
                }).join('');
                $('#orderStatusUpdateStatus').html(options);
            }
        },
        error: function(error) {
            console.error('Có lỗi khi lấy thông tin chi tiết đơn hàng:', error);
        }
    });

    // Khi đóng modal, đặt lại trạng thái "inert"
    $('#updateOrderStatusModal').on('hidden.bs.modal', function() {
        $('#updateOrderStatusModal').attr('inert', true);
    });

    // Gắn sự kiện lưu trạng thái đơn hàng
    $(document).off('click', '#saveOrderStatusButton').on('click', '#saveOrderStatusButton', function() {
        var button = $(this);
        button.prop('disabled', true); // Disable button để tránh nhiều yêu cầu cùng lúc

        var orderStatus = $('#orderStatusUpdateStatus').val();

        if (!orderId) {
            alert('ID đơn hàng không hợp lệ.');
            button.prop('disabled', false);
            return;
        }

        var order = {
            id: orderId,
            orderStatus: orderStatus,
        };

        console.log("Order object being sent:", order);

        // Gửi yêu cầu AJAX để lưu trạng thái đơn hàng
        $.ajax({
            url: _ctx + '/orders-vinh/saveOrderStatus/',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(order),
            success: function(response) {
                console.log("UDPATE FINAL ORDER STATUS: ", order);
                if (response.status === "success") {
                    alert(response.message);
                    $('#updateOrderStatusModal').modal('hide'); // Đóng modal
                    location.reload(); // Reload trang để cập nhật
                } else {
                    alert(response.message);
                }
                button.prop('disabled', false);
            },
            error: function(error) {
                console.error("Error saving order:", error);
                alert("An error occurred while saving/updating the order.");
                button.prop('disabled', false);
            }
        });
    });
}

function closeUpdateOrderModal() {
	document.getElementById("updateOrderModal").style.display = "none";
	document.getElementById("modalOverlay").style.display = "none";
}

function closeModalOrderStatus() {
	document.getElementById("statusModal").style.display = "none";
	document.getElementById("modalOverlay").style.display = "none";
}

function saveStatus() {
	const newStatus = document.getElementById("statusSelect").value;
	console.log("Cập nhật trạng thái cho hàng:", currentRow, "thành:", newStatus);

	// Ví dụ: Update data in Handsontable
	//htOrder.setDataAtRowProp(currentRow, 'status', newStatus);

	closeModal();  // Close modal after save
}