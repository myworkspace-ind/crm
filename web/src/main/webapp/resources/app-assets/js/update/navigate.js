document.addEventListener('DOMContentLoaded', (event) =>{
	// Lấy tất cả các phần tử với class 'menu-item'
	const menuItems = document.querySelectorAll('.menu-item');
	
	menuItems.forEach((item)=>{
		item.addEventListener('click', (event)=>{
			event.preventDefault();
			
			const url = item.getAttribute('href');
			
			window.location.href = url;
		});
	});
});

window.addEventListener('DOMContentLoaded', function () {
  const navItems = document.querySelectorAll('.nav-item123');

  navItems.forEach(item => {
    const link = item.querySelector('a');

    link.addEventListener('click', function () {
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
