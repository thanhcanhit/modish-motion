// cart.js

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
    }

    addItem(orderDetail) {
        const existingItem = this.items.find(item => item.variant.id === orderDetail.variant.id);
        if (existingItem) {
            existingItem.quantity += orderDetail.quantity;
        } else {
            this.items.push(orderDetail);
        }
        this.saveCart();
    }

    removeItem(variantId) {
        this.items = this.items.filter(item => item.variant.id !== variantId);
        this.saveCart();
    }

    changeQuantity(variantId, quantity) {
        const item = this.items.find(item => item.variant.id === variantId);
        if (item) {
            item.quantity = quantity;
            this.saveCart();
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
    }

    getCartItems() {
        return this.items;
    }
}

// Export the classes for use in other templates
export { OrderDetailDTO, Cart };