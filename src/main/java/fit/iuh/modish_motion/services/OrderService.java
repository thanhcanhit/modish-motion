package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.OrderDTO;
import fit.iuh.modish_motion.dto.OrderDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDTO> findAll();
    List<OrderDTO> findAll(Sort sort);
    Optional<OrderDTO> findById(Integer id);
    OrderDTO save(OrderDTO order);
    void deleteById(Integer id);
    Page<OrderDTO> findByPage(Pageable pageable);
    OrderDTO createOrder(OrderDTO orderDTO, List<OrderDetailDTO> orderDetails);
    List<OrderDTO> findByDateRange(Date startDate, Date endDate, Sort sort);
    Page<OrderDTO> findOrdersByCustomer(int customerId, int page, int size, String status);
//    List<OrderDTO> findOrdersByCustomerAndStatus(int customerId, String status);
    List<OrderDTO> findOrdersByDateAndStatus(Date date);
    List<OrderDTO> findOrdersByDateAndStatusBetween(Date startDate, Date endDate);
}