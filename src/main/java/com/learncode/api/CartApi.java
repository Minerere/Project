package com.learncode.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learncode.Entity.Product;
import com.learncode.Entity.User;
import com.learncode.Sv.CartItemSv;
import com.learncode.Sv.CartSv;
import com.learncode.Sv.ProductSv;
import com.learncode.Sv.UserSv;
import com.learncode.Utils.SessionUtil;
import com.learncode.dto.Cart;
import com.learncode.dto.CartItem;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cart")
public class CartApi {
	@Autowired
	private CartSv cartSv;
	@Autowired
	private CartItemSv cartItemSv;
	@Autowired
	private ProductSv productSv;
	@Autowired UserSv userSv;
	@Autowired HttpSession httpSession;
	
	@PostMapping("")
	public ResponseEntity<CartItem> addToCart(CartItem item, HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if(user == null) {
			SessionUtil.refreshSession(httpSession, userSv);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		Product product = productSv.findById(item.getProduct()).orElse(null);
		CartItem cItem = user.getCart() !=null ? cartItemSv.get(user.getCart().getId(), item.getProduct()) : null;
		int quantityOld = cItem != null ? cItem.getQuantity() : 0;
		if(product == null || (product.getQuantity() < (item.getQuantity() + quantityOld))) {
			SessionUtil.refreshSession(httpSession, userSv);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Cart cart;
		if(cartSv.get(user.getId()) == null) {
			cart = cartSv.create(user.getId());
		}else {
			cart = cartSv.get(user.getId());
		}
		CartItem cartItem;
		if(cartItemSv.get(cart.getId(), item.getProduct()) == null) {
			cartItem = new CartItem();
			cartItem.setCart(cart.getId());
			cartItem.setProduct(item.getProduct());
			cartItem.setQuantity(item.getQuantity());
			cartItem.setTotalAmount((productSv.findOne(item.getProduct()).getPromotionalPrice() > 0 ? productSv.findOne(item.getProduct()).getPromotionalPrice() : productSv.findOne(item.getProduct()).getPrice()) * cartItem.getQuantity());
			SessionUtil.refreshSession(httpSession, userSv);
			return new ResponseEntity<CartItem>(cartItemSv.add(cartItem), HttpStatus.OK);
		}else {
			cartItem = cartItemSv.get(cart.getId(), item.getProduct());
			cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
			cartItem.setTotalAmount((productSv.findOne(item.getProduct()).getPromotionalPrice() > 0 ? productSv.findOne(item.getProduct()).getPromotionalPrice() : productSv.findOne(item.getProduct()).getPrice()) * cartItem.getQuantity());
			SessionUtil.refreshSession(httpSession, userSv);
			return new ResponseEntity<CartItem>(cartItemSv.update(cartItem), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAll(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("user") == null) {
			SessionUtil.refreshSession(httpSession, userSv);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		User user = ((User)session.getAttribute("user"));
		cartSv.remove(user.getId());
		SessionUtil.refreshSession(httpSession, userSv);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/delete-by-id")
	public ResponseEntity<?> deleteById(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("user") == null) {
			SessionUtil.refreshSession(httpSession, userSv);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		User user = ((User)session.getAttribute("user"));
		cartSv.remove(user.getId());
		user.getCart().setTotalAmount(0);
		cartSv.save(user.getCart());
		SessionUtil.refreshSession(httpSession, userSv);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	
	@PutMapping("")
	public ResponseEntity<Cart> updateCart(CartItem item, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("user") == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		User user = ((User)session.getAttribute("user"));
		Cart cart;
		if(cartSv.get(user.getId()) == null) {
			cart = cartSv.create(user.getId());
		}else {
			cart = cartSv.get(user.getId());
		}
		CartItem cartItem;
		if(cartItemSv.get(cart.getId(), item.getProduct()) == null) {
			cartItem = new CartItem();
			cartItem.setCart(cart.getId());
			cartItem.setProduct(item.getProduct());
			cartItem.setQuantity(item.getQuantity());
			cartItem.setTotalAmount((productSv.findOne(item.getProduct()).getPromotionalPrice() > 0 ? productSv.findOne(item.getProduct()).getPromotionalPrice() : productSv.findOne(item.getProduct()).getPrice()) * cartItem.getQuantity());
			cartItemSv.add(cartItem);
			SessionUtil.refreshSession(httpSession, userSv);
			return new ResponseEntity<Cart>(cartSv.get(user.getId()), HttpStatus.OK);
		}else {
			cartItem = cartItemSv.get(cart.getId(), item.getProduct());
			cartItem.setQuantity(item.getQuantity());
			cartItem.setTotalAmount((productSv.findOne(item.getProduct()).getPromotionalPrice() > 0 ? productSv.findOne(item.getProduct()).getPromotionalPrice() : productSv.findOne(item.getProduct()).getPrice()) * cartItem.getQuantity());
			cartItemSv.update(cartItem);
			SessionUtil.refreshSession(httpSession, userSv);
			return new ResponseEntity<Cart>(cartSv.get(user.getId()), HttpStatus.OK);
		}
	}
}
