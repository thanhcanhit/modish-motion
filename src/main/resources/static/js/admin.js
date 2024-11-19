let editingProductId = null;
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

function editProduct(productId) {
    fetch(`/adminFake/get?id=${productId}`)
        .then(response => response.json())
        .then(product => {
            // document.getElementById('product-id').value = product.id || '';
            document.getElementById('product-name').value = product.name || '';
            document.getElementById('product-price').value = product.promotionPrice || '';
            document.getElementById('product-tags').value = product.tags || '';
            document.getElementById('product-category').value = product.category?.id || '';
            document.querySelector('textarea').value = product.characteristic || '';
            // document.querySelector(`input[name="gender"][value="${product.gender || 'Unisex'}"]`).checked = true;
            document.getElementById('product-modal-title').innerText = 'Update Product';
            openItemModal(false);
        })
        .catch(error => console.error('Error loading product:', error));
}


function deleteProduct(productId) {
    if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?')) {
        fetch(`/adminFake/delete?id=${productId}`, { method: 'POST' })
            .then(() => location.reload()); // Tải lại trang sau khi xóa
    }
}


function saveProduct() {
    const product = {
        id: document.getElementById('product-id').value,
        name: document.getElementById('product-name').value,
        promotionPrice: document.getElementById('product-price').value,
        tags: document.getElementById('product-tags').value,
        category: { id: document.getElementById('product-category').value },
        characteristic: document.querySelector('textarea').value,
        gender: document.querySelector('input[name="radio-10"]:checked').value
    };

    fetch('/adminFake/save', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(product)
    }).then(() => {
        closeItemModal();
        location.reload(); // Tải lại trang
    });
}
let editingVariantId = null;

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
}

function editVariant(itemId, variantId) {
    fetch(`/adminFake/${itemId}/variants/get?variantId=${variantId}`)
        .then(response => response.json())
        .then(variant => {
            document.getElementById('variant-id').value = variant.id || '';
            document.getElementById('variant-name').value = variant.name || '';
            document.getElementById('variant-price').value = variant.price || '';
            document.getElementById('variant-quantity').value = variant.availableQuantity || '';
            document.querySelector('#variant-modal select[name="color_id"]').value = variant.color?.id || '';
            document.querySelector('#variant-modal select[name="size_id"]').value = variant.size?.id || '';
            document.getElementById('variant-modal-title').innerText = 'Update Variant';
            openVariantModal(false);
        })
        .catch(error => console.error('Error loading variant:', error));
}


function saveVariant() {
    const itemId = document.getElementById('product-id').value; // ID của sản phẩm
    const variant = {
        id: document.getElementById('variant-id').value,
        name: document.getElementById('variant-name').value,
        price: document.getElementById('variant-price').value,
        availableQuantity: document.getElementById('variant-quantity').value,
        color: { id: document.querySelector('#variant-modal select[name="color_id"]').value },
        size: { id: document.querySelector('#variant-modal select[name="size_id"]').value }
    };

    fetch(`/adminFake/${itemId}/variants/save`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(variant),
    }).then(() => {
        closeVariantModal();
        loadVariants(itemId); // Cập nhật danh sách biến thể
    });
}
function deleteVariant(itemId, variantId) {
    if (confirm('Bạn có chắc chắn muốn xóa biến thể này không?')) {
        fetch(`/adminFake/${itemId}/variants/delete?variantId=${variantId}`, {
            method: 'POST',
        })
            .then(() => {
                // Tải lại danh sách biến thể
                loadVariants(itemId);
            })
            .catch(error => console.error('Error deleting variant:', error));
    }
}

function loadVariants(itemId) {
    fetch(`/adminFake/${itemId}/variants`)
        .then(response => response.json())
        .then(variants => {
            const variantsList = document.getElementById('variants-list');
            variantsList.innerHTML = ''; // Xóa danh sách cũ

            variants.forEach(variant => {
                const row = document.createElement('tr');
                row.classList.add('hover:bg-gray-100');
                row.innerHTML = `
                    <td>${variant.name}</td>
                    <td>${variant.price}</td>
                    <td>${variant.availableQuantity}</td>
                    <td class="flex justify-end">
                        <button class="btn btn-sm mx-1"
                                onclick="editVariant(this.getAttribute('data-item-id'), this.getAttribute('data-variant-id'))"
                                th:attr="data-item-id=${variant.itemId}"
                                th:attrappend="data-variant-id=${variant.id}"><i data-lucide="Pencil"></i></button>
                        <button class="btn btn-sm mx-1"
                                data-variant-id="${variant.id}"
                                data-item-id="${variant.itemId}"
                                onclick="deleteVariant(this.getAttribute('data-item-id'), this.getAttribute('data-variant-id'))"><i data-lucide="Trash2"></i></button>
                    </td>
                `;
                variantsList.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading variants:', error));
}