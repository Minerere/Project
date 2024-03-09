package com.learncode.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.ProductBrand;

@Repository
public interface ProductBrandRepo extends JpaRepository<ProductBrand, Integer> {
    
}
