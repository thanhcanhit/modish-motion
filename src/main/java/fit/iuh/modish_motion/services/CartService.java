package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.CartSessionDTO;
import fit.iuh.modish_motion.dto.CartItemDTO;

public interface CartService {
    CartSessionDTO getCart();
    void addItem(CartItemDTO item);
    void removeItem(String variantId);
    void updateQuantity(String variantId, int quantity);
    void clearCart();
    int getTotalItems();
    boolean validateQuantity(String variantId, int newQuantity);
} 