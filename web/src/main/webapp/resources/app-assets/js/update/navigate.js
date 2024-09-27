
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

