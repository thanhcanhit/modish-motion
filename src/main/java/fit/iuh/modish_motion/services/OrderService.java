package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findAll();
    Optional<Order> findById(Integer id);
    Order save(Order order);
    void deleteById(Integer id);
    Page<Order> findByPage(Pageable pageable);
}