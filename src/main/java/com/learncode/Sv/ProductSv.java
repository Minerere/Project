package com.learncode.Sv;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.learncode.Entity.Product;
import com.learncode.Repo.PinnedProductRepo;
import com.learncode.Repo.ProductBrandRepo;
import com.learncode.Repo.ProductCategoryRepo;
import com.learncode.Repo.ProductCommentRepo;
import com.learncode.Repo.ProductImageRepo;
import com.learncode.Repo.ProductParamRepo;
import com.learncode.Repo.ProductRepo;
import com.learncode.Repo.ProductVariantRepo;

@Service
public class ProductSv {
    @Autowired ProductRepo productRepo;
    @Autowired ProductBrandRepo productBrandRepo;
    @Autowired ProductCategoryRepo productCategoryRepo;
    @Autowired ProductCommentRepo productCommentRepo;
    @Autowired ProductParamRepo productParamRepo;
    @Autowired ProductVariantRepo productVariantRepo;
    @Autowired PinnedProductRepo pinnedProductRepo;
    @Autowired ProductImageRepo productImageRepo;
    
    public Page<Product> findAll(Pageable pageable){
    	return productRepo.findAll(pageable);
    }
    
    public List<Product> findAll() {
        return productRepo.findAll();
    }
    
    public Optional<Product> findById(int id) {
    	return productRepo.findById(id);
    }
    
    public void deleteById(int id) {
    	 productRepo.deleteById(id);
    }
    
    public void save(Product product) {
    	productRepo.save(product);
    }
    
    public Product findOne(Integer id) {
    	return productRepo.findOneById(id);
    }
    
}