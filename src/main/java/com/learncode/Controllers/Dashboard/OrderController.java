package com.learncode.Controllers.Dashboard;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.learncode.Entity.Order;
import com.learncode.Entity.User;
import com.learncode.Entity.UserRole;
import com.learncode.Sv.OrderSv;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller(value = "OrderDashboardController")
@RequestMapping("/dashboard/orders")
public class OrderController {
    @Autowired
    HttpSession httpSession;
    @Autowired
    OrderSv orderSv;

    @GetMapping("/list")
    public String list(Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user != null && (user.getRole().equals(UserRole.ADMIN) || user.getRole().equals(UserRole.EMPLOYEE))) {
            List<Order> orders = orderSv.findAll();
            Collections.reverse(orders);
            model.addAttribute("orders", orders);
            return "Dashboard/listOrder";
        } else {
            return "redirect:/login";
        }
    }
    @GetMapping("/detail/{id}")
	public String showDetailOrder(@PathVariable("id") Integer id, Model model, HttpSession session) {
        User user = (User) httpSession.getAttribute("user");
        if (user != null && (user.getRole().equals(UserRole.ADMIN) || user.getRole().equals(UserRole.EMPLOYEE))) {
            Order order = orderSv.findOneById(id);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(order.getCreatedAt());
            calendar.add(Calendar.DAY_OF_MONTH, 3);
            order.setUpdatedAt(calendar.getTime());
            model.addAttribute("order", order);
            return "Dashboard/detailOrder";
        } else {
            return "redirect:/login";
        }
	}
    @PostMapping("/update-status")
    public String updateStatusOrder(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "status", required = false) String status) {
        User user = (User) httpSession.getAttribute("user");
        if (user != null && (user.getRole().equals(UserRole.ADMIN) || user.getRole().equals(UserRole.EMPLOYEE))) {
            if (id != null) {
                Order order = orderSv.findOneById(id);
                if (order != null) {
                    orderSv.update(order, status);
                    redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái đơn hàng thành công");
                    return "redirect:/dashboard/orders/list";
                }
            }
            return "redirect:/dashboard/orders/list";
        } else {
            return "redirect:/login";
        }
    }
 	@GetMapping("/delete/{id}")
	public String deleteOrder(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
            orderSv.delete(id);
            redirectAttributes.addFlashAttribute("success", "Xóa đơn hàng thành công");
        } catch(Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
		return "redirect:/dashboard/orders/list";
	}
}
