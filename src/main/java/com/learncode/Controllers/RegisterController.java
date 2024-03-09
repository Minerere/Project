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

@Controller
public class RegisterController {
    @Autowired PinnedProductSv pinnedProductSv;
    @Autowired ProductSv productSv;
	@Autowired
	UserSv userService;
	@GetMapping("/register")
	public String showRegister(Model model) {
        List<Product> pinnedProducts = pinnedProductSv.findAll().stream().map(ppd -> ppd.getProduct()).distinct().toList();
        List<Product> products = productSv.findAll();
        List<ProductCategory> categories = products.stream().map(product -> product.getProductCategory()).distinct().toList();
        model.addAttribute("pinnedProducts", pinnedProducts);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
		return "User/register";
	}
	@PostMapping("/register")
	public String procress(@RequestParam("email") String email,
							@RequestParam("password") String password,
							@RequestParam("fullName")String fullName,
							Model model) {
		
		User existingUser = userService.findByEmail(email);
		if(existingUser != null) {
			model.addAttribute("res", new Res(true, "Địa chỉ email này đã được sử dụng."));
			return "User/register";
		}
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setUsername(fullName);
		user.setRole(UserRole.USER);
		try {
			userService.save(user);
			return "redirect:/login";
		}catch (Exception e) {
			model.addAttribute("res", new Res(true, e.getMessage()));
			return "User/register";
		}
	}
}
