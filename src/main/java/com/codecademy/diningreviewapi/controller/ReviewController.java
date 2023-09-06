package com.codecademy.diningreviewapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.codecademy.diningreviewapi.enums.ReviewStatus;
import com.codecademy.diningreviewapi.model.DiningReview;
import com.codecademy.diningreviewapi.model.Restaurant;
import com.codecademy.diningreviewapi.repository.DiningReviewRepo;
import com.codecademy.diningreviewapi.repository.RestaurantRepo;

@RequestMapping("/reviews")
@RestController
public class ReviewController {
	
	private final RestaurantRepo restaurantRepo;
	private final DiningReviewRepo diningReviewRepo;
	
	public ReviewController
			(
			final RestaurantRepo restaurantRepo,
			final DiningReviewRepo dinningReviewRepo
			)
		{
		this.restaurantRepo = restaurantRepo;
		this.diningReviewRepo = dinningReviewRepo;
		}
	
	//anyone can get a list of all approved reviews
	@GetMapping("/getAll")
	public List<DiningReview> getAllApprovedReviews()
	{ 
		return diningReviewRepo.findByReviewStatus(ReviewStatus.APPROVED);
	}
	
	// fetch all approved dining reviews for a restaurant
	@GetMapping("/getByRestaurantId/{restaurantId}")
	public List<DiningReview> getApprovedRestaurantReviews(@PathVariable Integer restaurantId){
		
		List<DiningReview> approvedReviews = new ArrayList<>();
		
		validateRestaurantById(restaurantId);
		approvedReviews = diningReviewRepo.findAllByRestaurantIdAndReviewStatus(restaurantId, ReviewStatus.APPROVED);
		return approvedReviews;
	}
	
	private Restaurant validateRestaurantById(Integer restaurantId)
	{
		Optional<Restaurant> restaurantOptional = restaurantRepo.findById(restaurantId);
		if(!restaurantOptional.isPresent())
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No restaurant with such id.");
		}
		
		return restaurantOptional.get();
	}

}
