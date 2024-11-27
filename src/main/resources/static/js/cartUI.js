import { CartAPI } from './cart.js';

document.addEventListener('DOMContentLoaded', async () => {
    const cartItemsContainer = document.getElementById('cart-items');
    const totalAmountElement = document.getElementById('total-amount');
    const grandTotalElement = document.getElementById('grand-total');
    const shippingFeeElement = document.getElementById('shipping-fee');
    const progressElement = document.getElementById('shipping-progress');
    const freeShippingMessageElement = document.getElementById('free-shipping-message');
    const buyButton = document.getElementById('buy-button');

    const SHIPPING_FEE = 20000;
    const FREE_SHIPPING_THRESHOLD = 500000;

    async function renderCartItems() {
        const cart = await CartAPI.getCart();
        cartItemsContainer.innerHTML = '';
        const emptyCartMessage = document.getElementById('empty-cart-message');

        if (cart.items.length === 0) {
            emptyCartMessage.classList.remove('hidden');
            cartItemsContainer.classList.add('hidden');
            return;
        } else {
            emptyCartMessage.classList.add('hidden');
            cartItemsContainer.classList.remove('hidden');
        }

        let quantityUpdated = false;

        for (const item of cart.items) {
            // Check if cart quantity exceeds available quantity
            if (item.quantity > item.variant.availableQuantity) {
                await CartAPI.updateQuantity(item.variant.id, item.variant.availableQuantity);
                showToast(`Số lượng sản phẩm "${item.variant.name}" đã được điều chỉnh do vượt quá số lượng có sẵn`, 'warning');
                quantityUpdated = true;
                continue; // Skip this iteration and let the next render handle it
            }

            const row = document.createElement('div');
            row.classList.add('flex', 'gap-4', 'border-b', 'border-gray-300', 'items-start', 'bg-white', 'p-4');

            // Create product image
            const img = document.createElement('img');
            img.src = item.variant.imageUrls[0];
            img.alt = item.variant.name;
            img.classList.add('w-32', 'h-40', 'object-cover');
            row.appendChild(img);

            // Create product details
            const detailsDiv = document.createElement('div');
            detailsDiv.classList.add('flex-1');

            // Add product name
            const productName = document.createElement('a');
            productName.href = `/product/${item.variant.itemId}`;
            productName.textContent = item.variant.name;
            productName.classList.add('text-md', 'font-semibold', 'line-clamp-2');
            detailsDiv.appendChild(productName);

            // Add price
            const price = document.createElement('p');
            price.textContent = `${item.variant.price.toLocaleString()} đ`;
            price.classList.add('text-gray-700', 'font-thin');
            detailsDiv.appendChild(price);

            // Add color and size options
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

            // Add remaining quantity display
            const remainingQuantity = document.createElement('p');
            remainingQuantity.classList.add('text-xs', 'flex', 'items-center', 'gap-1', 'text-gray-500', 'ml-2');
            remainingQuantity.textContent = `Còn lại: ${item.variant.availableQuantity} sản phẩm`;
            optionsDiv.appendChild(remainingQuantity);

            detailsDiv.appendChild(optionsDiv);

            // Update the quantity controls section to show warning when near max quantity
            const quantityDiv = document.createElement('div');
            quantityDiv.classList.add('flex', 'flex-col', 'gap-1', 'mt-2');

            const quantityControls = document.createElement('div');
            quantityControls.classList.add('flex', 'items-center', 'gap-2');

            const decreaseButton = document.createElement('button');
            decreaseButton.textContent = '-';
            decreaseButton.classList.add('btn', 'border', 'btn-sm');
            decreaseButton.addEventListener('click', async () => {
                if (item.quantity > 1) {
                    await CartAPI.updateQuantity(item.variant.id, item.quantity - 1);
                    await renderCartItems();
                    await updateTotalPrice();
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
            increaseButton.disabled = item.quantity >= item.variant.availableQuantity;
            increaseButton.addEventListener('click', async () => {
                if (item.quantity < item.variant.availableQuantity) {
                    await CartAPI.updateQuantity(item.variant.id, item.quantity + 1);
                    await renderCartItems();
                    await updateTotalPrice();
                } else {
                    showToast('Đã đạt số lượng tối đa có sẵn', 'warning');
                }
            });

            // Add warning message if quantity is close to maximum
            if (item.quantity >= item.variant.availableQuantity) {
                const warningMessage = document.createElement('p');
                warningMessage.classList.add('text-xs', 'text-warning');
                warningMessage.textContent = 'Đã đạt số lượng tối đa';
                quantityDiv.appendChild(warningMessage);
            }

            quantityControls.appendChild(decreaseButton);
            quantityControls.appendChild(quantityInput);
            quantityControls.appendChild(increaseButton);
            quantityDiv.appendChild(quantityControls);

            detailsDiv.appendChild(quantityDiv);
            row.appendChild(detailsDiv);

            // Add remove button
            const removeButton = document.createElement('button');
            removeButton.classList.add('btn', 'btn-xs', 'btn-error', 'text-red-200', 'rounded');
            removeButton.addEventListener('click', async () => {
                await CartAPI.removeItem(item.variant.id);
                await renderCartItems();
                await updateTotalPrice();
            });
            removeButton.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-trash-2"><path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/><line x1="10" x2="10" y1="11" y2="17"/><line x1="14" x2="14" y1="11" y2="17"/></svg>';

            row.appendChild(removeButton);
            cartItemsContainer.appendChild(row);
        }

        // If any quantities were updated, re-render to show correct quantities
        if (quantityUpdated) {
            await renderCartItems();
        }

        await updateTotalPrice();
    }

    async function updateTotalPrice() {
        const cart = await CartAPI.getCart();
        let totalAmount = cart.total;

        // Update progress bar and shipping fee
        const progressValue = Math.min((totalAmount / FREE_SHIPPING_THRESHOLD) * 100, 100);
        progressElement.value = progressValue;

        if (progressValue >= 100) {
            freeShippingMessageElement.innerHTML = '<b class="font-semibold text-success">Bạn đã được miễn phí vận chuyển!</b>';
            shippingFeeElement.textContent = '0 đ';
            shippingFeeElement.classList.add('text-success');
        } else {
            const remainingAmount = FREE_SHIPPING_THRESHOLD - totalAmount;
            freeShippingMessageElement.innerHTML = `Mua thêm <b class="font-semibold text-warning">${remainingAmount.toLocaleString()} đ</b> để được <b>Miễn phí vận chuyển</b>`;
            shippingFeeElement.textContent = `${SHIPPING_FEE.toLocaleString()} đ`;
            shippingFeeElement.classList.remove('text-success');
        }

        totalAmountElement.textContent = `${totalAmount.toLocaleString()} đ`;
        const shippingFee = progressValue >= 100 ? 0 : SHIPPING_FEE;
        const grandTotal = totalAmount + shippingFee;
        grandTotalElement.textContent = `${grandTotal.toLocaleString()} đ`;
    }

    function showToast(message, type = 'success') {
        const toast = document.getElementById('alert-toast');
        const toastMessage = document.getElementById('toast-message');

        toast.className = 'alert';
        switch (type) {
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

        setTimeout(() => {
            toast.classList.add('hidden');
        }, 3000);
    }

    // Initialize
    await renderCartItems();
    await updateTotalPrice();

    // Buy button handler
    buyButton.addEventListener('click', async () => {
        const cart = await CartAPI.getCart();
        if (cart.items.length === 0) {
            showToast('Giỏ hàng của bạn đang trống', 'warning');
            return;
        }

        // Store cart items in session storage for checkout
        sessionStorage.setItem('checkoutItems', JSON.stringify(cart.items));
        window.location.href = '/checkout';
    });
});