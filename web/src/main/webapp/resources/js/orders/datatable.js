var dataSet;


$(document).ready(function () {
    $.ajax({
        url : _ctx + 'orders-datatable/loaddata',
        type : 'GET',
        dataType : 'json',
        contentType : 'application/json',
        success : function(res) {
            // Debug
            console.log("res=" + JSON.stringify(res));
            if (res) {
                dataSet = res.data;
                initTable();
            }                
        },
        error : function (e) {
            console.log("Error: " + e);
        }
    });
    
});

function initTable() {
    $("#tblDatatable").DataTable({
        data: dataSet,
        dom: 'Bfrtip',
        buttons: [
            'copyHtml5',
            'excelHtml5',
            'csvHtml5',
            'pdfHtml5'
        ]
    });
}

