import { Cart } from './cart.js';

document.addEventListener('DOMContentLoaded', () => {
    const checkoutItemsContainer = document.getElementById('checkout-items');
    const totalAmountElement = document.getElementById('total-amount');
    const shippingFeeElement = document.getElementById('shipping-fee');
    const grandTotalElement = document.getElementById('grand-total');
    const freeShippingMessageElement = document.getElementById('free-shipping-message');
    const progressElement = document.getElementById('shipping-progress');
    const paymentDescriptionElement = document.querySelector('.p-4.bg-gray-50');

    const SHIPPING_FEE = 20000;
    const FREE_SHIPPING_THRESHOLD = 500000;

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

    // Khởi tạo giao diện
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

    // Lấy thông tin người dùng từ server
    async function loadUserInfo() {
        try {
            const response = await fetch('/api/user/info');
            if (!response.ok) {
                throw new Error('Failed to load user info');
            }
            const userInfo = await response.json();
            
            // Điền thông tin vào form
            document.getElementById('customer-name').value = userInfo.fullName || '';
            document.getElementById('customer-phone').value = userInfo.phone || '';
            document.getElementById('customer-email').value = userInfo.email || '';
            document.getElementById('customer-address').value = userInfo.address || '';
            
        } catch (error) {
            console.error('Error loading user info:', error);
            showToast('Không thể tải thông tin người dùng', 'error');
        }
    }

    // Load user info when page loads
    loadUserInfo();

    // Xử lý nút đặt hàng
    document.querySelector('.btn-warning').addEventListener('click', async () => {
        // Lấy thông tin từ form
        const orderNote = document.getElementById('order-note').value;
        const selectedPayment = document.querySelector('input[name="payment"]:checked');

        if (!selectedPayment) {
            showToast('Vui lòng chọn phương thức thanh toán', 'warning');
            return;
        }

        // Lấy items từ sessionStorage
        const items = JSON.parse(sessionStorage.getItem('checkoutItems') || '[]');
        
        // Tạo object chứa thông tin đơn hàng
        const orderData = {
            note: orderNote,
            items,
            paymentMethod: selectedPayment.value,
            shippingFee: SHIPPING_FEE
        };

        try {
            const response = await fetch('/checkout/api/orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(orderData)
            });

            if (!response.ok) {
                throw new Error('Có lỗi xảy ra khi đặt hàng');
            }

            const result = await response.json();
            
            // Xóa items đã đặt khỏi giỏ hàng
            const cart = new Cart();
            items.forEach(item => {
                cart.removeItem(item.variant.id);
            });
            
            // Xóa sessionStorage
            sessionStorage.removeItem('checkoutItems');
            
            showToast('Đặt hàng thành công!', 'success');
            
            // Chuyển hướng sau 2 giây
            setTimeout(() => {
                window.location.href = `/orders/${result.orderId}`;
            }, 2000);

        } catch (error) {
            showToast(error.message, 'error');
        }
    });

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