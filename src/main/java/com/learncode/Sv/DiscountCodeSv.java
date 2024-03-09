package com.learncode.Sv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learncode.Entity.DiscountCode;
import com.learncode.Repo.DiscountCodeRepo;

@Service
public class DiscountCodeSv {
    @Autowired
	private DiscountCodeRepo discountCodeRepo;

    public List<DiscountCode> findAll() {
        return discountCodeRepo.findAll();
    }

    public DiscountCode findById(int id) {
        return discountCodeRepo.findById(id).orElse(null);
    }

    public DiscountCode findByCode(String code) {
        return discountCodeRepo.findByCode(code);
    }

    public DiscountCode save(DiscountCode discountCode) {
        return discountCodeRepo.save(discountCode);
    }

    public void deleteById(int id) {
        discountCodeRepo.deleteById(id);
    }
    
}
