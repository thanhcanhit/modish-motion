package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Add custom queries if needed
    List<Order> findByOrderAtBetween(Date startDate, Date endDate, Sort orderAt);

    Page<Order> findByCustomerIdAndStatus(int customerId, String status, Pageable pageable);

    @Query("from Order o where o.customer.id = :customerId order by o.orderAt desc")
    Page<Order> findByCustomerId(int customerId, Pageable pageable);
//    List<Order> findByCustomer_Id(int customerId);
//    List<Order> findByCustomer_IdAndStatus(int customerId, int status);

    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.orderAt) = :date AND o.status = :status")
    List<Order> findOrdersByOrderAtAndStatus(@Param("date") Date date, @Param("status") int status);

    // Truy vấn các đơn hàng có trạng thái thành công và nằm trong khoảng thời gian từ startDate đến endDate
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.orderAt BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByStatusAndDateBetween(@Param("status") int status,
                                                 @Param("startDate") Date startDate,
                                                 @Param("endDate") Date endDate);
}