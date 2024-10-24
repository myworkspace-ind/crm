/**
 * Process event click on button "Save" in screen "Customer Management".
 */
$(document).ready(function() {
    $('#frmTask').submit(function(e) {
        e.preventDefault();
        
        var colHeaders = htTask.getColHeader();
        var tableData = htTask.getData();
        
        // Build array of column width
        var colWidths=[]
        for (let i = 0; i < colHeaders.length; i++) {
            let w = htTask.getColWidth(i);
            colWidths.push(w);
        }

        var formDataJson = JSON.stringify({"colWidths": colWidths, "colHeaders": colHeaders, "data": tableData});
        
        $.ajax({	
            url : _ctx + 'customer-management/save',
            type : 'POST',
            data : formDataJson,
            dataType: "json",
            contentType: 'application/json',
            success : function(result) {
                console.log("Result:" + JSON.stringify(result));
                updateData(result);
            },
            error : function() {
                console.log("Error!");
            }
        });
    });
});

/**
 * Update the Handsontable with new data
 */
function updateData(result) {
    htTask.loadData(result.data);
    
    
    // So the result message
    $("#success-alert").fadeTo(2000, 500).slideUp(500, function() {
        $("#success-alert").slideUp(2000);
    });
}
