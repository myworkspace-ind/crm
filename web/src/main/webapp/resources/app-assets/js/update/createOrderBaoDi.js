const openCreateOrderBtn = document.getElementById('openCreateOrderBtn');
const createOrderModal = document.getElementById('createOrderModal');
const modalOverlay = document.getElementById('modalOverlay');

openCreateOrderBtn.addEventListener('click', function() {
	createOrderModal.style.display = 'block';
	modalOverlay.style.display = 'block';
})

function closeModal() {
	createOrderModal.style.display = 'none';
	modalOverlay.style.display = 'none';
}

function changeTitle(newTitle) {
	document.getElementById("title").innerText = newTitle;
}
function getDataIdFromDatalist(datalistId, value) {
    let dataId = null;
    $('#' + datalistId + ' option').each(function () {
        if ($(this).val() === value) {
            dataId = $(this).data('id');
        }
    });
    return dataId;
}
function checkSelectionGoodOrder() {
            var input = document.getElementById('orderCodeInputCreate');
            var datalist = document.getElementById('orderCodes');
            var options = datalist.options;
            for (var i = 0; i < options.length; i++) {
                if (options[i].value === input.value) {
                    alert('Bạn đã tạo đơn hàng: ' + input.value + '. Vui lòng nhập Mã đơn hàng khác không có trong danh sách gợi ý!!!!');
                    break;
                }
            }
        }
document.getElementById("createOrderButton").addEventListener("click", function() {
	let orderCategoryName = $('#orderCategoryCreate').val();
    let orderCategoryId = getDataIdFromDatalist('orderCategories', orderCategoryName);

    // Lấy giá trị của loại hàng hóa
    let goodsCategoryName = $('#orderGoodsCreate').val();
    let goodsCategoryId = getDataIdFromDatalist('goodsCategories', goodsCategoryName);
	
	const orderData = {
		createDate: document.getElementById("orderCreateDateCreate").value,
		deliveryDate: document.getElementById("orderDeliveryDateCreate").value,
		orderCode: document.getElementById("orderCodeInputCreate").value,
		orderCategory: orderCategoryId ? orderCategoryId.toString() : null,
		goodsCategory: goodsCategoryId ? goodsCategoryId.toString() : null,
		orderStatus: document.getElementById("orderStatusCreate").value,
		transportMethod: document.getElementById("orderTransportCreate").value,
		sender: document.getElementById("orderSenderNameCreate").value,
		receiver: document.getElementById("orderReceiverNameCreate").value,
		requirement: document.getElementById("orderRequirementCreate").value,
		address: document.getElementById("orderAddressCreate").value
	};
	
	// Kiểm tra nếu dữ liệu đã đầy đủ trước khi gửi
	if (!orderData.createDate) {
		alert("Vui lòng điền ngày tạo đơn!");
		return;
	}
	if (!orderData.deliveryDate) {
		alert("Vui lòng điền ngày giao hàng!");
		return;
	}
	if (!orderData.orderCode) {
		alert("Vui lòng điền mã đơn hàng!");
		return;
	}
	if (!orderData.orderCategory) {
		alert("Vui lòng chọn danh mục đơn hàng!");
		return;
	}
	if (!orderData.goodsCategory) {
		alert("Vui lòng chọn loại hàng hóa!");
		return;
	}
	if (!orderData.orderStatus) {
		alert("Vui lòng chọn trạng thái đơn hàng!");
		return;
	}
	if (!orderData.transportMethod) {
		alert("Vui lòng chọn phương thức vận chuyển!");
		return;
	}
	if (!orderData.sender) {
		alert("Vui lòng điền tên người gửi!");
		return;
	}
	if (!orderData.receiver) {
		alert("Vui lòng điền tên người nhận!");
		return;
	}
	if (!orderData.requirement) {
		alert("Vui lòng điền yêu cầu! Nếu không có yêu cầu gì, vui lòng điền 'Không có'");
		return;
	}
	if (!orderData.address) {
		alert("Vui lòng điền địa chỉ nhận hàng!");
		return;
	}

	//const _ctx = "/crm-web/";
	// Gửi request POST tới backend để lưu đơn hàng
	fetch(`${_ctx}orders-datatable/create-order`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(orderData)
	})
		.then(response => {
			if (!response.ok) {
				// Nếu response không thành công, ném ra lỗi
				return response.json().then(err => {
					throw new Error(err.errorMessage || 'Có lỗi xảy ra.');
				});
			}
			return response.json();
		})
		.then(data => {
			alert(data.message);  // Hiển thị thông báo từ backen
			//location.reload();
			window.location.href = _ctx + 'orders-datatable';
		})
		.catch(error => {
			console.error("Lỗi khi tạo đơn hàng:", error);
			alert("Đã xảy ra lỗi: " + error.message);
		});

});