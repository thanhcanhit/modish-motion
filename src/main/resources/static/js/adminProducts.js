const container = document.getElementById('image-links-container');
const newLinkInput = document.getElementById('new-link-input');
const template = document.getElementById('link-template').content;
const containerTag = document.getElementById('tags-container');
const newLinkInputTag = document.getElementById('tags-input');
const templateTag = document.getElementById('tags-template').content;
/** Add new link to the list */
function addLink() {
    const value = newLinkInput.value.trim();
    if (!value) return;
    createLinkElement(value);
    newLinkInput.value = '';
}
function addTag() {
    const value = newLinkInputTag.value.trim();
    if (!value) return;
    createTagsElement(value);
    newLinkInputTag.value = '';
}

/** Remove link from the list */
function removeLink(button) {
    const linkElement = button.parentNode;
    container.removeChild(linkElement);
}
function removeTag(button) {
    const linkElement = button.parentNode;
    containerTag.removeChild(linkElement);
}

/** Create a link element in the UI */
function createLinkElement(value) {
    const clone = template.cloneNode(true);
    const img = clone.querySelector('img');
    const input = clone.querySelector('input');
    input.value = value;
    img.src = value;
    container.appendChild(clone);
}
function createTagsElement(value) {
    const clone = templateTag.cloneNode(true);
    const input = clone.querySelector('input');
    input.value = value;
    containerTag.appendChild(clone);
}

function navigateToItem(rowElement) {
    const url = rowElement.getAttribute('data-url');
    if (url) {
        window.location.href = url;
    } else {
        console.error("No URL found in data attribute");
    }
}
function createAlert(type, message) {
    const alertContainer = document.getElementById('alert-container');

    // Tạo phần tử alert
    const alert = document.createElement('div');
    alert.setAttribute('role', 'alert');
    alert.classList.add('alert');

    // Thêm class theo loại alert
    switch (type) {
        case 'error':
            alert.classList.add('alert-error');
            alert.innerHTML = `
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current text-white" fill="none" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span class="text-white">${message}</span>`;
            break;
        case 'warning':
            alert.classList.add('alert-warning');
            alert.innerHTML = `
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current text-white" fill="none" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                </svg>
                <span class="text-white">${message}</span>`;
            break;
        case 'success':
            alert.classList.add('alert-success');
            alert.innerHTML = `
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current text-white" fill="none" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span class="text-white">${message}</span>`;
            break;
    }

    // Thêm alert vào container
    alertContainer.appendChild(alert);

    // Tự động ẩn sau 5 giây
    setTimeout(() => {
        alert.remove();
    }, 5000);
}
function validateProductFields(product) {
    if (!product.name.trim()) {
        createAlert('error', 'Tên sản phẩm không được để trống.');
        return false;
    }
    if (!product.promotionPrice || product.promotionPrice < 0) {
        createAlert('warning', 'Giá sản phẩm phải lớn hơn hoặc bằng 0.');
        return false;
    }
    if (!product.category.id) {
        createAlert('error', 'Vui lòng chọn loại sản phẩm.');
        return false;
    }
    if (!product.gender.trim()) {
        createAlert('error', 'Giới tính không được để trống.');
        return false;
    }
    return true;
}

function validateVariantFields(variant) {
    if (!variant.name.trim()) {
        createAlert('error', 'Tên biến thể không được để trống.');
        return false;
    }
    if (!variant.price || variant.price < 0) {
        createAlert('warning', 'Giá biến thể phải lớn hơn hoặc bằng 0.');
        return false;
    }
    if (!variant.availableQuantity || variant.availableQuantity < 0) {
        createAlert('warning', 'Số lượng phải lớn hơn hoặc bằng 0.');
        return false;
    }
    if (!variant.color.id) {
        createAlert('error', 'Vui lòng chọn màu.');
        return false;
    }
    if (!variant.size.id) {
        createAlert('error', 'Vui lòng chọn kích thước.');
        return false;
    }
    return true;
}






function openItemModal(isAdd) {
    const container = document.getElementById('tags-container');
    if(isAdd){
        document.getElementById('product-modal-title').innerText = 'Add Product';
        document.getElementById('product-form').reset();
        document.getElementById('item-modal').classList.remove('hidden');
        container.innerHTML="";
    }

    document.getElementById('item-modal').classList.remove('hidden');
}

function closeItemModal() {
    document.getElementById('item-modal').classList.add('hidden');
}
function saveProduct() {
    // const tags = Array.from(document.querySelectorAll('#tags-container input')).map(input => input.value.trim());
    const product = {
        id: document.getElementById('product-id').value, // Lấy id từ input ẩn
        name: document.getElementById('product-name').value,
        promotionPrice: document.getElementById('product-price').value,
        tags: document.getElementById('product-tags').value,
        category: { id: document.getElementById('product-category').value },
        characteristic: document.getElementById('item-characteristic').value,
        gender: document.getElementById('product-gender').value,
    };
    if (!validateProductFields(product)) return;
    fetch('/admin/products/save', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(product),
    })
        .then(() => {
            closeItemModal();
            createAlert('success', 'Đã lưu sản phẩm thành công.');
            setTimeout(() => {
                location.reload();
            }, 1000); // 1000ms = 1 giây
        })
        .catch(error => {console.error('Error saving product:', error); createAlert('error', 'Đã xảy ra lỗi khi lưu sản phẩm.');});
}


function editProduct(productId) {
    fetch(`/admin/products/get?id=${productId}`)
        .then(response => response.json())
        .then(product => {
            document.getElementById('product-id').value = product.id ;
            document.getElementById('product-name').value = product.name ;
            document.getElementById('product-price').value = product.promotionPrice ;
            document.getElementById('product-category').value = product.category?.id ;
            document.getElementById('product-tags').value = product.tags ;
            document.querySelector('textarea').value = product.characteristic ;
            document.getElementById('product-gender').value = product.gender ;

            document.getElementById('product-modal-title').innerText = 'Update Product';
            openItemModal(false);
        })
        .catch(error => console.error('Error loading product:', error));
}



function deleteProduct(productId) {
    showActionAlert(
        "Bạn có chắc chắn muốn xóa sản phẩm này không?",
        () => {
            // Hành động khi nhấn Accept
            fetch(`/admin/products/delete?id=${productId}`, { method: 'POST' })
                .then(response => response.text())
                .then(result => {
                    if (result.startsWith('error:')) {
                        createAlert('error', result.replace('error:', '').trim());
                    } else {
                        createAlert('success', 'Sản phẩm đã được xóa thành công.');
                        deleteRowById(`${productId}`);
                    }
                })
                .catch(error => {
                    console.error('Error deleting product:', error);
                    createAlert('error', 'Đã xảy ra lỗi khi xóa sản phẩm.');
                });
        },
        () => {
            // Hành động khi nhấn Deny
            createAlert('info', 'Hủy xóa sản phẩm.');
        }
    );
}







function openVariantModal(isAdd) {
    const container = document.getElementById('image-links-container'); // Lấy container chứa các link

    if (isAdd) {
        // Đặt tiêu đề modal là "Add Variant"
        document.getElementById('variant-modal-title').innerText = 'Add Variant';

        // Reset form
        document.getElementById('variant-form').reset();

        // Xóa hết các link ảnh trong container
        container.innerHTML = '';
    }

    // Hiển thị modal
    document.getElementById('variant-modal').classList.remove('hidden');
}

function closeVariantModal() {
    document.getElementById('variant-modal').classList.add('hidden');
}
function saveVariant() {
    const itemId = document.getElementById('selected-item-id').value;
    if (!itemId) {
        console.error('Item ID is missing');
        return;
    }

    // Thu thập các link ảnh từ giao diện
    const imageLinks = Array.from(document.querySelectorAll('#image-links-container input')).map(input => input.value.trim());

    const variant = {
        id: document.getElementById('variant-id').value,
        name: document.getElementById('variant-name').value,
        price: document.getElementById('variant-price').value,
        availableQuantity: document.getElementById('variant-quantity').value,
        color: { id: document.querySelector('#variant-modal select[name="color_id"]').value },
        size: { id: document.querySelector('#variant-modal select[name="size_id"]').value },
        imageUrls: imageLinks,
    };
    if (!validateVariantFields(variant)) return;

    fetch(`/admin/products/${itemId}/variants/save`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(variant),
    })
        .then(() => {
            closeVariantModal();
            createAlert('success', 'Đã lưu loại sản phẩm thành công.');
            setTimeout(() => {
                location.reload();
            }, 1000); // 1000ms = 1 giây
        })
        .catch(error => {console.error('Error saving variant:', error); createAlert('error', 'Đã xảy ra lỗi khi lưu loại sản phẩm.');});
}
function editVariant(itemId, variantId) {
    fetch(`/admin/products/${itemId}/variants/get?variantId=${variantId}`)
        .then(response => response.json())
        .then(variant => {
            document.getElementById('variant-id').value = variant.id;
            document.getElementById('variant-name').value = variant.name;
            document.getElementById('variant-price').value = variant.price;
            document.getElementById('variant-quantity').value = variant.availableQuantity;
            document.querySelector('#variant-modal select[name="color_id"]').value = variant.color?.id;
            document.querySelector('#variant-modal select[name="size_id"]').value = variant.size?.id;

            // Làm sạch và thêm các link ảnh
            const container = document.getElementById('image-links-container');
            container.innerHTML = ''; // Xóa các link cũ
            (variant.imageUrls || []).forEach(link => createLinkElement(link));

            document.getElementById('variant-modal-title').innerText = 'Update Variant';
            openVariantModal(false);
        })
        .catch(error => console.error('Error loading variant:', error));
}


function showActionAlert(message, onAccept, onDeny) {
    const alert = document.getElementById('action-alert');
    const alertMessage = document.getElementById('action-alert-message');
    const alertDeny = document.getElementById('action-alert-deny');
    const alertAccept = document.getElementById('action-alert-accept');

    // Cập nhật nội dung thông báo
    alertMessage.textContent = message;

    // Hiển thị alert
    alert.classList.remove('hidden');

    // Xử lý nút "Accept"
    const handleAccept = () => {
        if (onAccept) onAccept(); // Gọi callback khi nhấn "Accept"
        closeAlert(); // Đóng alert
    };

    // Xử lý nút "Deny"
    const handleDeny = () => {
        if (onDeny) onDeny(); // Gọi callback khi nhấn "Deny"
        closeAlert(); // Đóng alert
    };

    // Gắn sự kiện
    alertAccept.addEventListener('click', handleAccept);
    alertDeny.addEventListener('click', handleDeny);

    // Hàm đóng alert và xóa sự kiện
    function closeAlert() {
        alert.classList.add('hidden'); // Ẩn alert
        alertAccept.removeEventListener('click', handleAccept);
        alertDeny.removeEventListener('click', handleDeny);
    }
}


function deleteVariant(itemId, variantId) {
    showActionAlert(
        "Bạn có chắc chắn muốn xóa biến thể này không?",
        () => {
            // Hành động khi nhấn Accept
            fetch(`/admin/products/${itemId}/variants/delete?variantId=${variantId}`, {
                method: 'POST',
            })
                .then(response => response.text())
                .then(result => {
                    if (result.startsWith('error:')) {
                        createAlert('error', result.replace('error:', '').trim());
                    } else {
                        createAlert('success', 'Biến thể đã được xóa thành công.');
                        deleteRowById(`${variantId}`);
                    }
                })
                .catch(error => {
                    console.error('Error deleting variant:', error);
                    createAlert('error', 'Đã xảy ra lỗi khi xóa biến thể.');
                });
        },
        () => {
            // Hành động khi nhấn Deny
            createAlert('info', 'Hủy xóa biến thể.');
        }
    );
}

function deleteRowById(rowId) {
    const row = document.getElementById(rowId); // Lấy dòng theo ID
    if (row) {
        row.parentNode.removeChild(row); // Xóa dòng
    } else {
        console.error(`Không tìm thấy dòng với ID: ${rowId}`);
    }
}
