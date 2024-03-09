package com.learncode.Controllers.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.learncode.Entity.Product;
import com.learncode.Entity.ProductCategory;
import com.learncode.Entity.User;
import com.learncode.Sv.OrderSv;
import com.learncode.Sv.PinnedProductSv;
import com.learncode.Sv.ProductSv;
import com.learncode.Sv.UserSv;
import com.learncode.Utils.SessionUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
    @Autowired
    private OrderSv orderSv;
    @Autowired
    private UserSv userSv;
    @Autowired
    private ProductSv productSv;
    @Autowired
    private HttpSession httpSession;
    @Autowired
    PinnedProductSv pinnedProductSv;

    @GetMapping("/my-orders")
    public String showMyOrder(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<Product> pinnedProducts = pinnedProductSv.findAll().stream().map(ppd -> ppd.getProduct()).distinct().toList();
            List<Product> products = productSv.findAll();
            List<ProductCategory> categories = products.stream().map(product -> product.getProductCategory()).distinct().toList();
            model.addAttribute("pinnedProducts", pinnedProducts);
            model.addAttribute("products", products);
            model.addAttribute("categories", categories);
            SessionUtil.refreshSession(httpSession, userSv);
            model.addAttribute("orders", orderSv.findOrderByUserId(user.getId()));
            return "User/myOrder";
        }
        return "redirect:/login";
    }
}
