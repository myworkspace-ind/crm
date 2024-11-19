function getCurrentDate() {
	const today = new Date();
	const year = today.getFullYear();
	const month = String(today.getMonth() + 1).padStart(2, '0');
	const day = String(today.getDate()).padStart(2, '0');
	return `${year}-${month}-${day}`;
}

//document.getElementById('orderCreateDateDetail').value = getCurrentDate();
//document.getElementById('orderDeliveryDate').value = getCurrentDate();
document.getElementById('orderDate').value = getCurrentDate();
document.getElementById('deliveryDate').value = getCurrentDate();
document.getElementById('orderCreateDateCreate').value = getCurrentDate();
document.getElementById('orderDeliveryDateCreate').value = getCurrentDate();
