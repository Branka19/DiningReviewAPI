package com.codecademy.diningreviewapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.codecademy.diningreviewapi.enums.ReviewStatus;
import com.codecademy.diningreviewapi.model.DiningReview;

public interface DiningReviewRepo extends CrudRepository<DiningReview, Integer>{

	List<DiningReview> findAll();
	Optional<DiningReview> findByUserNameAndId(String userName, Integer id);
	List<DiningReview> findByReviewStatus(ReviewStatus status);
	List<DiningReview> findAllByRestaurantId(Integer id);
	List<DiningReview> findAllByRestaurantIdAndReviewStatus(Integer id, ReviewStatus status);
	
	Integer countByReviewStatus(ReviewStatus status);
	
	Integer countByRestaurantIdAndReviewStatusAndPeanutScoreGreaterThan(Integer id, ReviewStatus status, Double value);
	Integer countByRestaurantIdAndReviewStatusAndEggScoreGreaterThan(Integer id, ReviewStatus status, Double value);
	Integer countByRestaurantIdAndReviewStatusAndDairyScoreGreaterThan(Integer id, ReviewStatus status, Double value);
	
	
}
