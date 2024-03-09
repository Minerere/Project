package com.learncode.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.CartItem;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {
	@Query("SELECT c FROM CartItem c WHERE c.cart.id=?1 AND c.product.id=?2")
	CartItem findByCartIdAndProductId(int cartId, int productId);
	@Query("SELECT c FROM CartItem c WHERE c.cart.id=?1")
	List<CartItem> findAllByCartId(int cartId);
	@Query("SELECT c FROM CartItem c WHERE c.id=?1")
	CartItem findOneById(int id);
	@Modifying
	@Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.product.id = :productId")
    void updateAll(@Param("productId") int product_id, @Param("quantity") int quantity);
	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.product.id = :productId")
    void deleteByProductId(@Param("productId") int product_id);
}
