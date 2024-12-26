var htOrder;
var updateList = []; // Đặt updateList ở phạm vi toàn cục để lưu các thay đổi
var createList = [];

/**
 * Processing after the webpage is loaded in the browser.
 */
$(document).ready(function() {
    loadTableData();
	$('#frmOrdersConfigurationStatus').off('submit').on('submit', function (e) {
	        e.preventDefault();
	        saveChanges();
			return false;
	});
});

/**
 * Load column width, header, initTable()
 */
function loadTableData() {
    $.ajax({
        url: _ctx + 'orders-configuration-ky/load-statuses',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function(res) {
            console.log("res=" + JSON.stringify(res));
            if (res) {
                initTable(res.colHeaders, res.colWidths, res.data, res.columnTypes);
            }
        },
        error: function(e) {
            console.log("Error: " + e);
        }
    });
}

/**
 * Khởi tạo Handsontable
 */
function initTable(colHeaders, colWidths, data) {
    var container = document.getElementById('tblOrderConfigurationStatus');
    if (container) {
        let mergeCells = [];
        let row, rowspan;

        for (let i = 0; i < data.length; i++) {
            if (data[i][1] != null && data[i][1] != "") {
                row = i;
                rowspan = 1;

                for (let j = i + 1; j < data.length; j++) {
                    if (data[j][1] != null && data[j][1] != "") {
                        rowspan = j - i;
                        break;
                    }
                }

                if (rowspan > 1) {
                    mergeCells.push({ row: i, col: 0, rowspan: rowspan, colspan: 1 });
                    mergeCells.push({ row: i, col: 1, rowspan: rowspan, colspan: 1 });
                }
            }
        }

        htOrder = new Handsontable(container, {
            data: data,
            colHeaders: colHeaders,
            colWidths: colWidths,
            columns: [
                { 
                    type: 'text', 
                    allowEmpty: true, 
                    readOnly: true
                },
                { 
                    type: 'text', 
                    allowEmpty: true, 
                    readOnly: true
                },
                { 
                    type: 'text', 
                    allowEmpty: true,
                    renderer: function(instance, td, row, col, prop, value, cellProperties) {
                        Handsontable.renderers.TextRenderer.apply(this, arguments);
                        
                        if (value === "Thêm trạng thái mới") {
                            td.style.fontStyle = 'italic';
                            cellProperties.readOnly = true;
                        }
                        return td;
                    }
                },
                {
                    type: 'text',
                    allowEmpty: true,
                    readOnly: true
                },
                { 
                    type: 'text', 
                    allowEmpty: true, 
                    readOnly: true
                }
            ],
            rowHeaders: true,
            minRows: 8,
            height: 400,
            currentRowClassName: 'currentRow',
            currentColClassName: 'currentCol',
            manualColumnResize: true,
            manualRowResize: true,
            minSpareRows: 1,
            contextMenu: true,
            licenseKey: 'non-commercial-and-evaluation',
            mergeCells: mergeCells,
            hiddenColumns: {
                columns: [3, 4], // Chỉ mục của các cột cần ẩn
                indicators: false // Không hiển thị biểu tượng cột bị ẩn
            },

            afterChange: function(changes, source) {
                if (changes) {
                    changes.forEach(([row, col, oldValue, newValue]) => {
                        console.log(`Cell [${row}, ${col}] changed from "${oldValue}" to "${newValue}"`);

                        let idTrangThai = null;
                        let idLoaiDonHang = null;

                        try {
                            idTrangThai = htOrder.getDataAtCell(row, 3);
                            idLoaiDonHang = htOrder.getDataAtCell(row, 4);
                        } catch (error) {
                            console.info("Giá trị cần create nên null");
                        }

                        if(idLoaiDonHang != null && idTrangThai != null) {
                            updateList.push([row, col, oldValue, newValue, idTrangThai, idLoaiDonHang]);
                        } else if(col != 0 && oldValue != "" && newValue != null && idLoaiDonHang != null && col != 4) {
                            if(col == 2) {
								idLoaiDonHang = htOrder.getDataAtCell(row,4);
								createList.push([row, col, oldValue, newValue, idTrangThai, idLoaiDonHang]);
                            } else {
                                createList.push([row, col, oldValue, newValue, idTrangThai, idLoaiDonHang]);
                            }
                        }
                    });
                }
            },

            afterRender: function () {
                const cells = container.querySelectorAll('.htCore tbody td');
                cells.forEach(function (cell) {
                    // Xử lý double click với cách lấy tọa độ mới
                    cell.addEventListener('dblclick', function (e) {
                        // Lấy vị trí thực tế trong Handsontable
                        const coords = htOrder.getCoords(this);
                        if (!coords) return;  // Kiểm tra nếu không lấy được tọa độ
                        
                        const physicalRow = coords.row;
                        const physicalCol = coords.col;
						let mergeDataCategory = null;
						let mergeDataIdCategory = null;
						let dataTest = null;
						
						
                        
                        // Log để kiểm tra
                        console.log(`Double-clicked cell coordinates - Row: ${physicalRow}, Col: ${physicalCol}`);
                        console.log(`Cell value: ${htOrder.getDataAtCell(physicalRow, physicalCol)}`);
                        
                        if (physicalRow >= 0 && physicalCol >= 0 && 
                            htOrder.getDataAtCell(physicalRow, physicalCol) === "Thêm trạng thái mới") {
                            e.preventDefault();
                            // Thêm row mới phía trên
                            htOrder.alter('insert_row_above', physicalRow);
							console.log(`Id loại đơn hàng cần thêm là: ${htOrder.getDataAtCell(physicalRow-1, 4)}`);
							dataTest = htOrder.getDataAtCell(physicalRow+1,4);
							if(htOrder.getDataAtCell(physicalRow+1,4) != null && htOrder.getDataAtCell(physicalRow+1,4) !=""){
								mergeDataCategory = htOrder.getDataAtCell(physicalRow+1, 1);
								mergeDataIdCategory = htOrder.getDataAtCell(physicalRow+1, 0);
								htOrder.setDataAtCell(physicalRow, 1, mergeDataCategory);
								htOrder.setDataAtCell(physicalRow, 0, mergeDataIdCategory);
								htOrder.setDataAtCell(physicalRow, 4, mergeDataIdCategory);
								htOrder.getPlugin('mergeCells').merge(physicalRow, 0, physicalRow+1, 0); 
								htOrder.getPlugin('mergeCells').merge(physicalRow, 1, physicalRow+1, 1); 
							}
							else{
								mergeDataIdCategory = htOrder.getDataAtCell(physicalRow-1, 4);
								htOrder.setDataAtCell(physicalRow,4,mergeDataIdCategory);
							}
                        }
                    });

                    // Xử lý hover effect
                    cell.addEventListener('mouseover', function () {
                        this.style.fontWeight = 'bold';
                        this.style.color = 'red';
                    });

                    cell.addEventListener('mouseout', function () {
                        this.style.fontWeight = 'normal';
                        this.style.color = 'black';
                    });
                });
            }
        });
    } else {
        console.error("Container tblOrderConfigurationStatus không tồn tại.");
    }
}
//
function saveChanges() {
    if (updateList.length > 0 || createList.length > 0) {
        // Chỉ gửi updateList đến server nếu có thay đổi
        var formDataJson = JSON.stringify({
            update: updateList,
			create: createList
        });

        console.log("Sending data to server:", formDataJson); // Log dữ liệu gửi đi để kiểm tra

        // Gửi AJAX request
        $.ajax({
            url: _ctx + 'orders-configuration-ky/save-category-status',
            type: 'POST',
            data: formDataJson,
            dataType: "json",
            contentType: 'application/json',
			success: function(res) {
	            console.log("resSave=" + JSON.stringify(res));
	            if (res) {
	                initTable(res.colHeaders, res.colWidths, res.data, res.columnTypes);
					updateList.length = 0;
					createList.length = 0;
	            }
	        },
	        error: function(e) {
	            console.log("Error: " + e);
				updateList.length = 0;
				createList.length = 0;
	        }
        });
		
    } else {
        console.log("No changes to save.");
    }
}