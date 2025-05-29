document.addEventListener("DOMContentLoaded", function() {
	document.querySelectorAll(".task-group-header").forEach(header => {
		header.addEventListener("click", function() {
			const contentId = header.getAttribute("data-toggle-target");
			const content = document.getElementById(contentId);
			const arrow = header.querySelector(".arrow-icon");

			if (content.style.display === "none") {
				content.style.display = "block";
				arrow.textContent = "▲";
			} else {
				content.style.display = "none";
				arrow.textContent = "▼";
			}
		});
	});
});