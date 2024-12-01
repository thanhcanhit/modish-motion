package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.ProductStatisticDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ProductStatisticService {
    List<ProductStatisticDTO> getTopSellingProductsByDate(Date dateDate);

    List<ProductStatisticDTO> getTopSellingProductsByMonth(int year, int month);
}
