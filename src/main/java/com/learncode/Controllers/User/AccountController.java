package com.learncode.Controllers.User;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.learncode.Entity.Product;
import com.learncode.Entity.ProductCategory;
import com.learncode.Entity.User;
import com.learncode.Sv.PinnedProductSv;
import com.learncode.Sv.ProductSv;
import com.learncode.Sv.UserSv;
import com.learncode.Utils.SessionUtil;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	UserSv userSv;
	@Autowired
	HttpSession httpSession;
	@Autowired
	PinnedProductSv pinnedProductSv;
	@Autowired
	ProductSv productSv;

	@GetMapping("/information")
	public String information(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if ((User) session.getAttribute("user") == null) {
			return "redirect:/login";
		} else {
			if (user != null) {
				List<Product> pinnedProducts = pinnedProductSv.findAll().stream().map(ppd -> ppd.getProduct())
						.distinct().toList();
				List<Product> products = productSv.findAll();
				List<ProductCategory> categories = products.stream().map(product -> product.getProductCategory())
						.distinct().toList();
				model.addAttribute("pinnedProducts", pinnedProducts);
				model.addAttribute("products", products);
				model.addAttribute("categories", categories);
				model.addAttribute("user", user);
				SessionUtil.refreshSession(httpSession, userSv);
			}
		}
		return "User/account/information";
	}

	@PostMapping("/information")
	public String changeInformationHandle(HttpSession session, RedirectAttributes redirectAttributes,
			@RequestParam int id,
			@RequestParam String email,
			@RequestParam String username,
			@RequestParam String tel,
			@RequestParam String deliveryAddress,
			@RequestParam MultipartFile image) {
		try {
			User user = userSv.findById(id).orElse(null);
			if (user != null) {
				user.setEmail(email);
				user.setUsername(username);
				user.setTel(tel);
				user.setDeliveryAddress(deliveryAddress);
				if (image != null && !image.isEmpty()) {

					String productImg = StringUtils.cleanPath(image.getOriginalFilename());
					String uploadDir = "src/main/resources/static/public/uploads";
					String filePath = uploadDir + "/" + productImg;
					String fixPath = "/public/uploads/" + productImg;
					Files.createDirectories(Paths.get(uploadDir));
					Files.copy(image.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
					Thread.sleep(3000);
					user.setImage(fixPath);
				}
				userSv.save(user);
				redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin trang cá nhân thành công");
			} else {
				redirectAttributes.addFlashAttribute("error", "Không thể cập nhật thông tin cá nhân");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Không thể cập nhật thông tin cá nhân");
		}
		SessionUtil.refreshSession(session, userSv);
		return "redirect:/account/information";
	}

	@GetMapping("/change-password")
	public String changePassword(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if ((User) session.getAttribute("user") == null) {
			return "redirect:/login";
		} else {
			if (user != null) {
				List<Product> pinnedProducts = pinnedProductSv.findAll().stream().map(ppd -> ppd.getProduct())
						.distinct().toList();
				List<Product> products = productSv.findAll();
				List<ProductCategory> categories = products.stream().map(product -> product.getProductCategory())
						.distinct().toList();
				model.addAttribute("pinnedProducts", pinnedProducts);
				model.addAttribute("products", products);
				model.addAttribute("categories", categories);
				model.addAttribute("user", user);
				SessionUtil.refreshSession(httpSession, userSv);
			}
		}
		return "User/account/change-password";
	}

	@PostMapping("/change-password")
	public String changePasswordHandle(HttpSession session, RedirectAttributes redirectAttributes,
			@RequestParam int id,
			@RequestParam String old_password,
			@RequestParam String new_password,
			@RequestParam String confirm_password) {
		try {
			User user = userSv.findById(id).orElse(null);
			if (user.getPassword().equals(old_password)) {
				user.setPassword(confirm_password);
				userSv.save(user);
				redirectAttributes.addFlashAttribute("success", "Thay đổi mật khẩu thành công");
			} else {
				redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không chính xác");
			}
		} catch (Exception ex) {
			redirectAttributes.addFlashAttribute("error", "Thay đổi mật khẩu thất bại");
		}
		SessionUtil.refreshSession(httpSession, userSv);
		return "redirect:/account/change-password";
	}

	@GetMapping("/my-orders")
	public String myOrders(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if ((User) session.getAttribute("user") == null) {
			return "redirect:/login";
		} else {
			if (user != null) {
				List<Product> pinnedProducts = pinnedProductSv.findAll().stream().map(ppd -> ppd.getProduct())
						.distinct().toList();
				List<Product> products = productSv.findAll();
				List<ProductCategory> categories = products.stream().map(product -> product.getProductCategory())
						.distinct().toList();
				model.addAttribute("pinnedProducts", pinnedProducts);
				model.addAttribute("products", products);
				model.addAttribute("categories", categories);
				model.addAttribute("user", user);
				SessionUtil.refreshSession(httpSession, userSv);
			}
		}
		return "User/account/my-orders";
	}
}
