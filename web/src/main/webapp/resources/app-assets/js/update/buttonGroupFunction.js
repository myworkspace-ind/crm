document.addEventListener('DOMContentLoaded', () => {
	const deleteBtn = document.getElementById("deleteBtn");
	const modal = document.getElementById("confirmationModal");
	const overlay = document.getElementById("overlay");
	const cancelBtn = document.getElementById("cancelBtn");
	const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");
	const checkboxes = document.querySelectorAll('.customer-checkbox');
	const quantityCheck = document.querySelector('.quantity-check');
	const quitBtn = document.querySelector('.quit');

	// Variable to store selected customer IDs
	let selectedCustomerIds = [];

	checkboxes.forEach(checkbox => {
		checkbox.addEventListener('change', function() {
			const id = this.value;

			if (this.checked) {
				// Add ID to list if checkbox is selected
				selectedCustomerIds.push(id);
			} else {
				// Remove ID from list if checkbox is unchecked
				selectedCustomerIds = selectedCustomerIds.filter(customerId => customerId !== id);
			}

			// Update information on the number of selected customers
			document.querySelector('.quantity-check').textContent = selectedCustomerIds.length;
		});
	});

	deleteBtn.addEventListener('click', () => {
		if (selectedCustomerIds.length === 0) {
			alert("Vui lòng chọn ít nhất một khách hàng để xóa.");
		} else {
			// Show modal to delete customers
			modal.style.display = "block";
			overlay.style.display = "block";
		}
	});

	cancelBtn.addEventListener("click", () => {
		modal.style.display = "none";
		overlay.style.display = "none";
	});

	confirmHideBtn.addEventListener('click', () => {
		console.log("ID khách hàng muốn xóa:", selectedCustomerIds);
		modal.style.display = "none";
		overlay.style.display = "none";
		hideCustomers(selectedCustomerIds);
	});

	quitBtn.addEventListener('click', () => {
		selectedCustomerIds = [];
		quantityCheck.textContent = 0;

		checkboxes.forEach(checkbox => {
			checkbox.checked = false;
		});
	});

	function hideCustomers(selectedCustomerIds) {
		fetch(`${_ctx}customer/hide-customers`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(selectedCustomerIds) 
		})
			.then(response => {
				if (!response.ok) {
					throw new Error(`Lỗi ẩn khách hàng: ${response.statusText}`);
				}
				return response.json();
			})
			.then(data => {
				alert(`Khách hàng với ID ${data.ids} đã được ẩn.`);
				location.href = `${_ctx}customer/list`;
				// Cập nhật giao diện: xóa checkbox và cập nhật số lượng
				selectedCustomerIds.forEach(id => {
					console.log("Danh sách ID đã chọn:", selectedCustomerIds);
					
					const checkbox = document.querySelector(`input[type="checkbox"][value="${id.toString()}"]`);
					console.log("Checkbox tìm được:", checkbox);  

					if(checkbox){
						console.log(`Found checkbox with ID: ${id}`);
						const row = checkbox.closest('tr');
						if(row){
							//delete row away from DOM
							console.log("Đang xóa hàng chứa checkbox với ID:", id);
							row.remove();
						} else{
							console.warn(`Không tìm thấy <tr> chứa checkbox cho ID: ${id}`);
						}
					} else{
						console.warn(`Không tìm thấy checkbox cho ID: ${id}`);
					}
				});

				// Đặt lại danh sách ID đã chọn và số lượng
				selectedCustomerIds = []; // Đặt lại danh sách ID
				document.querySelector('.quantity-check').textContent = selectedCustomerIds.length; // Đặt lại số lượng
			})
			.catch(error => {
				console.error(`Xóa khách hàng thất bại. Lỗi: ${error}`);
				alert(`Có lỗi xảy ra khi xóa khách hàng: ${error.message}`);
			});
	}


});


