import { Cart } from './cart.js';

document.addEventListener('DOMContentLoaded', () => {
    const checkoutItemsContainer = document.getElementById('checkout-items');
    const totalAmountElement = document.getElementById('total-amount');
    const shippingFeeElement = document.getElementById('shipping-fee');
    const grandTotalElement = document.getElementById('grand-total');
    const freeShippingMessageElement = document.getElementById('free-shipping-message');
    const progressElement = document.getElementById('shipping-progress');
    const orderForm = document.getElementById('order-form');

    const SHIPPING_FEE = 20000;
    const FREE_SHIPPING_THRESHOLD = 500000;
    let progressValue = 0;

    function renderCheckoutItems() {
        const checkoutItems = JSON.parse(sessionStorage.getItem('checkoutItems') || '[]');
        checkoutItemsContainer.innerHTML = '';

        checkoutItems.forEach(item => {
            const itemElement = document.createElement('div');
            itemElement.classList.add('flex', 'items-center', 'gap-4', 'border-b', 'pb-4');
            
            itemElement.innerHTML = `
                <img src="${item.variant.imageUrls[0]}" 
                     class="object-cover w-20 h-24 rounded-md" 
                     alt="${item.variant.name}" />
                <div class="flex-1">
                    <h3 class="font-semibold line-clamp-2">${item.variant.name}</h3>
                    <div class="flex items-center gap-2 mt-1">
                        <p class="text-sm text-gray-500">Màu: ${item.variant.color.color}</p>
                        <span class="rounded-full w-4 h-4 inline-block border" 
                              style="background-color: ${item.variant.color.hex}"></span>
                        <p class="text-sm text-gray-500">Size: ${item.variant.size.size}</p>
                    </div>
                    <div class="flex items-center justify-between mt-2">
                        <p class="font-semibold text-warning">${item.variant.price.toLocaleString()} đ</p>
                        <p class="text-sm text-gray-600">x${item.quantity}</p>
                    </div>
                </div>
            `;
            
            checkoutItemsContainer.appendChild(itemElement);
        });

        updateTotalPrice(checkoutItems);
    }

    function updateTotalPrice(items) {
        let totalAmount = 0;

        items.forEach(item => {
            totalAmount += item.variant.price * item.quantity;
        });

        // Cập nhật progress bar và message
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

    // Handle form submission
    orderForm.addEventListener('submit', async (e) => {
        e.preventDefault(); // Prevent form from submitting normally
        
        const selectedPayment = document.querySelector('input[name="payment"]:checked');

        if (!selectedPayment) {
            showToast('Vui lòng chọn phương thức thanh toán', 'warning');
            return;
        }

        // Get items from sessionStorage
        const items = JSON.parse(sessionStorage.getItem('checkoutItems') || '[]');
        
        if (items.length === 0) {
            showToast('Giỏ hàng của bạn đang trống', 'warning');
            return;
        }

        try {
            // Disable submit button to prevent double submission
            const submitButton = orderForm.querySelector('button[type="submit"]');
            submitButton.disabled = true;
            submitButton.innerHTML = `
                <span class="loading loading-spinner"></span>
                Đang xử lý...
            `;

            // Create order data
            const orderData = {
                items: items.map(item => ({
                    variant: item.variant,
                    quantity: item.quantity
                })),
                paymentMethod: selectedPayment.value,
                shippingFee: progressValue >= 100 ? 0 : SHIPPING_FEE
            };

            // Send request to create order
            const response = await fetch('/checkout/api/orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(orderData)
            });

            if (!response.ok) {
                throw new Error(await response.text() || 'Có lỗi xảy ra khi đặt hàng');
            }

            const result = await response.json();
            
            // Clear cart
            const cart = new Cart();
            items.forEach(item => {
                cart.removeItem(item.variant.id);
            });
            
            // Clear checkout items
            sessionStorage.removeItem('checkoutItems');
            
            // Show success message
            showToast('Đặt hàng thành công!', 'success');
            
            // Redirect after delay
            setTimeout(() => {
                window.location.href = `/orders/${result.orderId}`;
            }, 2000);

        } catch (error) {
            showToast(error.message, 'error');
            // Re-enable submit button on error
            const submitButton = orderForm.querySelector('button[type="submit"]');
            submitButton.disabled = false;
            submitButton.innerHTML = `
                <i data-lucide="shopping-cart" class="w-5 h-5"></i>
                Đặt hàng ngay
            `;
        }
    });

    // Initialize the interface
    renderCheckoutItems();

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

    // Thêm event listener cho radio payment
    document.querySelectorAll('input[name="payment"]').forEach(radio => {
        radio.addEventListener('change', function() {
            const paymentMethod = this.parentElement.querySelector('span').textContent;
            let description = '';
            
            switch(paymentMethod.trim()) {
                case 'Tiền mặt (COD)':
                    description = `
                        <p class="text-sm text-gray-600">Sử dụng tiền mặt để thanh toán</p>
                        <p class="text-sm text-gray-500">Vui lòng thanh toán số tiền sau khi sản phẩm được giao tới cho bạn vào khoảng 3-5 ngày làm việc</p>
                    `;
                    break;
                case 'Ví điện tử ZaloPay':
                case 'Ví điện tử Momo':
                case 'Ví điện tử VNPAY':
                    description = `
                        <p class="text-sm text-gray-600">Thanh toán qua ví điện tử</p>
                        <p class="text-sm text-gray-500">Bạn sẽ được chuyển đến trang thanh toán sau khi xác nhận đơn hàng</p>
                    `;
                    break;
                case 'Thẻ nội địa hoặc ngân hàng':
                case 'Thẻ quốc tế':
                    description = `
                        <p class="text-sm text-gray-600">Thanh toán qua thẻ</p>
                        <p class="text-sm text-gray-500">Bạn sẽ được chuyển đến cổng thanh toán an toàn sau khi xác nhận đơn hàng</p>
                    `;
                    break;
            }
            
            paymentDescriptionElement.innerHTML = description;
        });
    });
}); 