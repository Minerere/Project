package com.learncode.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.Product;
import com.learncode.Entity.ProductImage;

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImage, Integer> {
	@Query("SELECT p FROM ProductImage p WHERE p.product.id=?1")
	ProductImage findOneByProductId(int id);
	
	@Query("SELECT p FROM ProductImage p WHERE p.product.id=?1")
	List<ProductImage> findByProductId(int id);
	
	 List<ProductImage> findByProduct(Product product);
}
