package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Order;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Add custom queries if needed
    List<Order> findByOrderAtBetween(Date startDate, Date endDate, Sort orderAt);

    Page<Order> findByCustomerIdAndStatus(int customerId, String status, Pageable pageable);

    Page<Order> findByCustomerId(int customerId, Pageable pageable);
//    List<Order> findByCustomer_Id(int customerId);
//    List<Order> findByCustomer_IdAndStatus(int customerId, int status);
}