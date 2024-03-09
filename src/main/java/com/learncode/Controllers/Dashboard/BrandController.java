package com.learncode.Controllers.Dashboard;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.learncode.Entity.ProductBrand;
import com.learncode.Entity.User;
import com.learncode.Entity.UserRole;
import com.learncode.Sv.ProductBrandSv;
import com.learncode.Sv.ProductCategorySv;
import com.learncode.Sv.ProductSv;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class BrandController {
	@Autowired
	ProductSv productService;

	@Autowired
	ProductBrandSv brandService;

	@Autowired
	ProductCategorySv categoryService;

	@GetMapping("/addBrand")
	public String addBrand(Model model, HttpSession session) {
		session.getAttribute("user");
		User user = (User) session.getAttribute("user");

		List<ProductBrand> brands = brandService.findAll();
		model.addAttribute("brands", brands);

		ProductBrand brand = new ProductBrand();
		model.addAttribute("brandS", brand);

		if (user != null) {
			model.addAttribute("user", user);
			return "Dashboard/addBrand";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/brandTable")
	public String showBrand(Model model, HttpSession session) {
		session.getAttribute("user");
		User user = (User) session.getAttribute("user");
		List<ProductBrand> brands = brandService.findAll();
		model.addAttribute("brands", brands);
		ProductBrand brand = new ProductBrand();
		model.addAttribute("BrandS", brand);
		if ((User) session.getAttribute("user") != null
				&& (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.EMPLOYEE)) {
			model.addAttribute("user", user);
			return "Dashboard/brandTable";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("brandTable/delete/{id}")
	public String deleteBrand(RedirectAttributes redirectAttributes, @PathVariable int id) {
		try {
			brandService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xóa thành công");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Thương hiệu đang được sử dụng");
		}
		return "redirect:/dashboard/brandTable";
	}

	@GetMapping("/brandTable/{id}")
	public String showUpdateForm(@PathVariable int id, Model model, HttpSession session) {
		session.getAttribute("user");
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		List<ProductBrand> brands = brandService.findAll();
		model.addAttribute("brands", brands);
		Optional<ProductBrand> brandget = brandService.findById(id);
		ProductBrand brand = new ProductBrand();

		if (brandget.isPresent()) {
			brand = brandget.get();
		}
		model.addAttribute("brandS", brand);
		return "Dashboard/addBrand";
	}

	@PostMapping("/addBrand/save-or-update")
	public String saveOrUpdateBrand(@ModelAttribute ProductBrand brand, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			brandService.save(brand);
			redirectAttributes.addFlashAttribute("success", "Lưu thành công");
			return "redirect:/dashboard/brandTable";
		} catch (Exception ex) {
			model.addAttribute("error", "Có lỗi xảy ra: " + ex.getMessage());
			model.addAttribute("brandS", brand);
			return "Dashboard/addBrand";
		}
	}
}
