package com.learncode.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
	Product findOneById(Integer id);
	Page<Product> findAll(Pageable pageable);
}
