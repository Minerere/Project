package com.learncode.Controllers.Dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learncode.Entity.User;
import com.learncode.Entity.UserRole;
import com.learncode.Sv.OrderSv;
import com.learncode.Sv.ProductBrandSv;
import com.learncode.Sv.ProductSv;
import com.learncode.Sv.UserSv;

import jakarta.servlet.http.HttpSession;

@Controller(value = "IndexDashboardController")
@RequestMapping("/dashboard")
public class IndexController {

	@Autowired
	UserSv userService;

	@Autowired
	private OrderSv orderService;

	@Autowired
	private ProductSv productSv;

	@Autowired ProductBrandSv brandSv;

	@GetMapping({ "/", "", "/index" })
	public String showDashboardHome(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null && (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.EMPLOYEE)) {
			model.addAttribute("users", userService.findAll());
			model.addAttribute("orders", orderService.findAll());
			model.addAttribute("products", productSv.findAll());
			model.addAttribute("brands", brandSv.findAll());
			return "Dashboard/index";
		} else {
			return "redirect:/login";
		}
	}

	// @GetMapping("list_order/delete/{id}")
	// public String deleteOrder(@PathVariable int id, RedirectAttributes redirectAttributes) {
	// 	orderService.delete(id);
	// 	redirectAttributes.addFlashAttribute("alertMessage", "Delete orders successfully!");
	// 	redirectAttributes.addFlashAttribute("alertType", "success");
	// 	return "redirect:/dashboard/list_order";
	// }

	// @GetMapping("list_order/detail/{id}")
	// public String showDetailOrder(@PathVariable("id") Integer id, Model model, HttpSession session) {
	// 	session.getAttribute("loggedInUser");
	// 	User loggedInUser = (User) session.getAttribute("loggedInUser");
	// 	model.addAttribute("loggedInUser", loggedInUser);
	// 	Order order = orderService.findOneById(id);
	// 	Calendar calendar = Calendar.getInstance();
	// 	calendar.setTime(order.getCreatedAt());
	// 	calendar.add(calendar.DAY_OF_MONTH, 3);
	// 	order.setUpdatedAt(calendar.getTime());
	// 	List<OrderDetail> orderDetails = orderDetailSv.findByOrderId(id);
	// 	model.addAttribute("orderModel", order);
	// 	model.addAttribute("orderDetails", orderDetails);
	// 	model.addAttribute("userModel", userService.findOne(order.getUser().getId()));
	// 	model.addAttribute("totalQuantity", orderDetailSv.countProductByOrderId(order.getId()));
	// 	return "Dashboard/detailOrder";
	// }

}
