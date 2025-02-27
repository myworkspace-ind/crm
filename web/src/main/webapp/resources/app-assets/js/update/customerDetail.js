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
		emailModal.classList.remove("show");
		setTimeout(() => {
			emailModal.style.display = "none";

		}, 400);
	});
})

document.addEventListener("DOMContentLoaded", function() {
	const letterTab = document.getElementById("activeIcon32-tab1");
	const draftTab = document.getElementById("linkIcon32-tab1");
	const tableLetter = document.getElementById("table-letter");
	const tableDraft = document.getElementById("table-draft");

	function showTable(tableToShow, tableToHide) {
		tableToShow.style.display = "block";
		tableToHide.style.display = "none";
	}

	letterTab.addEventListener("click", function() {
		showTable(tableLetter, tableDraft);
	})

	draftTab.addEventListener("click", function() {
		showTable(tableDraft, tableLetter);
	})

	tableDraft.style.display = "none";

})

document.addEventListener("DOMContentLoaded", function() {
	const customerId = new URLSearchParams(window.location.search).get("id");
	if (!customerId) {
		console.error("Không tìm thấy customerId");
		return;
	} else {
		console.log("CustomerID: ", customerId);
	}

	fetch(`${_ctx}customer/get-email-to-customer?customerId=${customerId}`)
		.then(response => response.json())
		.then(data => {
			console.log("đã tới được đây!");
			const emailList = document.getElementById("email-list");
			emailList.innerHTML = ""; // Xóa nội dung cũ

			data.forEach(email => {
				const row = document.createElement("tr");
				row.classList.add("clickable-row");
				row.dataset.emailId = email.id;

				row.innerHTML = `
                    <td class="th1"><input type="checkbox" class="email-to-customer-checkbox" /></td>
                    <td>${email.subject}</td>
                    <td>${new Date(email.sendDate).toLocaleString()}</td>
                    <td>${email.sender}</td>
                `;
				emailList.appendChild(row);
			});

			// Thêm sự kiện click vào từng hàng email
			document.querySelectorAll(".clickable-row").forEach(row => {
				row.addEventListener("click", function() {
					const emailToCustomerId = this.dataset.emailId;
					console.log("Dataset: ", this.dataset);
					console.log("EmailID: ", emailToCustomerId);

					if (emailToCustomerId) {
						//console.log ("EmailID: ",emailId);
						fetch(`${_ctx}customer/get-detail-email-to-customer?emailToCustomerId=${emailToCustomerId}`)
							.then(response => response.json())
							.then(responseData  => {
								console.log("Detail email:", responseData); // Kiểm tra dữ liệu nhận được
								
								if (!responseData.email) {
									console.error("Dữ liệu email không hợp lệ:", responseData);
									return;
								}
								
								const email = responseData.email;

								document.getElementById("detailEmailID").textContent = email.id || "N/A";
								document.getElementById("detailEmailFrom").textContent = email.sender || "Không có thông tin";
								document.getElementById("detailEmailTo").textContent = email.customer ? email.customer.email : "Không có email";
								
								document.getElementById("detailEmailSubject").textContent = email.subject || "Không có chủ đề";
								
								document.getElementById("detailEmailTime").textContent = email.sendDate ? new Date(email.sendDate).toLocaleString() : "Không có ngày gửi";
								document.getElementById("detailEmailStatus").textContent = email.status === "SENT" ? "Đã gửi" : "Lưu nháp";
								document.getElementById("detailEmailContent").innerHTML = email.content ? email.content.replace(/\r\n/g, "<br>") : "Không có nội dung";

								// Hiển thị modal
								//document.getElementById("detailEmailModal").style.display = "block";
							})
							.catch(error => console.error("Lỗi khi lấy chi tiết email:", error));
					}
				});
			});
		})
		.catch(error => console.error("Lỗi khi lấy danh sách email:", error));
});

/*document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("detailEmailModal");
    const closeModalBtn = document.getElementById("closeDetailEmailModal");

	document.getElementById("email-list").addEventListener("click", function (event) {
	    const row = event.target.closest(".clickable-row");
	    if (!row) return;
	    modal.classList.add("show");
	    modal.style.display = "block";
	});

    // Khi click vào nút đóng
    closeModalBtn.addEventListener("click", function () {
        modal.classList.remove("show"); 
        setTimeout(() => {
            modal.style.display = "none";
        }, 500); 
    });
});*/

document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("detailEmailModal");
	const emailOverlay = document.getElementById("emailOverlay");
    const closeModalBtn = document.getElementById("closeDetailEmailModal");

    document.getElementById("email-list").addEventListener("click", function (event) {
        const row = event.target.closest(".clickable-row");
        if (!row) return;
        modal.classList.add("show"); // Thêm class để kích hoạt hiệu ứng trượt
		emailOverlay.classList.add("show");
    });

    // Khi click vào nút đóng
    closeModalBtn.addEventListener("click", function () {
        modal.classList.remove("show");
		emailOverlay.classList.remove("show");
    });
});



/*document.addEventListener("DOMContentLoaded", function () {
	document.querySelectorAll(".openEmailModalBtn").forEach((button) => {
		button.addEventListener("click", function () {
			let customerId = this.getAttribute("data-customer-id");
			let customerEmail = this.getAttribute("data-customer-email");
			let senderEmail = document.getElementById("loggedInUserEmail").value; // Lấy email người gửi

			document.getElementById("emailTo").value = customerEmail;
			document.getElementById("emailModal").setAttribute("data-customer-id", customerId);
			document.getElementById("emailModal").setAttribute("data-sender-email", senderEmail);
		});
	});

	document.getElementById("sendEmailBtn").addEventListener("click", function (event) {
		event.preventDefault();
	    
		let customerId = document.getElementById("emailModal").getAttribute("data-customer-id");
		let senderEmail = document.getElementById("emailModal").getAttribute("data-sender-email"); // Lấy email người gửi
		let emailSubject = document.getElementById("emailSubject").value;
		let emailContent = document.getElementById("emailContent").value;
	    
		let emailData = {
			subject: emailSubject,
			content: emailContent,
			sender: senderEmail, // email người gửi
			customer: { id: customerId }
		};
		console.log(_ctx);

		fetch(`${_ctx}email-to-customer/send`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify(emailData),
		})
		.then(response => response.json())
		.then(data => {
			if (data.message) {
				alert(data.message);
			} else if (data.errorMessage) {
				alert(data.errorMessage);
			}
		})
		.catch(error => console.error("Error:", error));
	});
});*/






