package com.learncode.Sv;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learncode.dto.CartItem;
import com.learncode.Entity.Cart;
import com.learncode.Repo.CartItemRepo;
import com.learncode.Repo.CartRepo;
import com.learncode.Repo.ProductRepo;
import com.learncode.Repo.ProductVariantRepo;
import com.learncode.Sv.CartItemSv;

@Service
public class CartItemSv {
	
	@Autowired
	private CartItemRepo cartItemRepo;
	@Autowired
	private CartRepo cartRepo;
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private ProductVariantRepo productVariantRepo;
	
	
	public CartItem add(CartItem item) {
		int totalAmount = item.getTotalAmount();
		Cart cart = cartRepo.findOneId(item.getCart());
		cart.setTotalAmount(cart.getTotalAmount() + totalAmount);
		cartRepo.save(cart);
		com.learncode.Entity.CartItem entity = new com.learncode.Entity.CartItem();
		entity.setCart(cartRepo.findOneId(item.getCart()));
		entity.setProduct(productRepo.findOneById(item.getProduct()));
		entity.setProductVariant(productVariantRepo.findOneById(item.getProductVariant()));
		entity.setQuantity(item.getQuantity());
		entity.setTotalAmount(item.getTotalAmount());
		com.learncode.Entity.CartItem entityNew = cartItemRepo.save(entity);
		return new CartItem
				(
						entityNew.getId(),
						entityNew.getQuantity(),
						entityNew.getTotalAmount(),
						entityNew.getCart().getId(),
						entityNew.getProduct().getId(),
						-1
				);
	}

	
	public CartItem update(CartItem item) {
		com.learncode.Entity.CartItem itemOld = cartItemRepo.findOneById(item.getId());
		int totalAmount = item.getTotalAmount();
		Cart cart = cartRepo.findOneId(item.getCart());
		cart.setTotalAmount(cart.getTotalAmount() - itemOld.getTotalAmount() + totalAmount);
		cartRepo.save(cart);
		com.learncode.Entity.CartItem entityNew = cartItemRepo.save(
				new com.learncode.Entity.CartItem
				(
						item.getId(),
						item.getQuantity(),
						item.getTotalAmount(),
						cartRepo.findOneId(item.getCart()),
						productRepo.findOneById(item.getProduct()),
						null
				));
		return new CartItem
				(
						entityNew.getId(),
						entityNew.getQuantity(),
						entityNew.getTotalAmount(),
						entityNew.getCart().getId(),
						entityNew.getProduct().getId(),
						-1
				);
	}

	public void remove(CartItem item) {
		int totalAmount = item.getTotalAmount();
		Cart cart = cartRepo.findOneId(item.getCart());
		cart.setTotalAmount(cart.getTotalAmount() - totalAmount);
		cartRepo.save(cart);
		cartItemRepo.deleteById(item.getId());
	}

	public List<CartItem> list(int cartId) {
		List<com.learncode.Entity.CartItem> cartItems = cartItemRepo.findAllByCartId(cartId);
		if(cartItems.size() == 0)
			return null;
		List<CartItem> dtos = new ArrayList<CartItem>();
		for(com.learncode.Entity.CartItem item : cartItems) {
			dtos.add(new CartItem
					(
							item.getId(),
							item.getQuantity(),
							item.getTotalAmount(),
							item.getCart().getId(),
							item.getProduct().getId(),
							-1
					));
		}
		return dtos;
	}

	
	public CartItem get(int cartId, int product_id) {
		com.learncode.Entity.CartItem cartItem = cartItemRepo.findByCartIdAndProductId(cartId, product_id);
		if(cartItem != null) {
			return new CartItem(cartItem.getId(), 
					cartItem.getQuantity(), 
					cartItem.getTotalAmount(), 
					cartItem.getCart().getId(), 
					cartItem.getProduct().getId(), 
					-1);
		}
		return null;
	}

	
	public List<com.learncode.Entity.CartItem> findAllByCartId(int cartId) {
		return cartItemRepo.findAllByCartId(cartId);
	}
	
	public void deleteById(int id) {
		
		cartItemRepo.deleteById(id);
	}

	public com.learncode.Entity.CartItem findById(int id) {
		return cartItemRepo.findById(id).orElse(null);
	}
}
