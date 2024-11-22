package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.dto.CartSessionDTO;
import fit.iuh.modish_motion.dto.CartItemDTO;
import fit.iuh.modish_motion.services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

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
    public void addItem(CartItemDTO item) {
        CartSessionDTO cart = getCart();
        cart.addItem(item);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void removeItem(String variantId) {
        CartSessionDTO cart = getCart();
        cart.removeItem(variantId);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void updateQuantity(String variantId, int quantity) {
        CartSessionDTO cart = getCart();
        cart.updateQuantity(variantId, quantity);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void clearCart() {
        CartSessionDTO cart = getCart();
        cart.clear();
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public int getTotalItems() {
        return getCart().getTotalItems();
    }
} 