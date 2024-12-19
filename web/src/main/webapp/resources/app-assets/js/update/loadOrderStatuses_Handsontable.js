function loadOrderStatuses_Handsontable() {
	const categoryId = document.getElementById("orderCategorySelect").value;
	window.location.href = _ctx + `orders-handsontable?categoryId=${categoryId}`;
}

/*document.addEventListener("DOMContentLoaded", function() {
	const orderCategorySelect = document.getElementById("orderCategoryCreate");
	const orderStatusSelect = document.getElementById("orderStatusCreate");

	//const _ctx = "/crm-web/";

	const defaultCategoryId = localStorage.getItem("orderCategoryID")!==null ? localStorage.getItem("orderCategoryID") : 0;

	loadOrderStatuses_ToCreateOrder(defaultCategoryId);

	if (orderCategorySelect && orderStatusSelect) {
		orderCategorySelect.addEventListener("change", function() {
			const categoryId = getDataIdFromDatalist('orderCategories', orderCategorySelect.value);
			loadOrderStatuses_ToCreateOrder(categoryId);  // Gọi lại API với categoryId mới
		});
	}

	function loadOrderStatuses_ToCreateOrder(categoryId) {
		fetch(`${_ctx}orders-handsontable/order-statuses?categoryId=${categoryId}`)
			.then(response => response.json())
			.then(orderStatuses => {
				// Xóa tất cả các tùy chọn hiện tại
				orderStatusSelect.innerHTML = "";

				orderStatuses.forEach(status => {
					const option = document.createElement("option");
					option.value = status[0];
					option.textContent = status[1];
					orderStatusSelect.appendChild(option);
				});
			})
			.catch(error => console.error("Error loading order statuses:", error));
	}

})*/

document.addEventListener("DOMContentLoaded", function() {
	const customerSuggestions = customerJson;
	const orderTypeSuggestions = orderTypeJson;

  	const customerInput = document.getElementById('customerInputHandsontable');
	const customerSuggestionsContainer = document.getElementById('customer-suggestions');
	const customerChipsContainer = document.getElementById('customer-chips-container-handsontable');

	const orderTypeInput = document.getElementById('orderTypeInput');
	const orderTypeSuggestionsContainer = document.getElementById('order-type-suggestions');
	const orderTypeChipsContainer = document.getElementById('order-type-chips-container');
	const orderStatusContainer = document.getElementById("orderStatusFilterHandsontable");

	//const _ctx = "/crm-web/";

	function showSuggestions(input, suggestions, container, type) {
	    container.innerHTML = '';
	    const value = input.value.toLowerCase();
	    const filteredSuggestions = type === "customer" 
	    						? suggestions.filter(item => item.contactPerson.toLowerCase().includes(value)) 
	    						: suggestions.filter(item => item.name.toLowerCase().includes(value)) ;
	    if (filteredSuggestions.length) {
	      	filteredSuggestions.forEach(item => {
		    	const textContentBase = type === "customer" ? item.contactPerson : item.name;
		        const suggestion = document.createElement('div');
		        suggestion.className = 'list-group-item list-group-item-action';
		        suggestion.textContent = textContentBase;
		        suggestion.onclick = () => {
		          addChip(type, item.id, textContentBase);
				  if(type!=="customer"){
					const orderTypeIds = Array.from(orderTypeChipsContainer.querySelectorAll('.chip')).map(chip => chip.dataset.id);
		          	loadAllOrderStatusesByCategory(orderTypeIds);
				  }
		          input.value = '';
		          container.style.display = 'none';
	        };
	        container.appendChild(suggestion);
	      });
	      container.style.display = 'block';
	    } else {
	      container.style.display = 'none';
	    }
	  }
	function addChip(type, id, name) {
	    const container = type === 'customer' ? customerChipsContainer : orderTypeChipsContainer;
	    const existingChips = Array.from(container.querySelectorAll('.chip')).map(chip => chip.dataset.id);
	    if (existingChips.includes(String(id))) {
	      alert(`"${name}" đã được chọn.`);
	      return;
	    }
	    const chip = document.createElement('div');
	    chip.className = 'chip';
	    chip.dataset.id = id; // Lưu ID trong chip
	    chip.textContent = name;
	    const close = document.createElement('span');
	    close.textContent = '×';
	    close.onclick = () => {
			container.removeChild(chip);
			if(type!=="customer") {
				const orderTypeIds = Array.from(orderTypeChipsContainer.querySelectorAll('.chip')).map(chip => chip.dataset.id);
	          	loadAllOrderStatusesByCategory(orderTypeIds);
		  	}
		}
	    chip.appendChild(close);
	    container.appendChild(chip);
	 }
	
	 customerInput.addEventListener('input', () => showSuggestions(customerInput, customerSuggestions, customerSuggestionsContainer, 'customer'));
	 customerInput.addEventListener('focus', () => showSuggestions(customerInput, customerSuggestions, customerSuggestionsContainer, 'customer'));
	 customerInput.addEventListener('blur', () => {
		  setTimeout(() => {
		    customerSuggestionsContainer.style.display = 'none';
		  }, 200); // Đợi 200ms để click vào phần tử gợi ý được ghi nhận
		});
	
		orderTypeInput.addEventListener('blur', () => {
		  setTimeout(() => {
		    orderTypeSuggestionsContainer.style.display = 'none';
		  }, 200);
		});
	
	  orderTypeInput.addEventListener('input', () => showSuggestions(orderTypeInput, orderTypeSuggestions, orderTypeSuggestionsContainer, 'orderType'));
	  orderTypeInput.addEventListener('focus', () => showSuggestions(orderTypeInput, orderTypeSuggestions, orderTypeSuggestionsContainer, 'orderType'));
	
	function loadAllOrderStatusesByCategory(categoryIds) {
		const categoryIdsLong = categoryIds.map(id => Number(id)); 
		const params = new URLSearchParams();
		categoryIdsLong.forEach(id => params.append('categoryIds', id));
		fetch(`${_ctx}orders-handsontable/order-statuses-list?${params.toString()}`)
			.then((response) => response.json())
			.then((orderStatuses) => {
				// Xóa nội dung cũ
				orderStatusContainer.innerHTML = "";

				if (orderStatuses.length === 0) {
					orderStatusContainer.innerHTML = "<p>Không có trạng thái nào phù hợp.</p>";
					return;
				}

				// Tạo checkbox cho từng trạng thái
				orderStatuses.forEach((status) => {
				    
				    // Kiểm tra nếu checkbox với giá trị status[0] đã tồn tại
				    const existingCheckbox = document.querySelector(`input[name="orderStatus"][value="${status[0]}"]`);
				    
				    if (!existingCheckbox) {  // Nếu chưa có checkbox với giá trị đó
				        const label = document.createElement("label");
				        label.classList.add("checkbox-spacing");

				        const checkbox = document.createElement("input");
				        checkbox.type = "checkbox";
				        checkbox.name = "orderStatus";
				        checkbox.value = status[0]; // Lấy id từ phần tử đầu tiên của mảng
				        checkbox.classList.add("custom-checkbox");

				        // Gắn tên trạng thái từ phần tử thứ hai của mảng
				        label.appendChild(checkbox);
				        label.appendChild(document.createTextNode(status[1]));

				        // Thêm label vào container
				        orderStatusContainer.appendChild(label);

				        // Thêm khoảng cách dòng
				        orderStatusContainer.appendChild(document.createElement("br"));
				    } 
				});

			})
			.catch((error) => {
				console.error("Error loading order statuses:", error);
				orderStatusContainer.innerHTML = "<p>Chưa có trạng thái đơn hàng nào phù hợp.</p>";
			});
	}
});
