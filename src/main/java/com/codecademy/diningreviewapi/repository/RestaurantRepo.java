package com.codecademy.diningreviewapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.codecademy.diningreviewapi.model.Restaurant;

public interface RestaurantRepo extends CrudRepository<Restaurant, Integer> {

	List<Restaurant> findAll();
	List<Restaurant> findByRestaurantName(String name);
	Optional<Restaurant> findById(Integer id);
 
	Optional<Restaurant> findByRestaurantNameAndRestaurantZipCode(String restaurantName, String zipCode);
	Optional<Restaurant> findByRestaurantNameAndRestaurantLocation(String restaurantName, String location);
	
	List<Restaurant> findAllByRestaurantZipCode(String zipCode);
	
	
	List<Restaurant> findByRestaurantZipCodeAndScorePeanutNotNullOrderByRestaurantNameDesc(String zipCode);
	List<Restaurant> findByRestaurantZipCodeAndScoreEggsNotNullOrderByRestaurantNameDesc(String zipCode);
	List<Restaurant> findByRestaurantZipCodeAndScoreDairyNotNullOrderByRestaurantNameDesc(String zipCode);

	
	


}