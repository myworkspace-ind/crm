document.addEventListener("DOMContentLoaded", function() {
	const orderSenderNameSelect = document.getElementById("orderSenderNameCreate");
	const orderSenderPhone = document.getElementById("orderSenderPhoneCreate");
	const orderSenderEmail = document.getElementById("orderSenderEmailCreate");
	const _ctx = "/crm-web/";
	const defaultCustomerId = localStorage.getItem("orderSenderNameCreate") ? localStorage.getItem("orderSenderNameCreate") : 1;
	loadSenderInfo(defaultCustomerId);

	if (orderSenderNameSelect && orderSenderPhone && orderSenderEmail) {
		orderSenderNameSelect.addEventListener("change", function() {
			const customerId = this.value;
			console.log("Selected Customer ID:", customerId); // Log giá trị customerIds
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
	const orderReceiverNameSelect = document.getElementById("orderReceiverNameCreate");
	const orderReceiverPhone = document.getElementById("orderReceiverPhoneCreate");
	const orderReceiverEmail = document.getElementById("orderReceiverEmailCreate");
	const _ctx = "/crm-web/";
	/*const defaultCustomerId = 1;*/
	const defaultCustomerId = localStorage.getItem("orderReceiverNameCreate") ? localStorage.getItem("orderReceiverNameCreate") : 1;

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

