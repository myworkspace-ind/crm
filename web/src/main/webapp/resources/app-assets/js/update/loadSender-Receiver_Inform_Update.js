document.addEventListener("DOMContentLoaded", function() {
	const orderSenderNameSelect = document.getElementById("orderSenderNameUpdate");
	const orderSenderPhone = document.getElementById("orderSenderPhoneUpdate");
	const orderSenderEmail = document.getElementById("orderSenderEmailUpdate");
	const _ctx = "/crm-web/";
	const defaultCustomerId = 1;

	loadSenderInfo(defaultCustomerId);

	if (orderSenderNameSelect && orderSenderPhone && orderSenderEmail) {
		orderSenderNameSelect.addEventListener("change", function() {
			const customerId = this.value;
			loadSenderInfo(customerId);
		});
	}

	function loadSenderInfo(customerId) {
		fetch(`${_ctx}orders-datatable/get-sender-receiver-details?customerId=${customerId}`)
			.then(response => response.json())
			.then(data => {
				if (data) {
					orderSenderPhone.value = data.phone || "";
					orderSenderEmail.value = data.email || "";
				}
			})
			.catch(error => {
				console.error("Error fetching sender details:", error);
			});
	} 

});

document.addEventListener("DOMContentLoaded", function() {
	const orderReceiverNameSelect = document.getElementById("orderReceiverNameUpdate");
	const orderReceiverPhone = document.getElementById("orderReceiverPhoneUpdate");
	const orderReceiverEmail = document.getElementById("orderReceiverEmailUpdate");
	const _ctx = "/crm-web/";
	const defaultCustomerId = 1;

	loadReceiverInfo(defaultCustomerId);

	if (orderReceiverNameSelect && orderReceiverPhone && orderReceiverEmail) {
		orderReceiverNameSelect.addEventListener("change", function() {
			const customerId = this.value;
			loadReceiverInfo(customerId);
		});
	}

	function loadReceiverInfo(customerId) {
		fetch(`${_ctx}orders-datatable/get-sender-receiver-details?customerId=${customerId}`)
			.then(response => response.json())
			.then(data => {
				if (data) {
					orderReceiverPhone.value = data.phone || "";
					orderReceiverEmail.value = data.email || "";
				}
			})
			.catch(error => {
				console.error("Error fetching sender details:", error);
			});
	} 

});

