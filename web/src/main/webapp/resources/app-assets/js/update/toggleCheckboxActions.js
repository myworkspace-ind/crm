document.querySelectorAll('.customer-checkbox').forEach(function(checkbox) {
	checkbox.addEventListener('change', function() {
		toggleButtonGroup();
	})
})

function toggleButtonGroup() {
	const anyChecked = document.querySelectorAll('.customer-checkbox:checked').length > 0;

	if (anyChecked) {
		document.querySelector('.row2').style.display = 'block';
	} else {
		document.querySelector('.row2').style.display = 'none';
	}
}

document.querySelector('.quit').addEventListener('click', function() {
   const checkboxes = document.querySelectorAll('.customer-checkbox');
   checkboxes.forEach(checkbox => {
      checkbox.checked = false;  // Uncheck all checkboxes
   });
   document.querySelector('.row2').style.display = 'none';
});

function updateCheckedCount() {
	// Count quantity of checkbox are is checked
	const checkedCount = document.querySelectorAll('.customer-checkbox:checked').length;
	// Update the content of the p.quantity-check element
	document.querySelector('.quantity-check').textContent = checkedCount;
}

const checkboxes = document.querySelectorAll('.customer-checkbox');
checkboxes.forEach(checkbox => {
    checkbox.addEventListener('change', updateCheckedCount);
});

// Gọi hàm để cập nhật ngay khi trang tải
updateCheckedCount();

