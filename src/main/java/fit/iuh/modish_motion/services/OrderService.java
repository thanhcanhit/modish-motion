package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.OrderDTO;
import fit.iuh.modish_motion.dto.OrderDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDTO> findAll();
    Optional<OrderDTO> findById(Integer id);
    OrderDTO save(OrderDTO order);
    void deleteById(Integer id);
    Page<OrderDTO> findByPage(Pageable pageable);
    OrderDTO createOrder(OrderDTO orderDTO, List<OrderDetailDTO> orderDetails);
}