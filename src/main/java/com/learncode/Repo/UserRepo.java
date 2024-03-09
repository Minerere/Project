package com.learncode.Repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learncode.Entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
	@Query("SELECT o FROM User o WHERE o.email = :e")
	public User findByEmail(@Param(value = "e") String Email);
	
	@Query("SELECT c FROM User c WHERE c.id=?1")
	User findOneById(int id);
}
