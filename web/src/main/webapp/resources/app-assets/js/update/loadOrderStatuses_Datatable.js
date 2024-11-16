function loadOrderStatuses_Datatable() {
    const categoryId = document.getElementById("orderCategorySelect").value;
    window.location.href = _ctx + `orders-datatable?categoryId=${categoryId}`;
}