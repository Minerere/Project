package com.learncode.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.ProductComment;

@Repository
public interface ProductCommentRepo extends JpaRepository<ProductComment, Integer> {
    
}
