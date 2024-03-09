package com.learncode.Controllers.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.learncode.Entity.Cart;
import com.learncode.Entity.Product;
import com.learncode.Entity.ProductCategory;
import com.learncode.Entity.User;
import com.learncode.Sv.CartItemSv;
import com.learncode.Sv.CartSv;
import com.learncode.Sv.PinnedProductSv;
import com.learncode.Sv.ProductSv;
import com.learncode.Sv.UserSv;
import com.learncode.Utils.SessionUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
    @Autowired CartSv cartSv;
    @Autowired CartItemSv cartItemSv;
    @Autowired
    PinnedProductSv pinnedProductSv;
    @Autowired
    ProductSv productSv;
    @Autowired UserSv userSv;
    @Autowired HttpSession httpSession;
    @GetMapping("/cart")
    public String showCart(HttpServletRequest request, Model model) {
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
            return "User/cart";
        }
        return "redirect:/login";
    }

    @GetMapping("/cart/delete/{id}")
    public String deleteCartItem(HttpSession session, @PathVariable int id, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Cart cart = cartSv.findByUserId(user.getId());
        cart.setTotalAmount(cart.getTotalAmount() - cartItemSv.findById(id).getTotalAmount());
        cartSv.save(cart);
        cartItemSv.deleteById(id);
        SessionUtil.refreshSession(httpSession, userSv);
        return "redirect:/cart";
    }
}
