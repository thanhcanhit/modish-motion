
package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    // Additional query methods can be defined here
}