var htOrder;

/**
 * Processing after the webpage are loaded in the browser.
 */
$(document).ready(function() {
	loadTableData();
});


/**
 * Load column width, header, initTable()
 */
function loadTableData() {

	$.ajax({
		url: _ctx + 'ordersConfigurationCRMOrderType_Bao/load',
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

function initTable(colHeaders, colWidths, data) {
	console.log("HST:", document.getElementById('tblOrderConfiguration'));

    var container = document.getElementById('tblOrderConfiguration');
    
	if (container) {
	       htOrder = new Handsontable(container, {
	           data: data,
	           colHeaders: colHeaders,
	           colWidths: colWidths,
	           columns: [
	               { type: 'text' }, // ID
	               { type: 'text' }, 
	               { type: 'text' },
	           ],
	           rowHeaders: true,
	           minRows: 8,
			   height: 300,
	           currentRowClassName: 'currentRow',
	           currentColClassName: 'currentCol',
	           manualColumnResize: true,
	           manualRowResize: true,
	           minSpareRows: 1,
	           contextMenu: true,
	           licenseKey: 'non-commercial-and-evaluation'
	       });
	   } else {
	       console.error("Container tblOrderConfiguration không tồn tại.");
	   }
}