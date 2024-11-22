
function navigateToItem(rowElement) {
    const url = rowElement.getAttribute('data-url');
    if (url) {
        window.location.href = url;
    } else {
        console.error("No URL found in data attribute");
    }
}


function openItemModal(isAdd) {
    if(isAdd){
        document.getElementById('product-modal-title').innerText = 'Add Product';
        document.getElementById('product-form').reset();
        document.getElementById('item-modal').classList.remove('hidden');
    }

    document.getElementById('item-modal').classList.remove('hidden');
}

function closeItemModal() {
    document.getElementById('item-modal').classList.add('hidden');
}
function saveProduct() {
    const product = {
        id: document.getElementById('product-id').value, // Lấy id từ input ẩn
        name: document.getElementById('product-name').value,
        promotionPrice: document.getElementById('product-price').value,
        tags: document.getElementById('product-tags').value,
        category: { id: document.getElementById('product-category').value },
        characteristic: document.getElementById('item-characteristic').value,
        gender: document.querySelector('input[name="gender"]:checked').value,
    };
    fetch('/admin/products/save', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(product),
    })
        .then(() => {
            closeItemModal();
            location.reload();
        })
        .catch(error => console.error('Error saving product:', error));
}


function editProduct(productId) {
    fetch(`/admin/products/get?id=${productId}`)
        .then(response => response.json())
        .then(product => {
            document.getElementById('product-id').value = product.id || ''; // Set giá trị id
            document.getElementById('product-name').value = product.name || '';
            document.getElementById('product-price').value = product.promotionPrice || 0;
            document.getElementById('product-category').value = product.category?.id || '';
            document.querySelector('textarea').value = product.characteristic || '';

            // Set gender radio button
            if (product.gender) {
                const genderInput = document.querySelector(`input[name="gender"][value="${product.gender}"]`);
                if (genderInput) genderInput.checked = true;
            }

            document.getElementById('product-modal-title').innerText = 'Update Product';
            openItemModal(false);
        })
        .catch(error => console.error('Error loading product:', error));
}



function deleteProduct(productId) {
    if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?')) {
        fetch(`/admin/products/delete?id=${productId}`, { method: 'POST' })
            .then(() => deleteRowById(`${productId}`)); // Tải lại trang sau khi xóa
    }
}



function openVariantModal(isAdd) {
    if(isAdd){
        document.getElementById('variant-modal-title').innerText = 'Add Variant';
        document.getElementById('variant-form').reset()
        document.getElementById('variant-modal').classList.remove('hidden');
    }
    document.getElementById('variant-modal').classList.remove('hidden');
}

function closeVariantModal() {
    document.getElementById('variant-modal').classList.add('hidden');
}const imageLinksInput = document.getElementById('variant-images').value;
function saveVariant() {
    const itemId = document.getElementById('selected-item-id').value;
    if (!itemId) {
        console.error('Item ID is missing');
        return;
    }
    const imageLinksInput = document.getElementById('variant-images').value;
    const variant = {
        id: document.getElementById('variant-id').value, // Lấy id từ input ẩn
        name: document.getElementById('variant-name').value,
        price: document.getElementById('variant-price').value,
        availableQuantity: document.getElementById('variant-quantity').value,
        color: { id: document.querySelector('#variant-modal select[name="color_id"]').value },
        size: { id: document.querySelector('#variant-modal select[name="size_id"]').value },
        imageUrls: imageLinksInput.split(',').map(link => link.trim())
    };
    fetch(`/admin/products/${itemId}/variants/save`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(variant),
    })
        .then(() => {
            closeVariantModal();
            location.reload();
        })
        .catch(error => console.error('Error saving variant:', error));
}

function editVariant(itemId, variantId) {
    fetch(`/admin/products/${itemId}/variants/get?variantId=${variantId}`)
        .then(response => response.json())
        .then(variant => {
            document.getElementById('variant-id').value = variant.id || ''; // Set giá trị id
            document.getElementById('variant-name').value = variant.name || '';
            document.getElementById('variant-price').value = variant.price || 0;
            document.getElementById('variant-quantity').value = variant.availableQuantity || 0;
            document.querySelector('#variant-modal select[name="color_id"]').value = variant.color?.id || '';
            document.querySelector('#variant-modal select[name="size_id"]').value = variant.size?.id || '';
            document.getElementById('variant-images').value = (variant.images || []).join(', ');
            document.getElementById('variant-modal-title').innerText = 'Update Variant';
            openVariantModal(false);
        })
        .catch(error => console.error('Error loading variant:', error));
}



function deleteVariant(itemId, variantId) {
    if (confirm('Bạn có chắc chắn muốn xóa biến thể này không?')) {
        fetch(`/admin/products/${itemId}/variants/delete?variantId=${variantId}`, {
            method: 'POST',
        })
            .then(() => {
                deleteRowById(`${variantId}`); // Xóa dòng trong bảng
            })
            .catch(error => console.error('Error deleting variant:', error));
    }
}
function deleteRowById(rowId) {
    const row = document.getElementById(rowId); // Lấy dòng theo ID
    if (row) {
        row.parentNode.removeChild(row); // Xóa dòng
    } else {
        console.error(`Không tìm thấy dòng với ID: ${rowId}`);
    }
}
