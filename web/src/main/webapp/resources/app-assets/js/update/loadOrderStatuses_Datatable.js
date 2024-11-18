function loadOrderStatuses_Datatable() {
	const categoryId = document.getElementById("orderCategorySelect").value;
	window.location.href = _ctx + `orders-datatable?categoryId=${categoryId}`;
}

document.addEventListener("DOMContentLoaded", function() {
	const orderCategorySelect = document.getElementById("orderCategoryCreate");
	const orderStatusSelect = document.getElementById("orderStatusCreate");
	const _ctx = "/crm-web/";
	const defaultCategoryId = 1;

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