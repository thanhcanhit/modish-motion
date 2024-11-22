package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.CartSessionDTO;
import fit.iuh.modish_motion.dto.CartItemDTO;
import fit.iuh.modish_motion.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")

public class CartRestController {
    private final CartService cartService;

    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartSessionDTO> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/add")
    public ResponseEntity<CartSessionDTO> addItem(@RequestBody CartItemDTO item) {
        cartService.addItem(item);
        return ResponseEntity.ok(cartService.getCart());
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<CartSessionDTO> removeItem(@PathVariable String variantId) {
        cartService.removeItem(variantId);
        return ResponseEntity.ok(cartService.getCart());
    }

    @PutMapping("/{variantId}")
    public ResponseEntity<CartSessionDTO> updateQuantity(
            @PathVariable String variantId,
            @RequestParam int quantity) {
        cartService.updateQuantity(variantId, quantity);
        return ResponseEntity.ok(cartService.getCart());
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getTotalItems() {
        return ResponseEntity.ok(cartService.getTotalItems());
    }
} 