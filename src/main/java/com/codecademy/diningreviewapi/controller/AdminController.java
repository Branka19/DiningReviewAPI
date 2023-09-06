package com.codecademy.diningreviewapi.controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.codecademy.diningreviewapi.enums.ReviewStatus;
import com.codecademy.diningreviewapi.model.AdminReviewAction;
import com.codecademy.diningreviewapi.model.DiningReview;
import com.codecademy.diningreviewapi.model.Restaurant;
import com.codecademy.diningreviewapi.model.RestaurantUser;
import com.codecademy.diningreviewapi.repository.DiningReviewRepo;
import com.codecademy.diningreviewapi.repository.RestaurantRepo;
import com.codecademy.diningreviewapi.repository.UserRepo;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	private final UserRepo userRepo;
	private final RestaurantRepo restaurantRepo;
	private final DiningReviewRepo diningReviewRepo;
	
	
	public AdminController(final UserRepo userRepo, final RestaurantRepo restaurantRepo,
			final DiningReviewRepo diningReviewRepo) {
		this.userRepo = userRepo;
		this.restaurantRepo = restaurantRepo;
		this.diningReviewRepo = diningReviewRepo;
	}
	
	// get a list of users
	@GetMapping("users/getAll")
	public List<RestaurantUser> getAllUsers() {
		return userRepo.findAll();
	}
	
	// get a list of all pending reviews
	@GetMapping("/reviews/getPending")
	public List<DiningReview> getPendingReviews() {
		
		List<DiningReview> pendingReviews = diningReviewRepo.findByReviewStatus(ReviewStatus.PENDING);

		if(pendingReviews == null || pendingReviews.isEmpty())
			System.out.println("The list is empty.");;
		
		return pendingReviews;
	}
	
	// approve or reject a review + recompute the latest scores
	@PutMapping("/reviews/{id}")
	public DiningReview approveReview(@PathVariable("id") Integer reviewId, @RequestBody AdminReviewAction action) {

		DiningReview reviewToUpdate = validateReview(reviewId);
		
		if(action.getAcceptTheReview())
			reviewToUpdate.setReviewStatus(ReviewStatus.APPROVED);
		else
			reviewToUpdate.setReviewStatus(ReviewStatus.REJECTED);
		
		DiningReview updatedReview = this.diningReviewRepo.save(reviewToUpdate);	
		
		Restaurant restaurant = validateRestaurant(updatedReview.getRestaurantId());
		updateScores(restaurant);
		
		return updatedReview;
	}
	//approve all reviews at once
	@PutMapping("/reviews/approveAll")
	public void approveAllReviews() {

		List<DiningReview> reviewList = diningReviewRepo.findAll();
		
		for(DiningReview review : reviewList)
		{
			review.setReviewStatus(ReviewStatus.APPROVED);
			diningReviewRepo.save(review);
			Restaurant restaurant = validateRestaurant(review.getRestaurantId());
			updateScores(restaurant);
		}
	}
	
	private DiningReview validateReview(Integer reviewId)
	{
		Optional<DiningReview> reviewToUpdateOptional = diningReviewRepo.findById(reviewId);
		if (!reviewToUpdateOptional.isPresent())
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No review with the given id.");
		}

		return reviewToUpdateOptional.get();
	}
	
	//restaurant validation
	private Restaurant validateRestaurant(Integer restaurantId)
	{
		Optional<Restaurant> restaurantOptional = restaurantRepo.findById(restaurantId);
		if(!restaurantOptional.isPresent())
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No restaurant with the given id.");
		}
		
		return restaurantOptional.get();
	}

	
		// updates scores for a specific restaurant
		private void updateScores(Restaurant restaurant)
		{
			Integer restaurantId = restaurant.getId();
			List<DiningReview> restaurantReviews = diningReviewRepo.findAllByRestaurantIdAndReviewStatus(restaurantId, ReviewStatus.APPROVED);
			
			int peanutCount = 0;
			int eggCount = 0;
			int dairyCount = 0;
			
			int peanutSum = 0;
			int eggSum = 0;
			int dairySum = 0;
			
			for(DiningReview review : restaurantReviews)
			{				
				if(review.getPeanutScore() != null)
				{
					peanutSum += review.getPeanutScore();
					peanutCount++;
				}
				
				if(review.getEggScore() != null)
				{
					eggSum += review.getEggScore();
					eggCount++;
				}
				
				if(review.getDairyScore() != null)
				{
					dairySum += review.getDairyScore();
					dairyCount++;
				}
			}
			
			int totalSum = peanutSum + eggSum + dairySum;
			int totalCount = peanutCount + eggCount + dairyCount;
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			if(peanutCount > 0)
			{
				Double peanutAvg = Double.valueOf((double)peanutSum / peanutCount);
				restaurant.setScorePeanut(df.format(peanutAvg));
			}
			
			if(eggCount > 0)
			{
				Double eggAvg = Double.valueOf((double)eggSum / eggCount);
				restaurant.setScoreEggs(df.format(eggAvg));
			}
			
			if(dairyCount > 0)
			{
				Double dairyAvg = Double.valueOf((double)dairySum / dairyCount);
				restaurant.setScoreDairy(df.format(dairyAvg));
			}
			
			Double overallScore = Double.valueOf((double)totalSum / totalCount);
			restaurant.setOverallScore(df.format(overallScore));
			
			this.restaurantRepo.save(restaurant);
				
		}
		
		//counts all approved reviews
		@GetMapping("/reviews/approvedCount")
		public Integer countAllApprovedReviews()
		{ 
			return diningReviewRepo.countByReviewStatus(ReviewStatus.APPROVED);
		}
		
		
		// find a review based on id
		@GetMapping("/reviews/{id}")
		public DiningReview getReviewById(@PathVariable String id) {
			
			boolean isNumeric = id.chars().allMatch( Character::isDigit );
			if(!isNumeric)
			{
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You entered an invalid review id.");
			}
			
			Integer reviewID = Integer.parseInt(id);
			Optional<DiningReview> reviewToFindOptional = this.diningReviewRepo.findById(reviewID);
			
			if(!reviewToFindOptional.isPresent())
				{
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A review with the given id is not found.");
				
				}

			DiningReview review = reviewToFindOptional.get();
			return review;
		}
}
