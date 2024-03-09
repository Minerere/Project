package com.learncode.Controllers.Dashboard;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.learncode.Entity.PinnedProduct;
import com.learncode.Entity.Product;
import com.learncode.Entity.ProductBrand;
import com.learncode.Entity.ProductCategory;
import com.learncode.Entity.ProductImage;
import com.learncode.Entity.User;
import com.learncode.Entity.UserRole;
import com.learncode.Sv.PinnedProductSv;
import com.learncode.Sv.ProductBrandSv;
import com.learncode.Sv.ProductCategorySv;
import com.learncode.Sv.ProductImageSv;
import com.learncode.Sv.ProductSv;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/dashboard")
public class ProductController {

	@Autowired
	ProductSv productService;

	@Autowired
	ProductBrandSv brandService;

	@Autowired
	ProductCategorySv categoryService;

	@Autowired
	ProductImageSv productImageSv;

	@Autowired
	PinnedProductSv pinnedProductSv;

	@GetMapping("/productTable")
	public String viewProductTable(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "s", defaultValue = "", required = false) Optional<String> s) {
		session.getAttribute("user");
		User user = (User) session.getAttribute("user");
		List<Product> products = productService.findAll().stream().filter(p -> p.getName().contains(s.get())).toList();
		model.addAttribute("products", products);
		List<ProductBrand> brands = brandService.findAll();
		model.addAttribute("brands", brands);
		List<ProductCategory> categoris = categoryService.findAll();
		model.addAttribute("categoris", categoris);
		if ((User) session.getAttribute("user") != null) {
			model.addAttribute("user", user);
			return "Dashboard/productTable";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/addProduct")
	public String addProduct(Model model, HttpSession session) {
		session.getAttribute("user");
		User user = (User) session.getAttribute("user");
		List<Product> products = productService.findAll();
		model.addAttribute("products", products);
		Product product = new Product();
		model.addAttribute("productE", product);
		List<ProductBrand> brands = brandService.findAll();
		model.addAttribute("brands", brands);
		List<ProductCategory> categoris = categoryService.findAll();
		model.addAttribute("categoris", categoris);
		if ((User) session.getAttribute("user") != null
				&& (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.EMPLOYEE)) {
			model.addAttribute("user", user);
			return "Dashboard/addProduct";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/productTable/{id}")
	public String showUpdateForm(@PathVariable int id, Model model, HttpSession session) {
		session.getAttribute("user");
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		List<Product> products = productService.findAll();
		model.addAttribute("products", products);
		List<ProductBrand> brands = brandService.findAll();
		model.addAttribute("brands", brands);
		List<ProductCategory> categoris = categoryService.findAll();
		model.addAttribute("categoris", categoris);
		Optional<Product> productget = productService.findById(id);
		Product product = new Product();
		if (productget.isPresent()) {
			product = productget.get();
		}
		model.addAttribute("productE", product);
		return "Dashboard/addProduct";
	}

	@PostMapping("/addProduct/save-or-update")
	public String saveOrUpdateProduct(@ModelAttribute ProductImage imageE, @ModelAttribute Product productE,
			@RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
		try {
			if (!file.isEmpty()) {
				String productImg = StringUtils.cleanPath(file.getOriginalFilename());
				String uploadDir = "src/main/resources/static/public/uploads";
				String filePath = uploadDir + "/" + productImg;
				String fixPath = "/public/uploads/" + productImg;
				Files.createDirectories(Paths.get(uploadDir));
				Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
				imageE.setUrl(fixPath);
				imageE.setProduct(productE);
			}
			productService.save(productE);
			productImageSv.save(imageE);
			redirectAttributes.addFlashAttribute("success", "Lưu sản phẩm thành công");
			return "redirect:/dashboard/productTable";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/dashboard/index";
		}
	}

	@GetMapping("/productTable/pin/{id}")
	public String pinProduct(HttpSession session, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		Product product = productService.findById(id).get();
		PinnedProduct pinnedProduct = new PinnedProduct();
		pinnedProduct.setType("HOT");
		pinnedProduct.setProduct(product);
		try {
			pinnedProductSv.save(pinnedProduct);
			redirectAttributes.addFlashAttribute("success", "Ghim sản phẩm thành công");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Sản phẩm đã được ghim trước đó rồi");
		}
		return "redirect:/dashboard/productTable";
	}

	@GetMapping("/productTable/un-pin/{id}")
	public String unPinProduct(HttpSession session, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			pinnedProductSv.deleteByProductId(id);
			redirectAttributes.addFlashAttribute("success", "Bỏ ghim sản phẩm thành công");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Bỏ ghim sản phẩm thất bại - " + e.getMessage());
		}
		return "redirect:/dashboard/productTable";
	}

	@GetMapping(value = "productTable/delete/{id}")
	public String deleteUser(HttpSession session, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		session.getAttribute("user");
		User user = (User) session.getAttribute("user");
		if ((User) session.getAttribute("user") != null
				&& (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.EMPLOYEE)) {
			model.addAttribute("user", user);
			try {
				productService.deleteById(id);
				redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công");
				return "redirect:/dashboard/productTable";
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("error", "Sản phẩm đang được sử dụng");
				return "redirect:/dashboard/productTable";
			}
		} else {
			return "redirect:/login";
		}

	}

}
