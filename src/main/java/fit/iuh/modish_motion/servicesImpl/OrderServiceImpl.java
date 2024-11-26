package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.dto.OrderDTO;
import fit.iuh.modish_motion.entities.Order;
import fit.iuh.modish_motion.repositories.OrderRepository;
import fit.iuh.modish_motion.services.OrderDetailService;
import fit.iuh.modish_motion.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public List<OrderDTO> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> OrderDTO.fromEntity(order, orderDetailService.findByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findAll(Sort sort) {
        List<Order> orders = orderRepository.findAll(sort);
        return orders.stream()
                .map(order -> OrderDTO.fromEntity(order, orderDetailService.findByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDTO> findById(Integer id) {
        return orderRepository.findById(id)
                .map(order -> OrderDTO.fromEntity(order, orderDetailService.findByOrderId(order.getId())));
    }

    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        Order order = orderDTO.toEntity();
        Order savedOrder = orderRepository.save(order);
        return OrderDTO.fromEntity(savedOrder, orderDetailService.findByOrderId(order.getId()));
    }

    @Override
    public void deleteById(Integer id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Page<OrderDTO> findByPage(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(order -> OrderDTO.fromEntity(order, orderDetailService.findByOrderId(order.getId())));
    }

    @Override
    public List<OrderDTO> findByDateRange(Date startDate, Date endDate, Sort sort) {
        // Adjust endDate to include the entire day
        Date adjustedEndDate = new Date(endDate.getTime() + (1000 * 60 * 60 * 24) - 1);
        List<Order> orders = orderRepository.findByOrderAtBetween(startDate, adjustedEndDate, sort);
        return orders.stream()
                .map(order -> OrderDTO.fromEntity(order, orderDetailService.findByOrderId(order.getId())))
                .collect(Collectors.toList());
    }
}
