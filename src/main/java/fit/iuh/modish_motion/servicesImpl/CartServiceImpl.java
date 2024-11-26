package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.dto.CartSessionDTO;
import fit.iuh.modish_motion.dto.CartItemDTO;
import fit.iuh.modish_motion.services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final HttpSession session;
    private static final String CART_SESSION_KEY = "cart";

    public CartServiceImpl(HttpSession session) {
        this.session = session;
    }

    @Override
    public CartSessionDTO getCart() {
        CartSessionDTO cart = (CartSessionDTO) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new CartSessionDTO();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    @Override
    public void addItem(CartItemDTO newItem) {
        CartSessionDTO cart = getCart();
        
        // Find if item already exists in cart
        Optional<CartItemDTO> existingItem = cart.getItems().stream()
            .filter(item -> item.getVariant().getId().equals(newItem.getVariant().getId()))
            .findFirst();
        
        int totalQuantity = newItem.getQuantity();
        if (existingItem.isPresent()) {
            totalQuantity += existingItem.get().getQuantity();
        }
        
        // Validate total quantity
        if (totalQuantity > newItem.getVariant().getAvailableQuantity()) {
            throw new IllegalArgumentException("Tổng số lượng vượt quá số lượng có sẵn");
        }

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(totalQuantity);
        } else {
            cart.getItems().add(newItem);
        }
        
        updateCart(cart);
    }

    private void updateCart(CartSessionDTO cart) {
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void removeItem(String variantId) {
        CartSessionDTO cart = getCart();
        cart.removeItem(variantId);
        updateCart(cart);
    }

    @Override
    public void updateQuantity(String variantId, int quantity) {
        CartSessionDTO cart = getCart();
        cart.updateQuantity(variantId, quantity);
        updateCart(cart);
    }

    @Override
    public void clearCart() {
        CartSessionDTO cart = getCart();
        cart.clear();
        updateCart(cart);
    }

    @Override
    public int getTotalItems() {
        return getCart().getTotalItems();
    }

    @Override
    public boolean validateQuantity(String variantId, int newQuantity) {
        CartSessionDTO cart = getCart();
        Optional<CartItemDTO> existingItem = cart.getItems().stream()
            .filter(item -> item.getVariant().getId().equals(variantId))
            .findFirst();
            
        if (existingItem.isPresent()) {
            return newQuantity <= existingItem.get().getVariant().getAvailableQuantity();
        }
        return true;
    }
} 