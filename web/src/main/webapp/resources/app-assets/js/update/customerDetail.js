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

document.addEventListener("DOMContentLoaded", function() {
	const letterTab = document.getElementById("activeIcon32-tab1");
	const draftTab = document.getElementById("linkIcon32-tab1");
	const tableLetter = document.getElementById("table-letter");
	const tableDraft = document.getElementById("table-draft");
	
	function showTable(tableToShow, tableToHide){
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

document.addEventListener("DOMContentLoaded", function(){
    const customerId = new URLSearchParams(window.location.search).get("id");
    if(!customerId){
        console.error("Không tìm thấy customerId");
        return;
    } else{
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
                    const emailId = this.dataset.emailId;
                    if (emailId) {
                        //window.location.href = `${_ctx}customer/email-detail?id=${emailId}`;
						alert("Bạn đã chọn một email!"); 
                    }
                });
            });
        })
        .catch(error => console.error("Lỗi khi lấy danh sách email:", error));
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






