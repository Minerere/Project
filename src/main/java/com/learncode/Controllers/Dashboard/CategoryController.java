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

import com.learncode.Entity.ProductCategory;
import com.learncode.Entity.User;
import com.learncode.Entity.UserRole;
import com.learncode.Sv.ProductCategorySv;

import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/dashboard")
public class CategoryController {
	@Autowired
	ProductCategorySv cateService;
	@GetMapping("/addCategory")
	public String addCategory(Model model, HttpSession session) {
		session.getAttribute("user");
		User user = (User) session.getAttribute("user");
		List<ProductCategory> cates = cateService.findAll();
		model.addAttribute("cates", cates);
		ProductCategory cate = new ProductCategory();
		model.addAttribute("cateS", cate);
		if (user != null && (user.getRole() == UserRole.ADMIN || user.getRole()== UserRole.EMPLOYEE)) {
			model.addAttribute("user", user);
			return "Dashboard/addCategory";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/categoryTable")
	public String showCategory(Model model, HttpSession session) {
		session.getAttribute("user");
		User user = (User) session.getAttribute("user");
		List<ProductCategory> cates = cateService.findAll();
		model.addAttribute("cates", cates);		
		ProductCategory cate = new ProductCategory();
		model.addAttribute("cateS", cate);
		if ((User) session.getAttribute("user") != null && (user.getRole() == UserRole.ADMIN || user.getRole()== UserRole.EMPLOYEE)) {
			model.addAttribute("user", user);
			return "Dashboard/categoryTable";
		} else {
			return "redirect:/login";
		}
	}
	@GetMapping("categoryTable/delete/{id}")
	public String deleteCategory(RedirectAttributes redirectAttributes, @PathVariable int id) {
		try {
cateService.deleteById(id);
redirectAttributes.addFlashAttribute("success", "Xóa thể loại thành công");
		} catch(Exception ex) {
redirectAttributes.addFlashAttribute("error", "Thể loại đang được sử dụng");
		}
		return "redirect:/dashboard/categoryTable";
	}

	@GetMapping("/categoryTable/{id}")
	public String showUpdateForm(@PathVariable int id, Model model, HttpSession session) {
		session.getAttribute("user");
	    User user = (User) session.getAttribute("user");
	    model.addAttribute("user", user);

	    List<ProductCategory> cates = cateService.findAll();
	    model.addAttribute("cates", cates); 

	    Optional<ProductCategory> categet = cateService.findById(id);
	    ProductCategory cate = new ProductCategory();

	    if (categet.isPresent()) {
	        cate = categet.get();
	    }

	    model.addAttribute("cateS", cate); 
	    return "Dashboard/addCategory";
	}

	@PostMapping("/addCategory/save-or-update")
	public String saveOrUpdateCate(@ModelAttribute ProductCategory cate, Model model, RedirectAttributes redirectAttributes) {
	    try {
	        cateService.save(cate);
			redirectAttributes.addFlashAttribute("success", "Lưu thành công");
	        return "redirect:/dashboard/categoryTable";
	    } catch (Exception ex) {
	        model.addAttribute("error", "Có lỗi xảy ra: " + ex.getMessage());
	        model.addAttribute("cateS", cate); 
	        return "Dashboard/addCategory";
	    }
	}
}
