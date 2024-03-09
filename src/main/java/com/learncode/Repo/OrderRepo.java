package com.learncode.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
	Order findOneById(int id);
	
	@Query("SELECT o FROM Order o WHERE o.user.id= :userId ORDER BY o.id DESC")
	List<Order> findOrderByUserId(@Param("userId") int userId);
}
