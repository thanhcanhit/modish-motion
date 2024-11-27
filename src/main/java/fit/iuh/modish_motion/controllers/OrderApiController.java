package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.OrderDTO;
import fit.iuh.modish_motion.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(
            @PathVariable int customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        Page<OrderDTO> orders = orderService.findOrdersByCustomer(customerId, page, size, status);
        return ResponseEntity.ok(orders.getContent());
    }

}
