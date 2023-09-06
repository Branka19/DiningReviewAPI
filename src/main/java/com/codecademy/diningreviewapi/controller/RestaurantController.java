package com.codecademy.diningreviewapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.codecademy.diningreviewapi.model.Restaurant;
import com.codecademy.diningreviewapi.repository.RestaurantRepo;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
	
	private final RestaurantRepo restaurantRepo;
	
	public RestaurantController(final RestaurantRepo restaurantRepo) {
		this.restaurantRepo = restaurantRepo;
	}
	
	// get all restaurants
	@GetMapping("/getAll")
	public List<Restaurant> getAllRestaurants() {
		return restaurantRepo.findAll();
	}
	

	// fetch the details of a restaurant, given its unique Id.
	@GetMapping("/{id}")
	public Restaurant getResturantById(@PathVariable String id)
		{
		
		boolean isNumeric = id.chars().allMatch( Character::isDigit );
		if(!isNumeric)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You entered an invalid restaurant id.");
		}
		
		Integer restaurantID = Integer.parseInt(id);
		Optional<Restaurant> restaurantToFindOptional = this.restaurantRepo.findById(restaurantID);
		
		if(!restaurantToFindOptional.isPresent())
			{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A restaurant with the given id is not found.");
			}

		Restaurant restaurant = restaurantToFindOptional.get();
		return restaurant;
	}
	
	// submit a new restaurant entry with a unique name and zip code   
	@PostMapping("/addNew")
	public Restaurant createNewRestaurant(@RequestBody Restaurant restaurant) {

		//German zip-code
		String deZipRegex = "\\b[0124678][0-9]{4}\\b(?!\\s?[ \\/-]\\s?[0-9]+)";
		
		if (restaurantRepo
				.findByRestaurantNameAndRestaurantZipCode(restaurant.getRestaurantName(), restaurant.getRestaurantZipCode())
				.isPresent())
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A restaurant with this name and zip code already exists.");
		}
		
		else if(!restaurant.getRestaurantZipCode().matches(deZipRegex))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The zip code is not valid.");
		}
		
		return this.restaurantRepo.save(restaurant);

	}
	
	// update a restaurant
	@PutMapping("/{restaurantName}/{zip}")
	public Restaurant updateRestaurant(
			@PathVariable String restaurantName,
			@PathVariable String zip,
			@RequestBody Restaurant restaurant) {
		Optional<Restaurant> restaurantOptional = this.restaurantRepo.findByRestaurantNameAndRestaurantZipCode(restaurantName, zip);
		if (!restaurantOptional.isPresent())
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no restaurant with the given name.");
		}
		
		Restaurant restaurantToUpdate = restaurantOptional.get();

		if (restaurant.getRestaurantName() != null) {
			restaurantToUpdate.setRestaurantName(restaurant.getRestaurantName());
		}
		if (restaurant.getRestaurantLocation() != null) {
			restaurantToUpdate.setRestaurantLocation(restaurant.getRestaurantLocation());
		}
		
		if (restaurant.getRestaurantZipCode() != null) {
			restaurantToUpdate.setRestaurantZipCode(restaurant.getRestaurantZipCode());
		}

		Restaurant updatedRestaurant = this.restaurantRepo.save(restaurantToUpdate);
		return updatedRestaurant;
	}
	
	/* fetch restaurants that match a given zip code and 
	 * have at least one user-submitted score for a given allergy in descending order
	 */
    @GetMapping("/search")
	public List<Restaurant> getAllRestaurantsByZipAndAllergy(
			@RequestParam(name="zip") String zip, 
			@RequestParam(name="allergy") String allergy)
	{
		List<Restaurant> restaurants = null;
		
		switch (allergy.toUpperCase()) {
		case "PEANUTS":
			
			restaurants = restaurantRepo.findByRestaurantZipCodeAndScorePeanutNotNullOrderByRestaurantNameDesc(zip);
			break;
					
		case "EGGS":
			restaurants = restaurantRepo.findByRestaurantZipCodeAndScoreEggsNotNullOrderByRestaurantNameDesc(zip);
			break;
			
		case "DAIRY":
			restaurants = restaurantRepo.findByRestaurantZipCodeAndScoreDairyNotNullOrderByRestaurantNameDesc(zip);
			break;				
								
		default:
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given allergy doesn't exist: " + allergy);
		}
		
		return restaurants;
	}
    
	// search restaurants by zip-code
	@GetMapping("/search/{zipCode}")

	public List<Restaurant> getRestaurantByZipCode(@PathVariable String zipCode) {
		
		String deZipRegex = "\\b[0124678][0-9]{4}\\b(?!\\s?[ \\/-]\\s?[0-9]+)";
		
		if(!zipCode.matches(deZipRegex))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The zip code is not valid.");
		}
		return this.restaurantRepo.findAllByRestaurantZipCode(zipCode);
	}

}
