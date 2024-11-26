class CartAPI {
    static async getCart() {
        const response = await fetch('/api/cart');
        return await response.json();
    }

    static async addItem(item) {
        const cart = await this.getCart();
        const existingItem = cart.items.find(cartItem => cartItem.variant.id === item.variant.id);
        const totalQuantity = (existingItem ? existingItem.quantity : 0) + item.quantity;
        
        if (totalQuantity > item.variant.availableQuantity) {
            throw new Error(`Giỏ hàng của bạn đã đạt số lượng sản phẩm còn lại: ${item.variant.availableQuantity}`);
        }

        const response = await fetch('/api/cart/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(item)
        });

        if (!response.ok) {
            throw new Error('Failed to add item to cart');
        }

        return await response.json();
    }

    static async removeItem(variantId) {
        const response = await fetch(`/api/cart/${variantId}`, {
            method: 'DELETE'
        });
        return await response.json();
    }

    static async updateQuantity(variantId, quantity) {
        const response = await fetch(`/api/cart/${variantId}?quantity=${quantity}`, {
            method: 'PUT'
        });
        return await response.json();
    }

    static async clearCart() {
        await fetch('/api/cart', {
            method: 'DELETE'
        });
    }

    static async getTotalItems() {
        const response = await fetch('/api/cart/count');
        return await response.json();
    }
}

// Cart class for compatibility with existing code
class Cart {
    constructor() {
        this.api = CartAPI;
    }

    async addItem(item) {
        await this.api.addItem(item);
        this.notifyUpdate();
    }

    async removeItem(variantId) {
        await this.api.removeItem(variantId);
        this.notifyUpdate();
    }

    async updateQuantity(variantId, quantity) {
        await this.api.updateQuantity(variantId, quantity);
        this.notifyUpdate();
    }

    async clearCart() {
        await this.api.clearCart();
        this.notifyUpdate();
    }

    async getTotalItems() {
        return await this.api.getTotalItems();
    }

    notifyUpdate() {
        window.dispatchEvent(new CustomEvent('cart-updated'));
    }
}

export { CartAPI, Cart };
