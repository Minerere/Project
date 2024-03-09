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

import com.learncode.Entity.User;
import com.learncode.Entity.UserRole;
import com.learncode.Sv.UserSv;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class UserController {
	
	@Autowired
	UserSv userNewService;
	
	@GetMapping("/addUser")
	public String addUser(Model model, HttpSession session) {
		session.getAttribute("user");	
		User user = (User) session.getAttribute("user");
		List<User> userNews = userNewService.getAllUsers();
		model.addAttribute("userNews", userNews);
		User userNew = new User();
		model.addAttribute("userNewE", userNew);
		if((User)session.getAttribute("user") != null && ((User)session.getAttribute("user")).getRole() == UserRole.ADMIN) {
			model.addAttribute("user", user);
			return "Dashboard/addUser";
		}else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/userTable")
	public String userNewManagement(Model model, HttpSession session) {
		session.getAttribute("user");	
		User user = (User) session.getAttribute("user");
		List<User> userNews = userNewService.getAllUsers();
		model.addAttribute("userNews", userNews);		
		if((User)session.getAttribute("user") != null && ((User)session.getAttribute("user")).getRole() == UserRole.ADMIN) {
			model.addAttribute("user", user);
			return "Dashboard/userTable";
		}else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/userTable/{id}")
	public String showUserId(@PathVariable int id, Model model, HttpSession session) {
		session.getAttribute("user");	
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		List<User> userNews = userNewService.getAllUsers();
		model.addAttribute("userNews", userNews);
		Optional<User> userNewGet = userNewService.findById(id);
		User userNew = new User();
		if (userNewGet.isPresent()) {
			userNew = userNewGet.get();
		}
		model.addAttribute("userNewE", userNew);
		return "Dashboard/addUser";
	}
	
	@PostMapping("/addUser/save-or-update")
	public String saveOrUpdateUser(@ModelAttribute User userNewE,Model model, RedirectAttributes redirectAttributes, HttpSession session) {		
		session.getAttribute("user");
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		try {
			userNewService.save(userNewE);
			redirectAttributes.addFlashAttribute("success", "Lưu người dùng thành công");
			return "redirect:/dashboard/userTable";
		}catch (Exception ex){			
			model.addAttribute("error", ex.getMessage());
			model.addAttribute("userNewE", userNewE);
			return "Dashboard/addUser";
		}
		
	}
	
	@GetMapping("userTable/delete/{id}")
	public String deleteUser(HttpSession session, @PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
		session.getAttribute("user");	
		User user = (User) session.getAttribute("user");
		List<User> userNews = userNewService.getAllUsers();
		model.addAttribute("userNews", userNews);		
		if((User)session.getAttribute("user") != null && ((User)session.getAttribute("user")).getRole() == UserRole.ADMIN) {
			model.addAttribute("user", user);
			try {
				userNewService.deleteRelateData(id);
				redirectAttributes.addFlashAttribute("success", "Xóa người dùng thành công");
				return "redirect:/dashboard/userTable";
			}catch(Exception e) {
				redirectAttributes.addFlashAttribute("error", e.getMessage());
				return "redirect:/dashboard/userTable";
			}
		}else {
			return "redirect:/login";
		}
		
	}
}
