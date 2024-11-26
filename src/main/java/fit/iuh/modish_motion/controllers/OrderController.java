package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.OrderDTO;
import fit.iuh.modish_motion.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort;
import java.util.ArrayList;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/dashboard/orders")
    public String listOrders(@RequestParam(required = false) String startDate,
                             @RequestParam(required = false) String endDate,
                             @RequestParam(required = false, defaultValue = "orderAt") String sort,
                             @RequestParam(required = false, defaultValue = "asc") String order,
                             @RequestParam(required = false) String searchType,
                             @RequestParam(required = false) String searchValue,
                             Model model) {
        List<OrderDTO> orders;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        try {
            if (startDate != null) {
                start = formatter.parse(startDate);
            }
            if (endDate != null) {
                end = formatter.parse(endDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (start != null && end != null) {
            orders = orderService.findByDateRange(start, end, Sort.by(Sort.Direction.DESC, "orderAt"));
            orders.sort((o1, o2) -> {
                if (direction == Sort.Direction.ASC) {
                    return o1.getOrderAt().compareTo(o2.getOrderAt());
                } else {
                    return o2.getOrderAt().compareTo(o1.getOrderAt());
                }
            });
        } else {
            orders = orderService.findAll(Sort.by(direction, sort));
        }

        if (searchType != null && searchValue != null && !searchValue.isEmpty()) {
            List<OrderDTO> filteredOrders = new ArrayList<>();
            for (OrderDTO o : orders) {
                if (searchType.equals("id") && String.valueOf(o.getId()).equals(searchValue)) {
                    filteredOrders.add(o);
                } else if (searchType.equals("name") && o.getCustomer().getName().toLowerCase().contains(searchValue.toLowerCase())) {
                    filteredOrders.add(o);
                }
            }
            orders = filteredOrders;
        }

        model.addAttribute("orders", orders);
        model.addAttribute("selectedTab", "orders");
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("sortOrder", order);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        return "dashboard-orders";
    }
}
