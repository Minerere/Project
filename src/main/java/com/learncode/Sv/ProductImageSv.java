package com.learncode.Sv;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learncode.Entity.Product;
import com.learncode.Entity.ProductImage;
import com.learncode.Repo.ProductImageRepo;
import com.learncode.Sv.ProductImageSv;

@Service
public class ProductImageSv {
	@Autowired
	private ProductImageRepo productImageRepo;

	
	public ProductImage findOneByProductId(int id) {
		return productImageRepo.findOneByProductId(id);
	}

	 public List<ProductImage> findByProduct(Product product) {
	        return productImageRepo.findByProduct(product);
	    }

	
	public List<ProductImage> findByProductId(int id) {
		return productImageRepo.findByProductId(id);
	}
	
	public void save(ProductImage productImage) {
		productImageRepo.save(productImage); 
	}
}
