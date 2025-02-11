/*document.addEventListener("DOMContentLoaded", function(){
	document.querySelectorAll(".dropdown-item").forEach(item => {
		item.addEventListener("click", function () {
			const targetId = this.getAttribute("data-target");
			const targetElement = document.getElementById(targetId);
			if(targetElement){
				targetElement.scrollIntoView({behavior: "smooth", block: "start"});
			}
		});
	});
});*/

document.addEventListener("DOMContentLoaded", function() {
	document.querySelectorAll(".dropdown-item").forEach(item => {
		item.addEventListener("click", function() {
			const targetId = this.getAttribute("data-target");
			const targetElement = document.getElementById(targetId);
			/*if (targetElement) {
				targetElement.scrollIntoView({ behavior: "smooth", block: "start" });
			}*/
			if (targetElement) {
				window.scrollTo({
					top: targetElement.offsetTop - 50, // Điều chỉnh để tránh bị che bởi header (nếu có)
					behavior: "smooth"
				});
			}
		});
	});
});

document.addEventListener("DOMContentLoaded", function() {
	const emailOverlay = document.getElementById("emailOverlay");
	const emailModal = document.getElementById("emailModal");
	const openEmailBtn = document.getElementById("openEmailModal");
	const closeEmailBtn = document.getElementById("closeEmailModal");

	openEmailBtn.addEventListener("click", function() {
		emailOverlay.classList.add("show");
		emailModal.style.display = "block"; // Hiển thị modal
		setTimeout(() => {
			emailModal.classList.add("show"); // Kích hoạt hiệu ứng trượt
		}, 10);
	});

	closeEmailBtn.addEventListener("click", function() {
		emailOverlay.classList.remove("show");
		emailModal.classList.remove("show"); // Ẩn hiệu ứng trượt
		setTimeout(() => {
			emailModal.style.display = "none"; // Ẩn modal sau khi trượt xong
			
		}, 400);
	});
})



