/*var dataSetCustomerCare */

function checkUncaredCustomers() {
    let hasUncared = false;
    
    // Kiểm tra xem có dòng nào trong tbody không
    let rows = $('#tblDatatableCustomerCare tbody tr');
    console.log(`🔍 Tổng số hàng trong bảng: ${rows.length}`);
    
    if (rows.length === 0) {
        console.log("⚠ Bảng chưa có dữ liệu!");
    }

    rows.each(function (index) {
        let status = $(this).find('td:nth-child(8)').text().trim();
        console.log(`📌 Hàng ${index + 1}: Tình trạng - '${status}'`);
        
        if (status === "Chưa chăm sóc") {
            hasUncared = true;
            console.log("❗ Phát hiện khách hàng chưa chăm sóc, dừng kiểm tra.");
            return false;
        }
    });

    if (hasUncared) {
        console.log("✅ Có khách hàng chưa chăm sóc => Bật cảnh báo chat.");
        $("#chat-button").addClass("chat-alert");
    } else {
        console.log("✅ Tất cả khách hàng đã được chăm sóc => Tắt cảnh báo chat.");
        $("#chat-button").removeClass("chat-alert");
    }
}


$(document).ready(function() {
	
	console.log("jQuery version:", $.fn.jquery);
	console.log("DataTables version:", $.fn.dataTable);
	
	$('#reloadCustomerCare').on('click', function(){
		//setTimeout(checkUncaredCustomers, 1000);
		
		$(this).prop('disabled', true).html('<i class="fa fa-spinner fa-spin"></i> Đang tải...');
		
		$.ajax({
			url: _ctx + 'customer-care/load-potential',
			method: 'GET',
			dataType: "text",
			success: function (response){
				console.log("API load-potential thành công:", response);
				alert(response);
				loadCustomerCareData(); // Gọi lại hàm để tải dữ liệu mới vào bảng
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
	    let customerId = $row.find('td:first').text().trim(); 
	    let priority = $row.find('.radio-badge-group input:checked').val(); 

	    if (!priority) {
	        alert("Vui lòng chọn mức độ ưu tiên trước khi lưu.");
	        return;
	    }
		
		if (priority) {
			updateList.push({ id: parseInt(customerId), priority });
		}
		
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
			success: function(dataSetCustomerCare) {
				console.log("Dữ liệu nhận được:", dataSetCustomerCare);
				
				let table = $('#tblDatatableCustomerCare').DataTable({
					data: dataSetCustomerCare,
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
								if (data === "Đã chăm sóc !") {
									return `
									                <div class="alert alert-success" role="alert">
									                    <strong>Đã chăm sóc!</strong>
									                </div>
									            `;
								} else if (data === "Chưa chăm sóc") {
									return `
									                <div class="alert alert-primary" role="alert">
									                    <strong>Chưa chăm sóc</strong>
									                </div>
									            `;
								}
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
				
				table.clear();
				table.rows.add(dataSetCustomerCare).draw(false);

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










