document.addEventListener('DOMContentLoaded', () => {
	//const IDLE_LIMIT = 10; // test 10s
	const IDLE_LIMIT = 5 * 60;
	let lastTick = Date.now();

	function resetIdleTime() {
		idleTime = 0;
		lastTick = Date.now();
	}

	function initIdleTracking() {
		const pinModal = document.getElementById('verifyPinModal');
		const pinInput = document.getElementById('verifyPinInput');
		const pinError = document.getElementById('verifyPinError');

		if (!pinModal || !pinInput || !pinError) {
			console.warn("PIN modal elements not found. Idle tracking disabled.");
			return;
		}

		// Nếu đang yêu cầu PIN thì hiển thị ngay
		if (localStorage.getItem('requirePin') === 'true') {
			showPinModal();
		}

		// Theo dõi idle mỗi giây
		setInterval(() => {
			const now = Date.now();
			const delta = (now - lastTick) / 1000;

			// Nếu chênh lệch quá lớn => sleep hoặc system inactive
			if (delta > IDLE_LIMIT) {
				console.log("Detected possible sleep or long inactivity.");
				localStorage.setItem('requirePin', 'true');
				showPinModal();
			}

			idleTime += delta;
			lastTick = now;

			if (idleTime >= IDLE_LIMIT) {
				idleTime = 0;
				localStorage.setItem('requirePin', 'true');
				showPinModal();
			}
		}, 1000);

		['mousemove', 'keydown', 'scroll', 'click'].forEach(event =>
			document.addEventListener(event, resetIdleTime)
		);

		function showPinModal() {
			pinModal.style.display = 'flex';
			pinInput.value = '';
			pinError.style.display = 'none';
		}

		window.verifyPin = function() {
			const pin = $('#verifyPinInput').val();
			console.log("Pin: ", pin);

			$.ajax({
				url: _ctx + 'auth/verify-pin',
				method: 'POST',
				contentType: 'application/json',
				data: JSON.stringify({ pin }),
				success: function() {
					$('#pinModal').hide();
					localStorage.setItem('requirePin', 'false');
					window.location.reload();
					resetIdleTime();
				},
				error: function() {
					$('#pinError').show();
				}
			});
		}
	}

	if (document.readyState === 'loading') {
		document.addEventListener('DOMContentLoaded', initIdleTracking);
	} else {
		initIdleTracking();
	}
})