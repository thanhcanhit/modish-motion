package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.dto.OrderDTO;
import fit.iuh.modish_motion.dto.OrderDetailDTO;
import fit.iuh.modish_motion.entities.Order;
import fit.iuh.modish_motion.repositories.OrderRepository;
import fit.iuh.modish_motion.services.OrderService;
import fit.iuh.modish_motion.services.OrderDetailService;
import fit.iuh.modish_motion.services.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final VariantService variantService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, 
                          OrderDetailService orderDetailService,
                          VariantService variantService) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
        this.variantService = variantService;
    }

    @Override
    public List<OrderDTO> findAll() {
        List<Order> orders = orderRepository.findAll();
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
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO, List<OrderDetailDTO> orderDetails) {
        // Save the order first
        Order order = orderDTO.toEntity();
        Order savedOrder = orderRepository.save(order);
        
        // Update order details with the saved order
        List<OrderDetailDTO> updatedDetails = orderDetails.stream()
                .map(detail -> {
                    // Update variant quantity
                    String variantId = detail.getVariant().getId();
                    int quantity = detail.getQuantity();
                    variantService.updateQuantity(variantId, -quantity);
                    
                    // Set order reference
                    detail.setOrder(savedOrder);
                    
                    // Save order detail
                    return orderDetailService.save(detail);
                })
                .collect(Collectors.toList());

        // Return complete order with details
        return OrderDTO.fromEntity(savedOrder, updatedDetails);
    }
}
