package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.entities.OrderDetail;
import fit.iuh.modish_motion.repositories.OrderDetailRepository;
import fit.iuh.modish_motion.services.OrderDetailService;
import fit.iuh.modish_motion.dto.OrderDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetailDTO> findAll() {
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        return orderDetails.stream()
                .map(OrderDetailDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDetailDTO> findById(Integer id) {
        return orderDetailRepository.findById(id)
                .map(OrderDetailDTO::fromEntity);
    }

    @Override
    public OrderDetailDTO save(OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = orderDetailDTO.toEntity();
        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        return OrderDetailDTO.fromEntity(savedOrderDetail);
    }

    @Override
    public void deleteById(Integer id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public Page<OrderDetailDTO> findByPage(Pageable pageable) {
        return orderDetailRepository.findAll(pageable)
                .map(OrderDetailDTO::fromEntity);
    }

    @Override
    public List<OrderDetailDTO> findByOrderId(Integer orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        return orderDetails.stream()
                .map(OrderDetailDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
