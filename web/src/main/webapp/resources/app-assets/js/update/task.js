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

$(document).ready(function () {
    $('#taskForm').on('submit', function (e) {
        e.preventDefault(); // Ngăn việc submit mặc định của form

        // Lay du lieu tu form tao task
        const taskDTO = {
            name: $('#taskName').val(),
            description: $('#taskDescription').val(),
            startDate: $('#startDate').val(),
            customerIds: []
        };

        $('.customer-block').each(function () {
            const customerId = $(this).find('.select2').val();
            if (customerId) {
				taskDTO.customerIds.push(Number(customerId));
            }
        });

		$.ajax({
		    url: _ctx + 'task/add-task',
		    method: 'POST',
		    contentType: 'application/json',
		    data: JSON.stringify(taskDTO),
		    success: function (response) {
		        Swal.fire({
		            icon: 'success',
		            title: 'Thành công',
		            text: response,
		            confirmButtonText: 'OK'
		        }).then(() => {
		            window.location.href = _ctx + 'task/list';
		        });
		    },
		    error: function (xhr) {
		        Swal.fire({
		            icon: 'error',
		            title: 'Lỗi',
		            text: xhr.responseText,
		            confirmButtonText: 'Đóng'
		        });
		    }
		});
    });
});

document.querySelectorAll(".task-item").forEach(item => {
	item.addEventListener("click", function (e) {
		if (e.target.tagName === 'BUTTON') return;

		const taskId = this.dataset.id;
		const taskName = this.dataset.name;
		const description = this.dataset.description;
		const startDate = this.dataset.start;
		const dueDate = this.dataset.due;
		const remindDate = this.dataset.remindme;
		const customers = JSON.parse(this.dataset.customers || "[]");

		openTaskSidebar(taskId, taskName, description, startDate, dueDate, remindDate, customers);
	});
});