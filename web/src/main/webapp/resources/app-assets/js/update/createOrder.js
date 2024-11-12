/**
 * 
 */
const openCreateOrderBtn = document.getElementById('openCreateOrderBtn');
const createOrderModal = document.getElementById('createOrderModal');
const modalOverlay = document.getElementById('modalOverlay');

openCreateOrderBtn.addEventListener('click', function(){
	createOrderModal.style.display = 'block';
	modalOverlay.style.display = 'block';
})

function closeModal() {
	createOrderModal.style.display = 'none';
	modalOverlay.style.display = 'none';
}

function changeTitle(newTitle){
	document.getElementById("title").innerText = newTitle;
}