function loadOrderStatuses_Datatable() {
	const categoryId = document.getElementById("orderCategorySelect").value;
	window.location.href = _ctx + `orders-datatable?categoryId=${categoryId}`;
}

document.addEventListener("DOMContentLoaded", function() {
	const orderCategorySelect = document.getElementById("orderCategoryCreate");
	const orderStatusSelect = document.getElementById("orderStatusCreate");
	const _ctx = "/crm-web/";
	const defaultCategoryId = 0;

	loadOrderStatuses_ToCreateOrder(defaultCategoryId);

	if (orderCategorySelect && orderStatusSelect) {
		orderCategorySelect.addEventListener("change", function() {
			const categoryId = this.value;
			loadOrderStatuses_ToCreateOrder(categoryId);  // Gọi lại API với categoryId mới
		});
	}

	function loadOrderStatuses_ToCreateOrder(categoryId) {
		fetch(`${_ctx}orders-datatable/order-statuses?categoryId=${categoryId}`)
			.then(response => response.json())
			.then(orderStatuses => {
				// Xóa tất cả các tùy chọn hiện tại
				orderStatusSelect.innerHTML = "";

				orderStatuses.forEach(status => {
					const option = document.createElement("option");
					option.value = status[0];
					option.textContent = status[1];
					orderStatusSelect.appendChild(option);
				});
			})
			.catch(error => console.error("Error loading order statuses:", error));
	}

})

/*document.addEventListener("DOMContentLoaded", function() {
	const orderCategory_UpdateStatusSelect = document.getElementById("orderCategoryUpdateStatus");
	const orderStatus_UpdateStatusSelect = document.getElementById("orderStatusUpdateStatus");
	const _ctx = "/crm-web/";

	$('#updateOrderStatusModal').on('shown.bs.modal', function() {
		const categoryId = orderCategory_UpdateStatusSelect.value;
		if (categoryId) {
			loadOrderStatuses_ToUpdateStatusOrder(categoryId);
		}
	});

	function loadOrderStatuses_ToUpdateStatusOrder(categoryId) {
		fetch(`${_ctx}orders-datatable/order-statuses?categoryId=${categoryId}`)
			.then(response => response.json())
			.then(orderStatuses => {
				// Xóa tất cả các tùy chọn hiện tại
				orderStatus_UpdateStatusSelect.innerHTML = "";

				orderStatuses.forEach(status => {
					const option = document.createElement("option");
					option.value = status[0];
					option.textContent = status[1];
					orderStatus_UpdateStatusSelect.appendChild(option);
				});
			})
			.catch(error => console.error("Error loading order statuses:", error));
	}

})*/

document.addEventListener("DOMContentLoaded", function() {
	const orderCategorySelect = document.getElementById("orderCategoryFilter");
	const orderStatusContainer = document.getElementById("orderStatusFilter");
	const _ctx = "/crm-web/";
	const defaultCategoryId = 0;

	loadOrderStatuses(defaultCategoryId);

	orderCategorySelect.addEventListener("change", function() {
		const categoryId = this.value;
		loadOrderStatuses(categoryId);
	});

	function loadOrderStatuses(categoryId) {
		fetch(`${_ctx}orders-datatable/order-statuses?categoryId=${categoryId}`)
			.then((response) => response.json())
			.then((orderStatuses) => {
				// Xóa nội dung cũ
				orderStatusContainer.innerHTML = "";

				if (orderStatuses.length === 0) {
					orderStatusContainer.innerHTML = "<p>Không có trạng thái nào phù hợp.</p>";
					return;
				}

				// Tạo checkbox cho từng trạng thái
				orderStatuses.forEach((status) => {
					const label = document.createElement("label");
					label.classList.add("checkbox-spacing");

					const checkbox = document.createElement("input");
					checkbox.type = "checkbox";
					checkbox.name = "orderStatus";
					checkbox.value = status[0]; // Lấy id từ phần tử đầu tiên của mảng
					checkbox.classList.add("custom-checkbox");

					// Gắn tên trạng thái từ phần tử thứ hai của mảng
					label.appendChild(checkbox);
					label.appendChild(document.createTextNode(status[1]));

					// Thêm label vào container
					orderStatusContainer.appendChild(label);

					// Thêm khoảng cách dòng
					orderStatusContainer.appendChild(document.createElement("br"));
				});
			})
			.catch((error) => {
				console.error("Error loading order statuses:", error);
				orderStatusContainer.innerHTML = "<p>Chưa có trạng thái đơn hàng nào phù hợp.</p>";
			});
	}
});
