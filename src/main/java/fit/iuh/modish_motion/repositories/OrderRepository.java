package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Add custom queries if needed
}