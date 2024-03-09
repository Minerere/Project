package com.learncode.Sv;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learncode.Entity.User;
import com.learncode.Entity.UserRole;
import com.learncode.Repo.CartRepo;
import com.learncode.Repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service // danh dau class la service
@RequiredArgsConstructor
public class UserSv {

	@Autowired
	UserRepo userRepository;

	@Autowired
	CartRepo cartRepo;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> findById(int id) {
		return userRepository.findById(id);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void save(User user) {
		userRepository.save(user);
	}

	public void deleteById(int id) {
		User user = userRepository.findById(id).orElse(null);

		if (user != null) {
			if (user.getRole().equals(UserRole.ADMIN)) {
				throw new IllegalArgumentException("Cannot delete admin user!");
			}
			try {
				userRepository.deleteById(id);
			} catch(Exception ex) {
				System.out.print(ex.getMessage());
			}
		} else {
			throw new IllegalArgumentException("User not found!");
		}
	}

	
	public User findOne(int id) {
		return userRepository.findOneById(id);
	}

	public void deleteRelateData(int id) {
		userRepository.deleteById(id);
		cartRepo.deleteById(id);
	}

}
