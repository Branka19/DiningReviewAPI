package com.codecademy.diningreviewapi.model;

import com.codecademy.diningreviewapi.enums.ReviewStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class DiningReview {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="review_generator")
	@SequenceGenerator(name="review_generator", sequenceName="REVIEW_SEQUENCE", allocationSize=1)
	private Integer id;
	
	@Column
	private String userName;
	
	@Column
	private Integer restaurantId;

	@Column
	private Integer peanutScore;
	
	@Column
	private Integer eggScore;
	
	@Column
	private Integer dairyScore;
	
	@Column
	private String commentary;
	
	@Column
	@Enumerated(EnumType.STRING)
	private ReviewStatus reviewStatus;

	public DiningReview(String userName, Integer restaurantId, Integer peanutScore, Integer eggScore,
			Integer dairyScore, String commentary, ReviewStatus reviewStatus) {
		this.userName = userName;
		this.restaurantId = restaurantId;
		this.peanutScore = peanutScore;
		this.eggScore = eggScore;
		this.dairyScore = dairyScore;
		this.commentary = commentary;
		this.reviewStatus = reviewStatus;
	}
	
	
	
}
