package com.codecademy.diningreviewapi.controller;

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

import com.codecademy.diningreviewapi.enums.ReviewStatus;
import com.codecademy.diningreviewapi.model.DiningReview;
import com.codecademy.diningreviewapi.model.Restaurant;
import com.codecademy.diningreviewapi.model.RestaurantUser;
import com.codecademy.diningreviewapi.repository.DiningReviewRepo;
import com.codecademy.diningreviewapi.repository.RestaurantRepo;
import com.codecademy.diningreviewapi.repository.UserRepo;

@RequestMapping("/users")
@RestController
public class UserController {
	
	private final UserRepo userRepo;
	private final RestaurantRepo restaurantRepo;
	private final DiningReviewRepo diningReviewRepo;
	
	public UserController(final UserRepo userRepo, final RestaurantRepo restaurantRepo,
			final DiningReviewRepo diningReviewRepo) {
		this.userRepo = userRepo;
		this.restaurantRepo = restaurantRepo;
		this.diningReviewRepo = diningReviewRepo;
	}
	
	//fetch the user profile belonging to a given display name
	@GetMapping("/{displayName}")
	public RestaurantUser getUserByDisplayName(@PathVariable String displayName)
	{
		
		Optional<RestaurantUser> userToFindOptional = this.userRepo.findByDisplayName(displayName);
		 
		 if(!userToFindOptional.isPresent()) 
		 { 
			 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with the given user name is not found.");
		 }
		 
		 RestaurantUser user = userToFindOptional.get();
		 return user;
	 
	}
	
	//As an unregistered user, create a user profile using a display name that’s unique
	@PostMapping("/addNew")
	public RestaurantUser createNewUser(@RequestBody RestaurantUser user) {
		
		if (userRepo.findByDisplayName(user.getDisplayName()).isPresent())
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user with this username already exists");
		}
		RestaurantUser newUser = this.userRepo.save(user);

		return newUser;
	}
	
	//As a registered user, I want to update my user profile. I cannot modify my unique display name.
	@PutMapping("/{displayName}")
	public RestaurantUser updateUser(
			@PathVariable String displayName,
			@RequestBody RestaurantUser user) {
		Optional<RestaurantUser> userToUpdateOptional = this.userRepo.findByDisplayName(displayName);
		if (!userToUpdateOptional.isPresent()) 
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with this name is not found.");
		}
		
		RestaurantUser userToUpdate = userToUpdateOptional.get();

		if(user.getDisplayName() != null)
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You cannot change your display name.");
		}
		
		if (user.getCity() != null) {
			userToUpdate.setCity(user.getCity());
		}
		if (user.getZipCode() != null) {
			userToUpdate.setZipCode(user.getZipCode());
		}
		if (user.getInterestedInDairyAllergies() != null) {
			userToUpdate.setInterestedInDairyAllergies(user.getInterestedInDairyAllergies());
		}

		if (user.getInterestedInEggAllergies() != null) {
			userToUpdate.setInterestedInEggAllergies(user.getInterestedInEggAllergies());
		}

		if (user.getInterestedInPeanutAllergies() != null) {
			userToUpdate.setInterestedInPeanutAllergies(user.getInterestedInPeanutAllergies());
		}

		RestaurantUser updatedUser = this.userRepo.save(userToUpdate);
		return updatedUser;
	}

	//registered user creates a new review
	@PostMapping("/reviews/addNew")
	public DiningReview createNewReview(
			@RequestParam String userName,
			@RequestParam String restaurantName,
			@RequestParam String zipCode,
			@RequestParam(required = false) String peanutScore,
			@RequestParam(required = false) String eggScore,
			@RequestParam(required = false) String dairyScore,
			@RequestParam(required = false) String comment) {

		if (!userRepo.findByDisplayName(userName).isPresent())
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You have to register to submit a review!");
		}

		Optional<Restaurant> restaurantToFindOptional = restaurantRepo.findByRestaurantNameAndRestaurantZipCode(restaurantName, zipCode);
		
		if(!restaurantToFindOptional.isPresent())
			{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given restaurant is not found.");
			}
		
		Integer peanuts = getScore(peanutScore);
		Integer eggs = getScore(eggScore);
		Integer dairy = getScore(dairyScore);

		Integer restaurantId = restaurantToFindOptional.get().getId();
		
		DiningReview newReview = new DiningReview(userName, restaurantId, peanuts, eggs, dairy, comment, ReviewStatus.PENDING);
		return this.diningReviewRepo.save(newReview);
	}
		
		private Integer getScore(String scoreString)
		{
			Integer score = null;
			
			if(!scoreString.isEmpty()){
				boolean isNumeric = scoreString.chars().allMatch( Character::isDigit );
				
				if(!isNumeric)
				{
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You entered an invalid score.");
				}
				
				score = Integer.parseInt(scoreString);
				
				if(score < 1 || score > 5)
				{
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your score should be between 1-5.");
				}
			}
			
			return score;
		}

	// user updates an existing review
	@PutMapping("/{userName}/reviews/{reviewId}")
	public DiningReview updateReview(
			@PathVariable String userName,
			@PathVariable Integer reviewId,
			@RequestBody DiningReview newReview)
		{
		
		Optional<DiningReview> reviewToUpdateOptional = this.diningReviewRepo.findByUserNameAndId(userName, reviewId);
		if (!reviewToUpdateOptional.isPresent())
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A review with this id is not found.");
		}
		
		//old version of the review
		DiningReview reviewToUpdate = reviewToUpdateOptional.get();
		
		
		if (newReview.getCommentary() != null) {
			reviewToUpdate.setCommentary(newReview.getCommentary());
		}
		if (newReview.getEggScore() != null) {
			reviewToUpdate.setEggScore(newReview.getEggScore());
		}
		if (newReview.getDairyScore() != null) {
			reviewToUpdate.setDairyScore(newReview.getDairyScore());
		}

		if (newReview.getPeanutScore() != null) {
			reviewToUpdate.setPeanutScore(newReview.getPeanutScore());
		}
		
		reviewToUpdate.setReviewStatus(ReviewStatus.PENDING);

		DiningReview updatedReview = this.diningReviewRepo.save(reviewToUpdate);
		return updatedReview;
	}
	
}
