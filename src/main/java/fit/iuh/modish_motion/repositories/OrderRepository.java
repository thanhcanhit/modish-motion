package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Add custom queries if needed
    List<Order> findByOrderAtBetween(Date startDate, Date endDate, Sort orderAt);
    // Additional query methods can be defined here
}