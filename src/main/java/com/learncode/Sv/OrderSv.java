package com.learncode.Sv;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learncode.Entity.OrderDetail;
import com.learncode.Repo.CartItemRepo;
import com.learncode.Repo.CartRepo;
import com.learncode.Repo.OrderDetailRepo;
import com.learncode.Repo.OrderRepo;
import com.learncode.dto.Cart;
import com.learncode.dto.Order;

import jakarta.transaction.Transactional;

@Service
public class OrderSv {

	@Autowired
	private CartRepo cartRepo;
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private OrderDetailRepo orderDetailRepo;
	@Autowired
	private CartItemRepo cartItemRepo;
	@Autowired
	private ProductSv productSv;
	@Autowired
	DiscountCodeSv discountCodeSv; // bo sung khai bao lop service cua discountcode @Sang

	@Transactional
	public Order convert(Cart cart, Order orderDto) {
		com.learncode.Entity.Cart cartEntity = cartRepo.findByUserId(cart.getUser());
		com.learncode.Entity.Order order = new com.learncode.Entity.Order();
		order.setCreatedAt(java.util.Date.from(Instant.now()));
		order.setTotalAmount(cartEntity.getTotalAmount());
		/* bo sung su dung Discountcode @Sang */
		if (orderDto.getDiscountCode() != null && orderDto.getDiscountCode() != "") {
			order.setDiscountCode(discountCodeSv.findByCode(orderDto.getDiscountCode()));
			order.setTotalPaymentAmount(order.getTotalAmount() - order.getDiscountCode().getValue());
			if (order.getTotalPaymentAmount() < 0) {
				order.setTotalPaymentAmount(0);
			}
		} else {
			order.setTotalPaymentAmount(order.getTotalAmount());
		}
		/* end bo sung @Sang */
		order.setUpdatedAt(java.util.Date.from(Instant.now()));
		order.setUser(cartEntity.getUser());
		order.setDeliveryAddress(orderDto.getDeliveryAddress());
		order.setTel(orderDto.getTel());
		order.setStatus("Đang giao hàng");
		com.learncode.Entity.Order orderNew = orderRepo.save(order);
		List<com.learncode.Entity.CartItem> cartItems = cartItemRepo.findAllByCartId(cart.getId());
		for (com.learncode.Entity.CartItem item : cartItems) {
			com.learncode.Entity.OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrder(orderNew);
			orderDetail.setProduct(item.getProduct());
			orderDetail.setQuantity(item.getQuantity());
			orderDetail.setTotalAmount(item.getTotalAmount());
			orderDetailRepo.save(orderDetail);
			item.getProduct().setQuantity(item.getProduct().getQuantity() - item.getQuantity()); // moi them de tru hang
																									// trong kho
			productSv.save(item.getProduct());
			if (item.getProduct().getQuantity() <= 0) {
				cartItemRepo.deleteByProductId(item.getProduct().getId());
			} else {
				cartItemRepo.updateAll(item.getProduct().getId(), item.getProduct().getQuantity());
			}
			refresh();
		}
		Order dto = new Order();
		dto.setCreatedAt(orderNew.getCreatedAt());
		dto.setId(orderNew.getId());
		dto.setTotalAmount(orderNew.getTotalAmount());
		dto.setUpdatedAt(orderNew.getUpdatedAt());
		dto.setUser(orderNew.getUser().getId());
		return dto;
	}

	void refresh() {
		List<com.learncode.Entity.Cart> allCart = cartRepo.findAll();
		for (com.learncode.Entity.Cart c : allCart) {
			List<com.learncode.Entity.CartItem> cartItems2 = cartItemRepo.findAllByCartId(c.getId());
			int cartTotalAmount = 0;
			for (com.learncode.Entity.CartItem item2 : cartItems2) {
				int totalAmount = item2.getQuantity() * item2.getProduct().getPromotionalPrice();
				cartTotalAmount += totalAmount;
				item2.setTotalAmount(totalAmount);
				cartItemRepo.save(item2);
			}
			c.setTotalAmount(cartTotalAmount);
		}
	}

	public List<com.learncode.Entity.Order> findAll() {
		return orderRepo.findAll();
	}

	@Transactional
	public void delete(int id) {
		orderDetailRepo.deleteByOrderId(id);
		orderRepo.deleteById(id);
	}

	public com.learncode.Entity.Order findOneById(int id) {
		return orderRepo.findOneById(id);
	}

	public com.learncode.Entity.Order update(com.learncode.Entity.Order order, String status) {
		com.learncode.Entity.Order orderModel = orderRepo.findOneById(order.getId());
		if (orderModel != null) {
			orderModel.setCreatedAt(order.getCreatedAt());
			orderModel.setDeliveryAddress(order.getDeliveryAddress());
			orderModel.setDiscountCode(null);
			orderModel.setOrderDetails(order.getOrderDetails());
			orderModel.setPaid(order.isPaid());
			orderModel.setTel(order.getTel());
			orderModel.setTotalAmount(order.getTotalAmount());
			orderModel.setTotalPaymentAmount(order.getTotalPaymentAmount());
			orderModel.setUpdatedAt(order.getUpdatedAt());
			orderModel.setUser(order.getUser());
			orderModel.setStatus(status);
			orderRepo.save(orderModel);
		}
		return orderModel;
	}

	public List<com.learncode.Entity.Order> findOrderByUserId(int userId) {
		return orderRepo.findOrderByUserId(userId);
	}
}
