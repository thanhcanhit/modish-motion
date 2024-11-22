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

        cart.items.forEach(item => {
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

            detailsDiv.appendChild(optionsDiv);

            // Add quantity controls
            const quantityDiv = document.createElement('div');
            quantityDiv.classList.add('flex', 'items-center', 'gap-2', 'mt-2');

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
            increaseButton.addEventListener('click', async () => {
                await CartAPI.updateQuantity(item.variant.id, item.quantity + 1);
                await renderCartItems();
                await updateTotalPrice();
            });

            quantityDiv.appendChild(decreaseButton);
            quantityDiv.appendChild(quantityInput);
            quantityDiv.appendChild(increaseButton);

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
        });

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