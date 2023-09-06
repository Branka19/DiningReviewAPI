package com.codecademy.diningreviewapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.codecademy.diningreviewapi.model.RestaurantUser;


public interface UserRepo extends CrudRepository<RestaurantUser, Integer>{
	
	List<RestaurantUser> findAll();
	
	Optional<RestaurantUser> findByDisplayName(String displayName);
}
