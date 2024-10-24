var htTask;

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
		url: _ctx + 'customer-management/load',
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
    var container = document.getElementById('tblTask');
    
    htTask = new Handsontable(container, {
        data: data,
        colHeaders: colHeaders,
        colWidths: colWidths,
        columns: [
            { type: 'text' }, // ID
            { type: 'text' }, // Name
            { type: 'text' }, // Address
            { type: 'text' } // Phone
        ],
        rowHeaders: true,
        minRows: 8,
        currentRowClassName: 'currentRow',
        currentColClassName: 'currentCol',
        manualColumnResize: true,
        manualRowResize: true,
        minSpareRows: 1,
        contextMenu: true,
        licenseKey: 'non-commercial-and-evaluation'
    });
}

