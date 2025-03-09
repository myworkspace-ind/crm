document.addEventListener('DOMContentLoaded', () => {
	const chatBtn = document.getElementById('chat-button');
	const chatAssistant = document.getElementById('chat-assistant');
	const modalContainer = document.getElementById('modal-container');
	const modalOverlay = document.getElementById('modal-overlay');
	const closeButton = document.getElementById('close-btn-modal');
	const minimizeButton = document.getElementById('minimize-btn-modal');
	let isMinimized = false;
	
	chatBtn.addEventListener('click', () => {
		if (chatAssistant.style.display === 'none' || chatAssistant.style.display === '') {
			chatAssistant.style.display = 'block';
		} else {
			chatAssistant.style.display = 'none';
		}
	})
	
	/*function toggleChat() {
		const chat = document.getElementById('chat-assistant');
		if (chat.style.display === 'none' || chat.style.display === '') {
			chat.style.display = 'block';
		} else {
			chat.style.display = 'none';
		}
	}*/
	
	window.switchTab = function(tabId) {
		const tabs = document.querySelectorAll('.tab-content');
		tabs.forEach(tab => tab.style.display = 'none');

		const activeTab = document.getElementById(tabId);
		if (activeTab) {
			activeTab.style.display = 'block';
		}

		const tabButtons = document.querySelectorAll('.tab-btn');
		tabButtons.forEach(btn => btn.classList.remove('active'));

		const clickedButton = document.querySelector(`button[onclick="switchTab('${tabId}')"]`);
		if (clickedButton) {
			clickedButton.classList.add('active');
		}
	};

	const defaultTab = document.getElementById('customer-reminder');
	if (defaultTab) {
		switchTab('customer-reminder');
	}
	
	//Mở customer care table
	const clickHere = document.getElementById('click-here');
	if (clickHere) {
		clickHere.addEventListener('click', () => {
			modalContainer.style.display = 'block';
			modalOverlay.style.display = 'block';
		});
	}
	
	//Đóng customer care table
	closeButton.addEventListener('click', () => {
		console.log("Close button clicked!");
		modalContainer.style.display = 'none';
		modalOverlay.style.display = 'none';
	});
	
	//Minimize customer care table
	/*minimizeButton.addEventListener('click', () => {
		if (!isMinimized) {
			modalContainer.classList.add('minimzed');
			minimizeButton.textContent = 'Restore';
		} else {
			modalContainer.classList.remove('minimized');
			minimizeButton.textContent = 'Minimize';
		}
		isMinimized = !isMinimized
	})*/
	
	document.querySelectorAll('.radio-badge-group input[type="radio"]').forEach(radio => {
	    radio.addEventListener('change', function () {
	        const parent = this.closest('.radio-badge-group');
	        parent.querySelectorAll('label').forEach(label => {
	            label.className = 'badge badge-default'; // Reset màu mặc định
	        });

	        const selectedLabel = parent.querySelector(`label[for="${this.id}"]`);
	        switch (this.value) {
	            case '1':
	                selectedLabel.classList.add('badge-danger');
	                break;
	            case '2':
	                selectedLabel.classList.add('badge-warning');
	                break;
	            case '3':
	                selectedLabel.classList.add('badge-primary');
	                break;
	        }
	    });
	});
});




