//package fit.iuh.modish_motion.servicesImpl;
//
//import fit.iuh.modish_motion.entities.Order;
//import fit.iuh.modish_motion.repositories.OrderRepository;
//import fit.iuh.modish_motion.services.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import fit.iuh.modish_motion.dto.OrderDTO;
//
//@Service
//public class OrderServiceImpl implements OrderService {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Override
//    public List<OrderDTO> findAll() {
//        List<Order> orders = orderRepository.findAll();
//        return orders.stream()
//                .map(OrderDTO::fromEntity)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Optional<OrderDTO> findById(Integer id) {
//        return orderRepository.findById(id)
//                .map(OrderDTO::fromEntity);
//    }
//
//    @Override
//    public OrderDTO save(OrderDTO orderDTO) {
//        Order order = orderDTO.toEntity();
//        Order savedOrder = orderRepository.save(order);
//        return OrderDTO.fromEntity(savedOrder);
//    }
//
//    @Override
//    public void deleteById(Integer id) {
//        orderRepository.deleteById(id);
//    }
//
//    @Override
//    public Page<OrderDTO> findByPage(Pageable pageable) {
//        return orderRepository.findAll(pageable)
//                .map(OrderDTO::fromEntity);
//    }
//}
