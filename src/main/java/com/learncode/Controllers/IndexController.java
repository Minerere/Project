package com.learncode.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.learncode.Entity.Order;
import com.learncode.Entity.Product;
import com.learncode.Entity.ProductBrand;
import com.learncode.Entity.ProductCategory;
import com.learncode.Entity.User;
import com.learncode.Sv.OrderSv;
import com.learncode.Sv.PinnedProductSv;
import com.learncode.Sv.ProductSv;
import com.learncode.Sv.UserSv;
import com.learncode.Utils.SessionUtil;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {
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

    @GetMapping({ "/", "/index", "" })
    public String showIndex(Model model) {
        List<Product> pinnedProducts = pinnedProductSv.findAll().stream().map(ppd -> ppd.getProduct()).distinct()
                .toList();
        List<Product> products = productSv.findAll();
        List<ProductCategory> categories = products.stream().map(product -> product.getProductCategory()).distinct()
                .toList();
        model.addAttribute("pinnedProducts", pinnedProducts);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        SessionUtil.refreshSession(httpSession, userSv);
        return "User/index";
    }

    @GetMapping("/products")
    public String showProducts(Model model,
            @RequestParam(value = "s", required = false, defaultValue = "") String s,
            @RequestParam(value = "c", required = false, defaultValue = "-1") int category_id,
            @RequestParam(value = "b", required = false, defaultValue = "-1") int brand_id,
            @RequestParam(value = "p", required = false, defaultValue = "-1") int price_id) {
        List<Product> pinnedProducts = pinnedProductSv.findAll().stream().map(ppd -> ppd.getProduct()).distinct()
                .toList();
        List<Product> products = productSv.findAll();
        List<ProductCategory> categories = products.stream().map(product -> product.getProductCategory()).distinct()
                .toList();
        List<ProductBrand> brands = products.stream().map(product -> product.getProductBrand()).distinct().toList();

        if (s.length() > 0)
            products = products.stream().filter(pd -> pd.getName().contains(s)).toList();
        if (category_id != -1)
            products = products.stream().filter(pd -> pd.getProductCategory().getId() == category_id).toList();
        if (brand_id != -1)
            products = products.stream().filter(pd -> pd.getProductBrand().getId() == brand_id).toList();
        if (price_id != -1)
            switch (price_id) {
                case 1:
                    products = products.stream().filter(pd -> pd.getPromotionalPrice() < 1000000).toList();
                    break;
                case 2:
                    products = products.stream()
                            .filter(pd -> pd.getPromotionalPrice() >= 1000000 && pd.getPromotionalPrice() <= 3000000).toList();
                    break;
                case 3:
                    products = products.stream()
                            .filter(pd -> pd.getPromotionalPrice() >= 3000000 && pd.getPromotionalPrice() <= 5000000).toList();
                    break;
                case 4:
                    products = products.stream()
                            .filter(pd -> pd.getPromotionalPrice() >= 5000000 && pd.getPromotionalPrice() <= 10000000).toList();
                    break;
                case 5:
                    products = products.stream().filter(pd -> pd.getPromotionalPrice() > 10000000).toList();
                    break;
                default:
                    break;
            }
        model.addAttribute("pinnedProducts", pinnedProducts);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        SessionUtil.refreshSession(httpSession, userSv);
        return "User/products";
    }

    @GetMapping(value = "/products/detail/{id}")
    public String showProductDetail(Model model, @PathVariable Integer id) {
        List<Product> pinnedProducts = pinnedProductSv.findAll().stream().map(ppd -> ppd.getProduct()).distinct()
                .toList();
        List<Product> products = productSv.findAll();
        List<ProductCategory> categories = products.stream().map(product -> product.getProductCategory()).distinct()
                .toList();
        model.addAttribute("pinnedProducts", pinnedProducts);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        SessionUtil.refreshSession(httpSession, userSv);
        Product product = productSv.findOne(id);
        // if (product.getQuantity() > 1) {
        model.addAttribute("product", product);
        return "User/productDetail";
        // } else
        // return "redirect:/products";
    }

    @GetMapping("/my-orders/{id}")
    public String showDetailOrder(@PathVariable("id") Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<Product> pinnedProducts = pinnedProductSv.findAll().stream().map(ppd -> ppd.getProduct()).distinct()
                    .toList();
            List<Product> products = productSv.findAll();
            List<ProductCategory> categories = products.stream().map(product -> product.getProductCategory()).distinct()
                    .toList();
            model.addAttribute("pinnedProducts", pinnedProducts);
            model.addAttribute("products", products);
            model.addAttribute("categories", categories);
            SessionUtil.refreshSession(httpSession, userSv);
            Order order = orderSv.findOneById(id);
            model.addAttribute("order", order);
            return "User/detailOrder";
        } else {
            return "redirect:/login";
        }
    }

}
