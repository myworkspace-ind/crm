document.addEventListener('DOMContentLoaded', () => {
	const chatBtn = document.getElementById('chat-button');
	const chatAssistant = document.getElementById('chat-assistant');
	const modalContainer = document.getElementById('modal-container');
	const modalOverlay = document.getElementById('modal-overlay');
	const closeButton = document.getElementById('close-btn-modal');

	chatBtn.addEventListener('click', () => {
		if (chatAssistant.style.display === 'none' || chatAssistant.style.display === '') {
			chatAssistant.style.display = 'block';
		} else {
			chatAssistant.style.display = 'none';
		}
	})

	/*function toggleChat() {
		const chat = document.getElementById('chat-assistant');
		if (chat.style.display === 'none' || chat.style.display === '') {
			chat.style.display = 'block';
		} else {
			chat.style.display = 'none';
		}
	}*/

	window.switchTab = function(tabId) {
		const tabs = document.querySelectorAll('.tab-content');
		tabs.forEach(tab => tab.style.display = 'none');

		const activeTab = document.getElementById(tabId);
		if (activeTab) {
			activeTab.style.display = 'block';
		}

		const tabButtons = document.querySelectorAll('.tab-btn');
		tabButtons.forEach(btn => btn.classList.remove('active'));

		const clickedButton = document.querySelector(`button[onclick="switchTab('${tabId}')"]`);
		if (clickedButton) {
			clickedButton.classList.add('active');
		}
	};

	const defaultTab = document.getElementById('customer-reminder');
	if (defaultTab) {
		switchTab('customer-reminder');
	}

	//Mở customer care table
	const clickHere = document.getElementById('click-here');
	if (clickHere) {
		clickHere.addEventListener('click', () => {
			modalContainer.style.display = 'block';
			modalOverlay.style.display = 'block';
		});
	}

	closeButton.addEventListener("click", function() {
		console.log("Close button clicked!");

		let unSavedCustomers = [];

		$.ajax({
			url: _ctx + "customer-care/check-unsaved-customercare",
			method: "GET",
			dataType: "json"
		}).then(function(response) {
			if (response && Array.isArray(response.customersWithoutData)) {
				unSavedCustomers = response.customersWithoutData;
				console.log("Customer care without data: ", unSavedCustomers);
				console.log("Length: ", unSavedCustomers.length)
			//	        if (unSavedCustomers.length === 0) {
			//	            modalContainer.style.display = "none";
			//	            modalOverlay.style.display = "none";
			//	        }
				if (unSavedCustomers.length > 0) {
					const customerList = unSavedCustomers.map(c => {
						const reminderDate = new Date(c[9]);
						const formattedDate = reminderDate.toLocaleDateString('vi-VN');
						return `<li> - ${c[2]} - Ngày nhắc nhở: ${formattedDate} </li>`
					})
					Swal.fire({
						title: "⚠ Khách hàng chưa được lưu!",
						html: `<p>Các khách hàng sau chưa được lưu vào hệ thống:</p>
					                   <ul style="text-align: left; list-style-type: none;">${customerList.join('')}</ul>`,
						icon: "warning",	
						showCancelButton: true,
						confirmButtonText: "Lưu KH",
						cancelButtonText: "Đóng"
					})
						.then(function(result) {
							if (result.isConfirmed) {
								$.ajax({
									url: _ctx + 'customer-care/save-customer-care',
									method: 'GET',
									dataType: "text",
									success: function(response) {
										console.log("API load-potential thành công:", response);
	
										if (response.includes("thành công")) {
											Swal.fire({
												title: "✅ Thành công!",
												text: response,
												icon: "success",
												confirmButtonText: "OK"
											}).then(() => {
												location.reload();
											});
										} else {
											Swal.fire({
												title: "⚠ Lỗi!",
												text: response,
												icon: "error",
												confirmButtonText: "Thử lại"
											}).then(() => {
												location.reload();
											});
										}
									},
									error: function(xhr, error) {
										console.error("Lỗi khi gọi API load-potential:", error);
										console.log("Chi tiết lỗi:", xhr);
										alert("Lỗi khi tải lại danh sách: " + xhr.responseText);
									},
								});
							}
						})
				}
			}
		})
		.catch(function(error) {
			console.error("Lỗi khi kiểm tra khách hàng chưa lưu:", error);
		});	
		
		Swal.fire({
				title: "⚠ Bạn nên cập nhật tình trạng chăm sóc khách hàng trước khi thoát khỏi Danh sách",
				icon: "info",
				showCancelButton: true,
				confirmButtonText: "Cập nhật tình trạng CSKH",
				cancelButtonText: "Đóng"
			}).then((result) => {
				if (result.isConfirmed) {
					// Gọi API update-care-status
					$.ajax({
						url: _ctx + 'customer-care/update-care-status',
						method: 'PUT',
						success: function(response) {
							Swal.fire({
								title: "✅ Cập nhật thành công!",
								text: "Tình trạng CSKH đã được cập nhật.",
								icon: "success",
								confirmButtonText: "OK"
							}).then(() => {
								location.reload();
							});
						},
						error: function(xhr, error) {
							console.error("Lỗi khi cập nhật tình trạng CSKH:", error);
							Swal.fire({
								title: "⚠ Lỗi!",
								text: "Không thể cập nhật tình trạng CSKH.",
								icon: "error",
								confirmButtonText: "Thử lại"
							});
						}
					});
				} else {
					// Đóng modal khi nhấn Đóng
					modalContainer.style.display = "none";
					modalOverlay.style.display = "none";
				}
			})	
	});
	
	
});


	//	document.querySelectorAll('.radio-badge-group input[type="radio"]').forEach(radio => {
	//	    radio.addEventListener('change', function () {
	//	        const parent = this.closest('.radio-badge-group');
	//	        parent.querySelectorAll('label').forEach(label => {
	//	            label.className = 'badge badge-default'; // Reset màu mặc định
	//	        });
	//
	//	        const selectedLabel = parent.querySelector(`label[for="${this.id}"]`);
	//	        switch (this.value) {
	//	            case '1':
	//	                selectedLabel.classList.add('badge-danger');
	//	                break;
	//	            case '2':
	//	                selectedLabel.classList.add('badge-warning');
	//	                break;
	//	            case '3':
	//	                selectedLabel.classList.add('badge-primary');
	//	                break;
	//	        }
	//	    });
	//	});

	//	function loadCustomerCareData() {
	//	        fetch(`${_ctx}customer-care/load-customer-care`)
	//	            .then(response => {
	//	                if (!response.ok) {
	//	                    throw new Error(`HTTP error! Status: ${response.status}`);
	//	                }
	//	                return response.json();
	//	            })
	//	            .then(data => {
	//	                let tableBody = document.getElementById("customerTableBody");
	//	                if (tableBody) {
	//	                    tableBody.innerHTML = "";
	//	                    data.forEach(customer => {
	//	                        let row = `<tr>
	//	                            <td>
	//	                                <button class="btn btn-update">Cập nhật</button>
	//	                                <button class="btn btn-hide">Ẩn</button>
	//	                            </td>
	//	                            <td class="company-name-customer-care" style="cursor: pointer;">${customer.customer.companyName || 'N/A'}</td>
	//	                            <td class="contact-person-customer-care">${customer.customer.contactPerson || 'N/A'}</td>
	//								<td class="main-status-customer-care" style="color: #FF6961; font-weight: 600">${customer.customer.mainStatus.name || 'N/A'}</td>
	//								<td class="interaction-btn-customer-care" style="cursor: pointer;">
	//									<button">Xem tương tác</button> 
	//								</td>
	//	                        </tr>`;
	//	                        tableBody.innerHTML += row;
	//	                    });
	//	                }
	//	            })
	//	            .catch(error => console.error("Lỗi khi tải dữ liệu: ", error));
	//	    }
	//
	//	    loadCustomerCareData();





