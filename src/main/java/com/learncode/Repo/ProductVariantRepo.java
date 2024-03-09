package com.learncode.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.ProductVariant;

@Repository
public interface ProductVariantRepo extends JpaRepository<ProductVariant, Integer> {
	@Query("SELECT c FROM ProductVariant c WHERE c.id=?1")
	ProductVariant findOneById(Integer id);
}
