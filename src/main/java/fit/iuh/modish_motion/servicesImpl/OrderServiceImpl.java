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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.DateFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
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
    public List<OrderDTO> findByDateRange(Date startDate, Date endDate, Sort sort) {
        // Adjust endDate to include the entire day
        Date adjustedEndDate = new Date(endDate.getTime() + (1000 * 60 * 60 * 24) - 1);
        List<Order> orders = orderRepository.findByOrderAtBetween(startDate, adjustedEndDate, sort);
        return orders.stream()
                .map(order -> OrderDTO.fromEntity(order, orderDetailService.findByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderDTO> findOrdersByCustomer(int customerId, int page, int size, String status) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orders;
            if (status != null && !status.isEmpty()) {
                orders = orderRepository.findByCustomerIdAndStatus(customerId, status, pageable);
                return orders
                        .map(order -> OrderDTO.fromEntity(order, orderDetailService.findByOrderId(order.getId())));
            } else {
                orders = orderRepository.findByCustomerId(customerId, pageable);
                return orders
                        .map(order -> OrderDTO.fromEntity(order, orderDetailService.findByOrderId(order.getId())));
            }
    }


    @Override
    public List<OrderDTO> findOrdersByDateAndStatus(Date date) {

        List<Order> orders = orderRepository.findOrdersByOrderAtAndStatus(date, 1);

        System.out.println("order danh sach: "+orders);

        // Chuyển đổi danh sách đơn hàng thành danh sách DTO
        return orders.stream()
                .map(order -> OrderDTO.fromEntity(order, orderDetailService.findByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findOrdersByDateAndStatusBetween(Date startDate, Date endDate) {
        // Truy vấn các đơn hàng có trạng thái thành công trong khoảng thời gian từ startDate đến endDate
        List<Order> orders = orderRepository.findOrdersByStatusAndDateBetween(1, startDate, endDate);

        System.out.println("Danh sách đơn hàng trong khoảng thời gian: " + orders);

        // Chuyển đổi danh sách đơn hàng thành danh sách DTO
        return orders.stream()
                .map(order -> OrderDTO.fromEntity(order, orderDetailService.findByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public List<OrderDTO> findOrdersByCustomerAndStatus(int customerId, String status) {
//        List<Order> orders;
//        if (status == null || status.isEmpty()) {
//            orders = orderRepository.findByCustomer_Id(customerId);
//        } else {
//            int statusCode = parseStatus(status);
//            orders = orderRepository.findByCustomer_IdAndStatus(customerId, statusCode);
//        }
//
//        return orders.stream()
//                .map(order -> OrderDTO.fromEntity(order, orderDetailService.findByOrderId(order.getId())))
//                .collect(Collectors.toList());
//    }

//    private int parseStatus(String status) {
//        switch (status.toLowerCase()) {
//            case "pending": return 0;
//            case "processing": return 1;
//            case "shipping": return 2;
//            case "completed": return 3;
//            case "cancelled": return 4;
//            default: throw new IllegalArgumentException("Invalid status: " + status);
//        }
//    }
}
