class OrderDetailDTO {
    constructor(id, order, variant, quantity) {
        this.id = id;
        this.order = order;
        this.variant = variant;
        this.quantity = quantity;
    }

    static fromJSON(json) {
        return new OrderDetailDTO(json.id, json.order, json.variant, json.quantity);
    }
}

class Cart {
    constructor() {
        this.items = this.loadCart();
        this.indicatorElement = null;
        this.initIndicator();
    }

    initIndicator() {
        const cartLink = document.querySelector('a[href="/cart"]');
        if (cartLink) {
            const indicator = document.createElement('div');
            indicator.classList.add('indicator');
            cartLink.parentNode.replaceChild(indicator, cartLink);
            indicator.appendChild(cartLink);

            const badge = document.createElement('span');
            badge.classList.add('indicator-item', 'badge', 'badge-warning', 'badge-sm');
            badge.style.width = '8px';
            badge.style.height = '8px';
            badge.style.padding = '0';
            indicator.appendChild(badge);

            this.indicatorElement = badge;
            this.updateIndicator();
        }
    }

    updateIndicator() {
        if (this.indicatorElement) {
            this.indicatorElement.style.display = this.items.length > 0 ? 'block' : 'none';
        }
    }

    addItem(orderDetail) {
        const existingItem = this.items.find(item => item.variant.id === orderDetail.variant.id);
        if (existingItem) {
            existingItem.quantity += orderDetail.quantity;
        } else {
            this.items.push(orderDetail);
        }
        this.saveCart();
        this.notifyChange();
    }

    removeItem(variantId) {
        this.items = this.items.filter(item => item.variant.id !== variantId);
        this.saveCart();
        this.notifyChange();
    }

    changeQuantity(variantId, quantity) {
        const item = this.items.find(item => item.variant.id === variantId);
        if (item) {
            item.quantity = quantity;
            this.saveCart();
            this.notifyChange();
        }
    }

    notifyChange() {
        this.updateIndicator();
        if (this.onChange) {
            this.onChange(this.items);
        }
    }

    saveCart() {
        localStorage.setItem('cart', JSON.stringify(this.items));
    }

    loadCart() {
        const cart = localStorage.getItem('cart');
        return cart ? JSON.parse(cart).map(OrderDetailDTO.fromJSON) : [];
    }

    clearCart() {
        this.items = [];
        this.saveCart();
        this.notifyChange();
    }

    getCartItems() {
        return this.items;
    }
}

export { OrderDetailDTO, Cart };
