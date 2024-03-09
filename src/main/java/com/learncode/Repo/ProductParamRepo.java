package com.learncode.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.ProductParam;

@Repository
public interface ProductParamRepo extends JpaRepository<ProductParam, Integer> {
    
}
