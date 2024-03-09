package com.learncode.Sv;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learncode.Entity.ProductBrand;
import com.learncode.Repo.ProductBrandRepo;

@Service
public class ProductBrandSv {
	@Autowired
	ProductBrandRepo BrandRepository;
	
	public Optional<ProductBrand> findById(int id) {
		return BrandRepository.findById(id); 
	}
	public List<ProductBrand> findAll() {
		return BrandRepository.findAll();
	}
	

	public void save(ProductBrand brand) {
		BrandRepository.save(brand);
    }
	
	public void deleteById(int id) {
		BrandRepository.deleteById(id);
	}  
}
