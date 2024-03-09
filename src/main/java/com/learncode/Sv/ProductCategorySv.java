package com.learncode.Sv;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learncode.Entity.ProductCategory;
import com.learncode.Repo.ProductCategoryRepo;

@Service
public class ProductCategorySv {
    
	@Autowired
	ProductCategoryRepo categoryRepository;
	
	public List<ProductCategory> findAll(){
		return categoryRepository.findAll();
	}
	public Optional<ProductCategory> findById(int id) {
		return categoryRepository.findById(id); 
	}
	
	

	public void save(ProductCategory cate) {
		categoryRepository.save(cate);
    }
	
	public void deleteById(int id) {
		categoryRepository.deleteById(id);
	}  
}
