/*var dataSetCustomerCare */

function notifyPotentialCustomers() {
	fetch(`${_ctx}customer-care/load-customer-care/`)
		.then(response => response.json())
		.then(data => {
			console.log("Danh sách khách hàng cần nhắc nhở:", data);

			// Kiểm tra nếu dữ liệu trả về có đúng định dạng object hay không
			if (!data || typeof data !== "object") {
				console.error("Dữ liệu trả về không hợp lệ:", data);
				return;
			}

			// Lấy danh sách khách hàng có hoặc không có dữ liệu
			const customersWithData = Array.isArray(data.customersWithData) ? data.customersWithData : [];
			const customersWithoutData = Array.isArray(data.customersWithoutData) ? data.customersWithoutData : [];

			// Gộp hai danh sách lại để xử lý chung
			const allCustomers = [...customersWithData, ...customersWithoutData];

			if (allCustomers.length === 0) {
				console.warn("Không có khách hàng nào cần nhắc nhở.");
				return;
			}

			allCustomers.forEach(customerData => {
				if (!Array.isArray(customerData) || customerData.length < 5) {
					console.warn("Dữ liệu khách hàng không hợp lệ:", customerData);
					return;
				}

				const customer = {
					id: customerData[0] || "unknown",
					companyName: customerData[2] || "Không rõ",
					contactName: customerData[3] || "Không có",
					status: customerData[4] || "Không xác định"
				};

				showMessageNotification(customer);
			});
		})
		.catch(error => console.error('Lỗi khi lấy danh sách khách hàng', error));
}

function showMessageNotification(customer) {
	const reminderList = document.getElementById("customer-reminder");
	if (!reminderList) return;

	if (document.querySelector(`#customer-reminder li[data-id='${customer.id}']`)) {
		return;
	}

	let reminderItem = document.createElement("li");
	reminderItem.classList.add("mb-3");
	reminderItem.setAttribute("data-id", customer.id);

	let now = new Date();
	let currentDate = now.toISOString().split("T")[0]; // Lấy ngày hiện tại (YYYY-MM-DD)

	let createdAt = customer.createdAt ? new Date(customer.createdAt) : now;
	let createdDate = createdAt.toISOString().split("T")[0]; // Lấy ngày tạo (YYYY-MM-DD)

	let displayDate;
	if (currentDate === createdDate) {
		displayDate = "Hôm nay";
	} else {
		displayDate = createdAt.toLocaleDateString("vi-VN", {
			day: "2-digit",
			month: "2-digit",
			year: "numeric"
		});
	}

	let currentTime = now.toLocaleTimeString("vi-VN", { hour: "2-digit", minute: "2-digit" });

	reminderItem.innerHTML = `
        <div class="bg-light p-2 rounded">
            <p class="mb-1 text-dark">
                Nhắc nhở: Khách hàng 
                <a href="#" onclick="highlightCustomer('${customer.id}')" class="fw-bold">${customer.companyName}</a> 
                cần chăm sóc ngay!
            </p>
            <small class="text-muted">${displayDate}, ${currentTime}</small>
        </div>`;

	reminderList.appendChild(reminderItem);
}


function highlightCustomer(customerId) {
	const modalContainer = document.getElementById('modal-container');
	const modalOverlay = document.getElementById('modal-overlay');

	if (!modalContainer || !modalOverlay) return;

	// Mở modal nếu nó đang bị ẩn
	if (modalContainer.style.display === "none" || modalContainer.style.display === "") {
		modalContainer.style.display = "block";
		modalOverlay.style.display = "block";
	}

	const table = document.getElementById("tblDatatableCustomerCare");
	if (!table) return;

	// Tìm tất cả các hàng trong tbody
	const rows = table.querySelectorAll("tbody tr");

	let targetRow = null;

	// Duyệt từng dòng để tìm ID trong cột đầu tiên
	rows.forEach(row => {
		const firstCell = row.querySelector("td"); // Cột đầu tiên chứa ID
		if (firstCell && firstCell.textContent.trim() === customerId.toString()) {
			targetRow = row;
		}
	});

	if (targetRow) {
		// Đợi bảng hiển thị xong rồi mới cuộn và viền
		setTimeout(() => {
			targetRow.scrollIntoView({ behavior: "smooth", block: "center" });
			targetRow.style.transition = "border 0.5s ease-in-out";
			targetRow.style.border = "2px solid red";

			setTimeout(() => { targetRow.style.border = "none"; }, 5000);
		}, 300); // Đợi 300ms để modal mở hoàn toàn
	} else {
		console.log(`Không tìm thấy dòng khách hàng với ID: ${customerId}`);
	}
}



function checkUncaredCustomers() {
	let hasUncared = false;

	// Kiểm tra xem có dòng nào trong tbody không
	let rows = $('#tblDatatableCustomerCare tbody tr');
	console.log(`🔍 Tổng số hàng trong bảng: ${rows.length}`);

	if (rows.length === 0) {
		console.log("⚠ Bảng chưa có dữ liệu!");
	}

	rows.each(function(index) {
		let status = $(this).find('td:nth-child(8)').text().trim();
		console.log(`📌 Hàng ${index + 1}: Tình trạng - '${status}'`);

		if (status === "Chưa chăm sóc") {
			hasUncared = true;
			console.log("❗ Phát hiện khách hàng chưa chăm sóc, dừng kiểm tra.");
			return false;
		}
	});

	let chatButton = $("#chat-button");
	let chatMessages = $("#chat-messages");

	if (hasUncared) {
		console.log("✅ Có khách hàng chưa chăm sóc => Bật cảnh báo chat.");
		chatButton.addClass("chat-alert");

		//Thêm tin nhắn thông báo báo nếu chưa có
		if ($("#chat-messages .alert-message").length === 0) {
			chatMessages.append('<div class="alert-message">⚠ Hãy chăm sóc khách hàng của bạn!</div>');
		}
	} else {
		console.log("✅ Tất cả khách hàng đã được chăm sóc => Tắt cảnh báo chat.");
		chatButton.removeClass("chat-alert");

		//Xóa tin nhắn thông báo
		$("#chat-messages .alert-message").remove();
	}
}


$(document).ready(function() {

	console.log("jQuery version:", $.fn.jquery);
	console.log("DataTables version:", $.fn.dataTable);

	$('#reloadCustomerCare').on('click', function() {
		//setTimeout(checkUncaredCustomers, 1000);

		$(this).prop('disabled', true).html('<i class="fa fa-spinner fa-spin"></i> Đang tải...');

		$.ajax({
			url: _ctx + 'customer-care/load-potential',
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
						loadCustomerCareData();
						location.reload();
					});
				} else {
					Swal.fire({
						title: "⚠ Lỗi!",
						text: response,
						icon: "error",
						confirmButtonText: "Thử lại"
					}).then(() => {
						loadCustomerCareData();
						location.reload();
					});
				}

			},
			error: function(xhr, error) {
				console.error("Lỗi khi gọi API load-potential:", error);
				console.log("Chi tiết lỗi:", xhr);
				alert("Lỗi khi tải lại danh sách: " + xhr.responseText);
			},
			complete: function() {
				$('#reloadCustomerCare').prop('disabled', false).html('<i class="fa fa-sync"></i> Tải lại danh sách');
			}
		});
	})

	$('#tblDatatableCustomerCare tbody').on('click', '.btn-save', function() {
		let updateList = [];

		let $row = $(this).closest('tr'); // Lấy hàng chứa nút được nhấn
		let customerCareId = $row.find('td:first').text().trim();
		let priority = $row.find('.radio-badge-group input:checked').val();

		if (!customerCareId) {
			alert("Không tìm thấy ID khách hàng.");
			return;
		}

		if (!priority) {
			alert("Vui lòng chọn mức độ ưu tiên trước khi lưu.");
			return;
		}

		$.ajax({
			url: _ctx + 'customer-care/check-exist/' + customerCareId, // API kiểm tra
			method: "GET",
			success: function(response) {
				if (!response.exists) {
					Swal.fire({
						title: "⚠ Khách hàng chưa có trong hệ thống!",
						html: `Vui lòng nhấn nút LƯU DANH SÁCH để cập nhật danh sách khách hàng cần chăm sóc.`,
						icon: "warning",
						confirmButtonText: "OK",
						confirmButtonColor: "#d33",
						customClass: {
							title: "custom-title",
							popup: "custom-popup"
						}
					});
					return;
				}

				updateList.push({ id: parseInt(customerCareId), priority });
				console.log("🔄 Dữ liệu gửi đi:", updateList);

				$.ajax({
					url: _ctx + 'customer-care/update-priority',
					method: "PUT",
					headers: { "Content-Type": "application/json" },
					data: JSON.stringify(updateList),
					success: function(response) {
						alert("Cập nhật thành công!");
						console.log("✅ API Response:", response);
					},
					error: function(xhr, error) {
						console.error("❌ Lỗi khi cập nhật priority:", error);
						alert("Lỗi khi cập nhật dữ liệu: " + xhr.responseText);
					}
				});

			},
			error: function(xhr, error) {
				console.error("❌ Lỗi khi kiểm tra khách hàng:", error);
				alert("Lỗi khi kiểm tra thông tin khách hàng.");
			}
		});
	});


	$('#tblDatatableCustomerCare tbody').on('click', '.care-button', function() {
		alert("Đã click vào nút Chăm sóc");
		//		let rowData = $('#tblDatatableCustomerCare').DataTable().row($(this).parents('tr')).data();
		//		console.log("Dữ liệu hàng:", rowData);
		//
		//		// Gắn dữ liệu vào modal (nếu cần)
		//		$('#careModal .modal-body').html(`<p>Đang chăm sóc khách hàng: <strong>${rowData[2]}</strong></p>`);
		//
		//		// Hiển thị modal
		//		$('#careModal').modal('show');
	});


	if (!$.fn.DataTable.isDataTable('#tblDatatableCustomerCare')) {
		loadCustomerCareData();
	}

	function loadCustomerCareData() {
		console.log("Bắt đầu gửi request AJAX...");
		$.ajax({
			url: _ctx + 'customer-care/load-customer-care/',
			method: "GET",
			dataType: "json",
			success: function(data) {
				console.log("Dữ liệu nhận được:", data);

				if (!data || typeof data !== "object") {
					console.error("Dữ liệu trả về không đúng định dạng:", data);
					return;
				}

				//Lấy danh sách KH cần chăm sóc (có hoặc ko có dữ liệu)
				const customersWithData = Array.isArray(data.customersWithData) ? data.customersWithData : [];
				const customersWithoutData = Array.isArray(data.customersWithoutData) ? data.customersWithoutData : [];

				// Gộp tất cả khách hàng lại
				const allCustomers = [...customersWithData, ...customersWithoutData];

				if (allCustomers.length === 0) {
					console.warn("Không có khách hàng nào để hiển thị.");
					return;
				}

				console.log("Dữ liệu allCustomers:", allCustomers);

				// Kiểm tra nếu DataTable đã tồn tại thì xóa nó trước
				if ($.fn.DataTable.isDataTable('#tblDatatableCustomerCare')) {
					var table = $('#tblDatatableCustomerCare').DataTable();
					table.clear(); // Xóa dữ liệu hiện tại
					table.rows.add(allCustomers); // Thêm dữ liệu mới
					table.draw(); // Vẽ lại bảng	
				} else {
					// Khởi tạo lại DataTable
					let table = $('#tblDatatableCustomerCare').DataTable({
						data: allCustomers,
						dom: 'Bfrtip',
						paging: true, // Phân trang
						searching: true, // Tìm kiếm
						ordering: true, // Sắp xếp
						lengthMenu: [5, 10, 25],
						buttons: [
							{
								extend: 'copyHtml5',
								text: '<i class="fa fa-copy"></i> Sao chép',
								className: 'btn-copy'
							},
							{
								extend: 'excelHtml5',
								text: '<i class="fa fa-file-excel"></i> Xuất Excel',
								className: 'btn-excel'
							},
							{
								extend: 'csvHtml5',
								text: '<i class="fa fa-file-csv"></i> Xuất CSV',
								className: 'btn-csv'
							},
							{
								extend: 'pdfHtml5',
								text: '<i class="fa fa-file-pdf"></i> Xuất PDF',
								className: 'btn-pdf'
							}
						],
						initComplete: function() {
							console.log("✅ DataTable đã khởi tạo xong, kiểm tra dữ liệu...");
							checkUncaredCustomers();
							notifyPotentialCustomers();
						},
						columnDefs: [
							{
								targets: 0,
								visible: true
							},
							{
								targets: 1,
								data: null,
								defaultContent: `
														        <div class="btn-group-customer-care" role="group">
														            <button class="btn btn-primary btn-save"><i class="bi bi-save"></i></button>
														            <button class="btn btn-danger btn-sm"><i class="bi bi-eye-slash"></i></button>
														        </div>`
							},
							{
								targets: 2,
								className: 'company-name-column'
							},
							{
								targets: 5,
								data: null,
								defaultContent: `
																<div class="interaction-btn-customer-care" style="cursor: pointer;">
																	<button>Xem tương tác</button>
																</div>`
							},
							{
								targets: 6,
								data: null,
								render: function(data, type, row, meta) {
									let priority = row[6];

									if (priority) {
										switch (priority) {
											case "Rất quan trọng":
												return `<span class="badge badge-danger">${priority}</span>`;
											case "Quan trọng":
												return `<span class="badge badge-warning">${priority}</span>`;
											case "Trung bình":
												return `<span class="badge badge-primary">${priority}</span>`;
											default:
												return `<span class="badge badge-default">${priority || "Chưa có dữ liệu"}</span>`;
										}
									}

									setTimeout(() => {
										updateBadgeStyle($(`.radio-badge-group[data-row="${meta.row}"]`), data, meta.row)
									}, 0);

									return `
															        <div class="radio-badge-group"> 
															            <input type="radio" id="option1-row${meta.row}" name="priority-row${meta.row}" value="Rất quan trọng" hidden>
															            <label for="option1-row${meta.row}" class="badge badge-default">Rất quan trọng</label>
															                                        
															            <input type="radio" id="option2-row${meta.row}" name="priority-row${meta.row}" value="Quan trọng" hidden>
															            <label for="option2-row${meta.row}" class="badge badge-default">Quan trọng</label>
															                                        
															            <input type="radio" id="option3-row${meta.row}" name="priority-row${meta.row}" value="Trung bình" hidden>
															            <label for="option3-row${meta.row}" class="badge badge-default">Trung bình</label>
															        </div>`;
								}
							},
							{
							    targets: 7,
							    render: function(data, type, row, meta) {
							        if (!data || data.trim() === "") {
							            data = "Chưa chăm sóc";
							        }

							        let alerts = [];

							        // Chia data thành các trạng thái nếu có nhiều trạng thái (giả sử các trạng thái phân tách nhau bằng dấu phẩy)
							        let statuses = data.split(",").map(status => status.trim());

							        // Kiểm tra từng trạng thái và thêm alert vào mảng
							        statuses.forEach(status => {
							            if (status === "Đã chăm sóc") {
							                alerts.push(
							                    `<div class="alert alert-success" role="alert">
							                        <strong>Đã chăm sóc</strong>
							                    </div>`
							                );
							            } else if (status === "Chưa chăm sóc") {
							                alerts.push(
							                    `<div class="alert alert-primary" role="alert">
							                        <strong>Chưa chăm sóc</strong>
							                    </div>`
							                );
							            } else if (status === "Chăm sóc đúng hạn") {
							                alerts.push(
							                    `<div class="alert alert-info" role="alert">
							                        <strong>Chăm sóc đúng hạn</strong>
							                    </div>`
							                );
							            } else if (status === "Quá hạn chăm sóc") {
							                alerts.push(
							                    `<div class="alert alert-danger" role="alert">
							                        <strong>Quá hạn chăm sóc</strong>
							                    </div>`
							                );
							            }
							        });

							        // Nếu có ít nhất một alert, trả về tất cả các alert kết hợp
							        if (alerts.length > 0) {
							            return alerts.join(''); // Ghép các alert lại thành chuỗi
							        }

							        // Nếu không có alert nào, trả về giá trị data
							        return data;
							    }
							},

							{
								targets: 8,
								data: null,
								defaultContent: `
															<div class="action-care">
																<button class="care-button">
																	<span class="icon"></span>
																	<span class="text">Chăm sóc</span>
																</button>
																
																<button class="meet-button">
																	<span class="icon"></span>
																	<span class="text">Hẹn gặp mặt</span>
																</button>
															</div>`
							},
						],
						createdRow: function(row, data, dataIndex) {
							let priority = data[6];
							let $radioGroup = $(row).find('.radio-badge-group');
							let $selectedRadio = $radioGroup.find(`input[value="${priority}"]`);
							$selectedRadio.prop('checked', true);
							// Gọi hàm cập nhật màu khi tải dữ liệu
							updateBadgeStyle($radioGroup, priority);
						}
					});
				}

				$('#tblDatatableCustomerCare tbody').on('change', 'input[type="radio"]', function() {
					let $radioGroup = $(this).closest('.radio-badge-group');
					updateBadgeStyle($radioGroup, $(this).val());
				});

				// Hàm cập nhật màu cho badge dựa trên giá trị
				function updateBadgeStyle($radioGroup, priority, rowIndex) {
					//					console.log(`🔄 Cập nhật màu cho hàng ${rowIndex}, mức độ: ${priority}`);

					$radioGroup.find('label').removeClass('badge-danger badge-warning badge-primary').addClass('badge-default');

					let $selectedRadio = $radioGroup.find(`input[value="${priority}"]`);

					if ($selectedRadio.length) {
						let labelFor = $selectedRadio.attr('id');
						let $label = $radioGroup.find(`label[for="${labelFor}"]`);

						switch (priority) {
							case "Rất quan trọng":
								$label.addClass('badge-danger');
								break;
							case "Quan trọng":
								$label.addClass('badge-warning');
								break;
							case "Trung bình":
								$label.addClass('badge-primary');
								break;
						}
					}
				}
			},
			error: function(xhr, error) {
				console.error("Lỗi khi tải dữ liệu:", error);
				alert("Lỗi khi tải dữ liệu: " + xhr.responseText);
			}
		});
	}

})










