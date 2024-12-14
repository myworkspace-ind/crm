document.addEventListener('DOMContentLoaded', () => {
  const formId = 'updateCustomerForm';
  const form = document.getElementById(formId);
  const storageKey = 'customerFormData';
  const isEditMode = document.getElementById('customer_id').value !== ""; // Kiểm tra nếu có ID thì đang ở chế độ edit

  // Tải dữ liệu từ Local Storage
  const loadFormData = () => {
    if (!isEditMode) {
      const savedData = JSON.parse(localStorage.getItem(storageKey));
      if (savedData) {
        for (const [key, value] of Object.entries(savedData)) {
          const input = form.elements[key];
          if (input) {
            if (input.tagName === 'SELECT' || input.tagName === 'TEXTAREA' || input.tagName === 'INPUT') {
              input.value = value;
            } 
          }
        }
      }
    }
  };

  // Lưu dữ liệu form vào Local Storage 
  const saveFormData = () => {
    if (!isEditMode) {
      const formData = {};
      Array.from(form.elements).forEach(input => {
        if (input.name) {
          formData[input.name] = input.value;
        }
      });
      localStorage.setItem(storageKey, JSON.stringify(formData));
    }
  };

  // Xóa dữ liệu trong Local Storage (áp dụng khi submit form hoặc khi vào chế độ edit)
  const clearFormData = () => {
    localStorage.removeItem(storageKey);
  };

  // Gán sự kiện
  form.addEventListener('input', saveFormData);
  form.addEventListener('change', saveFormData);
  form.addEventListener('submit', clearFormData);

  // Xóa dữ liệu Local Storage khi vào chế độ edit
  if (isEditMode) {
    clearFormData();
  }

  // Tải dữ liệu khi trang được tải
  loadFormData();
});
