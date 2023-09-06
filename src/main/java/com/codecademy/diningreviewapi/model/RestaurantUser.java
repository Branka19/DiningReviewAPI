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
@Getter
@Setter
@Entity
public class RestaurantUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_generator")
	@SequenceGenerator(name="user_generator", sequenceName="USER_SEQUENCE", allocationSize=1)
	private Integer id;
	
	//has to be unique to that user
	@Getter
	@Column(unique=true)
	private String displayName;
	
	@Column
	private String city;
	
	@Column
	private String zipCode;
	
	@Column
	Boolean interestedInPeanutAllergies;
	
	@Column
	Boolean interestedInEggAllergies;
	
	@Column
	Boolean interestedInDairyAllergies;

	public RestaurantUser(String displayName, String city, String zipCode, Boolean interesteadInpeanutAllergies,
			Boolean interesteadIneggAllergies, Boolean interesteadIndairyAllergies) {
		this.displayName = displayName;
		this.city = city;
		this.zipCode = zipCode;
		this.interestedInPeanutAllergies = interesteadInpeanutAllergies;
		this.interestedInEggAllergies = interesteadIneggAllergies;
		this.interestedInDairyAllergies = interesteadIndairyAllergies;

	}

}
