package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {
    List<OrderDetail> findAll();
    Optional<OrderDetail> findById(Integer id);
    OrderDetail save(OrderDetail orderDetail);
    void deleteById(Integer id);
    Page<OrderDetail> findByPage(Pageable pageable);
}