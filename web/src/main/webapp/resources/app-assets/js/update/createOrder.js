/**
 * 
 */
const openCreateOrderBtn = document.getElementById('openCreateOrderBtn');
const updateOrderModal = document.getElementById('updateOrderModal');
const modalOverlay = document.getElementById('modalOverlay');

openCreateOrderBtn.addEventListener('click', function(){
	updateOrderModal.style.display = 'block';
	modalOverlay.style.display = 'block';
})

function closeModal() {
	updateOrderModal.style.display = 'none';
	modalOverlay.style.display = 'none';
}

function changeTitle(newTitle){
	document.getElementById("title").innerText = newTitle;
}