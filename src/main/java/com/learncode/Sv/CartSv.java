package com.learncode.Sv;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learncode.Repo.CartItemRepo;
import com.learncode.Repo.CartRepo;
import com.learncode.Repo.UserRepo;
import com.learncode.Sv.CartSv;
import com.learncode.dto.Cart;

@Service
public class CartSv {
	@Autowired
	private CartRepo cartRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CartItemRepo cartItemRepo;
	
	
	public Cart create(int userId) {
		com.learncode.Entity.Cart cart = new com.learncode.Entity.Cart();
		cart.setTotalAmount(0);
		cart.setUser(userRepo.findOneById(userId));
		com.learncode.Entity.Cart cartAfter = cartRepo.save(cart);
		return new Cart(cartAfter.getId(), cartAfter.getTotalAmount(), cartAfter.getUser().getId());
	}

	
	public Cart get(int userId) {
		com.learncode.Entity.Cart cart = cartRepo.findByUserId(userId);
		if(cart != null) {
			return new Cart(cart.getId(), cart.getTotalAmount(), cart.getUser().getId());
		}
		return null;
	}

	public com.learncode.Entity.Cart save(com.learncode.Entity.Cart cart) {
		return cartRepo.save(cart);
	}

	
	public void remove(int userId) {
		List<com.learncode.Entity.CartItem> cartItems = cartItemRepo.findAll();
		for(com.learncode.Entity.CartItem item : cartItems) {
			if(item.getCart().getUser().getId() == userId) {
				cartItemRepo.delete(item);
			}
		}
		if(get(userId) != null)
			cartRepo.deleteById(get(userId).getId());
	}
	

	
	public void deleteByCartId(int id) {
		cartRepo.deleteById(id);
	}








	public com.learncode.Entity.Cart findByUserId(int userId) {
		return cartRepo.findByUserId(userId);
	}
}
