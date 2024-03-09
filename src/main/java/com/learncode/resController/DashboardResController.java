package com.learncode.resController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learncode.Entity.User;
import com.learncode.Entity.UserRole;
import com.learncode.Repo.UserRepo;

import jakarta.servlet.ServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Res{
	boolean hasError;
	String message;
}

@RestController
@RequestMapping("/dashboard")
public class DashboardResController {
	
	@Autowired
	User userLogin;
	
	@Autowired
	UserRepo userRepository;
	
	@PostMapping("/login")
	public String login(ServletRequest request, @RequestParam(name = "email")String Email, @RequestParam(name = "password")String Password) {
		userLogin = userRepository.findByEmail(Email);
		if(userLogin != null) {
			if(userLogin.getPassword().equals(Password)) {
				if(userLogin.getRole().equals(UserRole.ADMIN) || userLogin.getRole().equals(UserRole.EMPLOYEE)) {
					return "redirect/:dashobard/index";
				}else {
					return "redirect:/index";
				}
			}
		}
		request.setAttribute("res", new Res(true, "Wrong login information!"));
		return "user/login";
	}

	
}
