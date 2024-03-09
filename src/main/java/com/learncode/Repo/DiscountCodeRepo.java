package com.learncode.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.DiscountCode;

@Repository
public interface DiscountCodeRepo extends JpaRepository<DiscountCode, Integer>{
    @Query("SELECT o FROM DiscountCode o WHERE o.code = :code")
    public DiscountCode findByCode(@Param(value = "code") String code);
}
