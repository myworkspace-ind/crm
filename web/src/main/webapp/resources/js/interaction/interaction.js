const appState = {
	originalData: [], // Biến lưu trữ dữ liệu gốc
	currentFilter: { startDate: null, endDate: null, contactPerson: null }, // Trạng thái bộ lọc
};

const urlParams = new URLSearchParams(window.location.search);
const customerId = urlParams.get('id'); // Lấy giá trị của tham số 'id'

$(document).ready(function() {
	if (customerId) {
		loadTableData(customerId);
	} else {
		console.error('Không tìm thấy ID khách hàng trong URL.');
	}

	attachEventHandlers();
});

function attachEventHandlers() {
	$('#frmInteractionConfiguration').submit(handleFormSubmit);
	$('.btn-search-interaction').on('click', handleSearchInteraction);
}

function handleFormSubmit(e) {
	e.preventDefault();

	let customerId = urlParams.get('id') || window.id;

	if (!customerId) {
		console.error('Không có customerId để lưu tương tác!');
		showErrorToast('Không xác định được khách hàng để lưu.');
		return;
	}

	//	const tableData = htInteraction.getData().filter(row =>
	//		row.some(cell => cell !== null && cell !== '') // Loại bỏ các dòng hoàn toàn trống
	//	);
	//
	//	const isValid = tableData.every(row =>
	//		[0, 1, 2, 3].every(colIndex => row[colIndex] !== null && row[colIndex] !== '')
	//	);
	//
	//	if (!isValid) {
	//		showErrorToast('Vui lòng nhập đầy đủ thông tin trước khi lưu.');
	//		return;
	//	}

	//	const colHeaders = htInteraction.getColHeader();
	//	const colWidths = [];
	//
	//	for (let i = 0; i < colHeaders.length; i++) {
	//		colWidths.push(htInteraction.getColWidth(i));
	//	}
	//
	//	const formDataJson = JSON.stringify({
	//		colWidths: colWidths,
	//		colHeaders: colHeaders,
	//		data: tableData,
	//	});
	//
	//	console.log("Data to send:", formDataJson);

	saveInteraction(customerId);

	//	$.ajax({
	//		url: `${_ctx}customer/save-interaction?customer_id=${customerId}`,
	//		type: 'POST',
	//		data: formDataJson,
	//		dataType: "json",
	//		contentType: 'application/json',
	//		success: function(result) {
	//			console.log("Server Response:", result);
	//			reloadAndFilterData();
	//			showSuccessToast('Cập nhật thành công');
	//		},
	//		error: function(xhr) {
	//			console.error("AJAX Error:", xhr.responseText);
	//		}
	//	});
}

//function saveInteraction(customerId) {
//	console.log("ID cua customer lan 1: ", customerId);
//
//	const tableData = htInteraction.getData().filter(row =>
//		row.some(cell => cell !== null && cell !== '')
//	);
//
//	const isValid = tableData.every(row =>
//		[0, 1, 3, 4].every(colIndex => row[colIndex] !== null && row[colIndex] !== '')
//	);
//
//	if (!isValid) {
//		showErrorToast('Vui lòng nhập đầy đủ thông tin trước khi lưu.');
//		return;
//	}
//
//	const colHeaders = htInteraction.getColHeader();
//	const colWidths = [];
//
//	for (let i = 0; i < colHeaders.length; i++) {
//		colWidths.push(htInteraction.getColWidth(i));
//	}
//
//	const dataObject = {
//		colWidths: colWidths,
//		colHeaders: colHeaders,
//		data: tableData
//	};
//
//	const formData = new FormData();
//	//formData.append("data", new Blob([JSON.stringify(dataObject)], { type: "application/json" }));
//	formData.append("data", JSON.stringify(dataObject)); // plain text
//	console.log("Data: ", JSON.stringify(dataObject));
//	formData.append("customer_id", customerId);
//
//	// Thay vì dùng ?. kiểm tra thủ công
//	const inputElement = $("#htFileInput")[0];
//	console.log('Input file element:', inputElement);
//	console.log('Files:', inputElement ? inputElement.files : 'inputElement null');
//	const files = inputElement ? inputElement.files : null;
//
////	if (files && files.length > 0) {
////		for (let i = 0; i < files.length; i++) {
////			formData.append("files", files[i]); // key "files" phải khớp với @RequestPart("files")
////		}
////	}
//
//	for (const [key, files] of Object.entries(fileCache)) {
//		const [row, col] = key.split('-');
//
//		files.forEach((file, index) => {
//			if (file instanceof File) {
//				formData.append(`files[${row}][${col}][]`, file);
//				//formData.append("files", file);
//			}
//		});
//	}
//
//	$.ajax({
//		url: `${_ctx}customer/save-interaction`,
//		type: 'POST',
//		data: formData,
//		processData: false,  // không chuyển FormData thành chuỗi
//		contentType: false,  // để jQuery tự set header đúng multipart/form-data
//		success: function(result) {
//			console.log("Server Response:", result);
//			reloadAndFilterData();
//			showSuccessToast('Cập nhật thành công');
//		},
//		error: function(xhr) {
//			console.error("AJAX Error:", xhr.responseText);
//		}
//	});
//
//	console.log("ID cua customer lan 2: ", customerId);
//}



function saveInteraction(customerId) {
	console.log("ID cua customer lan 1: ", customerId);

	const tableData = htInteraction.getData().filter(row =>
		row.some(cell => cell !== null && cell !== '') // Loại bỏ các dòng hoàn toàn trống
	);

	const isValid = tableData.every(row =>
		[0, 1, 3, 4].every(colIndex => row[colIndex] !== null && row[colIndex] !== '')
	);

	if (!isValid) {
		showErrorToast('Vui lòng nhập đầy đủ thông tin trước khi lưu.');
		return;
	}

	const colHeaders = htInteraction.getColHeader();
	const colWidths = [];

	for (let i = 0; i < colHeaders.length; i++) {
		colWidths.push(htInteraction.getColWidth(i));
	}

	const formDataJson = JSON.stringify({
		colWidths: colWidths,
		colHeaders: colHeaders,
		data: tableData,
	});

	console.log("Data to send:", formDataJson);
	$.ajax({
		url: `${_ctx}customer/save-interaction?customer_id=${customerId}`,
		type: 'POST',
		data: formDataJson,
		dataType: "json",
		contentType: 'application/json',
		success: function(result) {
			console.log("Server Response:", result);
			reloadAndFilterData();
			showSuccessToast('Cập nhật thành công');
		},
		error: function(xhr) {
			console.error("AJAX Error:", xhr.responseText);
		}
	});
	console.log("ID cua customer lan 2: ", customerId);
}

function handleSearchInteraction() {
	appState.currentFilter.startDate = $('#startDateInput').val();
	appState.currentFilter.endDate = $('#endDateInput').val();
	appState.currentFilter.contactPerson = $('#customerSelect').val();

	filterTableData(
		appState.currentFilter.startDate,
		appState.currentFilter.endDate,
		appState.currentFilter.contactPerson
	);
}

function reloadAndFilterData() {
	return loadTableData(customerId).then(() => {
		filterTableData(
			appState.currentFilter.startDate,
			appState.currentFilter.endDate,
			appState.currentFilter.contactPerson
		);
	});
}

function loadTableData(customerId) {
	return new Promise((resolve, reject) => {
		if (!customerId) {
			console.error('Customer ID is required to load interactions.');
			reject('Customer ID is missing');
			return;
		}

		//        const url = `${_ctx}/customer/load-interaction?id=${customerId}`;
		//Không thêm / trước customer vì trong _ctx đã có /sẵn rồi
		const url = `${_ctx}customer/load-interaction?id=${customerId}`;
		$.ajax({
			url: url,
			type: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success: function(res) {
				console.log("Dữ liệu nhận được:", JSON.stringify(res));

				if (res) {
					appState.originalData = res.data;
					initTable(res.colHeaders, res.colWidths, res.data);
					updateCustomerSelect(res.contactPersons);
					resolve();
				}
			},
			error: function(e) {
				console.error("Lỗi tải dữ liệu:", e);
				reject(e);
			}
		});
	});
}

function updateCustomerSelect(contactPersons) {
	const customerSelect = $('#customerSelect');
	customerSelect.empty();
	customerSelect.append('<option value="" selected>Tất cả</option>');

	contactPersons.forEach(function(contactPerson) {
		customerSelect.append('<option value="' + contactPerson + '">' + contactPerson + '</option>');
	});
}

function initTable(colHeaders, colWidths, data) {
	const container = document.getElementById('tblInteraction');

	if (container) {
		htInteraction = new Handsontable(container, {
			data: data,
			colHeaders: colHeaders,
			colWidths: colWidths,
			columns: [
				{ type: 'text' },
				{ type: 'text' },
				{
					type: 'date',
					dateFormat: 'YYYY-MM-DD',
					correctFormat: true,
					defaultDate: new Date(),
					datePickerConfig: { format: 'YYYY-MM-DD' },
					readOnly: true
				}, //Ngày tạo
				{ type: 'text' },
				{
					type: 'date',
					dateFormat: 'YYYY-MM-DD',
					correctFormat: true,
					defaultDate: new Date(),
					datePickerConfig: { format: 'YYYY-MM-DD' }
				},
//				{	
//					renderer: fileUploadRenderer
//				},
				{ 	renderer: buttonRenderer 
					
				},
			],
			height: 400,
			currentRowClassName: 'currentRow',
			currentColClassName: 'currentCol',
			manualColumnResize: true,
			manualRowResize: true,
			columnSorting: true,  // Bật tính năng sắp xếp cột
			autoColumnSize: true,  // Tự động điều chỉnh kích thước cột
			minSpareRows: 1,
			contextMenu: true,
			licenseKey: 'non-commercial-and-evaluation',
		});
	} else {
		console.error("Container tblInteraction không tồn tại.");
	}	
}

function buttonRenderer(instance, td, row, col, prop, value, cellProperties) {
	Handsontable.dom.empty(td);
	
	// Nút Xóa
	const deleteButton = document.createElement('button');
	deleteButton.type = 'button';
	deleteButton.innerHTML = '<i class="fas fa-trash"></i>';
	deleteButton.onclick = function() {
		deleteRow(row, value);
	};
	deleteButton.style.marginRight = '5px';
	td.appendChild(deleteButton);
	
	
	// Nút Upload Files
	const uploadButton = document.createElement('button');
	uploadButton.type = 'button';
	uploadButton.innerHTML = '<i class="fas fa-upload"></i>';
	uploadButton.title = 'Upload Files';
	uploadButton.onclick = function() {
		//alert('đã bấm')
		openUploadModal(row, value); // Hàm mở modal upload
	};
	td.appendChild(uploadButton);

	return td;
}

function openUploadModal(row, interactionId) {
	console.log("ID interaction: ", interactionId);
	const modal = document.getElementById('uploadModal');
	if (modal) {
		// Đặt interaction ID vào modal để sử dụng khi upload
		modal.dataset.row = row;
		modal.dataset.interactionId = interactionId;
		
		$('#uploadModal').modal('show'); 
		loadExistingFiles(interactionId);
	}
}
let selectedFiles = [];

document.getElementById('uploadInputFiles').addEventListener('change', function (e) {
    const previewContainer = document.getElementById('previewFilesList');
    
    // Thêm file mới vào mảng
    const newFiles = Array.from(e.target.files);
    selectedFiles = selectedFiles.concat(newFiles);

    // Làm mới lại danh sách hiển thị từ mảng
    renderFilePreview();
});

function renderFilePreview() {
    const previewContainer = document.getElementById('previewFilesList');
    previewContainer.innerHTML = '';

    selectedFiles.forEach((file, index) => {
        const fileDiv = document.createElement('div');
        fileDiv.style.marginBottom = '10px';

        if (file.type.startsWith('image/')) {
            const img = document.createElement('img');
            img.src = URL.createObjectURL(file);
            img.style.width = '80px';
            img.style.height = '80px';
            img.style.objectFit = 'cover';
            img.style.marginRight = '10px';
            img.onload = () => URL.revokeObjectURL(img.src);
            fileDiv.appendChild(img);
        } else {
            const link = document.createElement('a');
            link.href = URL.createObjectURL(file);
            link.textContent = file.name;
            link.target = '_blank';
            link.onclick = () => {
                setTimeout(() => URL.revokeObjectURL(link.href), 5000);
            };
            fileDiv.appendChild(link);
        }

        // Thêm nút xoá file khỏi preview (nếu muốn)
        const delBtn = document.createElement('button');
        delBtn.textContent = 'X';
        delBtn.style.marginLeft = '5px';
        delBtn.onclick = () => {
            selectedFiles.splice(index, 1); // Xoá khỏi mảng
            renderFilePreview(); // Cập nhật lại giao diện
        };

        fileDiv.appendChild(delBtn);
        previewContainer.appendChild(fileDiv);
    });
}


function loadExistingFiles(interactionId) {
    $.ajax({
        url: `${_ctx}customer/interaction-files-upload/${interactionId}`,
        type: 'GET',
        success: function(files) {
            const list = document.getElementById('existingFilesList'); // Đổi id
            list.innerHTML = '';
            files.forEach(function(file) {
                const div = document.createElement('div');
                const link = document.createElement('a');
                link.href = file.filePath;
                link.textContent = file.fileName || 'Tải tài liệu';
                link.target = '_blank';
                link.style.marginRight = '10px';

                const delBtn = document.createElement('button');
                delBtn.textContent = 'X';
                delBtn.style.marginLeft = '5px';
                delBtn.onclick = function() {
                    deleteFile(file.id);
                };

                div.appendChild(link);
                div.appendChild(delBtn);
                list.appendChild(div);
            });
        },
        error: function(xhr, status, error) {
            console.error('Error loading files:', error);
        }
    });
}


function closeUploadModal() {
    $('#uploadModal').modal('hide');
   // document.getElementById('uploadedFilesList').innerHTML = '';
   // document.getElementById('uploadInputFiles').value = '';
	const backdrop = document.querySelector('.modal-backdrop');
	if (backdrop) backdrop.style.display = 'none';
}

function deleteRow(rowIndex, interactionId) {
	if (confirm(`Bạn có chắc muốn xóa hàng số ${rowIndex + 1}?`)) {
		$.ajax({
			url: `${_ctx}customer/delete-interaction?id=${interactionId}`,
			type: 'DELETE',
			success: function(response) {
				console.log('Xóa thành công:', response);
				reloadAndFilterData();
				showSuccessToast('Xóa thành công');
			},
			error: function(error) {
				console.error('Lỗi khi xóa:', error);
				alert('Có lỗi xảy ra khi xóa dữ liệu. Vui lòng thử lại.');
			}
		});
	}
}

function filterTableData(startDate, endDate, contactPerson) {
	const allData = appState.originalData;
	const start = startDate ? new Date(startDate) : null;
	const end = endDate ? new Date(endDate) : null;

	if ((start && isNaN(start.getTime())) || (end && isNaN(end.getTime()))) {
		console.error("Ngày không hợp lệ.");
		return;
	}

	// Kiểm tra nếu ngày bắt đầu lớn hơn ngày kết thúc
	if (start && end && start > end) {
		showErrorToast('Ngày bắt đầu không được lớn hơn ngày kết thúc.');
		return; // Dừng xử lý nếu điều kiện sai
	}

	const filteredData = allData.filter(function(row) {
		const recordDate = new Date(row[1]);
		let matchDate = true;

		if (start && end) {
			matchDate = recordDate >= start && recordDate <= end;
		} else if (start) {
			matchDate = recordDate >= start;
		} else if (end) {
			matchDate = recordDate <= end;
		}

		const matchCustomer = !contactPerson || row[0] === contactPerson;
		return matchDate && matchCustomer;
	});

	htInteraction.loadData(filteredData);
}

let isToastVisible = false; // Biến flag để kiểm tra trạng thái hiển thị toast

function showToast(message, type) {
	if (isToastVisible) return; // Nếu một toast đang hiển thị, không hiển thị toast mới

	isToastVisible = true; // Đánh dấu là toast đang hiển thị
	let toast;

	// Chọn thông báo dựa trên loại (success hoặc warning)
	if (type === 'success') {
		toast = $('#success-alert');
		toast.removeClass('alert-warning').addClass('alert-success');
	} else if (type === 'warning') {
		toast = $('#warning-alert');
		toast.removeClass('alert-success').addClass('alert-warning');
	}

	toast.text(message); // Đặt nội dung của toast

	// Hiển thị toast từ đầu
	toast.show().animate({
		left: '+=20px'  // Lắc sang phải
	}, 100).animate({
		left: '-=40px'  // Lắc sang trái
	}, 100).animate({
		left: '+=20px'  // Quay lại vị trí ban đầu
	}, 100);

	// Sau 3 giây (3000ms), ẩn toast (fade-out)
	setTimeout(function() {
		toast.fadeOut(100);  // 200ms để mờ dần ra
		isToastVisible = false; // Đánh dấu là toast đã ẩn
	}, 500);  // Toast sẽ hiển thị trong 0.5 giây
}

function showSuccessToast(message) {
	showToast(message, 'success');
}

function showErrorToast(message) {
	showToast(message, 'warning');
}

// Cache để lưu file theo ô
const fileCache = {};
function fileUploadRenderer(instance, td, row, col, prop, value, cellProperties) {
	td.innerHTML = '';
	const key = `${row}-${col}`;

	let files = value;
	if (!Array.isArray(files)) files = [];

	//const cachedFiles = fileCache[key] || [];
	files = [...files];

	// Cập nhật cache
	fileCache[key] = files;

	files.forEach(file => {
		if (file.filePath) {
			const fileUrl = `${file.filePath}`;

			const link = document.createElement('a');
			link.href = fileUrl;
			link.textContent = file.fileName || 'Tải tài liệu';
			link.target = '_blank';
			link.style.display = 'block';
			link.style.marginBottom = '5px';
			td.appendChild(link);

		} else {
			// File upload từ client (File object)
			if (file.type.startsWith('image/')) {
				const img = document.createElement('img');
				img.src = URL.createObjectURL(file);
				img.style.width = '50px';
				img.style.height = '50px';
				img.style.objectFit = 'cover';
				img.style.marginRight = '5px';
				td.appendChild(img);
			} else {
				const span = document.createElement('span');
				span.textContent = file.name || 'Tài liệu';
				span.style.display = 'block';
				span.style.marginBottom = '5px';
				td.appendChild(span);
			}
		}
	});

	const fileInput = document.createElement('input');
	fileInput.type = 'file';
	fileInput.multiple = true;
	fileInput.className = 'htFileInput';
	fileInput.id = "htFileInput"

	fileInput.addEventListener('change', (e) => {
		const newFiles = Array.from(e.target.files);
		const existingFiles = fileCache[key] || [];

		const mergedFiles = [...existingFiles];
		newFiles.forEach(newFile => {
			if (!existingFiles.some(f => f.name === newFile.name && f.size === newFile.size)) {
				mergedFiles.push(newFile);
			}
		});

		fileCache[key] = mergedFiles;
	
		//Cập nhật giá trị ô của Handsontable
		instance.setDataAtCell(row, col, mergedFiles);
	});

	td.appendChild(fileInput);
}



//function fileUploadRenderer(instance, td, row, col, prop, value, cellProperties) {
//	td.innerHTML = '';
//	const key = `${row}-${col}`;
//
//	const files = fileCache[key] || [];
//
//	// Hiển thị lại ảnh từ cache
//	files.forEach(file => {
//		if (file.type.startsWith('image/')) {
//			const img = document.createElement('img');
//			img.src = URL.createObjectURL(file);
//			img.style.width = '50px';
//			img.style.height = '50px';
//			img.style.objectFit = 'cover';
//			img.style.marginRight = '5px';
//			td.appendChild(img);
//		} else {
//			const span = document.createElement('span');
//			span.textContent = file.name;
//			span.style.marginRight = '5px';
//			td.appendChild(span);
//		}
//	});
//
//	const fileInput = document.createElement('input');
//	fileInput.type = 'file';
//	fileInput.multiple = true;
//	fileInput.className = 'htFileInput';
//
//	fileInput.addEventListener('change', (e) => {
//		const selectedFiles = Array.from(e.target.files);
//
//		// Lưu vào cache
//		fileCache[key] = selectedFiles;
//
//		// Force re-render cell
//		instance.render();  // Gọi lại renderer cho toàn bảng
//	});
//
//	td.appendChild(fileInput);
//}

//function fileRenderer(instance, td, row, col, prop, value, cellProperties) {
//	Handsontable.renderers.TextRenderer.apply(this, arguments);
//	td.innerHTML = ''; // Clear ô
//
//	const button = document.createElement('button');
//	button.innerText = '📎 Tải file';
//	button.style.cursor = 'pointer';
//	button.className = 'upload-file-btn';
//
//	button.onclick = function () {
//		const input = document.getElementById('fileUploader');
//		input.value = '';
//		input.accept = ''; // Chấp nhận tất cả loại file
//
//		input.onchange = function (e) {
//			const file = e.target.files[0];
//			if (file) {
//				const url = URL.createObjectURL(file); // tạm
//				instance.setDataAtCell(row, col, url); // gán giá trị
//			}
//		};
//
//		input.click(); // Mở hộp thoại
//	};
//
//	// Nếu đã có file
//	if (value) {
//		const link = document.createElement('a');
//		link.href = value;
//		link.innerText = '📄 Xem file';
//		link.target = '_blank';
//		td.appendChild(link);
//		td.appendChild(document.createElement('br'));
//	}
//
//	td.appendChild(button);
//}
//
//function imageRenderer(instance, td, row, col, prop, value, cellProperties) {
//	Handsontable.renderers.TextRenderer.apply(this, arguments);
//	td.innerHTML = '';
//
//	const button = document.createElement('button');
//	button.innerText = '🖼 Tải ảnh';
//	button.style.cursor = 'pointer';
//	button.className = 'upload-image-btn';
//
//	button.onclick = function () {
//		const input = document.getElementById('fileUploader');
//		input.value = '';
//		input.accept = 'image/*';
//
//		input.onchange = function (e) {
//			const file = e.target.files[0];
//			if (file) {
//				const url = URL.createObjectURL(file);
//				instance.setDataAtCell(row, col, url);
//			}
//		};
//
//		input.click();
//	};
//
//	if (value) {
//		const img = document.createElement('img');
//		img.src = value;
//		img.style.maxWidth = '80px';
//		img.style.maxHeight = '60px';
//		td.appendChild(img);
//		td.appendChild(document.createElement('br'));
//	}
//
//	td.appendChild(button);
//}

