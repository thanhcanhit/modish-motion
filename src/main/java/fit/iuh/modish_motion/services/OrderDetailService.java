package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.OrderDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {
    List<OrderDetailDTO> findAll();
    Optional<OrderDetailDTO> findById(Integer id);
    OrderDetailDTO save(OrderDetailDTO orderDetail);
    void deleteById(Integer id);
    Page<OrderDetailDTO> findByPage(Pageable pageable);
    public List<OrderDetailDTO> findByOrderId(Integer orderId);
}