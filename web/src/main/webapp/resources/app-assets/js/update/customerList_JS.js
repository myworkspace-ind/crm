/*document.getElementById('select-all').addEventListener('change', function() {
		const checkboxes = document.querySelectorAll('input[name="customer"]');
		checkboxes.forEach(checkbox => {
			checkbox.checked = this.checked;
		});
});*/

/*const resizables = document.querySelectorAll('.resizable');

resizables.forEach(th => {
	th.addEventListener('mousedown', initResize);

	function initResize(e) {
		const startWidth = th.offsetWidth;
		let startX = e.clientX;

		window.addEventListener('mousemove', resize);
		window.addEventListener('mouseup', stopResize);

		function resize(e) {
			const newWidth = Math.max(startWidth + (e.clientX - startX), 50); // Giới hạn chiều rộng tối thiểu
			th.style.width = `${newWidth}px`;
		    
			requestAnimationFrame(() => {
				const currentWidth = th.offsetWidth;
				const diff = currentWidth - startWidth;
				th.style.transform = `translateX(${diff}px)`;
			});
		}

		function stopResize() {
			window.removeEventListener('mousemove', resize);
			window.removeEventListener('mouseup', stopResize);
		}
	}
});

*/

function savePerson(e, rowId) {
	var person = e.value;
	localStorage.setItem('NP_' + rowId, person);

	/*if (person === '1') {
		localStorage.setItem('NP' + rowId, '1');
	}
	else if (person === '2') {
		localStorage.setItem('NP' + rowId, '2');
	}
	else if (person === '3') {
		localStorage.setItem('NP' + rowId, '3');
	}
	else {
		localStorage.setItem('NP' + rowId, '0');
	}*/
}

function adjustColor(e, rowId) {
	var color = e.value;
	var newC = document.getElementById('newColor_' + rowId);

	switch (color) {
		case 'moi cap nhat':
			newC.style.backgroundColor = '#ffac84';
			break;
		case 'dung quen':
			newC.style.backgroundColor = '#ff8448';
			break;
		default:
			newC.style.backgroundColor = 'white';
			break;
	}

	localStorage.setItem('SC_' + rowId, newC.style.backgroundColor);
	localStorage.setItem('SS_' + rowId, color);
}

function changeColor(e, rowId) {
	var colorSelect = e.value;
	var td = document.getElementById('colorTd_' + rowId); // Cập nhật để lấy ID đúng

	// Thay đổi màu sắc dựa trên lựa chọn
	switch (colorSelect) {
		case 'moi':
			td.style.backgroundColor = '#ff62ff';
			break;
		case 'chua bat dau':
			td.style.backgroundColor = '#3BAFDA';
			break;
		case 'hoan lai':
			td.style.backgroundColor = '#0080ff';
			break;
		case 'dang trong tien trinh':
			td.style.backgroundColor = '#967ADC';
			break;
		case 'hoan thanh':
			td.style.backgroundColor = '#DA4453';
			break;
		case 'huy':
			td.style.backgroundColor = '#808080';
			break;
		case 'chot':
			td.style.backgroundColor = '#37BC9B';
			break;
		default:
			td.style.backgroundColor = 'white';
			break;
	}

	// Lưu màu sắc vào Local Storage
	localStorage.setItem('selectedColor_' + rowId, td.style.backgroundColor);
	localStorage.setItem('selectedState_' + rowId, colorSelect);
}


function restoreColor() {
	var rowIds = ['row1', 'row2']; // Danh sách các rowId bạn muốn khôi phục màu sắc
	rowIds.forEach(function(rowId) {
		var savedColor = localStorage.getItem('selectedColor_' + rowId);
		var savedState = localStorage.getItem('selectedState_' + rowId);
		var td = document.getElementById('colorTd_' + rowId); // Truy cập ID đúng cho TD
		var selectElement = document.getElementById('colorSelect_' + rowId); // Nhận ID của select tương ứng

		if (savedColor) {
			td.style.backgroundColor = savedColor;

			// Thiết lập giá trị cho select dựa trên màu sắc đã lưu
			switch (savedColor) {
				case '#ff62ff':
					selectElement.value = 'moi';
					break;
				case '#3BAFDA':
					selectElement.value = 'chua bat dau';
					break;
				case '#0080ff':
					selectElement.value = 'hoan lai';
					break;
				case '#967ADC':
					selectElement.value = 'dang trong tien trinh';
					break;
				case '#DA4453':
					selectElement.value = 'hoan thanh';
					break;
				case '#808080':
					selectElement.value = 'huy';
					break;
				case '#37BC9B':
					selectElement.value = 'chot';
					break;
				default:
					selectElement.value = '';
					break;
			}
		}
		if (savedState) {
			selectElement.value = savedState; // Cập nhật giá trị cho select
		}
	});
}


function restoreColor2() {
	var rowIds = ['row1', 'row2']; // Danh sách các rowId bạn muốn khôi phục màu sắc
	rowIds.forEach(function(rowId) {
		var savedColor = localStorage.getItem('SC_' + rowId);
		var savedState = localStorage.getItem('SS_' + rowId);
		var td = document.getElementById('newColor_' + rowId); // Truy cập ID đúng cho TD
		var selectElement = document.getElementById('color_' + rowId); // Nhận ID của select tương ứng

		if (savedColor) {
			td.style.backgroundColor = savedColor;

			switch (savedColor) {
				case '#ffac84':
					selectElement.value = 'moi cap nhat';
					break;
				case '#ff8448':
					selectElement.value = 'dung quen';
					break;
				default:
					selectElement.value = '';
					break;
			}
		}
		if (savedState) {
			selectElement.value = savedState; // Cập nhật giá trị cho select
		}
	});
}

function restorePerson() {
	var rows = ['row1', 'row2']; // Danh sách các rowId (mỗi dòng một id riêng)

	rows.forEach(function(rowId) {
		var savedPerson = localStorage.getItem('NP_' + rowId);

		if (savedPerson) {
			var selectElement = document.getElementById('personSelect_' + rowId);

			if (savedPerson === '1') {
				selectElement.value = '1';
			} else if (savedPerson === '2') {
				selectElement.value = '2';
			} else if (savedPerson === '3') {
				selectElement.value = '3';
			} else {
				selectElement.value = '0'; // Giá trị mặc định nếu không có người phụ trách
			}

			// Kích hoạt sự kiện change nếu cần
			selectElement.dispatchEvent(new Event('change'));
		}
	});
}

window.onload = function() {
	restoreColor();
	restoreColor2();
	restorePerson();
};



// Checkbox 7
document.getElementById('filter-option-7').addEventListener('change', function() {
	const timeFilters = document.getElementById('time-filters');

	// Toggle visibility of the datepickers
	if (window.getComputedStyle(timeFilters).display === 'none') {
		timeFilters.style.display = 'block';
	} else {
		timeFilters.style.display = 'none';
	}
});

// Checkbox 6
document.getElementById('filter-option-6').addEventListener('change', function() {
	const targetMainStatus = document.getElementById('main-status-select');
	const targetSubStatus = document.getElementById('sub-status-select');

	if (window.getComputedStyle(targetMainStatus).display === 'none') {
		targetMainStatus.style.display = 'block';
		targetSubStatus.style.display = 'block';
	} else {
		targetMainStatus.style.display = 'none';
		targetSubStatus.style.display = 'none';
	}
});

// Checkbox 5
document.getElementById('filter-option-5').addEventListener('change', function() {
	const targetPhone = document.getElementById('phone');

	if (window.getComputedStyle(targetPhone).display === 'none') {
		targetPhone.style.display = 'block';
	} else {
		targetPhone.style.display = 'none';
	}
});

// Checkbox 4
document.getElementById('filter-option-4').addEventListener('change', function() {
	const targetEmail = document.getElementById('email');

	if (window.getComputedStyle(targetEmail).display === 'none') {
		targetEmail.style.display = 'block';
	} else {
		targetEmail.style.display = 'none';
	}
});


// Checkbox 3
document.getElementById('filter-option-3').addEventListener('change', function() {
	const targetRepresentativeName = document.getElementById('representative-name');

	if (window.getComputedStyle(targetRepresentativeName).display === 'none') {
		targetRepresentativeName.style.display = 'block';
	} else {
		targetRepresentativeName.style.display = 'none';
	}
});

// Checkbox 2
document.getElementById('filter-option-2').addEventListener('change', function() {
	const targetCompanyName = document.getElementById('company-name');
	const targetAddressName = document.getElementById('company-address');

	if (window.getComputedStyle(targetCompanyName).display === 'none') {
		targetCompanyName.style.display = 'block';
		targetAddressName.style.display = 'block';
	} else {
		targetCompanyName.style.display = 'none';
		targetAddressName.style.display = 'none';
	}
});



// Checkbox 1
document.getElementById('filter-option-1').addEventListener('change', function() {
	const target = document.getElementById('responsible-select');
	if (target.style.display === 'none' || target.style.display === '') {
		target.style.display = 'block';
	} else {
		target.style.display = 'none';
	}
});

document.getElementById('filter-toggle').addEventListener('click', function() {
	const filterBox = document.getElementById('filter-box');
	if (filterBox.style.display === 'none' || filterBox.style.display === '') {
		filterBox.style.display = 'block';
	} else {
		filterBox.style.display = 'none';
	}
});

document.addEventListener('DOMContentLoaded', (event) => {
	// Lấy tất cả các phần tử với class 'menu-item'
	const menuItems = document.querySelectorAll('.menu-item');

	menuItems.forEach((item) => {
		item.addEventListener('click', (event) => {
			event.preventDefault();

			const url = item.getAttribute('href');

			window.location.href = url;
		});
	});
});

window.addEventListener('DOMContentLoaded', function() {
	const navItems = document.querySelectorAll('.nav-item123');

	navItems.forEach(item => {
		const link = item.querySelector('a');

		link.addEventListener('click', function() {
			// Remove 'active' class from all items before adding it to the clicked one
			navItems.forEach(nav => nav.classList.remove('active'));
			item.classList.add('active');

			// Chuyển hướng đến link href
			window.location.href = link.href;

			// Không ngăn việc điều hướng mặc định
			event.preventDefault();
		});

		if (link.pathname === window.location.pathname) {
			item.classList.add('active');
		} else {
			item.classList.remove('active');
		}
	});
});

document.querySelectorAll('.nav-item123').forEach(item => {
	item.addEventListener('mouseenter', function() {
		const submenu = this.querySelector('.menu-content123');
		const rect = this.getBoundingClientRect();

		// Calculate the top position relative to the page's scroll position
		submenu.style.top = (rect.top + window.scrollY - 70) + 'px';
	});
});



/*document.addEventListener('DOMContentLoaded', function () {
  // Lấy tất cả các thẻ a trong menu
  var menuItems = document.querySelectorAll('.nav-item .menu-item');

  menuItems.forEach(function (item) {
	item.addEventListener('click', function () {
	  // Loại bỏ lớp active từ tất cả các nav-item
	  document.querySelectorAll('.nav-item').forEach(function (navItem) {
		navItem.classList.remove('active');
	  });

	  // Thêm lớp active cho nav-item của mục đã chọn
	  var parentNavItem = item.closest('.nav-item');
	  if (parentNavItem) {
		parentNavItem.classList.add('active');
	  }
	});
  });
});
*/

/*document.addEventListener('DOMContentLoaded', function() {
  // Lấy tất cả các nav-item
  var navItems = document.querySelectorAll('.nav-item');

  // Xác định trang hiện tại
  var currentPage = window.location.pathname.split('/').pop();
  
  // Xóa lớp selected từ tất cả các nav-item
  navItems.forEach(function(navItem) {
	navItem.classList.remove('selected');
  });

  // Duyệt qua tất cả các nav-item
  navItems.forEach(function(navItem) {
	// Lấy giá trị của data-page
	var page = navItem.getAttribute('data-page');
    
	// Kiểm tra xem data-page có khớp với trang hiện tại không
	if (page === currentPage) {
	  // Thêm lớp selected cho nav-item chứa liên kết này
	  navItem.classList.add('selected');
	}
  });
});

document.addEventListener('DOMContentLoaded', function() {
  // Lấy tất cả các nav-item
  var navItems = document.querySelectorAll('.nav-item');

  // Xác định trang hiện tại
  var currentPage = window.location.pathname.split('/').pop();
  
  // Xóa lớp selected từ tất cả các nav-item
  navItems.forEach(function(navItem) {
	navItem.classList.remove('selected');
  });

  // Duyệt qua tất cả các nav-item
  navItems.forEach(function(navItem) {
	// Lấy giá trị của data-page
	var page = navItem.getAttribute('data-page');
    
	// Kiểm tra xem data-page có khớp với trang hiện tại không
	if (page === currentPage) {
	  // Thêm lớp selected cho nav-item chứa liên kết này
	  navItem.classList.add('selected');
	}
  });
});*/