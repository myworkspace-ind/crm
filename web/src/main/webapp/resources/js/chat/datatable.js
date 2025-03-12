/*var dataSetCustomerCare */

$(document).ready(function() {
//	console.log("jQuery version:", $.fn.jquery);
//	console.log("DataTables version:", $.fn.DataTable);
	console.log("jQuery version:", $.fn.jquery);
	console.log("DataTables version:",$.fn.dataTable);

//	let table = $('#tblDatatableCustomerCare');
//
//	if (table.length === 0) {
//		console.error("❌ Không tìm thấy bảng #tblDatatableCustomerCare trong DOM!");
//		return;
//	} else {
//		console.log("✅ Tìm thấy bảng #tblDatatableCustomerCare.");
//	}
//
//	console.log("🔍 Kiểm tra DataTable đã khởi tạo chưa...");
//
//	if ($.fn.DataTable.isDataTable('#tblDatatableCustomerCare')) {
//		console.warn("⚠️ Bảng đã được khởi tạo trước đó, không khởi tạo lại.");
//	} else {
//		console.log("✅ Chưa có DataTable, tiến hành khởi tạo...");
//		table.DataTable();
//		console.log("🎉 DataTable đã được khởi tạo thành công.");
//	}
//
//
//
//	$(document).ready(function () {
//	    if ($.fn.DataTable) {
//	        if (!$.fn.DataTable.isDataTable('#tblDatatableCustomerCare')) {
//	            $('#tblDatatableCustomerCare').DataTable();
//	        }
//	    } else {
//	        console.error("DataTables chưa được tải!");
//	    }
//	});


	if (!$.fn.DataTable.isDataTable('#tblDatatableCustomerCare')) {
		console.log("Bắt đầu gửi request AJAX...");
		$.ajax({
			url: _ctx + 'customer-care/load-customer-care/',
			method: "GET",
			dataType: "json",
			success: function(dataSetCustomerCare) {
				console.log("Dữ liệu nhận được:", dataSetCustomerCare);

				$('#tblDatatableCustomerCare').DataTable({
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
					columnDefs: [
						{
							targets: 1, 
							data: null,
							defaultContent: `
								<div class="btn-group-customer-care" role="group">
									<button>Cập nhật</button>
									<button>Ẩn</button>
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
							defaultContent: `
							<div class="radio-badge-group"> 
								<input type="radio" id="option1" name="very-importance" value="1">
								<label for="option1-row6" class="badge badge-default">1. Rất quan trọng</label>
																			
								<input type="radio" id="option2" name="importance" value="2">
								<label for="option2-row6" class="badge badge-default">2. Quan trọng</label>
																			
								<input type="radio" id="option3" name="medium" value="3">
								<label for="option3-row6" class="badge badge-default">3. Bình thường</label>
							</div>`
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
						let priority = data[6]; // Giá trị độ ưu tiên từ database
						let $radioGroup = $(row).find('.radio-badge-group');

						// Đặt checked dựa vào dữ liệu từ database
						$radioGroup.find(`input[value="${priority}"]`).prop('checked', true);

						// Gọi hàm cập nhật màu khi tải dữ liệu
						updateBadgeStyle($radioGroup, priority);
					}
				});
				// Lắng nghe sự kiện khi chọn radio
				$('#tblDatatableCustomerCare tbody').on('change', 'input[type="radio"]', function() {
					let $radioGroup = $(this).closest('.radio-badge-group');
					console.log("radioGroup: ", $radioGroup);
					
					let selectedValue = $(this).val(); // Lấy giá trị được chọn
					console.log("selectedValue: ", selectedValue);
					
					updateBadgeStyle($radioGroup, selectedValue);
				});
				
				// Hàm cập nhật màu cho badge dựa trên giá trị
				function updateBadgeStyle($radioGroup, priority) {
					$radioGroup.find('label').removeClass('badge-danger badge-warning badge-primary').addClass('badge-default');

					switch (priority) {
						case "1":
							$radioGroup.find('input[value="1"]').next('label').removeClass('badge-default').addClass('badge-danger');
							break;
						case "2":
							$radioGroup.find('input[value="2"]').next('label').removeClass('badge-default').addClass('badge-warning');
							break;
						case "3":
							$radioGroup.find('input[value="3"]').next('label').removeClass('badge-default').addClass('badge-primary');
							break;
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










