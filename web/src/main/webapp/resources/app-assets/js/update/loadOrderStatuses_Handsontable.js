/*function loadOrderStatuses(categoryId) {
	$.ajax({
		url: _ctx + `/orders/updateOrderStatuses/${categoryId}`,
		type: 'GET',
		success: function(response) {
			// Chèn nội dung trả về vào phần tử HTML với id 'orderStatusContainer'
			$('#orderStatusContainer').html(response);
		},
		error: function(xhr, status, error) {
			console.error('Error:', error);
		}
	});
}*/

function loadOrderStatuses_Handsontable() {
    const categoryId = document.getElementById("orderCategorySelect").value;
    window.location.href = _ctx + `/orders?categoryId=${categoryId}`;
}
