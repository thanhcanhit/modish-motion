import { Cart } from './cart.js';

document.addEventListener('DOMContentLoaded', () => {
    const cart = new Cart();
    const cartItemsContainer = document.getElementById('cart-items');
    const totalAmountElement = document.getElementById('total-amount');
    const grandTotalElement = document.getElementById('grand-total');
    const shippingFeeElement = document.getElementById('shipping-fee');
    const progressElement = document.getElementById('shipping-progress');
    const freeShippingMessageElement = document.getElementById('free-shipping-message');
    const checkAllCheckbox = document.getElementById('check-all');
    const buyButton = document.getElementById('buy-button');

    const SHIPPING_FEE = 20000;
    const FREE_SHIPPING_THRESHOLD = 500000;

    function saveCartData() {
        cart.saveCart();
    }

    function renderCartItems() {
        cartItemsContainer.innerHTML = '';
        const emptyCartMessage = document.getElementById('empty-cart-message');
        const cartData = cart.getCartItems();

        if (cartData.length === 0) {
            emptyCartMessage.classList.remove('hidden');
            cartItemsContainer.classList.add('hidden');
            return;
        } else {
            emptyCartMessage.classList.add('hidden');
            cartItemsContainer.classList.remove('hidden');
        }

        cartData.forEach((item, index) => {
            const row = document.createElement('div');
            row.classList.add('flex', 'gap-4', 'border-b', 'border-gray-300', 'items-start', 'bg-white', 'p-4');

            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.classList.add('checkbox', 'text-blue-500', 'self-center', 'checkbox-md');
            checkbox.addEventListener('change', updateTotalPrice);
            row.appendChild(checkbox);

            const img = document.createElement('img');
            img.src = item.variant.imageUrls[0];
            img.alt = item.variant.name;
            img.classList.add('w-32', 'h-40', 'object-cover');
            row.appendChild(img);

            const detailsDiv = document.createElement('div');
            detailsDiv.classList.add('flex-1');

            const productName = document.createElement('a');
            productName.href = `/product/${item.variant.itemId}`;
            productName.textContent = item.variant.name;
            productName.classList.add('text-md', 'font-semibold', 'line-clamp-2');
            detailsDiv.appendChild(productName);

            const price = document.createElement('p');
            price.textContent = `${item.variant.price.toLocaleString()} đ`;
            price.classList.add('text-gray-700', 'font-thin');
            detailsDiv.appendChild(price);

            const optionsDiv = document.createElement('div');
            optionsDiv.classList.add('flex', 'items-center', 'mt-2', 'space-x-2');

            const color = document.createElement('p');
            color.classList.add('text-xs', 'flex', 'items-center', 'gap-1', 'font-semibold');
            color.innerHTML = `Màu: ${item.variant.color.color}`;
            color.innerHTML += `<span class="rounded-full w-4 h-4 inline-block border" style="background-color: ${item.variant.color.hex}"></span>`;
            optionsDiv.appendChild(color);

            const size = document.createElement('p');
            size.classList.add('text-xs', 'flex', 'items-center', 'gap-1', 'font-semibold');
            size.textContent = `Size: ${item.variant.size.size}`;
            optionsDiv.appendChild(size);

            detailsDiv.appendChild(optionsDiv);

            const quantityDiv = document.createElement('div');
            quantityDiv.classList.add('flex', 'items-center', 'gap-2', 'mt-2');

            const decreaseButton = document.createElement('button');
            decreaseButton.textContent = '-';
            decreaseButton.classList.add('btn', 'border', 'btn-sm');
            decreaseButton.addEventListener('click', () => {
                if (item.quantity > 1) {
                    item.quantity--;
                    quantityInput.value = item.quantity;
                    cart.changeQuantity(item.variant.id, item.quantity);
                    updateTotalPrice();
                }
            });

            const quantityInput = document.createElement('input');
            quantityInput.type = 'text';
            quantityInput.value = item.quantity;
            quantityInput.classList.add('input', 'w-12', 'input-sm', 'text-center');
            quantityInput.readOnly = true;

            const increaseButton = document.createElement('button');
            increaseButton.textContent = '+';
            increaseButton.classList.add('btn', 'border', 'btn-sm');
            increaseButton.addEventListener('click', () => {
                item.quantity++;
                quantityInput.value = item.quantity;
                cart.changeQuantity(item.variant.id, item.quantity);
                updateTotalPrice();
            });

            quantityDiv.appendChild(decreaseButton);
            quantityDiv.appendChild(quantityInput);
            quantityDiv.appendChild(increaseButton);

            detailsDiv.appendChild(quantityDiv);
            row.appendChild(detailsDiv);

            const removeButton = document.createElement('button');
            removeButton.classList.add('btn', 'btn-xs', 'btn-error', 'text-red-200', 'rounded');
            removeButton.addEventListener('click', () => {
                cart.removeItem(item.variant.id);
                renderCartItems();
                updateTotalPrice();
            });
            removeButton.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-trash-2"><path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/><line x1="10" x2="10" y1="11" y2="17"/><line x1="14" x2="14" y1="11" y2="17"/></svg>';

            row.appendChild(removeButton);
            cartItemsContainer.appendChild(row);
        });
    }

    function updateTotalPrice() {
        let totalProductPrice = 0;
        let selectedItems = 0;

        document.querySelectorAll('#cart-items .checkbox').forEach((checkbox, index) => {
            if (checkbox.checked) {
                totalProductPrice += cart.getCartItems()[index].variant.price * cart.getCartItems()[index].quantity;
                selectedItems++;
            }
        });

        let shippingFee = totalProductPrice >= FREE_SHIPPING_THRESHOLD ? 0 : SHIPPING_FEE;

        totalAmountElement.textContent = `${totalProductPrice.toLocaleString()} đ`;
        shippingFeeElement.textContent = `${shippingFee.toLocaleString()} đ`;

        const grandTotal = totalProductPrice + shippingFee;
        grandTotalElement.textContent = `${grandTotal.toLocaleString()} đ`;

        const progressValue = Math.min((totalProductPrice / FREE_SHIPPING_THRESHOLD) * 100, 100);
        progressElement.value = progressValue;

        if (progressValue >= 100) {
            freeShippingMessageElement.innerHTML = '<b class="font-semibold text-amber-500">Bạn đã đủ điều kiện miễn phí vận chuyển!</b>';
        } else {
            const remainingAmount = FREE_SHIPPING_THRESHOLD - totalProductPrice;
            freeShippingMessageElement.innerHTML = `Mua thêm <b class="font-semibold text-amber-500">${remainingAmount.toLocaleString()} đ</b> để nhận <b>Miễn phí vận chuyển</b>`;
        }

        checkAllCheckbox.checked = selectedItems === cart.getCartItems().length;
    }

    function toggleCheckAll() {
        const isChecked = checkAllCheckbox.checked;
        document.querySelectorAll('.checkbox').forEach(checkbox => {
            checkbox.checked = isChecked;
        });
        updateTotalPrice();
    }

    checkAllCheckbox.addEventListener('change', toggleCheckAll);

    renderCartItems();
    updateTotalPrice();

    function showToast(message, type = 'info') {
        const toast = document.getElementById('alert-toast');
        const toastMessage = document.getElementById('toast-message');
        
        // Xóa các class cũ
        toast.className = 'alert';
        
        // Thêm class mới dựa vào type
        switch(type) {
            case 'success':
                toast.classList.add('alert-success');
                break;
            case 'error':
                toast.classList.add('alert-error');
                break;
            case 'warning':
                toast.classList.add('alert-warning');
                break;
            default:
                toast.classList.add('alert-info');
        }
        
        toastMessage.textContent = message;
        toast.classList.remove('hidden');
        
        // Ẩn toast sau 3 giây
        setTimeout(() => {
            toast.classList.add('hidden');
        }, 3000);
    }

    // Sửa lại event listener cho nút Mua hàng
    buyButton.addEventListener('click', () => {
        const selectedItems = [];
        document.querySelectorAll('#cart-items .checkbox').forEach((checkbox, index) => {
            if (checkbox.checked) {
                selectedItems.push(cart.getCartItems()[index]);
            }
        });

        if (selectedItems.length === 0) {
            showToast('Vui lòng chọn ít nhất một sản phẩm', 'warning');
            return;
        }

        // Lưu selected items vào sessionStorage
        sessionStorage.setItem('checkoutItems', JSON.stringify(selectedItems));
        
        // Debug log
        console.log('Selected items:', selectedItems);
        console.log('Redirecting to checkout...');
        
        // Chuyển hướng đến trang checkout
        window.location.href = '/checkout';
    });
});