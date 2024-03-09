package com.learncode.Sv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learncode.Entity.PinnedProduct;
import com.learncode.Repo.PinnedProductRepo;

@Service
public class PinnedProductSv {
    @Autowired
    private PinnedProductRepo pinnedProductRepo;
    
    public PinnedProductSv(PinnedProductRepo pinnedProductRepo) {
        this.pinnedProductRepo = pinnedProductRepo;
    }

    
    public List<PinnedProduct> findAll() {
        return pinnedProductRepo.findAll();
    }
    public PinnedProduct save(PinnedProduct pinnedProduct) {
        return pinnedProductRepo.save(pinnedProduct);
    }
    public void deleteByProductId(int productId){
        pinnedProductRepo.delete(pinnedProductRepo.findByProductId(productId));
    }
    
    public boolean existsByProductId(int productId) {
    	return pinnedProductRepo.existsByProductId(productId);
    }
}
