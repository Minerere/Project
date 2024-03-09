package com.learncode.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.OrderDetail;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail, Integer> {
	@Modifying
	@Query("DELETE FROM OrderDetail o WHERE o.order.id = :orderId")
	void deleteByOrderId(@Param("orderId") int orderId);
	
	
	@Query("SELECT o FROM OrderDetail o WHERE o.order.id = :orderId")
	List<OrderDetail> findByOrderId(@Param("orderId") int orderId);
	
	@Query("SELECT COUNT(*) FROM OrderDetail o WHERE o.order.id = :orderId")
	Integer countProductByOrderId(@Param("orderId") int orderId);
	
	@Query("SELECT SUM(o.quantity) FROM OrderDetail o WHERE o.order.id = :orderId")
	Integer getTotalQuantityByOrderId(@Param("orderId") int orderId);
}
