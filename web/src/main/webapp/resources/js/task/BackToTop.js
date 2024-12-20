// Lấy nút Back to Top
const backToTopButton = document.getElementById('backToTop');

// Theo dõi sự kiện cuộn
window.addEventListener('scroll', () => {
  if (window.scrollY > 300) { // Khi cuộn qua 300px
    backToTopButton.style.display = 'block';
  } else {
    backToTopButton.style.display = 'none';
  }
});

// Xử lý sự kiện click để quay lại đầu trang
backToTopButton.addEventListener('click', () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth' // Cuộn mượt
  });
});