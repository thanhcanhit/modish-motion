package fit.iuh.modish_motion.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartSessionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<CartItemDTO> items = new ArrayList<>();
    private double total;
    
    public void addItem(CartItemDTO item) {
        for (CartItemDTO existingItem : items) {
            if (existingItem.getVariant().getId().equals(item.getVariant().getId())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                existingItem.updateSubtotal();
                updateTotal();
                return;
            }
        }
        
        item.updateSubtotal();
        items.add(item);
        updateTotal();
    }
    
    public void removeItem(String variantId) {
        items.removeIf(item -> item.getVariant().getId().equals(variantId));
        updateTotal();
    }
    
    public void updateQuantity(String variantId, int quantity) {
        for (CartItemDTO item : items) {
            if (item.getVariant().getId().equals(variantId)) {
                item.setQuantity(quantity);
                item.updateSubtotal();
                updateTotal();
                return;
            }
        }
    }
    
    public void clear() {
        items.clear();
        total = 0;
    }
    
    private void updateTotal() {
        total = items.stream()
                .mapToDouble(CartItemDTO::getSubtotal)
                .sum();
    }

    public int getTotalItems() {
        return items.stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();
    }
} 