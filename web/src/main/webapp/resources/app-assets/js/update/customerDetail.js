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
