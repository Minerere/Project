package com.learncode.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {
	@Query("SELECT c FROM Cart c WHERE c.user.id=?1")
	Cart findByUserId(int userId);
	@Query("SELECT c FROM Cart c WHERE c.id=?1")
	Cart findOneId(int id);
	
}
