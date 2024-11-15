// cartUI.js

document.addEventListener('DOMContentLoaded', () => {
    const cartItemsContainer = document.getElementById('cart-items');
    const totalAmountElement = document.getElementById('total-amount');
    const grandTotalElement = document.getElementById('grand-total');
    const shippingFeeElement = document.getElementById('shipping-fee');
    const progressElement = document.getElementById('shipping-progress');
    const freeShippingMessageElement = document.getElementById('free-shipping-message');

    const cartData = JSON.parse(localStorage.getItem('cart'));

    const SHIPPING_FEE = 20000; // Set the shipping fee
    const FREE_SHIPPING_THRESHOLD = 500000; // Free shipping threshold

    let totalProductPrice = 0;
    let shippingFee = SHIPPING_FEE;

    if (cartData && cartItemsContainer) {
        cartData.forEach(item => {
            const row = document.createElement('div');
            row.classList.add('flex', 'gap-4', 'border-b', 'border-gray-300', 'pb-4', 'items-center');

            // Product Image with Larger Height
            const img = document.createElement('img');
            img.src = item.variant.imageUrls[0];
            img.alt = item.variant.name;
            img.classList.add('w-32', 'h-32', 'object-cover', 'rounded-md');
            row.appendChild(img);

            // Product Details
            const detailsDiv = document.createElement('div');
            detailsDiv.classList.add('flex-1');

            const productName = document.createElement('h3');
            productName.textContent = item.variant.name;
            productName.classList.add('text-lg', 'font-semibold');
            detailsDiv.appendChild(productName);

            const price = document.createElement('p');
            price.textContent = `${item.variant.price.toLocaleString()} đ`;
            price.classList.add('text-gray-700');
            detailsDiv.appendChild(price);

            // Product Options (Color and Size)
            const optionsDiv = document.createElement('div');
            optionsDiv.classList.add('flex', 'items-center', 'mt-2', 'space-x-2');

            const color = document.createElement('p');
            color.textContent = `Màu: ${item.variant.color.color}`;
            optionsDiv.appendChild(color);

            const size = document.createElement('p');
            size.textContent = `Size: ${item.variant.size.size}`;
            optionsDiv.appendChild(size);

            detailsDiv.appendChild(optionsDiv);

            // Quantity Selector with Buttons
            const quantityDiv = document.createElement('div');
            quantityDiv.classList.add('flex', 'items-center', 'gap-2', 'mt-2');

            const decreaseButton = document.createElement('button');
            decreaseButton.textContent = '-';
            decreaseButton.classList.add('btn', 'border', 'px-2');
            decreaseButton.addEventListener('click', () => {
                if (item.quantity > 1) {
                    item.quantity--;
                    quantityInput.value = item.quantity;
                    updateTotalPrice();
                }
            });

            const quantityInput = document.createElement('input');
            quantityInput.type = 'text';
            quantityInput.value = item.quantity;
            quantityInput.classList.add('input', 'w-12', 'text-center');
            quantityInput.readOnly = true;

            const increaseButton = document.createElement('button');
            increaseButton.textContent = '+';
            increaseButton.classList.add('btn', 'border', 'px-2');
            increaseButton.addEventListener('click', () => {
                item.quantity++;
                quantityInput.value = item.quantity;
                updateTotalPrice();
            });

            quantityDiv.appendChild(decreaseButton);
            quantityDiv.appendChild(quantityInput);
            quantityDiv.appendChild(increaseButton);

            detailsDiv.appendChild(quantityDiv);
            row.appendChild(detailsDiv);
            cartItemsContainer.appendChild(row);

            // Calculate initial total product price
            totalProductPrice += item.variant.price * item.quantity;
        });

        // Initial update of the total amounts and progress
        updateTotalPrice();
    }

    // Function to update total price and progress dynamically
    function updateTotalPrice() {
        totalProductPrice = cartData.reduce((acc, item) => acc + item.variant.price * item.quantity, 0);

        // Check for free shipping eligibility
        shippingFee = totalProductPrice >= FREE_SHIPPING_THRESHOLD ? 0 : SHIPPING_FEE;

        // Update the displayed total product price and shipping fee
        totalAmountElement.textContent = `${totalProductPrice.toLocaleString()} đ`;
        shippingFeeElement.textContent = `${shippingFee.toLocaleString()} đ`;

        // Calculate grand total including the shipping fee
        const grandTotal = totalProductPrice + shippingFee;
        grandTotalElement.textContent = `${grandTotal.toLocaleString()} đ`;

        // Update progress bar
        const progressValue = Math.min((totalProductPrice / FREE_SHIPPING_THRESHOLD) * 100, 100);
        progressElement.value = progressValue;

        // Update free shipping message
        if (progressValue >= 100) {
            freeShippingMessageElement.textContent = 'Bạn đã đủ điều kiện miễn phí vận chuyển!';
        } else {
            const remainingAmount = FREE_SHIPPING_THRESHOLD - totalProductPrice;
            freeShippingMessageElement.textContent = `Mua thêm ${remainingAmount.toLocaleString()} đ để nhận miễn phí vận chuyển`;
        }
    }
});
