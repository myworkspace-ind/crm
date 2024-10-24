document.addEventListener('DOMContentLoaded', () => {
	const deleteBtn = document.getElementById("deleteBtn");
	const modal = document.getElementById("confirmationModal");
	const overlay = document.getElementById("overlay");
	const cancelBtn = document.getElementById("cancelBtn");
	const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");
	const checkboxes = document.querySelectorAll('.customer-checkbox');

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

	confirmDeleteBtn.addEventListener('click', () => {
		console.log("ID khách hàng muốn xóa:", selectedCustomerIds);
		modal.style.display = "none";
		overlay.style.display = "none";
		deleteCustomers(selectedCustomerIds);
	});

	function deleteCustomers(selectedCustomerIds) {
		fetch('/crm-web/delete-customers', {
			method: 'DELETE',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(selectedCustomerIds) // Chuyển đổi danh sách ID thành JSON
		})
			.then(response => {
				if (!response.ok) {
					throw new Error(`Lỗi xóa khách hàng: ${response.statusText}`);
				}
				return response.json();
			})
			.then(data => {
				console.log(`Khách hàng với ID ${data.ids} đã được xóa.`);

				// Cập nhật giao diện: xóa checkbox và cập nhật số lượng
				selectedCustomerIds.forEach(id => {
					const checkbox = document.querySelector(`input[type="checkbox"][value="${id}"]`);
					if (checkbox) {
						// Xóa hàng khỏi DOM
						checkbox.closest('tr').remove(); // Xóa toàn bộ hàng chứa checkbox
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
