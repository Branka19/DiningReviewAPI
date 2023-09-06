package com.codecademy.diningreviewapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
@Entity
public class Restaurant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="restaurant_generator")
	@SequenceGenerator(name="restaurant_generator", sequenceName="RESTAURANT_SEQUENCE", allocationSize=1)
	private Integer id;
	
	@Column
	String restaurantName;
	
	@Column
	String restaurantLocation;

	@Column
	String restaurantZipCode;
	
	@Column
	private String overallScore;
	
	@Column
	private String scorePeanut;
	
	@Column
	private String scoreEggs;
	
	@Column
	private String scoreDairy;

	public Restaurant(String restaurantName, String restaurantLocation, String restaurantZipCode, String overallScore,
			String scorePeanut, String scoreEggs, String scoreDairy) {
		this.restaurantName = restaurantName;
		this.restaurantLocation = restaurantLocation;
		this.restaurantZipCode = restaurantZipCode;
		this.overallScore = overallScore;
		this.scorePeanut = scorePeanut;
		this.scoreEggs = scoreEggs;
		this.scoreDairy = scoreDairy;
	}
	

}
