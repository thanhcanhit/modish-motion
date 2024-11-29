package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Item;
import fit.iuh.modish_motion.entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrderId(Integer orderId);
    @Query("SELECT od FROM OrderDetail od WHERE od.variant.id = :variantId")
    List<OrderDetail> findByVariantId(@Param("variantId") String variantId);
}