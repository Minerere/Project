package com.learncode.Sv;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learncode.Entity.OrderDetail;
import com.learncode.Repo.OrderDetailRepo;
import com.learncode.Sv.OrderDetailSv;

@Service
public class OrderDetailSv {
	@Autowired
	private OrderDetailRepo orderDetailRepo;

	
	public void deleteByOrderId(int id) {
		orderDetailRepo.deleteByOrderId(id);
	}

	
	public List<OrderDetail> findByOrderId(int id) {
		return orderDetailRepo.findByOrderId(id);
	}
	
	public Integer countProductByOrderId(int orderId) {
		return orderDetailRepo.getTotalQuantityByOrderId(orderId);
	}
}
