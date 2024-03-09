package com.learncode.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.learncode.Entity.Product;
import com.learncode.Entity.ProductCategory;
import com.learncode.Entity.User;
import com.learncode.Entity.UserRole;
import com.learncode.Sv.PinnedProductSv;
import com.learncode.Sv.ProductSv;
import com.learncode.Sv.UserSv;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
class Res{
	boolean hasError;
	String message;
}

@Controller
public class LoginController {
    @Autowired PinnedProductSv pinnedProductSv;
    @Autowired ProductSv productSv;

	@Autowired
	UserSv userService; // khi su dung service nhớ @Autowired de tu khoi tao doi tuong

	@GetMapping("/login")
	public String showLogin(Model model) {
        List<Product> pinnedProducts = pinnedProductSv.findAll().stream().map(ppd -> ppd.getProduct()).distinct().toList();
        List<Product> products = productSv.findAll();
        List<ProductCategory> categories = products.stream().map(product -> product.getProductCategory()).distinct().toList();
        model.addAttribute("pinnedProducts", pinnedProducts);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
		return "User/Login";
	}

	@PostMapping("/login")
	public String login(Model model,HttpServletRequest request,@RequestParam(name = "email")String Email,@RequestParam(name="password")String Password) {
		User userLogin = userService.findByEmail(Email);
		if(userLogin != null) {
			if (userLogin.getPassword().equals(Password)) {
				HttpSession session = request.getSession();
				session.setAttribute("user", userLogin);
				if(userLogin.getRole().equals(UserRole.ADMIN) || userLogin.getRole().equals(UserRole.EMPLOYEE)) {
					return "redirect:/dashboard/index";
				}else {
					return "redirect:/index";
				}
			}
		}
		model.addAttribute("res", new Res(true, "Kiểm tra lại thông tin đăng nhập"));
		return "User/login";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		return "redirect:/index";
	}
	
}
