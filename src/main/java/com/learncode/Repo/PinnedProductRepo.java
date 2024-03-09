package com.learncode.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.PinnedProduct;

@Repository
public interface PinnedProductRepo extends JpaRepository<PinnedProduct, Integer>{

    @Query("SELECT o FROM PinnedProduct o WHERE o.product.id =:pId")
    public PinnedProduct findByProductId(@Param("pId") int pId);
    boolean existsByProductId(int productId);
}
