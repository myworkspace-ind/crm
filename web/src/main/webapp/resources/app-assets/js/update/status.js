document.addEventListener("DOMContentLoaded", function() {
	const scrollImages = document.querySelector(".scroll-images");
	const scrollLength = scrollImages.scrollWidth - scrollImages.clientWidth;
	const leftButton = document.querySelector(".left");
	const rightButton = document.querySelector(".right");

	function checkScroll() {
		const currentScroll = scrollImages.scrollLeft;
		if (currentScroll === 0) {
			leftButton.setAttribute("disabled", "true");
			rightButton.removeAttribute("disabled");
		} else if (currentScroll === scrollLength) {
			rightButton.setAttribute("disabled", "true");
			leftButton.removeAttribute("disabled");
		} else {
			leftButton.removeAttribute("disabled");
			rightButton.removeAttribute("disabled");
		}
	}

	scrollImages.addEventListener("scroll", checkScroll);
	window.addEventListener("resize", checkScroll);
	checkScroll();

	function leftScroll() {
		scrollImages.scrollBy({
			left: -200,
			behavior: "smooth"
		});
	}

	function rightScroll() {
		scrollImages.scrollBy({
			left: 200,
			behavior: "smooth"
		});
	}

	function showAllCustomers() {
		const scrollForm = document.getElementById('frmScrollImages');
		const statusInput = document.createElement('input');
		statusInput.type = 'hidden';
		statusInput.name = 'statusId';
		statusInput.value = '';
		scrollForm.appendChild(statusInput);
		scrollForm.submit();
	}
	leftButton.addEventListener("click", leftScroll);
	rightButton.addEventListener("click", rightScroll);
});